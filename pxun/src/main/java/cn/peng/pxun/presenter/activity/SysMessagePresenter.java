package cn.peng.pxun.presenter.activity;


import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.modle.bmob.SysMessage;
import cn.peng.pxun.presenter.base.BasePresenter;
import cn.peng.pxun.ui.activity.SysMessageActivity;
import cn.peng.pxun.utils.ThreadUtil;

/**
 * Created by tofirst on 2017/9/28.
 */

public class SysMessagePresenter extends BasePresenter{
    private SysMessageActivity mActivity;

    public SysMessagePresenter(SysMessageActivity activity) {
        super(activity);
        this.mActivity = activity;
    }

    /**
     * 获取系统消息列表
     */
    public void getSysMessageList(){
        BmobQuery<SysMessage> bmobQuery = new BmobQuery();
        bmobQuery.addWhereEqualTo("toUserId", AppConfig.getUserId(AppConfig.appUser));
        bmobQuery.order("-createdAt");
        bmobQuery.findObjects(new FindListener<SysMessage>(){

            @Override
            public void done(List<SysMessage> list, BmobException e) {
                if (e == null) {
                    mActivity.onloadFinish(list);
                } else {
                    showToast("数据加载失败");
                }
            }
        });
    }

    /**
     * 同意好友申请
     * @param sysMsg
     */
    public void agreeAddContact(final SysMessage sysMsg) {
        ThreadUtil.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().acceptInvitation(sysMsg.getFromUserId());
                    sysMsg.setMsgState("1");
                    sysMsg.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null){
                                SysMessage newMsg = new SysMessage();
                                newMsg.setMessage(AppConfig.appUser.getUsername()+"同意了你的好友申请。");
                                newMsg.setFromUserId(sysMsg.getToUserId());
                                newMsg.setToUserId(sysMsg.getFromUserId());
                                newMsg.setMsgType("110");
                                newMsg.setMsgState("1");
                                addNewSysMessage(newMsg);
                            }else{
                                showToast("服务器连接较慢，请稍后重试");
                            }
                        }
                    });
                } catch (HyphenateException e1) {
                    e1.printStackTrace();
                    showToast("消息处理失败");
                }
            }
        });
    }

    private void addNewSysMessage(final SysMessage sysMsg) {
        sysMsg.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null){
                    showToast("消息处理成功");
                }else{
                    showToast("服务器连接较慢，请稍后重试");
                }
            }
        });
    }
}
