package cn.peng.pxun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.peng.pxun.presenter.base.BasePresenter;
import cn.peng.pxun.ui.activity.BaseActivity;

/**
 * Fragment的基类
 * @author Peng
 */
public abstract class BaseFragment<P extends BasePresenter> extends Fragment {
    /** 此Fragment所对应的业务操作类 */
    protected P presenter;
    /** Fragment所依赖的Activity */
    protected BaseActivity mActivity;
    // ButterKnife的解绑器
    private Unbinder mUnbinder;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        init();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = initLayout();
        //绑定ButterKnife
        mUnbinder = ButterKnife.bind(this, view);
        initView();
        initData();
        initListener();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //解绑ButterKnife
        if(mUnbinder != null){
            mUnbinder.unbind();
        }
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * 执行初始化操作,
     * @des 子类可选择复写,用来进行初始化操作
     */
    protected void init(){
        mActivity = (BaseActivity) getActivity();
        this.presenter = initPresenter();
    }

    /**
     * 初始化界面视图控件,
     * @des 子类可选择复写,用来初始化界面视图
     */
    protected void initView(){
    }

    /**
     * 初始化界面数据,
     * @des 子类可选择复写,用来初始化界面数据
     */
    protected void initData() {
    }

    /**
     * 初始化界面内的监听,
     * @des 子类可选择复写,初始化界面内的监听
     */
    protected void initListener() {
    }

    /**
     * 初始化Fragment界面视图
     * @des 子类必须复写
     * @return
     */
    public abstract View initLayout();

    /**
     * 初始化和此Fragment绑定的业务类
     * @return
     */
    public abstract P initPresenter();
}
