package cn.peng.pxun.presenter.fragment;

import android.os.SystemClock;

import java.util.List;

import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.modle.bmob.User;
import cn.peng.pxun.presenter.base.BaseUserPresenter;
import cn.peng.pxun.ui.fragment.FriendFragment;
import cn.peng.pxun.utils.ThreadUtil;

/**
 * Created by msi on 2016/12/26.
 */
public class FriendPresenter extends BaseUserPresenter {
    private FriendFragment mFriendFragment;

    private int index = 0;
    private boolean loadFriendState = false;

    public FriendPresenter(final FriendFragment fragment) {
        super(fragment);
        this.mFriendFragment = fragment;

        addListener();
    }

    private void addListener() {
        addFriendListListener(new FriendListListener() {
            @Override
            public void onGetFriendList(List<String> userIds) {
                if (userIds != null && userIds.size() > 0){
                    startLoadUser();
                }else {
                    loadFinish();
                }
            }
        });
        addUserInfoListener(new UserInfoListener() {
            @Override
            public void onGetUser(User user) {
                if (user != null){
                    AppConfig.friends.add(user);
                }

                index++;
                loadNext();
            }
        });
    }

    /**
     * 开始加载用户信息
     */
    private void startLoadUser() {
        index = 0;
        loadFriendState = true;
        AppConfig.friends.clear();

        loadNext();
    }

    /**
     * 加载下一个用户
     */
    private void loadNext(){
        if (index < friendIds.size()){
            String userId = friendIds.get(index);
            getUser(userId);
        } else {
            loadFinish();
        }
    }

    /**
     * 数据加载完成
     */
    private void loadFinish(){
        ThreadUtil.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                mFriendFragment.onLoadFinish();
            }
        });
    }

    /**
     * 从环信获取好友列表
     */
    public void getFriendList() {
        SystemClock.sleep(300);
        getFriendListFromHuanXin();
    }

    /**
     * 获取现在是否正在加载好友信息
     * @return
     */
    public boolean isLoadingFriend(){
        return loadFriendState;
    }
}
