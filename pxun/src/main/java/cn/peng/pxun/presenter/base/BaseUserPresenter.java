package cn.peng.pxun.presenter.base;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.modle.bean.RegiestResultBean;
import cn.peng.pxun.modle.bmob.SysMessage;
import cn.peng.pxun.modle.bmob.User;
import cn.peng.pxun.ui.activity.BaseActivity;
import cn.peng.pxun.ui.fragment.BaseFragment;
import cn.peng.pxun.utils.LogUtil;
import cn.peng.pxun.utils.MD5Util;
import cn.peng.pxun.utils.ThreadUtil;

/**
 * Created by tofirst on 2017/11/3.
 */

public class BaseUserPresenter extends BasePhotoPresenter{
    private UserInfoListener mUserInfoListener;
    private UpdataUserListener mUpdataUserListener;
    private FriendListListener mFriendListListener;

    protected List<String> friendIds;

    public BaseUserPresenter(BaseActivity activity) {
        super(activity);
    }

    public BaseUserPresenter(BaseFragment fragment) {
        super(fragment);
    }

    /**
     * 用户注册
     * @param user
     */
    public void registUser(final User user, final String userId, final String password){
        if (!isNetUsable(mContext)){
            EventBus.getDefault().post(new RegiestResultBean(AppConfig.NET_ERROR, 500));
            return;
        }

        final String md5Password = MD5Util.encode(password);
        user.setPassword(md5Password);
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User bmobUser, BmobException e) {
                if (e == null){
                    //bmob注册成功
                    ThreadUtil.runOnSubThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //注册到环信服务器
                                EMClient.getInstance().createAccount(userId, md5Password);
                                EventBus.getDefault().post(new RegiestResultBean(AppConfig.REGIEST_SUCCESS, user, password));
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                                //环信注册失败
                                EventBus.getDefault().post(new RegiestResultBean(AppConfig.REGIEST_HUANXIN_ERROR, e.getErrorCode()));
                            }
                        }
                    });
                }else {
                    //bmob注册失败
                    EventBus.getDefault().post(new RegiestResultBean(AppConfig.REGIEST_BMOB_ERROR, 500));
                }
            }
        });
    }

    /**
     * 用户信息保存
     * @param user
     */
    public void upDataUser(User user){
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
               if (mUpdataUserListener != null){
                   if (e == null){
                       mUpdataUserListener.onResult(AppConfig.SUCCESS);
                   }else {
                       mUpdataUserListener.onResult(AppConfig.SERVER_ERROR);
                   }
               }
            }
        });
    }

    /**
     * 获取用户信息
     * @param userId
     */
    public void getUser(String userId){
        if (!isNetUsable(mContext)) {
            return;
        }
        BmobQuery<User> bmobQuery = new BmobQuery();
        if (isPhoneNumber(userId)){
            bmobQuery.addWhereEqualTo("mobilePhoneNumber", userId);
        }else{
            userId = userId.toUpperCase();
            bmobQuery.addWhereEqualTo("thirdPartyID", userId);
        }
        bmobQuery.findObjects(new FindListener<User>(){
            @Override
            public void done(List<User> list, BmobException e) {
                if (mUserInfoListener != null){
                    if (e == null && list.size() > 0) {
                        mUserInfoListener.onGetUser(list.get(0));
                    } else {
                        if (e != null){
                            LogUtil.e(e.toString());
                        }
                        mUserInfoListener.onGetUser(null);
                    }
                }
            }
        });
    }

    /**
     * 添加好友
     * @param user
     */
    public void addFriend(final User user){
        if (!isNetUsable(mContext)){
            showToast("当前无网络连接,请检查网络");
            return;
        }
        final String userID = AppConfig.getUserId(user);
        if (isMe(userID)){
            showToast("无法添加自己为好友");
            return;
        }
        if (isFriend(userID)){
            showToast("你们已经是好友了");
            return;
        }
        ThreadUtil.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    String message = AppConfig.appUser.getUsername()+"请求添加您为好友,请同意!";
                    // 发送环信好友申请
                    EMClient.getInstance().contactManager().addContact(userID, message);

                    SysMessage sysMsg = new SysMessage();
                    sysMsg.setMessage(message);
                    sysMsg.setFromUserId(AppConfig.getUserId(AppConfig.appUser));
                    sysMsg.setToUserId(AppConfig.getUserId(user));
                    sysMsg.setMsgType("100");
                    sysMsg.setMsgState("0");
                    sysMsg.save(new SaveListener<String>(){

                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null){
                                showToast("好友申请发送成功");
                            }else{
                                showToast("服务器连接较慢，请稍后重试");
                            }
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    showToast("好友申请发送失败");
                }
            }
        });
    }

    /**
     * 从环信获取我的好友集合
     * @return
     */
    public void getFriendListFromHuanXin(){
        if (!isNetUsable(mContext)) {
            return;
        }
        ThreadUtil.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    friendIds = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    ThreadUtil.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mFriendListListener != null){
                                mFriendListListener.onGetFriendList(friendIds);
                            }
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 判断是否是自己
     * @param userId
     * @return
     */
    public boolean isMe(String userId){
        String appUserID = AppConfig.getUserId(AppConfig.appUser);
        if (appUserID.equals(userId)){
            return true;
        }
        return false;
    }

    /**
     * 判断该用户是否为好友
     * @param userId
     * @return
     */
    public boolean isFriend(String userId){
        if ("tuling".equals(userId)){
            return true;
        }
        if (friendIds != null && friendIds.size() > 0){
            for (String friendId : friendIds){
                friendId = friendId.toUpperCase();
                if (friendId.equals(userId)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 添加获取用户的监听
     * @param listener
     */
    public void addUserInfoListener(UserInfoListener listener){
        this.mUserInfoListener = listener;
    }

    public interface UserInfoListener{
        void onGetUser(User user);
    }

    /**
     * 添加获更新用户信息的监听
     * @param listener
     */
    public void addUpdataUserListener(UpdataUserListener listener){
        this.mUpdataUserListener = listener;
    }

    public interface UpdataUserListener{
        void onResult(int result);
    }

    /**
     * 添加获取好友列表的监听
     * @param listener
     */
    public void addFriendListListener(FriendListListener listener){
        this.mFriendListListener = listener;
    }

    public interface FriendListListener{
        void onGetFriendList(List<String> userIds);
    }
}
