package cn.peng.pxun.ui.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import cn.peng.pxun.R;
import cn.peng.pxun.modle.bmob.SysMessage;
import cn.peng.pxun.presenter.activity.SysMessagePresenter;
import cn.peng.pxun.ui.adapter.recycleview.SysMessageAdapter;
import cn.peng.pxun.utils.ToastUtil;

/**
 * 系统消息页面
 */
public class SysMessageActivity extends BaseActivity<SysMessagePresenter> {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.iv_goback)
    ImageView mIvGoback;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.sfl_sys_message)
    SwipeRefreshLayout mSflSysMessage;
    @BindView(R.id.rv_sys_message)
    RecyclerView mRvSysMessage;

    private SysMessageAdapter mAdapter;

    @Override
    protected void init() {
        super.init();
        presenter.getSysMessageList();
    }

    @Override
    public int setLayoutRes() {
        return R.layout.activity_sys_message;
    }

    @Override
    public SysMessagePresenter initPresenter() {
        return new SysMessagePresenter(this);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTvTitle.setText("系统消息");
        mRvSysMessage.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void initData() {
        mAdapter = new SysMessageAdapter(this, presenter);
        mRvSysMessage.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        mIvGoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSflSysMessage.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSflSysMessage.setRefreshing(true);
                presenter.getSysMessageList();
            }
        });
    }

    /**
     * 数据加载完成，刷新显示界面
     * @param list
     */
    public void onloadFinish(List<SysMessage> list) {
        if (mSflSysMessage.isRefreshing()){
            mSflSysMessage.setRefreshing(false);
        }
        if (list != null && list.size() > 0) {
            mAdapter.setSysMessageList(list);
        } else {
            ToastUtil.showToast(this, "暂无系统消息");
        }
    }
}
