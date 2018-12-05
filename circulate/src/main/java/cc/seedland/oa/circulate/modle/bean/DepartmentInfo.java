package cc.seedland.oa.circulate.modle.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hch on 2018/2/2.
 */

public class DepartmentInfo implements Parcelable {
    public String departmentName;
    // 节点值
    public String ID;
    public int iconRes;
    public List<UserInfo> member = new ArrayList<>();
    public List<DepartmentInfo> subDepartment = new ArrayList<>();
    public String supSubCompanyID; //上级分部ID
    public boolean haveSubCom; //是否有子分部
    public boolean haveSubDepart; //是否有子部门
    public boolean haveMember; //是否有人
    public boolean isCompany;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.departmentName);
        dest.writeString(this.ID);
        dest.writeInt(this.iconRes);
        dest.writeList(this.member);
        dest.writeList(this.subDepartment);
        dest.writeString(this.supSubCompanyID);
        dest.writeByte(this.haveSubCom ? (byte) 1 : (byte) 0);
        dest.writeByte(this.haveSubDepart ? (byte) 1 : (byte) 0);
        dest.writeByte(this.haveMember ? (byte) 1 : (byte) 0);
    }

    public DepartmentInfo() {
    }

    protected DepartmentInfo(Parcel in) {
        this.departmentName = in.readString();
        this.ID = in.readString();
        this.iconRes = in.readInt();
        this.member = new ArrayList<UserInfo>();
        in.readList(this.member, UserInfo.class.getClassLoader());
        this.subDepartment = new ArrayList<DepartmentInfo>();
        in.readList(this.subDepartment, DepartmentInfo.class.getClassLoader());
        this.supSubCompanyID = in.readString();
        this.haveSubCom = in.readByte() != 0;
        this.haveSubDepart = in.readByte() != 0;
        this.haveMember = in.readByte() != 0;
    }

    public static final Parcelable.Creator<DepartmentInfo> CREATOR = new Parcelable.Creator<DepartmentInfo>() {
        @Override
        public DepartmentInfo createFromParcel(Parcel source) {
            return new DepartmentInfo(source);
        }

        @Override
        public DepartmentInfo[] newArray(int size) {
            return new DepartmentInfo[size];
        }
    };
}
