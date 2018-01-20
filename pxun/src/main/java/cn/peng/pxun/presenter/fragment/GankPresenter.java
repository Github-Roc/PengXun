package cn.peng.pxun.presenter.fragment;

import cn.peng.pxun.presenter.base.BaseRetrofitPresenter;
import cn.peng.pxun.ui.fragment.GankFragment;

/**
 * Created by tofirst on 2018/1/3.
 */

public class GankPresenter extends BaseRetrofitPresenter {

    private GankFragment mGankFragment;

    public GankPresenter(GankFragment fragment) {
        super(fragment);
        this.mGankFragment = fragment;
    }

}
