package cn.peng.pxun.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.peng.pxun.R;
import cn.peng.pxun.presenter.base.BasePresenter;
import cn.peng.pxun.ui.view.NumberProgressBar;
import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

/**
 * Activity的基类
 * @author Peng
 */
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity {
    protected P presenter;
    // Activity中的上下文
    protected BaseActivity mActivity;
    // 加载中的dialog
    protected SweetAlertDialog loadingDialog;
    private AlertDialog progressDialog;
    protected NumberProgressBar progressBar;

    // 管理运行的所有的activity
    public final static List<AppCompatActivity> mActivities = new LinkedList();
    // ButterKnife的解绑器
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        setContentView(setLayoutRes());

        initView();
        initData();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mActivity = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mActivity = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissProgressDialog();
        dismissLoadingDialog();
        //解绑ButterKnife
        if (mUnbinder != null){
            mUnbinder.unbind();
        }
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
        synchronized (mActivities) {
            mActivities.remove(this);
        }
    }

    /**
     * 初始化一些设置,此方法会在setLayoutRes()之前执行
     */
    protected void init(){
        synchronized (mActivities) {
            mActivity = this;
            mActivities.add(this);
        }
        this.presenter = initPresenter();
    }

    /**
     * 初始化视图
     */
    protected void initView(){
        //绑定ButterKnife
        mUnbinder = ButterKnife.bind(this);
    }

    /**
     * 初始化页面数据
     */
    protected void initData(){
    }

    /**
     * 初始化页面监听
     */
    protected  void initListener(){
    }

    /**
     * 设置布局文件资源
     * @return
     */
    public abstract int setLayoutRes();

    /**
     * 初始化和此Activity绑定的业务类
     * @return
     */
    public abstract P initPresenter();

    /**
     * 获取加载中dialog
     * @return
     */
    public SweetAlertDialog getLoadingDialog(){
        return loadingDialog;
    }

    /**
     * 显示加载中提示框
     */
    public void showLoadingDialog(String title) {
        try{
            loadingDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            loadingDialog.setCancelable(false);
            loadingDialog.setTitleText(title);
            loadingDialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 关闭进度条提示框
     */
    public void dismissLoadingDialog(){
        if (loadingDialog != null){
            loadingDialog.dismiss();
        }
    }

    /**
     * 获取进度条控件
     * @return
     */
    public NumberProgressBar getProgressBar(){
        return progressBar;
    }

    /**
     * 显示进度条对话框
     */
    public void showProgressDialog(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        View view = View.inflate(this, R.layout.dialog_progressbar, null);
        builder.setCancelable(false);
        builder.setView(view);
        progressBar = (NumberProgressBar) view.findViewById(R.id.app_progress);
        progressDialog = builder.create();
        progressDialog.show();
    }

    /**
     * 关闭进度条对话框
     */
    public void dismissProgressDialog(){
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }

    /**
     * 请求多个权限
     * @param permissionItems
     */
    public void requestPermission(List<PermissionItem> permissionItems, PermissionCallback callback){
        HiPermission.create(this)
                .permissions(permissionItems)
                .checkMutiPermission(callback);
    }

}
