package cn.peng.pxun.modle.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 聊天消息的实体类
 */

@Entity
public class Message {
    public static final int TEXT_TYPE = 1;
    public static final int PIC_TYPE = 2;
    public static final int SPEECH_TYPE = 3;

    @Id(autoincrement = true)
    public Long id;
    //消息内容
    @NotNull
    public String message;
    //发送时间
    public String date;
    //消息时间
    public Long msgTime;
    //发送者ID
    public String fromUserID;
    //接受者ID
    public String toUserID;
    //消息类型
    public Integer messageType;
    //是否是与图灵机器人的聊天消息
    public Boolean isTuring;
    //图片路径
    public String picURL;
    @Generated(hash = 1215190165)
    public Message(Long id, @NotNull String message, String date, Long msgTime,
            String fromUserID, String toUserID, Integer messageType,
            Boolean isTuring, String picURL) {
        this.id = id;
        this.message = message;
        this.date = date;
        this.msgTime = msgTime;
        this.fromUserID = fromUserID;
        this.toUserID = toUserID;
        this.messageType = messageType;
        this.isTuring = isTuring;
        this.picURL = picURL;
    }
    @Generated(hash = 637306882)
    public Message() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getMessage() {
        return this.message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getFromUserID() {
        return this.fromUserID;
    }
    public void setFromUserID(String fromUserID) {
        this.fromUserID = fromUserID;
    }
    public String getToUserID() {
        return this.toUserID;
    }
    public void setToUserID(String toUserID) {
        this.toUserID = toUserID;
    }
    public Integer getMessageType() {
        return this.messageType;
    }
    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }
    public Boolean getIsTuring() {
        return this.isTuring;
    }
    public void setIsTuring(Boolean isTuring) {
        this.isTuring = isTuring;
    }
    public String getPicURL() {
        return this.picURL;
    }
    public void setPicURL(String picURL) {
        this.picURL = picURL;
    }
    public Long getMsgTime() {
        return this.msgTime;
    }
    public void setMsgTime(Long msgTime) {
        this.msgTime = msgTime;
    }


}
