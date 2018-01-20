package cn.peng.pxun.ui.adapter.recycleview;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.peng.pxun.R;
import cn.peng.pxun.modle.bmob.User;

/**
 * Created by msi on 2017/11/3.
 */

public class UserAdapter extends BaseQuickAdapter<User, BaseViewHolder> {

    public UserAdapter(@LayoutRes int layoutResId, @Nullable List<User> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, User item) {
        helper.setText(R.id.tv_message_name, item.getUsername());
        helper.setText(R.id.tv_message_signature, item.getSignaTure());

        if (!TextUtils.isEmpty(item.getHeadIcon())){
            Picasso.with(mContext).load(item.getHeadIcon()).into((ImageView) helper.getView(R.id.iv_message_icon));
        }else {
            ((ImageView) helper.getView(R.id.iv_message_icon)).setImageResource(R.drawable.icon_nan);
        }
    }
}
