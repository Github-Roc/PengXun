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
import cn.peng.pxun.modle.bmob.Group;
import cn.peng.pxun.ui.adapter.SuperBaseApapter;
import cn.peng.pxun.ui.adapter.holder.BaseHolder;

/**
 * Created by msi on 2016/12/26.
 */
public class GroupAdapter  extends SuperBaseApapter<Group> {

    public GroupAdapter(List<Group> dataSets) {
        super(dataSets);
    }

    @Override
    public GroupHolder setHolder() {
        return new GroupHolder();
    }

    class GroupHolder extends BaseHolder<Group> {
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
            if (!TextUtils.isEmpty(mData.getGroupIcon())){
                Picasso.with(MyApplication.context).load(mData.getGroupIcon()).into(mIvMessageIcon);
            }else {
                mIvMessageIcon.setImageResource(R.drawable.icon_group);
            }
            mTvMessageName.setText(mData.getGroupName());
            mTvMessageSignature.setText(mData.getGroupDesc());
        }
    }
}
