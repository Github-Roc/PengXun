package cn.peng.pxun.ui.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.peng.pxun.R;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.modle.bmob.Posts;
import cn.peng.pxun.presenter.activity.PublishPostsPresenter;
import cn.peng.pxun.ui.adapter.recycleview.PostsPicAdapter;
import cn.peng.pxun.ui.view.FullyGridLayoutManager;
import cn.peng.pxun.utils.ToastUtil;

/**
 * @author Peng
 */
public class PublishPostsActivity extends BaseActivity<PublishPostsPresenter> {

    @BindView(R.id.iv_posts_goback)
    ImageView mIvPostsGoback;
    @BindView(R.id.tv_posts_publish)
    TextView mTvPostsPublish;
    @BindView(R.id.et_posts_input)
    EditText mEtPostsInput;
    @BindView(R.id.rv_posts_piclist)
    RecyclerView mRvPostsPiclist;

    private List<LocalMedia> selectList;
    private PostsPicAdapter adapter;
    private Posts mPosts;

    private PostsPicAdapter.onAddPicClickListener onAddPicClickListener = new PostsPicAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            PictureSelector.create(PublishPostsActivity.this)
                    .openGallery(PictureMimeType.ofImage())
                    .maxSelectNum(1)
                    .forResult(PictureConfig.CHOOSE_REQUEST);
        }
    };

    @Override
    protected void init() {
        super.init();
        initPosts();
        selectList = new ArrayList<>();
    }

    @Override
    public int setLayoutRes() {
        return R.layout.activity_publish_posts;
    }

    @Override
    public PublishPostsPresenter initPresenter() {
        return new PublishPostsPresenter(this);
    }

    @Override
    protected void initView() {
        super.initView();

        FullyGridLayoutManager manager = new FullyGridLayoutManager(mActivity, 4, GridLayoutManager.VERTICAL, false);
        mRvPostsPiclist.setLayoutManager(manager);
        adapter = new PostsPicAdapter(mActivity, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(1);
        mRvPostsPiclist.setAdapter(adapter);
    }

    @Override
    protected void initListener() {
        mIvPostsGoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTvPostsPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postsContent = mEtPostsInput.getText().toString();
                if (TextUtils.isEmpty(postsContent)) {
                    ToastUtil.showToast(mActivity, "先写点什么吧");
                    return;
                }

                mPosts.setContent(postsContent);
                if(selectList != null && selectList.size() > 0){
                    showProgressDialog("正在上传图片");
                    presenter.upLoadFile(selectList.get(0).getPath());
                } else {
                    showLoadingDialog("请稍候...");
                    presenter.publishPosts(mPosts);
                }
            }
        });
        adapter.setOnItemClickListener(new PostsPicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            PictureSelector.create(mActivity).externalPicturePreview(position, selectList);
                            break;
                        case 2:
                            // 预览视频
                            PictureSelector.create(mActivity).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            // 预览音频
                            PictureSelector.create(mActivity).externalPictureAudio(media.getPath());
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    }

    private void initPosts() {
        mPosts = new Posts();
        mPosts.setPublishUserId(AppConfig.getUserId(AppConfig.appUser));
        mPosts.setPublishUserName(AppConfig.appUser.getUsername());
        mPosts.setPublishUserIcon(AppConfig.appUser.getHeadIcon());
        mPosts.setPostsType(Posts.POSTS_TYPE_JOKE);
        mPosts.setContentType(Posts.CONTENT_TYPE_TEXT);
    }

    public void onIconUploadProgress(int value) {
        progressBar.setProgress(value);
    }

    public void onIconUploadFinish(String picPath) {
        dismissProgressDialog();
        if (!TextUtils.isEmpty(picPath)){
            mPosts.setPicPath(picPath);
            mPosts.setContentType(Posts.CONTENT_TYPE_PIC);

            showLoadingDialog("请稍候...");
            presenter.publishPosts(mPosts);
        } else {
            ToastUtil.showToast(this, "图片上传失败");
        }
    }

    public void publishSuccess() {
        dismissLoadingDialog();
        ToastUtil.showToast(this, "发表成功");
        finish();
    }

}
