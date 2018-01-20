package cn.peng.pxun.ui.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import cn.peng.pxun.R;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.presenter.activity.MainPresenter;
import cn.peng.pxun.ui.fragment.FindFragment;
import cn.peng.pxun.ui.fragment.HomeFragment;
import cn.peng.pxun.ui.fragment.MessageFragment;
import cn.peng.pxun.ui.fragment.MineFragment;
import cn.peng.pxun.utils.ToastUtil;

public class MainActivity extends BaseActivity<MainPresenter> {

    @BindView(R.id.fl_main)
    FrameLayout mFlMain;
    @BindView(R.id.ll_main_home)
    LinearLayout mLlMainHome;
    @BindView(R.id.iv_main_home)
    ImageView mIvMainHome;
    @BindView(R.id.tv_main_home)
    TextView mTvMainHome;
    @BindView(R.id.ll_main_find)
    LinearLayout mLlMainFind;
    @BindView(R.id.iv_main_find)
    ImageView mIvMainFind;
    @BindView(R.id.tv_main_find)
    TextView mTvMainFind;
    @BindView(R.id.ll_main_message)
    LinearLayout mLlMainMessage;
    @BindView(R.id.iv_main_message)
    ImageView mIvMainMessage;
    @BindView(R.id.tv_main_message)
    TextView mTvMainMessage;
    @BindView(R.id.ll_main_mine)
    LinearLayout mLlMainMine;
    @BindView(R.id.iv_main_mine)
    ImageView mIvMainMine;
    @BindView(R.id.tv_main_mine)
    TextView mTvMainMine;
    @BindView(R.id.iv_main_create)
    ImageView mIvMainCreate;

    private FragmentManager mFm;
    private HomeFragment homeFragment;
    private FindFragment findFragment;
    private MessageFragment messageFragment;
    private MineFragment mineFragment;
    private static Boolean isExit = false;

    @Override
    public int setLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    public MainPresenter initPresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected void init() {
        super.init();

        if (!AppConfig.isInitTuring){
            presenter.initTuLing();
        }
    }

    @Override
    protected void initView() {
        super.initView();

        if (homeFragment == null) {
            homeFragment = new HomeFragment();
        }
        if (findFragment == null) {
            findFragment = new FindFragment();
        }
        if (messageFragment == null) {
            messageFragment = new MessageFragment();
        }
        if (mineFragment == null) {
            mineFragment = new MineFragment();
        }

        //初始化Fragment
        mFm = getSupportFragmentManager();
        FragmentTransaction ft = mFm.beginTransaction();
        ft.replace(R.id.fl_main, homeFragment);
        ft.commit();

        //刷新ActionBar和按钮标签
        refreshTab(mTvMainHome, mIvMainHome, mTvMainFind, mIvMainFind, mTvMainMessage, mIvMainMessage, mTvMainMine, mIvMainMine);
    }

    @Override
    protected void initListener() {
        mLlMainHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = mFm.beginTransaction();
                ft.replace(R.id.fl_main, homeFragment);
                ft.commit();
                refreshTab(mTvMainHome, mIvMainHome, mTvMainFind, mIvMainFind, mTvMainMessage, mIvMainMessage, mTvMainMine, mIvMainMine);
            }
        });
        mLlMainFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = mFm.beginTransaction();
                ft.replace(R.id.fl_main, findFragment);
                ft.commit();
                refreshTab(mTvMainFind, mIvMainFind, mTvMainHome, mIvMainHome, mTvMainMessage, mIvMainMessage, mTvMainMine, mIvMainMine);
            }
        });
        mLlMainMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = mFm.beginTransaction();
                ft.replace(R.id.fl_main, messageFragment);
                ft.commit();
                refreshTab(mTvMainMessage, mIvMainMessage, mTvMainHome, mIvMainHome, mTvMainFind, mIvMainFind, mTvMainMine, mIvMainMine);
            }
        });
        mLlMainMine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = mFm.beginTransaction();
                ft.replace(R.id.fl_main, mineFragment);
                ft.commit();
                refreshTab(mTvMainMine, mIvMainMine, mTvMainHome, mIvMainHome, mTvMainFind, mIvMainFind, mTvMainMessage, mIvMainMessage);
            }
        });
        mIvMainCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, PublishPostsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Timer tExit = null;
            if (isExit == false) {
                isExit = true; // 准备退出
                ToastUtil.showToast(mActivity, "在按一次退出鹏讯");

                tExit = new Timer();
                tExit.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        isExit = false; // 取消退出
                    }
                }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

            } else {
                finish();
                System.exit(0);
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConfig.MINETOPIC || requestCode == AppConfig.MINETOSETTING){
            mineFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 刷新按钮标签的UI
     */
    private void refreshTab(TextView tv1, ImageView iv1, TextView tv2, ImageView iv2, TextView tv3, ImageView iv3, TextView tv4, ImageView iv4) {
        tv1.setSelected(true);
        iv1.setSelected(true);
        tv2.setSelected(false);
        iv2.setSelected(false);
        tv3.setSelected(false);
        iv3.setSelected(false);
        tv4.setSelected(false);
        iv4.setSelected(false);
    }
}
