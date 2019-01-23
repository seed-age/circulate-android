package cc.seedland.oa.circulate.modle.net;

import cc.seedland.oa.circulate.global.Global;
import cc.seedland.oa.circulate.utils.LogUtil;

/**
 * Created by hch on 2018/1/13.
 */

public class HttpApis {
//    public static final String HostApi = "http://sloa2.isunn.cn";
    public static final String PREVIEW_API = "https://view.officeapps.live.com/op/view.aspx?src=";
//    String PREVIEW_API = "https://docs.google.com/viewer?url=";

    private static final String MAIL_COUNT = "/app/index/count-mail-number.htm";//首页统计
    private static final String RECEIVED_LIST = "/app/index/grid-receive-list.htm";//已收列表
    private static final String SENT_LIST = "/app/index/grid-send-list.htm";//已发列表
    private static final String DELETE_MAIL = "/app/send/delete-mail.htm";//已发删除
    private static final String SENT_FOCUS_MAIL = "/app/send/batch-attention.htm";//已发关注
    private static final String RECEIVED_FOCUS_MAIL = "/app/received/batch-insert-attention.htm";//已收关注
    private static final String JUMP_MAIL = "/app/received/batch-update-state.htm";//跳过
    private static final String REMOVED_LIST = "/app/index/grid-delete-list.htm";//删除列表
    private static final String SEARCH_REMOVED_LIST = "/app/alreadydelete/find-like-delete.htm";//搜索已删除
    private static final String SEARCH_RECEIVE_LIST = "/app/received/find-like-list.htm";//搜索收到
    private static final String SEARCH_SEND_LIST = "/app/send/find-search-like.htm";//搜索收到
    private static final String READ_CY = "/app/received/update-mail-status.htm";//标记为已读
    private static final String POST_RECEIVE_DISCUSS = "/app/received/insert-discuss.htm";//发表评论
    private static final String POST_SENT_DISCUSS = "/app/send/insert-send-discuss.htm";//发表评论
    private static final String DISCUSS_LIST = "/app/send/grid-discuss.htm";//评论列表
    private static final String MAIL_DETAIL = "/app/received/find-mail-particulars.htm";//传阅详情
    private static final String OBJECT_LIST = "/app/received/find-mail-object.htm";//传阅对象
    private static final String CONFIRM_CY = "/app/received/insert-confirm.htm";//确认
    private static final String ADD_CY_OBJECT = "/app/received/insert-mail-object.htm";//新增传阅对象
    private static final String REMOVE_OBJECT = "/app/received/delete-mail-object.htm";//删除传阅对象
    private static final String TEST_CONTACTS = "/app/create/find-user-mssage.htm";//模拟通讯录
    private static final String DOWNLOAD = "/app/received/send-download-file.htm";//下载附件
    private static final String DRAFT_LIST = "/app/wait/grid-wait-send-list.htm";//草稿列表
    private static final String DELETE_DRAFT = "/app/wait/batch-delete-wait.htm";//删除草稿
    private static final String SEARCH_DRAFT = "/app/wait/find-like-wait-send.htm";//搜索草稿
    private static final String LOAD_FILE_LIST = "/app/create/grid-file-mssage.htm";//获取附件列表
    private static final String CREATE_MAIL = "/app/create/insert-mail.htm";//新建传阅
    private static final String UPLOAD_FILE = "/app/create/find-file-name.htm";//上传附件
    private static final String SEARCH_FILE = "/app/create/find-like-name.htm";//附件搜索
    private static final String DELETE_FILE = "/app/create/delete-upload.htm";//删除附件
    private static final String PREVIEW_FILE = "/app/create/preview-file.htm";//预览附件
    private static final String INSERT_OBJECT_WAIT = "/app/wait/insert-mail-object-wait.htm";//新增待发传阅对象
    private static final String DELETE_OBJECT_WAIT = "/app/wait/delete-wait-mail-object.htm";//删除待发传阅对象

    /**
     * 首页统计
     * @return
     */
    public static String getMailCount() {
        LogUtil.i("call mail count api");
        return Global.sKnife.getHost() + MAIL_COUNT;
    }

    public static String getReceivedList() {
        LogUtil.i("call received list api");
        return Global.sKnife.getHost() + RECEIVED_LIST;
    }

    public static String getSentList() {
        LogUtil.i("call sent list api");
        return Global.sKnife.getHost() + SENT_LIST;
    }

    public static String getDeleteMail() {
        LogUtil.i("call delete mail api");
        return Global.sKnife.getHost() + DELETE_MAIL;
    }

    public static String getSentFocusMail() {
        LogUtil.i("call sent focus mail api");
        return Global.sKnife.getHost() + SENT_FOCUS_MAIL;
    }

    public static String getReceivedFocusMail() {
        LogUtil.i("call received focus mail api");
        return Global.sKnife.getHost() + RECEIVED_FOCUS_MAIL;
    }

    public static String getJumpMail() {
        LogUtil.i("call jump mail api");
        return Global.sKnife.getHost() + JUMP_MAIL;
    }

    public static String getRemovedList() {
        return Global.sKnife.getHost() + REMOVED_LIST;
    }

    public static String getSearchRemovedList() {
        return Global.sKnife.getHost() + SEARCH_REMOVED_LIST;
    }

    public static String getSearchReceiveList() {
        return Global.sKnife.getHost() + SEARCH_RECEIVE_LIST;
    }

    public static String getSearchSendList() {
        return Global.sKnife.getHost() + SEARCH_SEND_LIST;
    }

    public static String getReadCy() {
        return Global.sKnife.getHost() + READ_CY;
    }

    public static String getPostReceiveDiscuss() {
        return Global.sKnife.getHost() + POST_RECEIVE_DISCUSS;
    }

    public static String getPostSentDiscuss() {
        return Global.sKnife.getHost() + POST_SENT_DISCUSS;
    }

    public static String getDiscussList() {
        return Global.sKnife.getHost() + DISCUSS_LIST;
    }

    public static String getMailDetail() {
        return Global.sKnife.getHost() + MAIL_DETAIL;
    }

    public static String getObjectList() {
        return Global.sKnife.getHost() + OBJECT_LIST;
    }

    public static String getConfirmCy() {
        return Global.sKnife.getHost() + CONFIRM_CY;
    }

    public static String getAddCyObject() {
        return Global.sKnife.getHost() + ADD_CY_OBJECT;
    }

    public static String getRemoveObject() {
        return Global.sKnife.getHost() + REMOVE_OBJECT;
    }

    public static String getTestContacts() {
        return Global.sKnife.getHost() + TEST_CONTACTS;
    }

    public static String getDOWNLOAD() {
        return Global.sKnife.getHost() + DOWNLOAD;
    }

    public static String getDraftList() {
        return Global.sKnife.getHost() + DRAFT_LIST;
    }

    public static String getDeleteDraft() {
        return Global.sKnife.getHost() + DELETE_DRAFT;
    }

    public static String getSearchDraft() {
        return Global.sKnife.getHost() + SEARCH_DRAFT;
    }

    public static String getLoadFileList() {
        return Global.sKnife.getHost() + LOAD_FILE_LIST;
    }

    public static String getCreateMail() {
        return Global.sKnife.getHost() + CREATE_MAIL;
    }

    public static String getUploadFile() {
        return Global.sKnife.getHost() + UPLOAD_FILE;
    }

    public static String getSearchFile() {
        return Global.sKnife.getHost() + SEARCH_FILE;
    }

    public static String getDeleteFile() {
        return Global.sKnife.getHost() + DELETE_FILE;
    }

    public static String getPreviewFile() {
        return Global.sKnife.getHost() + PREVIEW_FILE;
    }

    public static String insertObjectWait() {
        return Global.sKnife.getHost() + INSERT_OBJECT_WAIT;
    }
    public static String deleteObjectWait() {
        return Global.sKnife.getHost() + DELETE_OBJECT_WAIT;
    }
}
