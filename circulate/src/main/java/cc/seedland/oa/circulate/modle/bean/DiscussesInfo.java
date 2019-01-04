package cc.seedland.oa.circulate.modle.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by hch on 2018/1/19.
 */

public class DiscussesInfo implements Parcelable, HeaderInfo {
    public long createTime;
    public String differentiate;
    public String discussContent;
    public int discussId;
    public String lastName;
    public String loginId;
    public int userId;
    public String workCode;
    public String time;

    private String avatar;

    public DiscussesInfo() {
    }

    protected DiscussesInfo(Parcel in) {
        createTime = in.readLong();
        differentiate = in.readString();
        discussContent = in.readString();
        discussId = in.readInt();
        lastName = in.readString();
        loginId = in.readString();
        userId = in.readInt();
        workCode = in.readString();
        time = in.readString();
    }

    public static final Creator<DiscussesInfo> CREATOR = new Creator<DiscussesInfo>() {
        @Override
        public DiscussesInfo createFromParcel(Parcel in) {
            return new DiscussesInfo(in);
        }

        @Override
        public DiscussesInfo[] newArray(int size) {
            return new DiscussesInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(createTime);
        parcel.writeString(differentiate);
        parcel.writeString(discussContent);
        parcel.writeInt(discussId);
        parcel.writeString(lastName);
        parcel.writeString(loginId);
        parcel.writeInt(userId);
        parcel.writeString(workCode);
        parcel.writeString(time);
    }

    @Override
    public String getHeaderUrl() {
        return avatar;
    }

    @Override
    public String getName() {
        return lastName;
    }

    @Override
    public void setHeaderUrl(String url) {
        this.avatar = url;
    }

    @Override
    public String getUserId() {
        return String.valueOf(userId);
    }
}
