package cn.peng.pxun.presenter.activity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.peng.pxun.modle.bmob.Group;
import cn.peng.pxun.modle.bmob.User;
import cn.peng.pxun.presenter.base.BaseUserPresenter;
import cn.peng.pxun.ui.activity.SearchActivity;
import cn.peng.pxun.utils.ToastUtil;

/**
 * Created by msi on 2017/9/23.
 */
public class SearchPresenter extends BaseUserPresenter {

    private SearchActivity mActivity;

    public SearchPresenter(SearchActivity activity) {
        super(activity);
        this.mActivity = activity;

        getFriendListFromHuanXin();
    }

    /**
     * 开始搜索
     * @param searchType
     * @param content
     */
    public void search(int searchType, String content) {
        switch (searchType){
            case SearchActivity.SEARCH_USER:
                searchUser(content);
                break;
            case SearchActivity.SEARCH_GROUP:
                searchGroup(content);
                break;
        }
    }

    /**
     * 搜索用户
     * @param content
     */
    private void searchUser(String content) {
        BmobQuery<User> bmobQuery = new BmobQuery();
        List<BmobQuery<User>> params = new ArrayList<>();
        params.add(new BmobQuery<User>().addWhereEqualTo("username", content));
        params.add(new BmobQuery<User>().addWhereEqualTo("loginNum", content));
        bmobQuery.or(params);
        bmobQuery.setLimit(50);
        bmobQuery.findObjects(new FindListener<User>(){

            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    mActivity.onUserSearch(list);
                } else {
                    ToastUtil.showToast(mContext,"查询失败：" + e.toString());
                    mActivity.onUserSearch(list);
                }
            }
        });
    }

    /**
     * 搜索群组
     * @param content
     */
    private void searchGroup(String content) {
        BmobQuery<Group> bmobQuery = new BmobQuery();
        List<BmobQuery<Group>> params = new ArrayList<>();
        params.add(new BmobQuery<Group>().addWhereEqualTo("groupName", content));
        params.add(new BmobQuery<Group>().addWhereEqualTo("groupNum", content));
        bmobQuery.or(params);
        bmobQuery.setLimit(50);
        bmobQuery.findObjects(new FindListener<Group>(){

            @Override
            public void done(List<Group> list, BmobException e) {
                if (e == null) {
                    mActivity.onGroupSearch(list);
                } else {
                    ToastUtil.showToast(mContext,"查询失败：" + e.toString());
                    mActivity.onGroupSearch(list);
                }
            }
        });
    }

}
