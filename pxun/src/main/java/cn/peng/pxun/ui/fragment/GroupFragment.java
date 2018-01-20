package cn.peng.pxun.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import cn.peng.pxun.R;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.modle.bmob.Group;
import cn.peng.pxun.presenter.fragment.GroupPresenter;
import cn.peng.pxun.ui.activity.ChatActivity;
import cn.peng.pxun.ui.adapter.listview.GroupAdapter;
import cn.peng.pxun.ui.view.SuperListView;

/**
 * 群组页面
 * Created by msi on 2016/12/21.
 */
public class GroupFragment extends BaseFragment<GroupPresenter> {
    @BindView(R.id.lv_group)
    SuperListView mLvGroup;
    @BindView(R.id.page_empty)
    View emptyView;
    @BindView(R.id.tv_empty_text)
    TextView tvEmptyText;

    private List<Group> groupList;
    private GroupAdapter mAdapter;

    @Override
    protected void init() {
        super.init();
        //EventBus.getDefault().register(this);
    }

    @Override
    public View initLayout() {
        View view = View.inflate(mActivity, R.layout.fragment_group, null);
        return view;
    }

    @Override
    public GroupPresenter initPresenter() {
        return new GroupPresenter(this);
    }

    @Override
    protected void initView() {
        tvEmptyText.setText("您还没有群组");
        mAdapter = new GroupAdapter(groupList);
        mLvGroup.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        if (AppConfig.groups != null && AppConfig.groups.size() > 0){
            groupList = AppConfig.groups;
            mAdapter.setDataSets(groupList);
        }else {
            mLvGroup.startRefresh();
            presenter.getGroupList();
        }
    }

    @Override
    protected void initListener() {
        mLvGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Group group = groupList.get(position-1);
                Intent intent = new Intent(mActivity, ChatActivity.class);
                intent.putExtra("isGroup", true);
                intent.putExtra("toChatGroup", group);
                startActivity(intent);
            }
        });
        mLvGroup.setOnLoadDataListener(new SuperListView.OnLoadDataListener() {
            @Override
            public void onRefresh() {
                emptyView.setVisibility(View.GONE);
                presenter.getGroupList();
            }
        });
    }

    public void onLoadFinish() {
        if (mLvGroup != null && mAdapter != null && emptyView != null) {
            if (mLvGroup.isRefresh()) {
                mLvGroup.onRefreshFinish();
            }
            if (AppConfig.groups != null && AppConfig.groups.size() > 0){
                groupList = AppConfig.groups;
                mAdapter.setDataSets(groupList);
            }else {
                emptyView.setVisibility(View.VISIBLE);
            }
        }
    }

}
