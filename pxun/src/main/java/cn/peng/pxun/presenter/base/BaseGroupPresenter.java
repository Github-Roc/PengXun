package cn.peng.pxun.presenter.base;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.peng.pxun.modle.bmob.Group;
import cn.peng.pxun.ui.activity.BaseActivity;
import cn.peng.pxun.ui.fragment.BaseFragment;
import cn.peng.pxun.utils.LogUtil;

/**
 * Created by msi on 2017/11/18.
 */

public class BaseGroupPresenter extends BaseUserPresenter{

    private GroupInfoListener mGroupInfoListener;

    public BaseGroupPresenter(BaseActivity activity) {
        super(activity);
    }

    public BaseGroupPresenter(BaseFragment fragment) {
        super(fragment);
    }

    /**
     * 获取群组信息
     * @param groupNum 群号码
     */
    public void getGroup(String groupNum){
        if (!isNetUsable(mContext)) {
            return;
        }
        BmobQuery<Group> bmobQuery = new BmobQuery();
        bmobQuery.addWhereEqualTo("groupNum", groupNum);
        bmobQuery.findObjects(new FindListener<Group>(){
            @Override
            public void done(List<Group> list, BmobException e) {
                if (mGroupInfoListener != null){
                    if (e == null && list.size() > 0) {
                        mGroupInfoListener.onGetGroup(list.get(0));
                    } else {
                        if (e != null){
                            LogUtil.e(e.toString());
                        }
                        mGroupInfoListener.onGetGroup(null);
                    }
                }
            }
        });
    }

    /**
     * 添加获取用户的监听
     * @param listener
     */
    public void addGroupInfoListener(GroupInfoListener listener){
        this.mGroupInfoListener = listener;
    }
    public interface GroupInfoListener{
        void onGetGroup(Group group);
    }
}
