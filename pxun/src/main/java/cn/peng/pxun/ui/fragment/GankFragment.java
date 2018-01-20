package cn.peng.pxun.ui.fragment;

import android.view.View;

import cn.peng.pxun.R;
import cn.peng.pxun.presenter.fragment.GankPresenter;

/**
 * Created by tofirst on 2018/1/3.
 */

public class GankFragment extends BaseFragment<GankPresenter>  {

    @Override
    public View initLayout() {
        View view = View.inflate(mActivity, R.layout.fragment_gank, null);
        return view;
    }

    @Override
    public GankPresenter initPresenter() {
        return new GankPresenter(this);
    }

}
