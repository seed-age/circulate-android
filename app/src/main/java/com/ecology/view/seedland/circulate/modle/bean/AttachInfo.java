package com.ecology.view.seedland.circulate.modle.bean;

import java.io.Serializable;

/**
 * Created by hch on 2018/1/19.
 */

public class AttachInfo implements Serializable{
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
}
