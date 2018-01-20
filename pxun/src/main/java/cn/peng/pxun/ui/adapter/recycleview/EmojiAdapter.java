package cn.peng.pxun.ui.adapter.recycleview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cn.peng.pxun.R;
import cn.peng.pxun.ui.activity.ChatActivity;

/**
 * Created by tofirst on 2017/10/17.
 */

public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.EmojiViewHolder> {
    private ChatActivity activity;
    private List<Integer> emojiList;
    private OnEmojiClickListener listener;

    public EmojiAdapter(ChatActivity activity){
        this.activity = activity;
        initEmoji();
    }

    private void initEmoji() {
        emojiList = new ArrayList<>();
        emojiList.add(R.drawable.emo_im_angel);
        emojiList.add(R.drawable.emo_im_cool);
        emojiList.add(R.drawable.emo_im_crying);
        emojiList.add(R.drawable.emo_im_embarrassed);
        emojiList.add(R.drawable.emo_im_foot_in_mouth);
        emojiList.add(R.drawable.emo_im_happy);
        emojiList.add(R.drawable.emo_im_kissing);
        emojiList.add(R.drawable.emo_im_laughing);
        emojiList.add(R.drawable.emo_im_lips_are_sealed);
        emojiList.add(R.drawable.emo_im_money_mouth);
        emojiList.add(R.drawable.emo_im_sad);
        emojiList.add(R.drawable.emo_im_surprised);
        emojiList.add(R.drawable.emo_im_tongue_sticking_out);
        emojiList.add(R.drawable.emo_im_undecided);
        emojiList.add(R.drawable.emo_im_winking);
        emojiList.add(R.drawable.emo_im_wtf);
        emojiList.add(R.drawable.emo_im_yelling);
        emojiList.add(R.drawable.emo_im_delete);
    }

    public void setOnEmojiClickListener(OnEmojiClickListener listener){
        this.listener = listener;
    }

    @Override
    public EmojiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(activity, R.layout.item_emoji, null);
        return new EmojiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmojiViewHolder holder, final int position) {
        holder.iv.setImageResource(emojiList.get(position));
        holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.onEmojiClick(emojiList.get(position), position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return emojiList.size();
    }

    /**
     * emoji表情的点击事件
     */
    public interface OnEmojiClickListener{
        void onEmojiClick(int emojoId, int position);
    }

    class EmojiViewHolder extends RecyclerView.ViewHolder{
        ImageView iv;

        public EmojiViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv_item_emoji);
        }
    }
}
