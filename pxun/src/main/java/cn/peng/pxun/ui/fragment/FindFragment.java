package cn.peng.pxun.ui.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.peng.pxun.R;
import cn.peng.pxun.modle.bmob.User;
import cn.peng.pxun.presenter.fragment.FindPresenter;
import cn.peng.pxun.ui.activity.DetailedActivity;
import cn.peng.pxun.ui.activity.SearchActivity;
import cn.peng.pxun.ui.adapter.recycleview.UserAdapter;

/**
 * Created by msi on 2016/12/21.
 */
public class FindFragment extends BaseFragment<FindPresenter> {

    @BindView(R.id.tv_title_text)
    TextView mTvTitleText;
    @BindView(R.id.iv_title_search)
    ImageView mIvTitleSearch;
    @BindView(R.id.app_refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.app_recyclerview)
    RecyclerView mRecycleview;

    private UserAdapter adapter;
    private List<User> userList;

    @Override
    protected void init() {
        super.init();
        userList = new ArrayList<>();
    }

    @Override
    public View initLayout() {
        View view = View.inflate(mActivity, R.layout.fragment_find, null);
        return view;
    }

    @Override
    public FindPresenter initPresenter() {
        return new FindPresenter(this);
    }

    @Override
    protected void initView() {
        mTvTitleText.setText("发现");
        mIvTitleSearch.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        adapter = new UserAdapter(R.layout.item_message, userList);
        adapter.bindToRecyclerView(mRecycleview);
        mRecycleview.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecycleview.setAdapter(adapter);

        mRefreshLayout.setRefreshing(true);
        presenter.getUserList();
    }

    @Override
    public void initListener() {
        mIvTitleSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, SearchActivity.class);
                intent.putExtra("searchType", SearchActivity.SEARCH_USER);
                startActivity(intent);
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                User user = adapter.getItem(position);
                Intent intent = new Intent(mActivity, DetailedActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getUserList();
            }
        });
    }

    public void setData(List<User> list) {
        if (mRefreshLayout!= null && mRefreshLayout.isRefreshing()){
            userList.clear();
            mRefreshLayout.setRefreshing(false);
        }

        userList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    public void setEmptyPage() {
        if (adapter != null){
            // 设置空页面
            adapter.setEmptyView(R.layout.app_page_empty);
        }
    }

    private void startUp() {
//        userList.add(AppConfig.appUser);
//        List<Posts> postsList = new ArrayList<>();
//        Posts posts1 = new Posts();
//        User user1 = userList.get(new Random().nextInt(userList.size()));
//        posts1.setPublishUserId(AppConfig.getUserId(user1));
//        posts1.setPublishUserName(user1.getUsername());
//        posts1.setPublishUserIcon(user1.getHeadIcon());
//        posts1.setPostsType(Posts.POSTS_TYPE_JOKE);
//        posts1.setContentType(Posts.CONTENT_TYPE_TEXT);
//        posts1.setContent("今天本主酒驾，碰到交警查车，交警看我达到醉驾标准，还保持头脑清醒，井然有序，  于是交警对我说，你的酒量非常好，我给你88分，剩下12分我扣下了，以后不能骄傲下不为例！");
//        postsList.add(posts1);
//
//        Posts posts2 = new Posts();
//        User user2 = userList.get(new Random().nextInt(userList.size()));
//        posts2.setPublishUserId(AppConfig.getUserId(user2));
//        posts2.setPublishUserName(user2.getUsername());
//        posts2.setPublishUserIcon(user2.getHeadIcon());
//        posts2.setPostsType(Posts.POSTS_TYPE_JOKE);
//        posts2.setContentType(Posts.CONTENT_TYPE_TEXT);
//        posts2.setContent("小侄女刚学会唱歌不久，自从那天她跟着电视把：多少手足忠魂埋骨他乡，唱成：多少小猪炖粉排骨真香，我就知道又一个吃货诞生了。");
//        postsList.add(posts2);
//
//        Posts posts3 = new Posts();
//        User user3 = userList.get(new Random().nextInt(userList.size()));
//        posts3.setPublishUserId(AppConfig.getUserId(user3));
//        posts3.setPublishUserName(user3.getUsername());
//        posts3.setPublishUserIcon(user3.getHeadIcon());
//        posts3.setPostsType(Posts.POSTS_TYPE_JOKE);
//        posts3.setContentType(Posts.CONTENT_TYPE_PIC);
//        posts3.setContent("别客气。都是兄弟");
//        posts3.setPicPath("http://pic.qiushibaike.com/system/pictures/11968/119685328/medium/app119685328.jpg");
//        postsList.add(posts3);
//
//        Posts posts4 = new Posts();
//        User user4 = userList.get(new Random().nextInt(userList.size()));
//        posts4.setPublishUserId(AppConfig.getUserId(user4));
//        posts4.setPublishUserName(user4.getUsername());
//        posts4.setPublishUserIcon(user4.getHeadIcon());
//        posts4.setPostsType(Posts.POSTS_TYPE_JOKE);
//        posts4.setContentType(Posts.CONTENT_TYPE_TEXT);
//        posts4.setContent("昨天傍晚出去拿快递，遇到几个高中同学，就一起聊了一会儿天，然后被强拉着去喝酒。<br/>一顿酒喝到八点多才回家，回到家把快递往沙发上一扔倒头就睡。<br/>早上起来，我看着沙发上的交通锥形桶陷入了沉思------卧槽？特么滴我快递呢？");
//        postsList.add(posts4);
//
//        Posts posts5 = new Posts();
//        User user5 = userList.get(new Random().nextInt(userList.size()));
//        posts5.setPublishUserId(AppConfig.getUserId(user5));
//        posts5.setPublishUserName(user5.getUsername());
//        posts5.setPublishUserIcon(user5.getHeadIcon());
//        posts5.setPostsType(Posts.POSTS_TYPE_JOKE);
//        posts5.setContentType(Posts.CONTENT_TYPE_PIC);
//        posts5.setContent("同学，你在哪，我公司需要你……");
//        posts5.setPicPath("http://pic.qiushibaike.com/system/pictures/11967/119678738/medium/app119678738.jpg");
//        postsList.add(posts5);
//
//        Posts posts6 = new Posts();
//        User user6 = userList.get(new Random().nextInt(userList.size()));
//        posts6.setPublishUserId(AppConfig.getUserId(user6));
//        posts6.setPublishUserName(user6.getUsername());
//        posts6.setPublishUserIcon(user6.getHeadIcon());
//        posts6.setPostsType(Posts.POSTS_TYPE_JOKE);
//        posts6.setContentType(Posts.CONTENT_TYPE_PIC);
//        posts6.setContent("事实就是如此！");
//        posts6.setPicPath("http://pic.qiushibaike.com/system/pictures/11968/119684821/medium/app119684821.jpg");
//        postsList.add(posts6);
//
//        Posts posts7 = new Posts();
//        User user7 = userList.get(new Random().nextInt(userList.size()));
//        posts7.setPublishUserId(AppConfig.getUserId(user7));
//        posts7.setPublishUserName(user7.getUsername());
//        posts7.setPublishUserIcon(user7.getHeadIcon());
//        posts7.setPostsType(Posts.POSTS_TYPE_JOKE);
//        posts7.setContentType(Posts.CONTENT_TYPE_TEXT);
//        posts7.setContent("女朋友在空间发了一条说说。——“他就这样走了，愿他在那边安好”。并附上我的一张照片。——然后我看到下面的评论，–“节哀吧！人死不能复生!”……我丫的就是去外地学习一段时间，有必要这么隆重?");
//        postsList.add(posts7);
//
//        Posts posts8 = new Posts();
//        User user8 = userList.get(new Random().nextInt(userList.size()));
//        posts8.setPublishUserId(AppConfig.getUserId(user8));
//        posts8.setPublishUserName(user8.getUsername());
//        posts8.setPublishUserIcon(user8.getHeadIcon());
//        posts8.setPostsType(Posts.POSTS_TYPE_JOKE);
//        posts8.setContentType(Posts.CONTENT_TYPE_TEXT);
//        posts8.setContent("记得小学的时候每个星期一都要升国旗吗，我们老师叫我和我们班班长拿话筒，本来是一起去的，可是班长他走到半路就突然跑了，我还以为发生了什么，就跟着跑了。。。然后全校师生就这样的看着我跟我班长一起跑去了厕所留下老师一脸无语的样子。后来才知道他但是是尿急了。。。");
//        postsList.add(posts8);
//
//        Posts posts9 = new Posts();
//        User user9 = userList.get(new Random().nextInt(userList.size()));
//        posts9.setPublishUserId(AppConfig.getUserId(user9));
//        posts9.setPublishUserName(user9.getUsername());
//        posts9.setPublishUserIcon(user9.getHeadIcon());
//        posts9.setPostsType(Posts.POSTS_TYPE_JOKE);
//        posts9.setContentType(Posts.CONTENT_TYPE_PIC);
//        posts9.setContent("当高手遇到高高手的巅峰对决");
//        posts9.setPicPath("http://pic.qiushibaike.com/system/pictures/11968/119685669/medium/app119685669.jpg");
//        postsList.add(posts9);
//
//
//        for (Posts posts : postsList){
//            posts.save(new SaveListener<String>() {
//                @Override
//                public void done(String s, BmobException e) {
//                    if (e == null){
//                        ToastUtil.showToast(mActivity, "上传成功");
//                    }else {
//                        ToastUtil.showToast(mActivity, "上传失败");
//                    }
//                }
//            });
//        }
    }
}
