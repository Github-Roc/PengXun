package cn.peng.pxun.ui.adapter.listview;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.peng.pxun.MyApplication;
import cn.peng.pxun.R;
import cn.peng.pxun.modle.bmob.User;
import cn.peng.pxun.ui.adapter.SuperBaseApapter;
import cn.peng.pxun.ui.adapter.holder.BaseHolder;

/**
 * 联系人数据适配器
 */
public class FriendAdapter extends SuperBaseApapter<User> {

    public FriendAdapter(List<User> dataSets) {
        super(dataSets);
    }

    @Override
    public BaseHolder setHolder() {
        return new ContactHolder();
    }

    class ContactHolder extends BaseHolder<User> {
        @BindView(R.id.iv_message_icon)
        ImageView mIvMessageIcon;
        @BindView(R.id.tv_message_name)
        TextView mTvMessageName;
        @BindView(R.id.tv_message_signature)
        TextView mTvMessageSignature;

        @Override
        public View initHolderView() {
            View view = View.inflate(MyApplication.context, R.layout.item_message, null);
            ButterKnife.bind(this, view);
            return view;
        }

        @Override
        public void bindView() {
            if (!TextUtils.isEmpty(mData.getHeadIcon())){
                Picasso.with(MyApplication.context).load(mData.getHeadIcon()).into(mIvMessageIcon);
            } else {
                if (!TextUtils.isEmpty(mData.getSex())){
                    if ("男".equals(mData.getSex())){
                        mIvMessageIcon.setImageResource(R.drawable.icon_nan);
                    }else if ("女".equals(mData.getSex())){
                        mIvMessageIcon.setImageResource(R.drawable.icon_nv);
                    }
                }
            }
            mTvMessageName.setText(mData.getUsername());
            mTvMessageSignature.setText(mData.getSignaTure());
        }
    }
}
