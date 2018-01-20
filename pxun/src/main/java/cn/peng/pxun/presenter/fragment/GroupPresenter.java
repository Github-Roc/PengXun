package cn.peng.pxun.presenter.fragment;

import android.os.SystemClock;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.modle.bmob.Group;
import cn.peng.pxun.presenter.base.BaseGroupPresenter;
import cn.peng.pxun.ui.fragment.GroupFragment;
import cn.peng.pxun.utils.ThreadUtil;

/**
 * Created by msi on 2016/12/26.
 */
public class GroupPresenter extends BaseGroupPresenter{
    private GroupFragment mGroupFragment;

    private List<EMGroup> mEMGroups;
    private int index = 0;

    public GroupPresenter(GroupFragment fragment) {
        super(fragment);
        this.mGroupFragment = fragment;

        addListener();
    }

    private void addListener() {
        addGroupInfoListener(new GroupInfoListener() {
            @Override
            public void onGetGroup(Group group) {
                if (group != null){
                    AppConfig.groups.add(group);
                }

                index++;
                loadNext();
            }
        });
    }

    private void startLoadGroups() {
        index = 0;
        AppConfig.groups.clear();

        loadNext();
    }

    private void loadNext(){
        if (index < mEMGroups.size()){
            String groupNum = mEMGroups.get(index).getGroupId();
            getGroup(groupNum);
        } else {
            loadFinish();
            mEMGroups = null;
        }
    }

    private void loadFinish(){
        ThreadUtil.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                mGroupFragment.onLoadFinish();
            }
        });
    }

    /**
     * 从服务器获取自己加入的和创建的群组列表
     * @return
     */
    public void getGroupList() {
        ThreadUtil.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(300);
                try {
                    mEMGroups = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
                    startLoadGroups();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    loadFinish();
                }
            }
        });
    }


}
