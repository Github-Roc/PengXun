package cn.peng.pxun.ui.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.peng.pxun.R;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.modle.bean.MovieBean;
import cn.peng.pxun.presenter.fragment.MoviePresenter;
import cn.peng.pxun.ui.activity.WebActivity;
import cn.peng.pxun.ui.adapter.recycleview.MovieAdapter;

/**
 * Created by tofirst on 2017/10/27.
 * 电影页面
 */

public class MovieFragment extends BaseFragment<MoviePresenter> {
    @BindView(R.id.app_refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.app_recyclerview)
    RecyclerView mRecycleview;

    private MovieAdapter adapter;
    private List<MovieBean.SubjectsBean> movieList;

    private int start;
    private int end;

    @Override
    protected void init() {
        super.init();
        start = 0;
        end = 20;
        movieList = new ArrayList<>();
    }

    @Override
    public View initLayout() {
        View view = View.inflate(mActivity, R.layout.fragment_movie, null);
        return view;
    }

    @Override
    public MoviePresenter initPresenter() {
        return new MoviePresenter(this);
    }

    @Override
    protected void initData() {
        adapter = new MovieAdapter(R.layout.item_movie, movieList);
        // 设置预加载 当列表滑动到倒数第3时加载更多
        adapter.setPreLoadNumber(3);
        adapter.bindToRecyclerView(mRecycleview);
        mRecycleview.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        mRecycleview.setAdapter(adapter);

        mRefreshLayout.setRefreshing(true);
        presenter.getMovieList(start, end);
    }

    @Override
    protected void initListener() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                MovieBean.SubjectsBean data = (MovieBean.SubjectsBean) baseQuickAdapter.getData().get(position);

                if (data != null){
                    Intent intent = new Intent(mActivity, WebActivity.class);
                    String url = "https://movie.douban.com/subject/"+data.id;
                    intent.putExtra("url",url);
                    startActivity(intent);
                }
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override public void onLoadMoreRequested() {
                if (mRefreshLayout.isRefreshing()){
                    adapter.loadMoreComplete();
                }else{
                    presenter.getMovieList(start, end);
                }
            }
        }, mRecycleview);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (adapter.isLoading()){
                    mRefreshLayout.setRefreshing(false);
                }else{
                    start = 0;
                    end = 20;
                    // 开启加载更多
                    adapter.setEnableLoadMore(true);
                    presenter.getMovieList(start, end);
                }
            }
        });
    }

    public void setData(MovieBean data, int code) {
        if (code == AppConfig.SUCCESS){
            if (data != null){
                if (mRefreshLayout!= null && mRefreshLayout.isRefreshing()){
                    movieList.clear();
                    adapter.setNewData(movieList);
                    mRefreshLayout.setRefreshing(false);
                }
                movieList.addAll(data.subjects);
                adapter.notifyDataSetChanged();

                start += 20;
                end += 20;
                if (adapter.isLoading()){
                    if (start < data.total){
                        adapter.loadMoreComplete();
                    }else{
                        adapter.loadMoreEnd();
                    }
                }
            }else{
                // 设置空页面
                adapter.setEmptyView(R.layout.app_page_empty);
            }
        }else{
            if (adapter.isLoading()){
                adapter.loadMoreFail();
            }
            if (mRefreshLayout.isRefreshing()){
                mRefreshLayout.setRefreshing(false);
            }
        }
    }
}
