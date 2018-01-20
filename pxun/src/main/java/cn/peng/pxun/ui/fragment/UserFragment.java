package cn.peng.pxun.ui.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.peng.pxun.R;
import cn.peng.pxun.modle.bmob.User;
import cn.peng.pxun.presenter.fragment.UserPresenter;
import cn.peng.pxun.ui.activity.DetailedActivity;
import cn.peng.pxun.ui.adapter.recycleview.UserAdapter;

/**
 * Created by tofirst on 2017/10/27.
 */

public class UserFragment extends BaseFragment<UserPresenter> {

    @BindView(R.id.app_refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.app_recyclerview)
    RecyclerView mRecycleview;

    private UserAdapter adapter;
    private List<User> userList;

    @Override
    public View initLayout() {
        View view = View.inflate(mActivity, R.layout.fragment_user, null);
        return view;
    }

    @Override
    public UserPresenter initPresenter() {
        return new UserPresenter(this);
    }

    @Override
    public void initData() {
        userList = new ArrayList<>();
        presenter.getUserList();
        adapter = new UserAdapter(R.layout.item_message, userList);
        mRecycleview.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecycleview.setAdapter(adapter);
    }

    @Override
    public void initListener() {
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
}
