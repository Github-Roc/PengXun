package cn.peng.pxun.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Calendar;

import butterknife.BindView;
import cn.peng.pxun.R;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.modle.bmob.User;
import cn.peng.pxun.modle.picker.City;
import cn.peng.pxun.modle.picker.County;
import cn.peng.pxun.modle.picker.Province;
import cn.peng.pxun.presenter.activity.UserInfoPresenter;
import cn.peng.pxun.presenter.base.BasePresenter;
import cn.peng.pxun.ui.view.HandIconView;
import cn.peng.pxun.ui.view.picker.AddressPickTask;
import cn.peng.pxun.ui.view.picker.DatePicker;
import cn.peng.pxun.utils.ConvertUtil;
import cn.peng.pxun.utils.ToastUtil;

import static cn.peng.pxun.ui.activity.ModifyUserInfoActivity.TYPE_SIGNATURE;
import static cn.peng.pxun.ui.activity.ModifyUserInfoActivity.TYPE_USERNAME;
import static cn.peng.pxun.utils.ToastUtil.showToast;

/**
 * 用户信息界面
 */
public class UserInfoActivity extends BaseActivity<UserInfoPresenter> {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.iv_goback)
    ImageView mIvGoback;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.ll_user_info_icon)
    LinearLayout mLlUserInfoIcon;
    @BindView(R.id.iv_user_info_icon)
    HandIconView mIvUserInfoIcon;
    @BindView(R.id.ll_user_info_name)
    LinearLayout mLlUserInfoName;
    @BindView(R.id.tv_user_info_name)
    TextView mTvUserInfoName;
    @BindView(R.id.ll_user_info_sex)
    LinearLayout mLlUserInfoSex;
    @BindView(R.id.rg_user_info_sex)
    RadioGroup mRgUserInfoSex;
    @BindView(R.id.rb_user_info_boy)
    RadioButton mRbUserInfoBoy;
    @BindView(R.id.rb_user_info_girl)
    RadioButton mRbUserInfoGirl;
    @BindView(R.id.ll_user_info_birthday)
    LinearLayout mLlUserInfoBirthday;
    @BindView(R.id.tv_user_info_birthday)
    TextView mTvUserInfoBirthday;
    @BindView(R.id.ll_user_info_address)
    LinearLayout mLlUserInfoAddress;
    @BindView(R.id.tv_user_info_address)
    TextView mTvUserInfoAddress;
    @BindView(R.id.ll_user_info_signature)
    LinearLayout mLlUserInfoSignature;
    @BindView(R.id.tv_user_info_signature)
    TextView mTvUserInfoSignature;

    private AlertDialog.Builder builder;
    private DatePicker datePicker;
    private AddressPickTask addressPicker;

    private User mUser;

    @Override
    protected void init() {
        super.init();
        initIconDialog();
        initBirthdayPicker();
        initAddressPicker();
    }

    @Override
    public int setLayoutRes() {
        return R.layout.activity_user_info;
    }

    @Override
    public UserInfoPresenter initPresenter() {
        return new UserInfoPresenter(this);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTvTitle.setText("个人信息");

        mUser = (User) AppConfig.appUser.clone();
        if(mUser != null){
            initUserInfo();
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
        mLlUserInfoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.show();
            }
        });
        mLlUserInfoName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ModifyUserInfoActivity.class);
                intent.putExtra("modifyType", TYPE_USERNAME);
                intent.putExtra("content", mUser.getUsername());
                startActivityForResult(intent, AppConfig.USERINFOTOMODIFY);
            }
        });
        mLlUserInfoBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show();
            }
        });
        mLlUserInfoAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressPicker.show();
            }
        });
        mLlUserInfoSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ModifyUserInfoActivity.class);
                intent.putExtra("modifyType", TYPE_SIGNATURE);
                intent.putExtra("content", mUser.getSignaTure());
                startActivityForResult(intent,AppConfig.USERINFOTOMODIFY);
            }
        });
        mRgUserInfoSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_user_info_boy:
                        mUser.setSex("男");
                        upDataUserInfo();
                        break;
                    case R.id.rb_user_info_girl:
                        mUser.setSex("女");
                        upDataUserInfo();
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case AppConfig.USERINFOTOCAMERA:
                    ClipActivity.prepare()
                            .aspectX(3).aspectY(3)//裁剪框横向及纵向上的比例
                            .inputPath(AppConfig.CACHE_PATH + "temp.jpg").outputPath(AppConfig.CACHE_PATH +AppConfig.getUserId(AppConfig.appUser)+"_icon.png")//要裁剪的图片地址及裁剪后保存的地址
                            .startForResult(this, AppConfig.USERINFOTOCLIP);
                    break;
                case AppConfig.USERINFOTOPIC:
                    Uri uri = presenter.getUri(data);
                    String imagePath = presenter.getPathFromUri(uri);

                    ClipActivity.prepare()
                            .aspectX(3).aspectY(3)//裁剪框横向及纵向上的比例
                            .inputPath(imagePath).outputPath(AppConfig.CACHE_PATH +AppConfig.getUserId(AppConfig.appUser)+"_icon.png")//要裁剪的图片地址及裁剪后保存的地址
                            .startForResult(this, AppConfig.USERINFOTOCLIP);
                    break;
                case AppConfig.USERINFOTOCLIP:
                    String path = ClipActivity.ClipOptions.createFromBundle(data).getOutputPath();
                    if (path != null) {
                        showProgressDialog("正在上传图片");
                        presenter.upLoadFile(path);
                    }
                    break;
                case AppConfig.USERINFOTOMODIFY:
                    String content = data.getStringExtra("content");
                    int modifyType = data.getIntExtra("modifyType", TYPE_USERNAME);
                    if (modifyType == TYPE_USERNAME) {
                        mUser.setUsername(content);
                    } else if (modifyType == TYPE_SIGNATURE) {
                        mUser.setSignaTure(content);
                    }
                    upDataUserInfo();
                    break;
            }
        }
    }

    /**
     * 初始化选择头衔方式的Dialog
     */
    private void initIconDialog() {
        String[] modole = {"拍照", "从图库选择"};

        builder = new AlertDialog.Builder(this);
        builder.setTitle("更换头像");
        builder.setSingleChoiceItems(modole, -1, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                dialog.dismiss();

                if (which == 0){
                    presenter.startCamera(AppConfig.USERINFOTOCAMERA);
                }else if (which == 1){
                    presenter.startPicDepot(AppConfig.USERINFOTOPIC);
                }
            }
        });
        builder.create();
    }

    /**
     * 初始化时间选择器
     */
    private void initBirthdayPicker() {
        datePicker = new DatePicker(this);
        datePicker.setTopPadding(ConvertUtil.toPx(this, 12));
        Calendar now = Calendar.getInstance();
        datePicker.setRangeStart(1970, 1, 1);
        datePicker.setRangeEnd(now.get(Calendar.YEAR), now.get(Calendar.MONTH)+1, now.get(Calendar.DAY_OF_MONTH));
        if (AppConfig.appUser != null && !TextUtils.isEmpty(AppConfig.appUser.getBirthday())){
            String[] dates = AppConfig.appUser.getBirthday().split("-");
            datePicker.setSelectedItem(Integer.parseInt(dates[0]), Integer.parseInt(dates[1]), Integer.parseInt(dates[2]));
        }else{
            datePicker.setSelectedItem(1990, 1, 1);
        }
        datePicker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                String birthday = year + "-" + month + "-" + day;
                mUser.setBirthday(birthday);
                upDataUserInfo();
            }
        });
        datePicker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                datePicker.setTitleText(year + "-" + datePicker.getSelectedMonth() + "-" + datePicker.getSelectedDay());
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                datePicker.setTitleText(datePicker.getSelectedYear() + "-" + month + "-" + datePicker.getSelectedDay());
            }

            @Override
            public void onDayWheeled(int index, String day) {
                datePicker.setTitleText(datePicker.getSelectedYear() + "-" + datePicker.getSelectedMonth() + "-" + day);
            }
        });
    }

    /**
     * 初始化地址选择器
     */
    private void initAddressPicker() {
        addressPicker = new AddressPickTask(this);
        addressPicker.setHideProvince(false);
        addressPicker.setHideCounty(false);
        addressPicker.setCallback(new AddressPickTask.Callback() {
            @Override
            public void onAddressInitFailed() {
                showToast(mActivity,"数据初始化失败");
            }

            @Override
            public void onAddressPicked(Province province, City city, County county) {
                String address;
                if (county == null) {
                    if(province.getAreaName().equals(city.getAreaName()) || "其他".equals(province.getAreaName())){
                        address = city.getAreaName();
                    }else{
                        address = province.getAreaName() + "-" + city.getAreaName();
                    }
                } else {
                    if(province.getAreaName().equals(city.getAreaName()) || "其他".equals(province.getAreaName())){
                        address = province.getAreaName() + "-" + city.getAreaName();
                    }else{
                        address = province.getAreaName() + "-" + city.getAreaName() + "-" + county.getAreaName();
                    }
                }
                mUser.setAddress(address);
                upDataUserInfo();
            }
        });

        if (AppConfig.appUser != null && !TextUtils.isEmpty(AppConfig.appUser.getAddress())){
            if (AppConfig.appUser.getAddress().contains("-")){
                String[] locations = AppConfig.appUser.getAddress().split("-");
                if (locations.length == 2){
                    addressPicker.execute(locations[0], locations[1]);
                }else if (locations.length == 3){
                    addressPicker.execute(locations[0], locations[1], locations[2]);
                }
            }else{
                addressPicker.execute(AppConfig.appUser.getAddress());
            }
        }else{
            addressPicker.execute("河北省", "石家庄市", "长安区");
        }
    }

    /**
     * 初始化用户信息
     */
    private void initUserInfo() {
        if (!TextUtils.isEmpty(AppConfig.appUser.getHeadIcon())){
            Picasso.with(this).load(AppConfig.appUser.getHeadIcon()).into(mIvUserInfoIcon);
        }
        if ("男".equals(AppConfig.appUser.getSex())){
            mRbUserInfoBoy.setChecked(true);
        }else if ("女".equals(AppConfig.appUser.getSex())){
            mRbUserInfoGirl.setChecked(true);
        }
        mTvUserInfoName.setText(AppConfig.appUser.getUsername());
        mTvUserInfoBirthday.setText(AppConfig.appUser.getBirthday());
        mTvUserInfoAddress.setText(AppConfig.appUser.getAddress());
        mTvUserInfoSignature.setText(AppConfig.appUser.getSignaTure());
    }

    /**
     * 更新用户信息
     */
    private void upDataUserInfo() {
        if (BasePresenter.isNetUsable(this)){
            showLoadingDialog("保存用户信息...");
            presenter.upDataUser(mUser);
        }else{
            ToastUtil.showToast(this, "请检查网络连接");
        }
    }

    /**
     * 展示修改结果
     * @param code
     */
    public void updataResult(int code) {
        dismissLoadingDialog();
        if (code == AppConfig.SUCCESS){
            AppConfig.appUser = mUser;
            initUserInfo();
            ToastUtil.showToast(this, "信息修改成功");
        }else {
            mUser = AppConfig.appUser;
            ToastUtil.showToast(this, "信息修改失败，请稍后重试");
        }
    }

    /**
     * 上传头像完成
     */
    public void onIconUploadFinish(String iconPath) {
        dismissProgressDialog();
        if (!TextUtils.isEmpty(iconPath)){
            mUser.setHeadIcon(iconPath);
            upDataUserInfo();
        } else {
            ToastUtil.showToast(this, "头像上传失败");
        }
    }

    public void onIconUploadProgress(int value) {
        progressBar.setProgress(value);
    }
}
