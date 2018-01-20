package cn.peng.pxun.ui.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.peng.pxun.R;
import cn.peng.pxun.presenter.base.BasePresenter;
import cn.peng.pxun.ui.adapter.viewpager.HomeAdapter;

/**
 * Created by tofirst on 2017/10/27.
 */

public class HomeFragment extends BaseFragment {

    @BindView(R.id.tab_title)
    TabLayout mTabTitle;
    @BindView(R.id.tv_title_text)
    TextView mTvTitleText;
    @BindView(R.id.vp_fragment_home)
    ViewPager mVpFragmentHome;

    private List<BaseFragment> fragmentList;

    @Override
    public View initLayout() {
        View view = View.inflate(mActivity, R.layout.fragment_home, null);
        initFragmentList();
        return view;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initData() {
        mTabTitle.setVisibility(View.VISIBLE);
        mTvTitleText.setVisibility(View.GONE);
        HomeAdapter mAdapter = new HomeAdapter(getChildFragmentManager(), fragmentList);
        //给ViewPager设置适配器
        mVpFragmentHome.setAdapter(mAdapter);
        //将TabLayout和ViewPager关联起来。
        mTabTitle.setupWithViewPager(mVpFragmentHome);
        mVpFragmentHome.setCurrentItem(0);
    }

    /**
     * 初始化Fragment
     */
    private void initFragmentList() {
        SquareFragment firstFragment = new SquareFragment();
        MovieFragment movieFragment = new MovieFragment();
        GankFragment gankFragment = new GankFragment();
        fragmentList = new ArrayList<>();
        fragmentList.add(firstFragment);
        fragmentList.add(movieFragment);
        fragmentList.add(gankFragment);
    }
}
