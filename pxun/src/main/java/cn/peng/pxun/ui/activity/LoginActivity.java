package cn.peng.pxun.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.peng.pxun.MyApplication;
import cn.peng.pxun.R;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.modle.bean.RegiestResultBean;
import cn.peng.pxun.modle.bmob.User;
import cn.peng.pxun.presenter.activity.LoginPresenter;
import cn.peng.pxun.utils.AppUtil;
import cn.peng.pxun.utils.ToastUtil;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

/**
 * 登录页面
 * @author pengpeng
 */
public class LoginActivity extends BaseActivity<LoginPresenter> {

    @BindView(R.id.et_login_loginNum)
    EditText mEtLoginNum;
    @BindView(R.id.et_login_password)
    EditText mEtPassword;
    @BindView(R.id.bt_login)
    Button mBtLogin;
    @BindView(R.id.tv_to_register)
    TextView mTvToRegister;
    @BindView(R.id.cb_login)
    CheckBox mCbLogin;
    @BindView(R.id.tv_no_login)
    TextView mTvNoLogin;
    @BindView(R.id.login_toolbar)
    Toolbar mLoginToolbar;
    @BindView(R.id.bt_login_qq)
    Button mBtLoginQq;
    @BindView(R.id.bt_login_weiBo)
    Button mBtLoginWeibo;

    UMAuthListener authListener = new UMAuthListener() {
        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
            ToastUtil.showToast(mActivity, platform.name());
        }

        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            dismissLoadingDialog();

            if(data != null && data.size() > 0){
                User user = new User();
                user.setThirdPartyID(data.get("uid"));
                user.setUsername(data.get("screen_name"));
                user.setHeadIcon(data.get("profile_image_url"));
                user.setSex(data.get("gender"));

                if (platform == SHARE_MEDIA.QQ){
                    user.setLoginType("QQ");
                    user.setAddress(data.get("province")+"省-"+data.get("city")+"市");
                }else if (platform == SHARE_MEDIA.SINA){
                    user.setLoginType("SINA");
                    user.setAddress(data.get("location")+"省");
                    user.setInfoBackGround(data.get("cover_image_phone"));
                }

                showLoadingDialog("正在登陆...");
                presenter.thirdPartyUserLogin(user);
            }
        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            dismissLoadingDialog();
            ToastUtil.showToast(mActivity, "授权失败：" + t.getMessage());
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            dismissLoadingDialog();
            ToastUtil.showToast(mActivity, "授权被取消");
        }
    };

    @Override
    protected void init() {
        super.init();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            initPermission();
        }
        EventBus.getDefault().register(this);
    }

    @Override
    public int setLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    public LoginPresenter initPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected void initView() {
        super.initView();

        String phone = MyApplication.sp.getString("phone", "");
        mEtLoginNum.setText(phone);
        boolean isRememberPassword = MyApplication.sp.getBoolean("isRemember", false);
        mCbLogin.setChecked(isRememberPassword);
        if (isRememberPassword){
            String password = MyApplication.sp.getString("password", "");
            mEtPassword.setText(password);
        }
    }

    @Override
    protected void initListener() {
        mBtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = mEtLoginNum.getText().toString().trim();
                String password = mEtPassword.getText().toString().trim();

                if (TextUtils.isEmpty(phone)) {
                    ToastUtil.showToast(LoginActivity.this, "帐号不能为空");
                    return;
                } else if (TextUtils.isEmpty(password)) {
                    ToastUtil.showToast(LoginActivity.this, "密码不能为空");
                    return;
                }

                login(phone, password);
            }
        });
        mTvToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
                startActivityForResult(intent, AppConfig.LOGINTOREGIEST);
            }
        });
        mTvNoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        mBtLoginQq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingDialog("正在授权，请稍后...");
                MyApplication.umengApi.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, authListener);
            }
        });
        mBtLoginWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingDialog("正在授权，请稍后...");
                MyApplication.umengApi.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.SINA, authListener);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        dismissLoadingDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MyApplication.umengApi.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (data != null){
                switch (requestCode){
                    case AppConfig.LOGINTOREGIEST:
                        User user = (User) data.getSerializableExtra("user");
                        String password = data.getStringExtra("password");
                        mEtLoginNum.setText(user.getLoginNum());
                        mEtPassword.setText(password);
                        mCbLogin.setChecked(true);
                        login(user.getLoginNum(), password);
                        break;
                }
            }
        }
    }

    /**
     * 检查权限,动态申请权限
     */
    private void initPermission() {
        if(Build.VERSION.SDK_INT>=23){
            List<PermissionItem> permissionItems = new ArrayList();
            permissionItems.add(new PermissionItem(Manifest.permission.ACCESS_FINE_LOCATION, "定位", R.drawable.permission_ic_location));
            permissionItems.add(new PermissionItem(Manifest.permission.READ_EXTERNAL_STORAGE, "读取存储卡", R.drawable.permission_ic_storage ));
            permissionItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "写入存储卡", R.drawable.permission_ic_storage ));
            permissionItems.add(new PermissionItem(Manifest.permission.READ_PHONE_STATE, "手机状态", R.drawable.permission_ic_phone));
            requestPermission(permissionItems, new PermissionCallback() {
                @Override
                public void onClose() {
                    ToastUtil.showToast(mActivity, "您取消了授权");
                }

                @Override
                public void onFinish() {
                    //ToastUtil.showToast(mActivity, "授权成功");
                }

                @Override
                public void onDeny(String permission, int position) {
                }

                @Override
                public void onGuarantee(String permission, int position) {
                }
            });
        }
    }

    /**
     * 登录用户
     * @param phone
     * @param password
     */
    private void login(String phone, String password) {
        showLoadingDialog("正在登陆...");
        presenter.login(phone, password);
    }

    /**
     * 处理三方用户注册结果
     * @param result
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onThirdPartyUserRegistResult(RegiestResultBean result) {
        switch (result.result) {
            case AppConfig.NET_ERROR:
                loadingDialog.setTitleText("注册失败")
                        .setContentText("网络异常,请先检查您的网络!")
                        .setConfirmText("确定")
                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                break;
            case AppConfig.REGIEST_HUANXIN_ERROR:
            case AppConfig.REGIEST_BMOB_ERROR:
                loadingDialog.setTitleText("注册失败")
                        .setContentText("请求连接超时，请稍后重试")
                        .setConfirmText("确定")
                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                break;
            case AppConfig.REGIEST_SUCCESS:
                String loginNum = result.mUser.getThirdPartyID();
                String password = result.password;
                presenter.loginHuanXin(loginNum, password);
                break;
        }
    }

    /**
     * 显示登陆结果
     * @param code
     */
    public void onLoginFinish(int code,int huanXinCode) {
        switch (code) {
            case AppConfig.SUCCESS:
                dismissLoadingDialog();
                boolean isRemember = mCbLogin.isChecked();
                presenter.keepUserLoginInfo(isRemember);
                AppUtil.setAppUser();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case AppConfig.NET_ERROR:
                loadingDialog.setTitleText("登录失败")
                        .setContentText("网络异常,请先检查您的网络!")
                        .setConfirmText("确定")
                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                break;
            case AppConfig.SERVER_ERROR:
                loadingDialog.setTitleText("登录失败")
                        .setContentText("服务器异常,请稍后重试!")
                        .setConfirmText("确定")
                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                break;
            case AppConfig.ERROR:
                loadingDialog.setTitleText("登录失败");
                if(huanXinCode == 202){
                    loadingDialog.setContentText("您输入的密码有误");
                }else if(huanXinCode == 204){
                    loadingDialog.setContentText("该用户不存在");
                }else{
                    loadingDialog.setContentText("与服务器连接较慢，请稍后重试");
                }
                loadingDialog.setConfirmText("确定");
                loadingDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                break;
        }
    }

}
