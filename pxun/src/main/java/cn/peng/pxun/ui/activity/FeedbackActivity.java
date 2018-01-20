package cn.peng.pxun.ui.activity;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import cn.peng.pxun.R;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.presenter.activity.FeedbackPresenter;
import cn.peng.pxun.ui.view.picker.OptionPicker;
import cn.peng.pxun.utils.ToastUtil;

/**
 * 意见反馈界面
 */
public class FeedbackActivity extends BaseActivity<FeedbackPresenter> {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.iv_goback)
    ImageView mIvGoback;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_feedback_type)
    TextView mTvFeedbackType;
    @BindView(R.id.et_feedback_content)
    EditText mEtFeedbackContent;
    @BindView(R.id.bt_feedback_submit)
    Button mBtFeedbackSubmit;

    private OptionPicker typePicker;

    @Override
    public int setLayoutRes() {
        return R.layout.activity_feedback;
    }

    @Override
    public FeedbackPresenter initPresenter() {
        return new FeedbackPresenter(this);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        initSexPicker();
        mTvTitle.setText("意见反馈");
    }

    @Override
    protected void initListener() {
        mIvGoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTvFeedbackType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typePicker.show();
            }
        });
        mBtFeedbackSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedbackType = mTvFeedbackType.getText().toString().trim();
                if ("请选择反馈原因".equals(feedbackType)){
                    ToastUtil.showToast(FeedbackActivity.this,"请先选择反馈原因!");
                    return;
                }
                String feedbackContent = mEtFeedbackContent.getText().toString().trim();
                if (TextUtils.isEmpty(feedbackContent)){
                    ToastUtil.showToast(FeedbackActivity.this,"意见不能为空哦!");
                    return;
                }

                showLoadingDialog("请稍候...");
                presenter.submitFeedback(feedbackType, feedbackContent);
            }
        });
    }

    /**
     * 初始化性别选择器
     */
    private void initSexPicker() {
        typePicker = new OptionPicker(this, AppConfig.FEEDBACK_TYPE);
        typePicker.setCycleDisable(true);
        typePicker.setTitleText( "请选择" );
        typePicker.setTitleTextSize(14);
        typePicker.setCancelTextSize(12);
        typePicker.setSubmitTextSize(12);
        typePicker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                mTvFeedbackType.setText(item);
            }
        });
    }

    /**
     * 反馈意见提交成功
     */
    public void onSubmitSuccess() {
        dismissLoadingDialog();
        mTvFeedbackType.setText("请选择反馈原因");
        mEtFeedbackContent.setText("");
        ToastUtil.showToast(FeedbackActivity.this,"您的意见提交成功");
    }
}
