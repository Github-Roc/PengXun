package cn.peng.pxun.modle.bmob;

import cn.bmob.v3.BmobObject;

/**
 * 用户反馈实体
 * Created by msi on 2017/11/14.
 */

public class UserFeedback extends BmobObject {
    //意见反馈内容
    private String feedbackContent;
    //意见反馈类型
    private String feedbackType;
    //意见反馈用户id
    private String feedbackUserId;

    public String getFeedbackContent() {
        return feedbackContent;
    }

    public void setFeedbackContent(String feedbackContent) {
        this.feedbackContent = feedbackContent;
    }

    public String getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(String feedbackType) {
        this.feedbackType = feedbackType;
    }

    public String getFeedbackUserId() {
        return feedbackUserId;
    }

    public void setFeedbackUserId(String feedbackUserId) {
        this.feedbackUserId = feedbackUserId;
    }
}
