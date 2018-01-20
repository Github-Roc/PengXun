package cn.peng.pxun.presenter.activity;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import cn.peng.pxun.MyApplication;
import cn.peng.pxun.presenter.base.BasePresenter;
import cn.peng.pxun.ui.activity.SettingActivity;
import cn.peng.pxun.utils.ThreadUtil;
import cn.peng.pxun.utils.ToastUtil;


/**
 * Created by msi on 2017/1/3.
 */
public class SettingPresenter extends BasePresenter{
    private SettingActivity mActivity;

    public SettingPresenter(SettingActivity activity) {
        super(activity);
        this.mActivity = activity;
    }

    /**
     * 获取当前应用的版本号
     */
    public String getVersionName() throws PackageManager.NameNotFoundException {
        // 获取packagemanager的实例
        PackageManager packageManager = MyApplication.context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(MyApplication.context.getPackageName(),0);
        String versionName = packInfo.versionName;
        return versionName;
    }

    /**
     * 注销帐号
     */
    public void logout() {
        EMClient.getInstance().logout(true, new EMCallBack() {
                @Override
                public void onSuccess() {
                    ThreadUtil.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            //清空登录信息
                            SharedPreferences.Editor editor = MyApplication.sp.edit();
                            editor.putBoolean("isLogin", false);
                            editor.commit();
                            //返回登录界面
                            mActivity.onLogoutSuccess();

                        }
                    });
                }

                @Override
                public void onProgress(int progress, String status) {

                }

                @Override
                public void onError(int code, String message) {
                    ThreadUtil.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showToast(mActivity, "注销帐号失败,请稍后重试!");
                        }
                    });
                }
        });
    }
}
