package cn.peng.pxun.ui.adapter.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import cn.peng.pxun.ui.fragment.BaseFragment;

/**
 * Created by msi on 2016/12/21.
 */
public class HomeAdapter extends FragmentPagerAdapter {
    List<BaseFragment> list;
    private String[] tabs = {
            "推荐","电影","干货"
    };

    public HomeAdapter(FragmentManager fm, List<BaseFragment> fragmentList) {
        super(fm);
        list = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }
}
