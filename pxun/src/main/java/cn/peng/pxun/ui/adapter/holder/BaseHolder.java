package cn.peng.pxun.ui.adapter.holder;

import android.view.View;

/**
 * ViewHolder的基类,
 */
public abstract class BaseHolder<DATA> {
    public View mHolderView;
    protected DATA mData;

    public BaseHolder(){
        mHolderView = initHolderView();
        mHolderView.setTag(this);
    }

    /**
     * 设置数据绑定View
     */
    public void setDataBindView(DATA data) {
        mData = data;
        bindView();
    }

    /**
     * 初始化View
     * @return
     */
    public abstract View initHolderView();

    /**
     * 绑定视图和数据
     */
    public abstract void bindView();
}
