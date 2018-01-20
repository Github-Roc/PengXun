package cn.peng.pxun.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.just.library.BaseAgentWebActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.peng.pxun.R;

/**
 * Web页面
 */
public class WebActivity extends BaseAgentWebActivity {

    @BindView(R.id.web_toolbar)
    Toolbar mWebToolbar;
    @BindView(R.id.iv_web_goback)
    ImageView mIvWebGoback;
    @BindView(R.id.tv_web_title)
    TextView mTvWebTitle;

    /** ButterKnife的解绑器 */
    private Unbinder mUnbinder;
    private String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getIntent().getStringExtra("url");
        setContentView(R.layout.activity_web);
        mUnbinder = ButterKnife.bind(this);

        setSupportActionBar(mWebToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTvWebTitle.setText("加载中...");
        mIvWebGoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mAgentWeb != null && mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @NonNull
    @Override
    protected ViewGroup getAgentWebParent() {
        return (ViewGroup) findViewById(R.id.web_layout);
    }

    @Override
    protected void setTitle(WebView view, String title) {
        mTvWebTitle.setText(title);
    }

    @Override
    protected int getIndicatorColor() {
        return Color.parseColor("#FF4081");
    }

    @Override
    protected int getIndicatorHeight() {
        return 3;
    }

    @Nullable
    @Override
    protected String getUrl() {
        return url;
    }
}
