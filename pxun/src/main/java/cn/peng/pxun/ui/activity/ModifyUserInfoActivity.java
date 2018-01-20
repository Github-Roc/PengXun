package cn.peng.pxun.ui.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import cn.peng.pxun.R;
import cn.peng.pxun.presenter.base.BasePresenter;
import cn.peng.pxun.utils.ToastUtil;

/**
 * 用户信息修改页面
 */
public class ModifyUserInfoActivity extends BaseActivity {
    public static final int TYPE_USERNAME = 2000;
    public static final int TYPE_SIGNATURE = 2001;
    private int modifyType;

    @BindView(R.id.iv_goback)
    ImageView mIvGoback;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_modify_username)
    EditText mEtModifyUsername;
    @BindView(R.id.et_modify_signature)
    EditText mEtModifySignature;
    @BindView(R.id.tv_modify_describe)
    TextView mTvModifyDescribe;
    @BindView(R.id.bt_modify_finish)
    Button mBtModifyFinish;


    @Override
    public int setLayoutRes() {
        return R.layout.activity_modify_info;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        modifyType = getIntent().getIntExtra("modifyType", TYPE_USERNAME);
        String content = getIntent().getStringExtra("content");
        if (modifyType == TYPE_USERNAME) {
            mEtModifyUsername.setVisibility(View.VISIBLE);
            mEtModifySignature.setVisibility(View.GONE);
            mTvTitle.setText("编辑昵称");
            mEtModifyUsername.setText(content);
            mTvModifyDescribe.setText("昵称不能超过12个字符。");
        } else if (modifyType == TYPE_SIGNATURE) {
            mEtModifyUsername.setVisibility(View.GONE);
            mEtModifySignature.setVisibility(View.VISIBLE);
            mTvTitle.setText("编辑个人签名");
            mEtModifySignature.setText(content);
            mTvModifyDescribe.setText("最长可以输入24个字符的个人签名。");
        }
    }

    @Override
    protected void initListener() {
        mIvGoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBtModifyFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = "";
                if (modifyType == TYPE_USERNAME) {
                    content = mEtModifyUsername.getText().toString().trim();
                } else if (modifyType == TYPE_SIGNATURE) {
                    content = mEtModifySignature.getText().toString().trim();
                }
                if (TextUtils.isEmpty(content)){
                    ToastUtil.showToast(mActivity, "请先填写内容");
                    return;
                }

                Intent data = new Intent();
                data.putExtra("modifyType", modifyType);
                data.putExtra("content", content);
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

}
