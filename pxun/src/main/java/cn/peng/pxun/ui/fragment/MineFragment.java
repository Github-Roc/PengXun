package cn.peng.pxun.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import cn.peng.pxun.R;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.presenter.fragment.MinePresenter;
import cn.peng.pxun.ui.activity.AboutAuthorActivity;
import cn.peng.pxun.ui.activity.ContactActivity;
import cn.peng.pxun.ui.activity.LoginActivity;
import cn.peng.pxun.ui.activity.SettingActivity;
import cn.peng.pxun.ui.activity.UserInfoActivity;
import cn.peng.pxun.ui.view.DampView;
import cn.peng.pxun.ui.view.HandIconView;
import cn.peng.pxun.utils.ToastUtil;

import static android.app.Activity.RESULT_OK;

/**
 * Created by msi on 2016/12/21.
 */
public class MineFragment extends BaseFragment<MinePresenter> {

    @BindView(R.id.dv_mine_parent)
    DampView mDvMineParent;
    @BindView(R.id.iv_mine_bg)
    ImageView mIvMineBg;
    @BindView(R.id.iv_mine_icon)
    HandIconView mIvMineIcon;
    @BindView(R.id.tv_mine_name)
    TextView mTvMineName;
    @BindView(R.id.iv_mine_sex)
    ImageView mIvMineSex;
    @BindView(R.id.tv_mine_info)
    TextView mTvMineInfo;
    @BindView(R.id.tv_mine_friend)
    TextView mTvMineFriend;
    @BindView(R.id.tv_mine_group)
    TextView mTvMineGroup;
    @BindView(R.id.ll_mine_about)
    LinearLayout mLlMineAbout;
    @BindView(R.id.ll_mine_setting)
    LinearLayout mLlMineSetting;

    private AlertDialog.Builder builder;
    private String oldBgPath;
    private String newBgPath;

    @Override
    protected void init() {
        super.init();
        initChangeBgDialog();
    }

    @Override
    public View initLayout() {
        View view = View.inflate(mActivity, R.layout.fragment_mine, null);
        return view;
    }

    @Override
    public MinePresenter initPresenter() {
        return new MinePresenter(this);
    }

    @Override
    protected void initData() {
        mDvMineParent.setImageView(mIvMineBg);
    }

    @Override
    protected void initListener() {
        mIvMineIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, UserInfoActivity.class);
                startActivity(intent);
            }
        });
        mIvMineBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.show();
            }
        });
        mTvMineInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, UserInfoActivity.class);
                startActivity(intent);
            }
        });
        mTvMineFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ContactActivity.class);
                intent.putExtra("position", 0);
                startActivity(intent);
            }
        });
        mTvMineGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppConfig.appUser != null){
                    Intent intent = new Intent(mActivity, ContactActivity.class);
                    intent.putExtra("position", 1);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(mActivity, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        mLlMineAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, AboutAuthorActivity.class);
                startActivity(intent);
            }
        });
        mLlMineSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, SettingActivity.class);
                startActivityForResult(intent, AppConfig.MINETOSETTING);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AppConfig.appUser != null) {
            mTvMineName.setText(AppConfig.appUser.getUsername());

            if (!TextUtils.isEmpty(AppConfig.appUser.getSex())) {
                if ("男".equals(AppConfig.appUser.getSex())) {
                    mTvMineName.setTextColor(Color.parseColor("#33B5E5"));
                    mIvMineSex.setImageResource(R.drawable.sex_boy);
                    mIvMineIcon.setImageResource(R.drawable.icon_nan);
                } else if ("女".equals(AppConfig.appUser.getSex())) {
                    mTvMineName.setTextColor(Color.parseColor("#FF4081"));
                    mIvMineSex.setImageResource(R.drawable.sex_girl);
                    mIvMineIcon.setImageResource(R.drawable.icon_nv);
                }
            } else {
                mIvMineSex.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(AppConfig.appUser.getInfoBackGround())) {
                Picasso.with(mActivity).load(AppConfig.appUser.getInfoBackGround()).into(mIvMineBg);
            }
            if (!TextUtils.isEmpty(AppConfig.appUser.getHeadIcon())) {
                Picasso.with(mActivity).load(AppConfig.appUser.getHeadIcon()).into(mIvMineIcon);
            }
            if (!TextUtils.isEmpty(AppConfig.appUser.getSignaTure())) {
                mTvMineInfo.setText(AppConfig.appUser.getSignaTure());
            }
        } else {
            mTvMineName.setText("未登录");
            mTvMineName.setTextColor(Color.parseColor("#000000"));
            mIvMineIcon.setImageResource(R.drawable.icon_nan);
            mIvMineSex.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case AppConfig.MINETOSETTING:
                    getActivity().finish();
                    break;
                case AppConfig.MINETOPIC:
                    Uri uri = presenter.getUri(data);
                    String imagePath = presenter.getPathFromUri(uri);

                    mActivity.showProgressDialog("正在上传图片");
                    presenter.upLoadFile(imagePath);
                    break;
            }
        }
    }

    /**
     * 初始化切换背景的Dialog
     */
    private void initChangeBgDialog() {
        String[] modole = {"图库选择", "取消"};

        builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("更换封面");
        builder.setSingleChoiceItems(modole, -1, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                dialog.dismiss();

                if (which == 0){
                    presenter.startPicDepot(AppConfig.MINETOPIC);
                }
            }
        });
        builder.create();
    }

    /**
     * 背景图片上传完成
     * @param path
     */
    public void onBgUpLoadFinish(String path) {
        mActivity.dismissProgressDialog();
        if (!TextUtils.isEmpty(path)){
            newBgPath = path;
            oldBgPath = AppConfig.appUser.getInfoBackGround();
            AppConfig.appUser.setInfoBackGround(newBgPath);
            mActivity.showLoadingDialog("保存用户信息...");
            presenter.upDataUser(AppConfig.appUser);
        } else {
            ToastUtil.showToast(mActivity, "封面上传失败");
        }
    }

    public void onBgUpLoadProgress(int value) {
        if (mActivity.getProgressBar() != null){
            mActivity.getProgressBar().setProgress(value);
        }
    }

    /**
     * 展示修改结果
     * @param code
     */
    public void updataResult(int code) {
        if (mActivity.getLoadingDialog() != null){
            mActivity.getLoadingDialog().cancel();
        }
        if (code == AppConfig.SUCCESS){
            Picasso.with(mActivity).load(AppConfig.appUser.getInfoBackGround()).into(mIvMineBg);
        }else {
            AppConfig.appUser.setInfoBackGround(oldBgPath);
            ToastUtil.showToast(mActivity, "封面更新失败");
        }
    }


}
