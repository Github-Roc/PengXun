package cn.peng.pxun.presenter.activity;

import cn.peng.pxun.presenter.base.BaseUserPresenter;
import cn.peng.pxun.ui.activity.UserInfoActivity;

/**
 * Created by tofirst on 2017/11/6.
 */

public class UserInfoPresenter extends BaseUserPresenter {
    private UserInfoActivity mActivity;

    public UserInfoPresenter(UserInfoActivity activity) {
        super(activity);
        this.mActivity = activity;

        addListener();
    }

    private void addListener() {
        addUpLoadFileListener(new UpLoadFileListener() {
            @Override
            public void onUpLoadFinish(String path) {
                mActivity.onIconUploadFinish(path);
            }

            @Override
            public void onUpLoadProgress(int value) {
                mActivity.onIconUploadProgress(value);
            }
        });
        addUpdataUserListener(new UpdataUserListener() {
            @Override
            public void onResult(int result) {
                mActivity.updataResult(result);
            }
        });
    }
}
