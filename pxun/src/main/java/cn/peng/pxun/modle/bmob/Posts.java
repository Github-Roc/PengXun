package cn.peng.pxun.modle.bmob;

import cn.bmob.v3.BmobObject;

/**
 * Created by msi on 2017/11/24.
 */

public class Posts extends BmobObject {
    // 帖子类型 (段子)
    public static final String POSTS_TYPE_JOKE = "100";
    // 帖子类型 (动态)
    public static final String POSTS_TYPE_DYNAMIC = "101";
    // 内容类型 (纯文本)
    public static final String CONTENT_TYPE_TEXT = "200";
    // 内容类型 (带图)
    public static final String CONTENT_TYPE_PIC = "201";

    // 发表帖子用户ID
    private String publishUserId;
    // 发表帖子用户昵称
    private String publishUserName;
    // 发表帖子用户头像
    private String publishUserIcon;
    // 帖子内容
    private String content;
    // 帖子图片路径
    private String picPath;
    // 帖子类型
    private String postsType;
    // 内容类型
    private String contentType;

    public String getPublishUserId() {
        return publishUserId;
    }

    public void setPublishUserId(String publishUserId) {
        this.publishUserId = publishUserId;
    }

    public String getPublishUserName() {
        return publishUserName;
    }

    public void setPublishUserName(String publishUserName) {
        this.publishUserName = publishUserName;
    }

    public String getPublishUserIcon() {
        return publishUserIcon;
    }

    public void setPublishUserIcon(String publishUserIcon) {
        this.publishUserIcon = publishUserIcon;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getPostsType() {
        return postsType;
    }

    public void setPostsType(String postsType) {
        this.postsType = postsType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
