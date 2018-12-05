package cc.seedland.oa.circulate.modle.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/26 0026.
 */

public class UserInfo implements Serializable{
    public String fullName;
    public String deptFullname;
    public String subcompanyId1;
    public long userMssageId;
    public int icon = -1;// 是否显示小图标,-1表示隐藏图标
    public String headerUrl;
    //分部门
    public String mobile;
    public String departmentId;
    public String deptFullName;
    public String fullCompanyName;
    public String lastName;
    public String loginId;
    public String subCompanyId;
    public String userId;
    public long userMessageId;
    public String workCode;
    public boolean isSelected;
    public String title; //岗位
    public String email;
    public String statusName; //状态名称
    public String pyName; //拼音简写
    public String pyFullName; //拼音
    public String name; //姓名
}
