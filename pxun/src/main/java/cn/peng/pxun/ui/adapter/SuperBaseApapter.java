package cn.peng.pxun.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import cn.peng.pxun.ui.adapter.holder.BaseHolder;

/**
 * 数据适配器的基类,
 */
public abstract class SuperBaseApapter<DATA> extends BaseAdapter{
    private List<DATA> dataSets;
    private BaseHolder mHolder;

    public SuperBaseApapter(List<DATA> dataSets){
        this.dataSets = dataSets;
    }

    @Override
    public int getCount() {
        if (dataSets != null && dataSets.size() > 0){
            return dataSets.size();
        }
        return 0;
    }

    @Override
    public DATA getItem(int i) {
        if (dataSets != null){
            return  dataSets.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup group) {
        BaseHolder holder = null;
        if (view == null) {
            holder = setHolder();
            view = holder.mHolderView;
        } else {
            holder = (BaseHolder) view.getTag();
        }
        holder.setDataBindView(dataSets.get(i));
        return view;
    }

    public void setDataSets(List<DATA> list){
        dataSets = list;
        notifyDataSetChanged();
    }

    /**
     * 设置ViewHolder
     * @return
     */
    public abstract BaseHolder setHolder();
}
