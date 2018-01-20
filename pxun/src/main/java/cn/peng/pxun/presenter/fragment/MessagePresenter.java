package cn.peng.pxun.presenter.fragment;

import android.os.SystemClock;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.modle.bean.ConversationBean;
import cn.peng.pxun.modle.bmob.Group;
import cn.peng.pxun.modle.bmob.User;
import cn.peng.pxun.presenter.base.BaseGroupPresenter;
import cn.peng.pxun.ui.fragment.MessageFragment;
import cn.peng.pxun.utils.ThreadUtil;

/**
 * Created by msi on 2017/9/27.
 */

public class MessagePresenter extends BaseGroupPresenter{
    private MessageFragment mMessageFragment;

    private int index = 0;
    private boolean isLoading = false;
    private ArrayList<EMConversation> mEmConversations;

    public MessagePresenter(MessageFragment fragment) {
        super(fragment);
        this.mMessageFragment = fragment;

        addListener();
    }

    private void addListener() {
        addUserInfoListener(new UserInfoListener() {
            @Override
            public void onGetUser(User user) {
                if (user != null){
                    ConversationBean conversation = setConversation();
                    conversation.user = user;
                    AppConfig.conversations.add(conversation);
                }

                index += 1;
                loadNext();
            }
        });
        addGroupInfoListener(new GroupInfoListener() {
            @Override
            public void onGetGroup(Group group) {
                if (group != null){
                    ConversationBean conversation = setConversation();
                    conversation.group = group;
                    AppConfig.conversations.add(conversation);
                }

                index += 1;
                loadNext();
            }
        });
    }

    /**
     * 开始加载会话消息
     */
    private void startLoadConversation() {
        index = 0;
        isLoading = true;
        AppConfig.conversations.clear();

        loadNext();
    }

    /**
     * 加载下一条会话消息
     */
    private void loadNext() {
        if (index < mEmConversations.size()){
            boolean isGroup = mEmConversations.get(index).isGroup();
            String id = mEmConversations.get(index).getUserName();
            if (isGroup){
                getGroup(id);
            } else {
                getUser(id);
            }
        }else {
            // 加载完成
            isLoading = false;
            loadFinish();
            mEmConversations = null;
        }
    }

    /**
     * 设置会话实体
     * @return
     */
    private ConversationBean setConversation() {
        EMConversation emConversation = mEmConversations.get(index);
        ConversationBean conversation = new ConversationBean();
        conversation.lastMsg = splitEmMessage(emConversation.getLastMessage());
        conversation.lastChatTime = emConversation.getLastMessage().getMsgTime();
        conversation.unreadCount = emConversation.getUnreadMsgCount();
        conversation.isGroup = emConversation.isGroup();
        return conversation;
    }

    /**
     * 会话消息加载完成
     */
    private void loadFinish() {
        ThreadUtil.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                mMessageFragment.onLoadFinish();
            }
        });
    }

    /**
     * 获取会话列表
     */
    public void getMessageList() {
        ThreadUtil.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(300);
                Map<String, EMConversation> conversationMap = EMClient.getInstance().chatManager().getAllConversations();
                if (conversationMap != null && conversationMap.size() > 0) {
                    mEmConversations = new ArrayList();
                    mEmConversations.addAll(conversationMap.values());
                    if (mEmConversations.size() > 0){
                        Collections.sort(mEmConversations, new Comparator<EMConversation>() {
                            @Override
                            public int compare(EMConversation o1, EMConversation o2) {
                                return (int) (o2.getLastMessage().getMsgTime() - o1.getLastMessage().getMsgTime());
                            }
                        });

                        startLoadConversation();
                    }else {
                        loadFinish();
                    }
                }else {
                    loadFinish();
                }
            }
        });
    }



    /**
     * 获取现在是否正在加载好会话消息
     * @return
     */
    public boolean isLoadingMessage(){
        return isLoading;
    }

}
