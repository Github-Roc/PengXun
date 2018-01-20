package cn.peng.pxun.presenter.activity;

import android.graphics.Bitmap;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.exceptions.HyphenateException;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.modle.bmob.Group;
import cn.peng.pxun.presenter.base.BasePresenter;
import cn.peng.pxun.ui.activity.CreateGroupActivity;

/**
 * Created by msi on 2017/9/23.
 */
public class CreateGroupPresenter extends BasePresenter{
    private CreateGroupActivity mActivity;

    public CreateGroupPresenter(CreateGroupActivity activity) {
        super(activity);
        this.mActivity = activity;
    }

    public void createGroup(final String name, final String desc, EMGroupManager.EMGroupStyle groupType, Bitmap icon) {
        if (!isNetUsable(mActivity)){
            mActivity.onCreateGroup(AppConfig.NET_ERROR,null);
            return;
        }

        try {
            EMGroupManager.EMGroupOptions option = new EMGroupManager.EMGroupOptions();
            option.maxUsers = 200;
            option.style = groupType;
            final EMGroup emGroup = EMClient.getInstance().groupManager().createGroup(name, desc, new String[]{}, "", option);

            Group group = new Group();
            group.setGroupMasterId(AppConfig.getUserId(AppConfig.appUser));
            group.setGroupNum(emGroup.getGroupId());
            group.setGroupName(name);
            group.setGroupDesc(desc);
            if (groupType == EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin ){
                group.setGroupType("10000");
            }else if (groupType == EMGroupManager.EMGroupStyle.EMGroupStylePrivateOnlyOwnerInvite ){
                group.setGroupType("20000");
            }
            group.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if(e==null){
                        mActivity.onCreateGroup(AppConfig.SUCCESS,emGroup.getGroupId());
                    }else{
                        try {
                            EMClient.getInstance().groupManager().destroyGroup(emGroup.getGroupId());
                        } catch (HyphenateException e1) {
                            e1.printStackTrace();
                        }
                        mActivity.onCreateGroup(AppConfig.ERROR,e.toString());
                    }
                }
            });
        } catch (HyphenateException e1) {
            e1.printStackTrace();
            mActivity.onCreateGroup(AppConfig.ERROR,null);
        }
    }
}
