package com.ecology.view.seedland.circulate.modle.bean;

import java.util.List;

/**
 * Created by hch on 2018/2/2.
 */

public class DepartmentInfo {
    public int iconRes;
    public String departmentName;
    public List<UserInfo> member;
    public List<DepartmentInfo> subDepartment;
    public boolean haveSubDepart;
}
