package cn.peng.pxun.presenter.activity;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.peng.pxun.modle.bmob.Posts;
import cn.peng.pxun.presenter.base.BasePhotoPresenter;
import cn.peng.pxun.ui.activity.PublishPostsActivity;

/**
 * Created by msi on 2017/11/26.
 */

public class PublishPostsPresenter extends BasePhotoPresenter{

    private PublishPostsActivity mActivity;

    public PublishPostsPresenter(PublishPostsActivity activity) {
        super(activity);
        this.mActivity = activity;

        addListener();
    }

    private void addListener() {
        addUpLoadFileListener(new UpLoadFileListener() {
            @Override
            public void onUpLoadFinish(String path) {
                mActivity.onIconUploadFinish(path);
            }

            @Override
            public void onUpLoadProgress(int value) {
                mActivity.onIconUploadProgress(value);
            }
        });
    }

    /**
     * 发表帖子
     * @param posts
     */
    public void publishPosts(Posts posts) {
        posts.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null){
                    mActivity.publishSuccess();
                } else {
                    showToast("服务器连接较慢，请稍后重试");
                }
            }
        });
    }
}
