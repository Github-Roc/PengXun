package cn.peng.pxun.presenter.fragment;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.peng.pxun.MyApplication;
import cn.peng.pxun.modle.bmob.Posts;
import cn.peng.pxun.modle.greendao.LocalPosts;
import cn.peng.pxun.modle.greendao.LocalPostsDao;
import cn.peng.pxun.presenter.base.BasePresenter;
import cn.peng.pxun.ui.fragment.SquareFragment;
import cn.peng.pxun.utils.LogUtil;

/**
 * Created by msi on 2017/11/25.
 */

public class SquarePresenter extends BasePresenter {

    private SquareFragment mSquareFragment;

    public SquarePresenter(SquareFragment fragment) {
        super(fragment);
        this.mSquareFragment = fragment;
    }

    public void getPostsList() {
        BmobQuery<Posts> bmobQuery = new BmobQuery();
        bmobQuery.addWhereNotEqualTo("postsType", 101);
        bmobQuery.order("-createdAt");
        bmobQuery.findObjects(new FindListener<Posts>() {
            @Override
            public void done(List<Posts> list, BmobException e) {
                if (e == null && list.size() > 0) {
                    LocalPostsDao postDaoDao = MyApplication.session.getLocalPostsDao();
                    List<LocalPosts> localPostsArrayList = new ArrayList<>();
                    for (Posts posts : list) {
                        LocalPosts localPosts = new LocalPosts(posts);
                        localPostsArrayList.add(localPosts);
                    }
                    postDaoDao.deleteAll();
                    postDaoDao.insertOrReplaceInTx(localPostsArrayList);
                    mSquareFragment.setData(localPostsArrayList);
                } else {
                    if (e != null) {
                        LogUtil.e(e.toString());
                    }
                    mSquareFragment.setEmptyPage();
                }
            }
        });
    }
}
