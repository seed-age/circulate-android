package com.ecology.view.seedland.circulate.modle.net;

/**
 * Created by hch on 2018/1/13.
 */

public interface HttpApis {
    String HostApi = "http://sloa2.isunn.cn";
    String PREVIEW_API = "https://view.officeapps.live.com/op/view.aspx?src=";
//    String PREVIEW_API = "https://docs.google.com/viewer?url=";

    String MAIL_COUNT = HostApi + "/app/index/count-mail-number.htm";//首页统计
    String RECEIVED_LIST = HostApi + "/app/index/grid-receive-list.htm";//已收列表
    String SENT_LIST = HostApi + "/app/index/grid-send-list.htm";//已发列表
    String DELETE_MAIL = HostApi + "/app/send/delete-mail.htm";//已发删除
    String SENT_FOCUS_MAIL = HostApi + "/app/send/batch-attention.htm";//已发关注
    String RECEIVED_FOCUS_MAIL = HostApi + "/app/received/batch-insert-attention.htm";//已收关注
    String JUMP_MAIL = HostApi + "/app/received/batch-update-state.htm";//跳过
    String REMOVED_LIST = HostApi + "/app/index/grid-delete-list.htm";//删除列表
    String SEARCH_REMOVED_LIST = HostApi + "/app/alreadydelete/find-like-delete.htm";//搜索已删除
    String SEARCH_RECEIVE_LIST = HostApi + "/app/received/find-like-list.htm";//搜索收到
    String SEARCH_SEND_LIST = HostApi + "/app/send/find-search-like.htm";//搜索收到
    String READ_CY = HostApi + "/app/received/update-mail-status.htm";//标记为已读
    String POST_RECEIVE_DISCUSS = HostApi + "/app/received/insert-discuss.htm";//发表评论
    String POST_SENT_DISCUSS = HostApi + "/app/send/insert-send-discuss.htm";//发表评论
    String DISCUSS_LIST = HostApi + "/app/send/grid-discuss.htm";//评论列表
    String MAIL_DETAIL = HostApi + "/app/received/find-mail-particulars.htm";//传阅详情
    String OBJECT_LIST = HostApi + "/app/received/find-mail-object.htm";//传阅对象
    String CONFIRM_CY = HostApi + "/app/received/insert-confirm.htm";//确认
    String ADD_CY_OBJECT = HostApi + "/app/received/insert-mail-object.htm";//新增传阅对象
    String REMOVE_OBJECT = HostApi + "/app/received/delete-mail-object.htm";//删除传阅对象
    String TEST_CONTACTS = HostApi + "/app/create/find-user-mssage.htm";//模拟通讯录
    String DOWNLOAD = HostApi + "/app/received/send-download-file.htm";//下载附件
    String DRAFT_LIST = HostApi + "/app/wait/grid-wait-send-list.htm";//草稿列表
    String DELETE_DRAFT = HostApi + "/app/wait/batch-delete-wait.htm";//删除草稿
    String SEARCH_DRAFT = HostApi + "/app/wait/find-like-wait-send.htm";//搜索草稿
    String LOAD_FILE_LIST = HostApi + "/app/create/grid-file-mssage.htm";//获取附件列表
    String CREATE_MAIL = HostApi + "/app/create/insert-mail.htm";//新建传阅
    String UPLOAD_FILE = HostApi + "/app/create/find-file-name.htm";//上传附件
    String SEARCH_FILE = HostApi + "/app/create/find-like-name.htm";//附件搜索
    String DELETE_FILE = HostApi + "/app/create/delete-upload.htm";//删除附件
    String PREVIEW_FILE = HostApi + "/app/create/preview-file.htm";//预览附件
}
