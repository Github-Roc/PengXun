package cn.peng.pxun.ui.adapter.recycleview;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.peng.pxun.R;
import cn.peng.pxun.modle.bean.MovieBean;

/**
 * Created by tofirst on 2017/10/30.
 */

public class MovieAdapter extends BaseQuickAdapter<MovieBean.SubjectsBean, BaseViewHolder> {

    public MovieAdapter(@LayoutRes int layoutResId, @Nullable List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, MovieBean.SubjectsBean subjectsBean) {
        baseViewHolder.setText(R.id.tv_movie_name, subjectsBean.title);

        //String[] picUrl = {subjectsBean.images.small, subjectsBean.images.medium, subjectsBean.images.large};
        Picasso.with(mContext).load(subjectsBean.images.small).into((ImageView) baseViewHolder.getView(R.id.iv_movie_pic));
    }
}
