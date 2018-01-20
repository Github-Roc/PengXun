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
import cn.peng.pxun.modle.bean.ConversationBean;
import cn.peng.pxun.ui.adapter.SuperBaseApapter;
import cn.peng.pxun.ui.adapter.holder.BaseHolder;

/**
 * Created by msi on 2017/9/27.
 */

public class MessageAdapter extends SuperBaseApapter {

    public MessageAdapter(List dataSets) {
        super(dataSets);
    }

    @Override
    public BaseHolder setHolder() {
        return new MessageHolder();
    }

    public class MessageHolder extends BaseHolder<ConversationBean> {
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
            if (mData.isGroup){
                if (!TextUtils.isEmpty(mData.group.getGroupIcon())){
                    Picasso.with(MyApplication.context).load(mData.group.getGroupIcon()).into(mIvMessageIcon);
                }else {
                    mIvMessageIcon.setImageResource(R.drawable.icon_group);
                }
                mTvMessageName.setText(mData.group.getGroupName());
                mTvMessageSignature.setText(mData.lastMsg);
            }else {
                if ("系统消息".equals(mData.user.getUsername())){
                    mIvMessageIcon.setImageResource(R.drawable.icon_sys_message);
                }else if ("智能小白".equals(mData.user.getUsername())){
                    mIvMessageIcon.setImageResource(R.drawable.icon_tuling);
                }else{
                    if (!TextUtils.isEmpty(mData.user.getHeadIcon())){
                        Picasso.with(MyApplication.context).load(mData.user.getHeadIcon()).into(mIvMessageIcon);
                    } else{
                        mIvMessageIcon.setImageResource(R.drawable.icon_nan);
                    }
                }
                mTvMessageName.setText(mData.user.getUsername());
                mTvMessageSignature.setText(mData.lastMsg);
            }
        }
    }


}