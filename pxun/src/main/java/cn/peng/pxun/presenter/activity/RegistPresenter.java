package cn.peng.pxun.presenter.activity;

import cn.peng.pxun.modle.bmob.User;
import cn.peng.pxun.presenter.base.BaseUserPresenter;
import cn.peng.pxun.ui.activity.BaseActivity;

/**
 * RegistActivity的业务类
 */
public class RegistPresenter extends BaseUserPresenter{

    public RegistPresenter(BaseActivity activity) {
        super(activity);
    }

    /**
     * 注册
     * @param loginNum 用户登录账号
     * @param username 用户名，昵称
     * @param password 密码
     * @param sex 用户性别
     * @param birthday 用户生日
     * @param address 用户所在地
     */
    public void regist(String loginNum, String username, String password, String sex, String birthday, String address) {
        if("未选择".equals(sex)){
            sex = "";
        }
        if("未选择".equals(birthday)){
            birthday = "";
        }
        if("未选择".equals(address)){
            address = "";
        }

        User user = new User();
        user.setLoginNum(loginNum);
        user.setUsername(username);
        user.setSex(sex);
        user.setBirthday(birthday);
        user.setAddress(address);
        user.setLoginType("REGIEST");

        registUser(user, loginNum, password);
    }
}
