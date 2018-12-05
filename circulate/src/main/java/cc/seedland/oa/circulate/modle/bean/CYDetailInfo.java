package cc.seedland.oa.circulate.modle.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/1/23 0023.
 */

public class CYDetailInfo implements MultiItemEntity{
    public static final int MAIL_DETAIL = 0x001;
    public static final int MAIL_DISCUSS = 0x002;
    private int itemType;
    private MailInfo mMailInfo;
    private List<DiscussesInfo> discussesList;
    private int totalRecord;

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public MailInfo getMailInfo() {
        return mMailInfo;
    }

    public void setMailInfo(MailInfo mailInfo) {
        mMailInfo = mailInfo;
    }

    public List<DiscussesInfo> getDiscussesList() {
        return discussesList;
    }

    public void setDiscussesList(List<DiscussesInfo> discussesList) {
        this.discussesList = discussesList;
    }

    public CYDetailInfo(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
