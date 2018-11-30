package com.ecology.view.seedland.circulate.modle.bean;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hch on 2018/1/19.
 */

public class MailInfo implements Serializable {
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

    private String time;
    public boolean isSelected;

    public String getTime() {
        if (!TextUtils.isEmpty(sendTime)) {
            time = sendTime.substring(0, sendTime.indexOf("-"));
        }
        return time;
    }
}
