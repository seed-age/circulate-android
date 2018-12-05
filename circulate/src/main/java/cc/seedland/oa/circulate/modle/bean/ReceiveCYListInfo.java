package cc.seedland.oa.circulate.modle.bean;

import cc.seedland.oa.circulate.utils.Utils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hch on 2018/1/17.
 */

public class ReceiveCYListInfo implements Serializable{
    public int currentPage;
    public boolean firstPage;
    public boolean lastPage;
    public List<MailInfo> list;
    public int listSize;
    public int pageSize;
    public static class ListBean implements Serializable{
        public boolean afreshConfim;
        public boolean ifConfirm;//是否确认
        public long joinTime;//加入时间
        public String lastName;//收件人名称
        public String loginId;//收件人登录名字
        public boolean receiveAttention;//已收传阅关注状态
        public MailInfo mail;
        public int mailState;
        public int receiveId;
        public int receiveStatus;
        public long receiveTime;
        public int serialNum;
        public int stepStatus;
        public int userId;
        public String workCode;
        private String time;
        public boolean isSelected;

        public String getTime() {
            if (receiveTime != 0) {
                String date = Utils.formatDate(receiveTime);
                time = date.substring(0,date.indexOf("-"));
            }
            return time;
        }
    }
}
