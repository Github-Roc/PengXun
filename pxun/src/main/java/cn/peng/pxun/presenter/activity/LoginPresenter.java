package cn.peng.pxun.presenter.activity;

import android.content.SharedPreferences;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.peng.pxun.MyApplication;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.modle.bmob.User;
import cn.peng.pxun.presenter.base.BaseUserPresenter;
import cn.peng.pxun.ui.activity.LoginActivity;
import cn.peng.pxun.utils.MD5Util;
import cn.peng.pxun.utils.ThreadUtil;

/**
 * LoginActivity的业务类
 */
public class LoginPresenter extends BaseUserPresenter{
    private LoginActivity mActivity;
    private String mLoginNum;
    private String mPassword;
    private boolean isThirdPartyLogin = false;

    public LoginPresenter(LoginActivity activity) {
        super(activity);
        this.mActivity = activity;
    }

    /**
     * 查询该三方用户是否注册过
     * 未注册用户先注册后登陆
     * @param user
     */
    public void thirdPartyUserLogin(final User user) {
        isThirdPartyLogin = true;
        BmobQuery<User> bmobQuery = new BmobQuery();
        bmobQuery.addWhereEqualTo("thirdPartyID", user.getThirdPartyID());
        bmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null){
                    if(list != null && list.size() > 0){
                        // 已注册,直接登录
                        loginHuanXin(user.getThirdPartyID(), user.getThirdPartyID());
                    }else{
                        // 未注册,先注册用户
                        regiestThirdPartyUser(user);
                    }
                }else{
                    e.printStackTrace();
                    setResult(AppConfig.SERVER_ERROR, 500);
                }
            }
        });
    }

    /**
     * 注册三方登录的用户
     * @param user
     */
    private void regiestThirdPartyUser(User user){
        String userId = user.getThirdPartyID();
        String password = user.getThirdPartyID();
        registUser(user, userId, password);
    }

    /**
     * 登录帐号
     * @param loginNum
     * @param password
     * @return
     */
    public void login(String loginNum, final String password) {
        if (!isNetUsable(mActivity)){
            mActivity.onLoginFinish(AppConfig.NET_ERROR, 100);
            return;
        }

        this.mLoginNum = loginNum;
        this.isThirdPartyLogin = false;

        loginHuanXin(loginNum, password);
    }

    /**
     * 登录环信服务器
     * @param accountNumber
     * @param password
     */
    public void loginHuanXin(final String accountNumber, String password) {
        this.mPassword = password;
        EMClient.getInstance().login(accountNumber, MD5Util.encode(password),new EMCallBack() {
            @Override
            public void onSuccess() {
                keepUserId(accountNumber);
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                setResult(AppConfig.SUCCESS, 200);
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                setResult(AppConfig.ERROR, code);
            }
        });
    }

    /**
     * 传递数据给actiivty
     * @param code
     * @param huanXinCode
     */
    private void setResult(final int code, final int huanXinCode) {
        ThreadUtil.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                mActivity.onLoginFinish(code, huanXinCode);
            }
        });
    }

    /**
     * 保存当前登录用户的用户ID
     * @param userId
     */
    private void keepUserId(String userId){
        SharedPreferences.Editor editor = MyApplication.sp.edit();
        editor.putString("userId",userId);
        editor.putBoolean("isLogin",true);
        editor.commit();
    }

    /**
     * 保存用户的登录信息
     * @param isRememberPassword
     */
    public void keepUserLoginInfo(boolean isRememberPassword) {
        SharedPreferences.Editor editor = MyApplication.sp.edit();
        if (!isThirdPartyLogin){
            editor.putBoolean("isThird",false);
            editor.putString("phone", mLoginNum);
            editor.putString("mPassword", mPassword);
            editor.putBoolean("isRemember",isRememberPassword);
        }else {
            editor.putBoolean("isThird",true);
            editor.putString("thirdpwd", mPassword);
        }
        editor.commit();
    }

}
