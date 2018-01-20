package cn.peng.pxun.utils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.peng.pxun.MyApplication;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.modle.bmob.User;

import static cn.peng.pxun.presenter.base.BasePresenter.isPhoneNumber;

/**
 * Created by tofirst on 2017/11/6.
 */

public class AppUtil {

    public static void setAppUser() {
        String userId = MyApplication.sp.getString("userId","");
        BmobQuery<User> bmobQuery = new BmobQuery();
        if (isPhoneNumber(userId)){
            bmobQuery.addWhereEqualTo("mobilePhoneNumber", userId);
        }else{
            userId = userId.toUpperCase();
            bmobQuery.addWhereEqualTo("thirdPartyID", userId);
        }
        bmobQuery.findObjects(new FindListener<User>(){

            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    AppConfig.appUser = list.get(0);
                    loginUser();
                }
            }
        });
    }

    public static void loginUser() {
        boolean isThird = MyApplication.sp.getBoolean("isThird", false);
        String password;
        if (isThird){
            password = MyApplication.sp.getString("thirdpwd", "");
        } else {
            password = MyApplication.sp.getString("password", "");
        }
        AppConfig.appUser.setPassword(MD5Util.encode(password));
        AppConfig.appUser.login(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null){
                    AppConfig.appUser.setSessionToken(user.getSessionToken());
                }
            }
        });
    }
}
