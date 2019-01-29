package cc.seedland.oa.circulate.modle.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hch on 2018/1/19.
 */

public class MailInfo implements Parcelable {
    public String allReceiveName;//收件人名字
    public List<AttachInfo> attachmentItemss;//附件集合
    public boolean attention;//是否关注该传阅
    public long completeTime; //传阅完成的时间
    public long createTime; //传阅创建的时间
    public List<DiscussesInfo> discusses;
    public boolean enabled;//是否已经删除
    public boolean hasAttachment;//是否有附件
    public boolean ifAdd;// 允许新添加人员
    public boolean ifImportant;//重要传阅
    public boolean ifNotify; //短信提醒
    public boolean ifRead; //开封已阅确认
    public boolean ifRemind;//确认时提醒
    public boolean ifRemindAll;//确认时提醒所有传阅对象
    public boolean ifSecrecy;//传阅密送(留个字段 ,不用实现)
    public boolean ifSequence; //有序确认(留个字段,不实现)
    public boolean ifUpdate;//允许修订附件
    public boolean ifUpload; //允许上传附件
    public String lastName;//发件人的姓名
    public String loginId;//发件人的登录名
    public String mailContent;//传阅内容
    public int mailId;//传阅ID
    public List<UserInfo> receivess;//收件人集合
    public String ruleName; //该字段用于记录选择的传阅规则的名字 ，已分号分隔 ；
    public String sendTime;//发送传阅的时间
    public int status;
    public int stepStatus;// 传阅流程状态  1 传阅中 2 待办传阅 3 已完成
    public String title; //传阅主题
    public List<CollectionInfo> userCollections;//关注集合
    public int userId; //发件人ID
    public String workCode;//   发件人工作编号
    public boolean receiveAttention;//已收传阅关注状态
    public int mailState;
    public int receiveMailState;//已读未读
    public boolean ifConfirmss;
    public boolean afreshConfimss;
    public String receiveTime;
    public int ReceiveCount;
    public String deleteTime;
    public String receiveUserIds;

    private String time;
    public boolean isSelected;

    public MailInfo() {
    }

    protected MailInfo(Parcel in) {
        allReceiveName = in.readString();
        attention = in.readByte() != 0;
        completeTime = in.readLong();
        createTime = in.readLong();
        enabled = in.readByte() != 0;
        hasAttachment = in.readByte() != 0;
        ifAdd = in.readByte() != 0;
        ifImportant = in.readByte() != 0;
        ifNotify = in.readByte() != 0;
        ifRead = in.readByte() != 0;
        ifRemind = in.readByte() != 0;
        ifRemindAll = in.readByte() != 0;
        ifSecrecy = in.readByte() != 0;
        ifSequence = in.readByte() != 0;
        ifUpdate = in.readByte() != 0;
        ifUpload = in.readByte() != 0;
        lastName = in.readString();
        loginId = in.readString();
        mailContent = in.readString();
        mailId = in.readInt();
        ruleName = in.readString();
        sendTime = in.readString();
        status = in.readInt();
        stepStatus = in.readInt();
        title = in.readString();
        userId = in.readInt();
        workCode = in.readString();
        receiveAttention = in.readByte() != 0;
        mailState = in.readInt();
        receiveMailState = in.readInt();
        ifConfirmss = in.readByte() != 0;
        afreshConfimss = in.readByte() != 0;
        receiveTime = in.readString();
        deleteTime = in.readString();
        receiveUserIds = in.readString();
        time = in.readString();
        isSelected = in.readByte() != 0;
    }

    public static final Creator<MailInfo> CREATOR = new Creator<MailInfo>() {
        @Override
        public MailInfo createFromParcel(Parcel in) {
            return new MailInfo(in);
        }

        @Override
        public MailInfo[] newArray(int size) {
            return new MailInfo[size];
        }
    };

    public String getTime() {
        if (!TextUtils.isEmpty(sendTime)) {
            time = sendTime.substring(0, sendTime.indexOf("-"));
        }
        return time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(allReceiveName);
        parcel.writeByte((byte) (attention ? 1 : 0));
        parcel.writeLong(completeTime);
        parcel.writeLong(createTime);
        parcel.writeByte((byte) (enabled ? 1 : 0));
        parcel.writeByte((byte) (hasAttachment ? 1 : 0));
        parcel.writeByte((byte) (ifAdd ? 1 : 0));
        parcel.writeByte((byte) (ifImportant ? 1 : 0));
        parcel.writeByte((byte) (ifNotify ? 1 : 0));
        parcel.writeByte((byte) (ifRead ? 1 : 0));
        parcel.writeByte((byte) (ifRemind ? 1 : 0));
        parcel.writeByte((byte) (ifRemindAll ? 1 : 0));
        parcel.writeByte((byte) (ifSecrecy ? 1 : 0));
        parcel.writeByte((byte) (ifSequence ? 1 : 0));
        parcel.writeByte((byte) (ifUpdate ? 1 : 0));
        parcel.writeByte((byte) (ifUpload ? 1 : 0));
        parcel.writeString(lastName);
        parcel.writeString(loginId);
        parcel.writeString(mailContent);
        parcel.writeInt(mailId);
        parcel.writeString(ruleName);
        parcel.writeString(sendTime);
        parcel.writeInt(status);
        parcel.writeInt(stepStatus);
        parcel.writeString(title);
        parcel.writeInt(userId);
        parcel.writeString(workCode);
        parcel.writeByte((byte) (receiveAttention ? 1 : 0));
        parcel.writeInt(mailState);
        parcel.writeInt(receiveMailState);
        parcel.writeByte((byte) (ifConfirmss ? 1 : 0));
        parcel.writeByte((byte) (afreshConfimss ? 1 : 0));
        parcel.writeString(receiveTime);
        parcel.writeString(deleteTime);
        parcel.writeString(receiveUserIds);
        parcel.writeString(time);
        parcel.writeByte((byte) (isSelected ? 1 : 0));
    }
}
