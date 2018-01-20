package cn.peng.pxun.presenter.activity;

import android.util.Log;

import com.turing.androidsdk.InitListener;
import com.turing.androidsdk.SDKInit;
import com.turing.androidsdk.SDKInitBuilder;

import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.presenter.base.BasePresenter;
import cn.peng.pxun.ui.activity.BaseActivity;

/**
 * Created by msi on 2017/10/20.
 */

public class MainPresenter extends BasePresenter{

    public MainPresenter(BaseActivity activity) {
        super(activity);
    }

    /**
     * 初始化图灵机器人
     */
    public void initTuLing() {
        SDKInitBuilder builder = new SDKInitBuilder(mContext).setSecret("77bd9b637dd3aff6").
                setTuringKey(AppConfig.TURING_APP_KEY).setUniqueId("1136313078");
        SDKInit.init(builder, new InitListener() {
            @Override
            public void onComplete() {
                AppConfig.isInitTuring = true;
                Log.i("ChatActivity","图灵机器人初始化成功");
            }
            @Override
            public void onFail(String s) {
                AppConfig.isInitTuring = false;
                Log.i("ChatActivity","图灵机器人初始化失败");
            }
        });
    }
}
