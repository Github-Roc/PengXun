package cn.peng.pxun.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import cn.peng.pxun.R;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.presenter.activity.SettingPresenter;
import cn.peng.pxun.utils.ToastUtil;

/**
 * 设置页面
 */
public class SettingActivity extends BaseActivity<SettingPresenter> {

    @BindView(R.id.iv_title_goback)
    ImageView mIvTitleGoback;
    @BindView(R.id.tv_title_text)
    TextView mTvTieleText;
    @BindView(R.id.ll_setting_clear)
    LinearLayout mLlSettingClear;
    @BindView(R.id.ll_setting_feedback)
    LinearLayout mLlSettingFeedback;
    @BindView(R.id.ll_setting_version)
    LinearLayout mLlSettingVersion;
    @BindView(R.id.tv_setting_version)
    TextView mTvSettingVersion;
    @BindView(R.id.tv_setting_logout)
    TextView mTvSettingLogout;


    @Override
    public int setLayoutRes() {
        return R.layout.activity_setting;
    }

    @Override
    public SettingPresenter initPresenter() {
        return new SettingPresenter(this);
    }

    @Override
    protected void initView() {
        super.initView();
        mIvTitleGoback.setVisibility(View.VISIBLE);
        mTvTieleText.setText("设置");
    }

    @Override
    protected void initListener() {
        mIvTitleGoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLlSettingClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(mActivity, "缓存清理成功");
            }
        });
        mLlSettingFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, FeedbackActivity.class);
                startActivity(intent);
            }
        });
        mLlSettingVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(mActivity, "当前已经是最新版本");
            }
        });
        mTvSettingLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingDialog("正在注销...");
                presenter.logout();
            }
        });
    }

    @Override
    protected void initData() {
        try {
            String versionName = presenter.getVersionName();
            mTvSettingVersion.setText("V"+versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            mTvSettingVersion.setText("版本号解析错误");
        }

    }

    /**
     * 注销成功
     */
    public void onLogoutSuccess(){
        loadingDialog.cancel();
        AppConfig.friends.clear();
        AppConfig.groups.clear();
        AppConfig.conversations.clear();
        Intent intent = new Intent(mActivity, LoginActivity.class);
        startActivity(intent);
        setResult(RESULT_OK);
        finish();
    }
}
