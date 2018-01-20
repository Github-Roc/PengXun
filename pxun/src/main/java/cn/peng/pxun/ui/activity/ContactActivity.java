package cn.peng.pxun.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.peng.pxun.R;
import cn.peng.pxun.presenter.base.BasePresenter;
import cn.peng.pxun.ui.adapter.viewpager.ContactPagerAdapter;
import cn.peng.pxun.ui.fragment.BaseFragment;
import cn.peng.pxun.ui.fragment.FriendFragment;
import cn.peng.pxun.ui.fragment.GroupFragment;

/**
 * 联系人（好友，群组）页面
 */
public class ContactActivity extends BaseActivity {

    @BindView(R.id.iv_title_goback)
    ImageView mIvTitleGoback;
    @BindView(R.id.tv_title_text)
    TextView mTvTitleText;
    @BindView(R.id.iv_title_more)
    ImageView mIvTitleMore;
    @BindView(R.id.tab_contact)
    TabLayout mTabContact;
    @BindView(R.id.vp_contact)
    ViewPager mVpContact;

    private AlertDialog.Builder builder;
    private List<BaseFragment> fragmentList;

    @Override
    public int setLayoutRes() {
        return R.layout.activity_contact;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        super.initView();
        initFragmentList();
        initDialog();
        mIvTitleGoback.setVisibility(View.VISIBLE);
        mIvTitleMore.setVisibility(View.VISIBLE);

        ContactPagerAdapter mAdapter = new ContactPagerAdapter(getSupportFragmentManager(), fragmentList);
        mVpContact.setAdapter(mAdapter);//给ViewPager设置适配器
        mTabContact.setupWithViewPager(mVpContact);//将TabLayout和ViewPager关联起来。

        int position = getIntent().getIntExtra("position", 0);
        mVpContact.setCurrentItem(position);
        changeText(position);
    }

    @Override
    protected void initListener() {
        mIvTitleGoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mIvTitleMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.show();
            }
        });
        mVpContact.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeText(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initFragmentList() {
        FriendFragment friendFragment = new FriendFragment();
        GroupFragment groupFragment = new GroupFragment();
        fragmentList = new ArrayList<>();
        fragmentList.add(friendFragment);
        fragmentList.add(groupFragment);
    }

    private void initDialog() {
        String items[] = {"添加好友", "加入群组", "创建群组"};

        builder = new AlertDialog.Builder(this);
        builder.setTitle("选择操作");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                Intent intent = new Intent();
                if (which == 0){
                    intent.setClass(ContactActivity.this, SearchActivity.class);
                    intent.putExtra("searchType", SearchActivity.SEARCH_USER);
                }else if (which == 1){
                    intent.setClass(ContactActivity.this, SearchActivity.class);
                    intent.putExtra("searchType", SearchActivity.SEARCH_GROUP);
                }else if (which == 2){
                    intent.setClass(ContactActivity.this, CreateGroupActivity.class);
                }
                startActivity(intent);
            }
        });
        builder.create();
    }

    private void changeText(int position) {
        if (position == 0) {
            mTvTitleText.setText("好友");
        } else if (position == 1) {
            mTvTitleText.setText("群组");
        }
    }
}
