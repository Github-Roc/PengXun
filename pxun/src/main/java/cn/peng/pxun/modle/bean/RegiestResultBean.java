package cn.peng.pxun.modle.bean;

import cn.peng.pxun.modle.bmob.User;

/**
 * 登录 注册结果的实体类
 * Created by msi on 2017/11/18.
 */

public class RegiestResultBean {
    public int result;
    public int errorCode;
    public User mUser;
    public String password;

    public RegiestResultBean(int result, int errorCode){
        this.result = result;
        this.errorCode = errorCode;
    }

    public RegiestResultBean(int result, User user, String password){
        this.result = result;
        this.mUser = user;
        this.password = password;
    }
}
