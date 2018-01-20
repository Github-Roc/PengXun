package cn.peng.pxun.modle.bmob;

import cn.bmob.v3.BmobObject;

/**
 * 群组实体
 * Created by msi on 2017/9/23.
 */

public class Group extends BmobObject{
    //群号码
    private String groupNum;
    //群名称
    private String groupName;
    //群图标
    private String groupIcon;
    //群描述
    private String groupDesc;
    //群类型
    private String groupType;
    //群主ID
    private String groupMasterId;

    public String getGroupNum() {
        return groupNum;
    }

    public void setGroupNum(String groupNum) {
        this.groupNum = groupNum;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupIcon() {
        return groupIcon;
    }

    public void setGroupIcon(String groupIcon) {
        this.groupIcon = groupIcon;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getGroupMasterId() {
        return groupMasterId;
    }

    public void setGroupMasterId(String groupMasterId) {
        this.groupMasterId = groupMasterId;
    }
}
