package cn.peng.pxun.modle.bmob;

import cn.bmob.v3.BmobUser;

/**
 * 用户实体
 * Created by msi on 2016/12/24.
 */

public class User extends BmobUser implements Cloneable {
    //登录帐号
    private String loginNum;

    //三方登录id
    private String thirdPartyID;
    //信息背景图片
    private String infoBackGround;
    //头像
    private String headIcon;
    //登录类型
    private String loginType;
    //个性签名
    private String signaTure;

    //真实姓名
    private String name;
    //性别
    private String sex;
    //年龄
    private String age;
    //生日
    private String birthday;
    //所在地
    private String address;

    public User() {
    }

    public User(String userId, String username) {
        setLoginNum(userId);
        setUsername(username);
    }

    public String getLoginNum() {
        return loginNum;
    }

    public void setLoginNum(String loginNum) {
        this.loginNum = loginNum;
    }

    public String getThirdPartyID() {
        return thirdPartyID;
    }

    public void setThirdPartyID(String thirdPartyID) {
        this.thirdPartyID = thirdPartyID;
    }

    public String getInfoBackGround() {
        return infoBackGround;
    }

    public void setInfoBackGround(String infoBackGround) {
        this.infoBackGround = infoBackGround;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getSignaTure() {
        return signaTure;
    }

    public void setSignaTure(String signaTure) {
        this.signaTure = signaTure;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public Object clone() {
        User user = null;
        try {
            user = (User) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return user;
    }
}
