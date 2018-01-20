package cn.peng.pxun.modle.bean;

import cn.peng.pxun.modle.bmob.Group;
import cn.peng.pxun.modle.bmob.User;

/**
 * 会话消息的实体
 * Created by tofirst on 2017/9/28.
 */

public class ConversationBean {

    // 会话用户
    public User user;
    // 会话群组
    public Group group;
    // 最后一条消息
    public String lastMsg;
    // 最后一次聊天时间
    public long lastChatTime;
    // 未读消息数量
    public int unreadCount;
    // 是否是群组
    public boolean isGroup;

    public ConversationBean(){

    }

    public ConversationBean(User user) {
        this.user = user;
        this.isGroup = false;
    }

    public ConversationBean(Group group) {
        this.group = group;
        this.isGroup = true;
    }
}
