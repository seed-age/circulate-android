package cc.seedland.oa.circulate.modle.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/26 0026.
 */

public class UserInfo implements Parcelable, HeaderInfo{

    public int icon = -1;// 是否显示小图标,-1表示隐藏图标
    public String headerUrl;
    //分部门
    public String mobile;
    public String departmentId;
//    public String deptFullName;
//    public String fullCompanyName;
    public String lastName;//姓名
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
    public String tel;
    public String departmentName; //部门
    public String subcompanyName; //公司
    public UserInfo(){}

    protected UserInfo(Parcel in) {
        icon = in.readInt();
        headerUrl = in.readString();
        mobile = in.readString();
        departmentId = in.readString();
//        deptFullName = in.readString();
//        fullCompanyName = in.readString();
        lastName = in.readString();
        loginId = in.readString();
        subCompanyId = in.readString();
        userId = in.readString();
        userMessageId = in.readLong();
        workCode = in.readString();
        isSelected = in.readByte() != 0;
        title = in.readString();
        email = in.readString();
        statusName = in.readString();
        pyName = in.readString();
        pyFullName = in.readString();
        tel = in.readString();
        departmentName = in.readString();
        subcompanyName = in.readString();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(icon);
        parcel.writeString(headerUrl);
        parcel.writeString(mobile);
        parcel.writeString(departmentId);
//        parcel.writeString(deptFullName);
//        parcel.writeString(fullCompanyName);
        parcel.writeString(lastName);
        parcel.writeString(loginId);
        parcel.writeString(subCompanyId);
        parcel.writeString(userId);
        parcel.writeLong(userMessageId);
        parcel.writeString(workCode);
        parcel.writeByte((byte) (isSelected ? 1 : 0));
        parcel.writeString(title);
        parcel.writeString(email);
        parcel.writeString(statusName);
        parcel.writeString(pyName);
        parcel.writeString(pyFullName);
        parcel.writeString(tel);
        parcel.writeString(departmentName);
        parcel.writeString(subcompanyName);
    }

    @Override
    public String getHeaderUrl() {
        return headerUrl;
    }

    @Override
    public String getName() {
        return lastName;
    }

    @Override
    public void setHeaderUrl(String url) {
        this.headerUrl = url;
    }

    @Override
    public String getUserId() {
        return userId;
    }
}
