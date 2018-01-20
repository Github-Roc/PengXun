package cn.peng.pxun.ui.adapter.holder;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.peng.pxun.MyApplication;
import cn.peng.pxun.R;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.modle.bmob.User;
import cn.peng.pxun.modle.greendao.Message;
import cn.peng.pxun.ui.activity.BigPicActivity;
import cn.peng.pxun.ui.activity.ChatActivity;
import cn.peng.pxun.ui.activity.DetailedActivity;
import cn.peng.pxun.ui.view.ChatView;

/**
 * ChatActivity的ViewHolder
 */
public class ChatHolder extends BaseHolder<Message> {
    private ChatActivity activity;
    @BindView(R.id.tv_chat_date)
    TextView mTvChatDate;
    @BindView(R.id.ll_chat_reply)
    LinearLayout mLlChatReply;
    @BindView(R.id.iv_chat_replyicon)
    ImageView mIvChatReplyicon;
    @BindView(R.id.ll_chat_ask)
    LinearLayout mLlChatAsk;
    @BindView(R.id.iv_chat_askicon)
    ImageView mIvChatAskicon;
    @BindView(R.id.cv_chat_reply)
    ChatView mCvChatReply;
    @BindView(R.id.cv_chat_ask)
    ChatView mCvChatAsk;

    private User toChatUser;

    public ChatHolder(ChatActivity activity, User toChatUser) {
        this.activity = activity;
        this.toChatUser = toChatUser;
    }

    @Override
    public View initHolderView() {
        View view = View.inflate(MyApplication.context, R.layout.item_chat, null);
        ButterKnife.bind(this, view);
        setOnClick();
        return view;
    }

    private void setOnClick() {
        mIvChatAskicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, DetailedActivity.class);
                intent.putExtra("isMe",true);
                activity.startActivity(intent);
            }
        });
        mIvChatReplyicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, DetailedActivity.class);
                intent.putExtra("isMe",false);
                intent.putExtra("user",toChatUser);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public void bindView() {
        if (AppConfig.getUserId(AppConfig.appUser).equalsIgnoreCase(mData.fromUserID)) {
            mLlChatReply.setVisibility(View.GONE);
            mTvChatDate.setVisibility(View.VISIBLE);
            mLlChatAsk.setVisibility(View.VISIBLE);

            if (!TextUtils.isEmpty(AppConfig.appUser.getHeadIcon())){
                Picasso.with(activity).load(AppConfig.appUser.getHeadIcon()).into(mIvChatAskicon);
            }
            mTvChatDate.setText(mData.date);
            mCvChatAsk.removeAllViews();
            if (mData.messageType != null){
                if (mData.messageType == Message.TEXT_TYPE){
                    TextView tv = mCvChatAsk.getTextView(16,Color.parseColor("#FFFFFF"));
                    tv.setText(mData.message);
                }else if (mData.messageType == Message.PIC_TYPE){
                    //图片消息
                }else if (mData.messageType == Message.SPEECH_TYPE){
                    //语音消息
                }else {
                    TextView tv = mCvChatAsk.getTextView(16,Color.parseColor("#FFFFFF"));
                    tv.setText(mData.message);
                }
            }
        } else {
            mTvChatDate.setVisibility(View.GONE);
            mLlChatAsk.setVisibility(View.GONE);
            mLlChatReply.setVisibility(View.VISIBLE);

            if (mData.isTuring){
                mIvChatReplyicon.setImageResource(R.drawable.icon_tuling);
            } else{
                if (!TextUtils.isEmpty(toChatUser.getHeadIcon())){
                    Picasso.with(activity).load(toChatUser.getHeadIcon()).into(mIvChatReplyicon);
                }else {
                    mIvChatReplyicon.setImageResource(R.drawable.icon_nan);
                }
            }

            mCvChatReply.removeAllViews();
            if (mData.messageType != null){
                if (mData.messageType == Message.TEXT_TYPE ) {
                    TextView tv = mCvChatReply.getTextView(16, Color.parseColor("#1800FF"));
                    tv.setText(mData.message);
                }else if (mData.messageType == Message.PIC_TYPE) {
                    TextView tv = mCvChatReply.getTextView(14,Color.parseColor("#1818FF"));
                    final ImageView iv = mCvChatReply.getImageView(75,100);
                    tv.setText(mData.message);
                    Picasso.with(activity)
                            .load(mData.picURL)
                            .resize(65, 100)
                            .centerCrop()
                            .placeholder(R.drawable.loding_pic)
                            .error(R.drawable.error_pic)
                            .into(iv);
                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int location[] = new int[2];
                            iv.getLocationOnScreen(location);

                            Intent intent = new Intent(activity,BigPicActivity.class);
                            intent.putExtra("left", location[0]);
                            intent.putExtra("top", location[1]);
                            intent.putExtra("height", iv.getHeight());
                            intent.putExtra("width", iv.getWidth());
                            intent.putExtra("url", mData.picURL);

                            activity.startActivity(intent);
                            activity.overridePendingTransition(0, 0);
                        }
                    });
                }else if (mData.messageType == Message.SPEECH_TYPE){
                    //语音消息
                }else {
                    TextView tv = mCvChatReply.getTextView(16,Color.parseColor("#1800FF"));
                    tv.setText(mData.message);
                }
            }
        }
    }
}
