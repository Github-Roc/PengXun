package cn.peng.pxun.ui.adapter.recycleview;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.peng.pxun.R;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.modle.bmob.Group;
import cn.peng.pxun.modle.bmob.User;
import cn.peng.pxun.presenter.activity.SearchPresenter;
import cn.peng.pxun.ui.activity.DetailedActivity;
import cn.peng.pxun.ui.activity.SearchActivity;
import cn.peng.pxun.utils.ThreadUtil;
import cn.peng.pxun.utils.ToastUtil;

import static cn.peng.pxun.ui.activity.SearchActivity.SEARCH_GROUP;
import static cn.peng.pxun.ui.activity.SearchActivity.SEARCH_USER;

/**
 * Created by msi on 2017/9/23.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchHolder>{

    private SearchActivity activity;
    private SearchPresenter presenter;
    private List<User> userData;
    private List<Group> groupData;
    private int searchType;

    public SearchAdapter(SearchActivity activity, SearchPresenter presenter){
        this.activity = activity;
        this.presenter = presenter;
    }

    public void setUserData(List<User> data){
        searchType = SEARCH_USER;
        this.userData = data;
        notifyDataSetChanged();
    }

    public void setGroupData(List<Group> data){
        searchType = SEARCH_GROUP;
        this.groupData = data;
        notifyDataSetChanged();
    }

    @Override
    public SearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(activity, R.layout.item_search,null);
        return new SearchHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchHolder holder, int position) {
        if(searchType == SEARCH_USER){
            final User user = userData.get(position);
            if (!TextUtils.isEmpty(user.getHeadIcon())){
                Picasso.with(activity).load(user.getHeadIcon()).into(holder.mIvMessageIcon);
            }
            holder.mTvMessageName.setText(user.getUsername());
            holder.mTvMessageSignature.setText(user.getSignaTure());
            holder.mIvMessageIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, DetailedActivity.class);
                    if (AppConfig.getUserId(user).equals(AppConfig.getUserId(AppConfig.appUser))){
                        intent.putExtra("isMe",true);
                    } else {
                        intent.putExtra("isMe",false);
                    }
                    intent.putExtra("user",user);
                    activity.startActivity(intent);
                }
            });
            holder.mIvAddContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.addFriend(user);
                }
            });
        }else if (searchType == SEARCH_GROUP){
            final Group group = groupData.get(position);
            holder.mTvMessageName.setText(group.getGroupName());
            holder.mTvMessageSignature.setText(group.getGroupDesc());
            holder.mIvAddContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if("20000".equals(group.getGroupType())){
                        ToastUtil.showToast(activity, "该群为私有群,不允许加入!");
                        return;
                    }
                    try {
                        EMClient.getInstance().groupManager().joinGroup(group.getGroupNum());
                        ThreadUtil.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showToast(activity, "请求发送成功");
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        ThreadUtil.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showToast(activity, "请求发送失败");
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (searchType == SEARCH_GROUP){
            if (groupData == null){
                return 0;
            }
            return groupData.size();
        }else {
            if (userData == null){
                return 0;
            }
            return userData.size();
        }
    }

    class SearchHolder extends RecyclerView.ViewHolder {

        public ImageView mIvMessageIcon;
        public TextView mTvMessageName;
        public TextView mTvMessageSignature;
        public ImageView mIvAddContact;

        public SearchHolder(View itemView) {
            super(itemView);
            mIvMessageIcon = (ImageView) itemView.findViewById(R.id.iv_message_icon);
            mTvMessageName = (TextView) itemView.findViewById(R.id.tv_message_name);
            mTvMessageSignature = (TextView) itemView.findViewById(R.id.tv_message_signature);
            mIvAddContact = (ImageView) itemView.findViewById(R.id.iv_add_contact);
        }
    }
}
