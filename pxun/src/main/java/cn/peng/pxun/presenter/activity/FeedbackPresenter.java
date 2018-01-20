package cn.peng.pxun.presenter.activity;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.modle.bmob.UserFeedback;
import cn.peng.pxun.presenter.base.BasePresenter;
import cn.peng.pxun.ui.activity.FeedbackActivity;

/**
 * Created by msi on 2017/11/14.
 */

public class FeedbackPresenter extends BasePresenter {
    private FeedbackActivity mActivity;

    public FeedbackPresenter(FeedbackActivity activity) {
        super(activity);
        this.mActivity = activity;
    }

    /**
     * 上传用户意见反馈记录
     * @param type
     * @param content
     */
    public void submitFeedback(String type, String content) {
        UserFeedback feedback = new UserFeedback();
        feedback.setFeedbackUserId(AppConfig.getUserId(AppConfig.appUser));
        feedback.setFeedbackType(type);
        feedback.setFeedbackContent(content);
        feedback.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null){
                    mActivity.onSubmitSuccess();
                } else {
                    showToast("网络连接较慢,请稍后重试");
                }
            }
        });
    }
}
