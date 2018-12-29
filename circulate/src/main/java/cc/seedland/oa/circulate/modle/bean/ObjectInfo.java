package cc.seedland.oa.circulate.modle.bean;

/**
 * Created by Administrator on 2018/1/24 0024.
 */

public class ObjectInfo implements HeaderInfo{
    public String loginId;
    public String lastName;
    public String remark;
    public String workCode;
    public String openTime;
    public String joinTime;
    public String userId;
    public long receiveId;
    public String receiveTime;
    public String confirmRecord;
    public String affirmTime;
    public String departmentName;
    public String subcompanyName;
    public int mailStatusss;
    public long reDifferentiate;//添加该传阅对象的用户
    private String headerUrl;

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
