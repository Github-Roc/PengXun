package cn.peng.pxun.ui.adapter.recycleview;

import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.peng.pxun.R;
import cn.peng.pxun.modle.bmob.Posts;
import cn.peng.pxun.modle.greendao.LocalPosts;

/**
 * Created by msi on 2017/11/25.
 * @author pengpeng
 */

public class SquareAdapter extends BaseQuickAdapter<LocalPosts, BaseViewHolder> {

    public SquareAdapter(int layoutResId, @Nullable List<LocalPosts> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LocalPosts item) {
        helper.setText(R.id.tv_posts_username, item.getPublishUserName());
        if (!TextUtils.isEmpty(item.getPublishUserIcon())){
            Picasso.with(mContext).load(item.getPublishUserIcon()).into((ImageView) helper.getView(R.id.iv_posts_usericon));
        }else {
            ((ImageView) helper.getView(R.id.iv_posts_usericon)).setImageResource(R.drawable.icon_nan);
        }

        helper.setText(R.id.tv_posts_content, Html.fromHtml(item.getContent()));
        if (Posts.CONTENT_TYPE_PIC.equals(item.getContentType())){
            helper.setGone(R.id.iv_posts_pic,true);
            Picasso.with(mContext).load(item.getPicPath()).into((ImageView) helper.getView(R.id.iv_posts_pic));
        }else {
            helper.setGone(R.id.iv_posts_pic,false);
        }
        helper.addOnClickListener(R.id.tv_posts_username)
                .addOnClickListener(R.id.iv_posts_usericon)
                .addOnClickListener(R.id.iv_posts_pic);
    }
}
