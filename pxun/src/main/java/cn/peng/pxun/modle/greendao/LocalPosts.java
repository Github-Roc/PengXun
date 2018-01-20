package cn.peng.pxun.modle.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;

import cn.peng.pxun.modle.bmob.Posts;

/**
 * @author adu
 * @date 2017/12/6 20:20
 */
@Entity
public class LocalPosts {
    @Id(autoincrement = true)
    public Long id;
    private String publishUserId;
    private String publishUserName;
    private String publishUserIcon;
    private String content;
    private String picPath;
    private String postsType;
    private String contentType;

    @Keep
    public LocalPosts(Posts posts){
        this.publishUserId=posts.getPublishUserId();
        this.publishUserName=posts.getPublishUserName();
        this.publishUserIcon=posts.getPublishUserIcon();
        this.content=posts.getContent();
        this.picPath=posts.getPicPath();
        this.postsType=posts.getPostsType();
        this.contentType=posts.getContentType();

    }

    @Generated(hash = 382211551)
    public LocalPosts() {
    }

    @Generated(hash = 1663371566)
    public LocalPosts(Long id, String publishUserId, String publishUserName,
            String publishUserIcon, String content, String picPath,
            String postsType, String contentType) {
        this.id = id;
        this.publishUserId = publishUserId;
        this.publishUserName = publishUserName;
        this.publishUserIcon = publishUserIcon;
        this.content = content;
        this.picPath = picPath;
        this.postsType = postsType;
        this.contentType = contentType;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getPublishUserId() {
        return this.publishUserId;
    }
    public void setPublishUserId(String publishUserId) {
        this.publishUserId = publishUserId;
    }
    public String getPublishUserName() {
        return this.publishUserName;
    }
    public void setPublishUserName(String publishUserName) {
        this.publishUserName = publishUserName;
    }
    public String getPublishUserIcon() {
        return this.publishUserIcon;
    }
    public void setPublishUserIcon(String publishUserIcon) {
        this.publishUserIcon = publishUserIcon;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getPicPath() {
        return this.picPath;
    }
    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }
    public String getPostsType() {
        return this.postsType;
    }
    public void setPostsType(String postsType) {
        this.postsType = postsType;
    }
    public String getContentType() {
        return this.contentType;
    }
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

}
