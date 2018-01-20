package cn.peng.pxun.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.peng.pxun.R;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.modle.bmob.User;
import cn.peng.pxun.presenter.fragment.FriendPresenter;
import cn.peng.pxun.ui.activity.ChatActivity;
import cn.peng.pxun.ui.adapter.listview.FriendAdapter;
import cn.peng.pxun.ui.view.SuperListView;
import cn.peng.pxun.utils.LogUtil;
import cn.peng.pxun.utils.ThreadUtil;

/**
 * 联系人界面的Fragment
 */
public class FriendFragment extends BaseFragment<FriendPresenter> {

    @BindView(R.id.lv_contact)
    SuperListView mLvContact;
    @BindView(R.id.page_empty)
    View emptyView;
    @BindView(R.id.tv_empty_text)
    TextView tvEmptyText;

    private List<User> friendList;
    private FriendAdapter mAdapter;

    @Override
    protected void init() {
        super.init();
        //EventBus.getDefault().register(this);
        friendList = new ArrayList<>();
    }

    @Override
    public FriendPresenter initPresenter() {
        return new FriendPresenter(this);
    }

    @Override
    public View initLayout() {
        View view = View.inflate(mActivity, R.layout.fragment_friend, null);
        return view;
    }

    @Override
    protected void initView() {
        tvEmptyText.setText("您还没有好友");
        mAdapter = new FriendAdapter(friendList);
        mLvContact.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        if (AppConfig.friends != null && AppConfig.friends.size() > 0){
            friendList = AppConfig.friends;
            mAdapter.setDataSets(friendList);
        }else {
            mLvContact.startRefresh();
            presenter.getFriendList();
        }
    }

    @Override
    protected void initListener() {
        mLvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = friendList.get(position-1);

                Intent intent = new Intent(mActivity, ChatActivity.class);
                intent.putExtra("isGroup", false);
                intent.putExtra("toChatUser", user);
                startActivity(intent);
            }
        });
        mLvContact.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final User user = friendList.get(position-1);
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle("系统消息");
                builder.setMessage("您确定要删除好友" + user.getUsername() + "吗");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ThreadUtil.runOnSubThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    EMClient.getInstance().contactManager().deleteContact(AppConfig.getUserId(user));
                                    friendList.remove(position);
                                    AppConfig.friends.remove(position);
                                    mAdapter.notifyDataSetChanged();
                                    presenter.showToast("删除好友成功");
                                } catch (HyphenateException e) {
                                    e.printStackTrace();
                                    LogUtil.e(e.toString());
                                }
                            }
                        });
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();
                return true;
            }
        });
        mLvContact.setOnLoadDataListener(new SuperListView.OnLoadDataListener() {
            @Override
            public void onRefresh() {
                emptyView.setVisibility(View.GONE);
                if (!presenter.isLoadingFriend()){
                    friendList.clear();
                    presenter.getFriendList();
                }else {
                    mLvContact.onRefreshFinish();
                }
            }
        });
    }

    public void onLoadFinish() {
        if (mLvContact != null && mAdapter != null && emptyView != null) {
            if (mLvContact.isRefresh()) {
                mLvContact.onRefreshFinish();
            }
            if (AppConfig.friends != null && AppConfig.friends.size() > 0){
                friendList = AppConfig.friends;
                mAdapter.setDataSets(friendList);
            }else {
                emptyView.setVisibility(View.VISIBLE);
            }
        }
    }

}
