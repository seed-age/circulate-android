package cc.seedland.oa.circulate.modle.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by hch on 2018/1/19.
 */

public class AttachInfo implements Parcelable{
    public boolean attached;
    public String bulkId;//附件上传批次ID
    public long createTime; //附件创建时间
    public String creator;//附件人
    public String fileCategory;
    public String fileName;//附件原名
    public int itemId;//附件ID
    public String itemSize;//附件大小
    public String saveName;//附件保存名, 由uuid+类型后缀 组成
    public int state;//附件状态
    public String urlPath;//附件链接地址
    public String path;
    public int userId;
    public long itemNeid;

    public AttachInfo() {
    }

    protected AttachInfo(Parcel in) {
        attached = in.readByte() != 0;
        bulkId = in.readString();
        createTime = in.readLong();
        creator = in.readString();
        fileCategory = in.readString();
        fileName = in.readString();
        itemId = in.readInt();
        itemSize = in.readString();
        saveName = in.readString();
        state = in.readInt();
        urlPath = in.readString();
        path = in.readString();
        userId = in.readInt();
        itemNeid = in.readLong();
    }

    public static final Creator<AttachInfo> CREATOR = new Creator<AttachInfo>() {
        @Override
        public AttachInfo createFromParcel(Parcel in) {
            return new AttachInfo(in);
        }

        @Override
        public AttachInfo[] newArray(int size) {
            return new AttachInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (attached ? 1 : 0));
        parcel.writeString(bulkId);
        parcel.writeLong(createTime);
        parcel.writeString(creator);
        parcel.writeString(fileCategory);
        parcel.writeString(fileName);
        parcel.writeInt(itemId);
        parcel.writeString(itemSize);
        parcel.writeString(saveName);
        parcel.writeInt(state);
        parcel.writeString(urlPath);
        parcel.writeString(path);
        parcel.writeInt(userId);
        parcel.writeLong(itemNeid);
    }
}
