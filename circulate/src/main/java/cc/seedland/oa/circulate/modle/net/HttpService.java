package cc.seedland.oa.circulate.modle.net;

import android.text.TextUtils;

import cc.seedland.oa.circulate.global.Constants;

import java.util.List;

/**
 * Created by hch on 2018/1/19.
 */

public class HttpService {
    private static int pageRows = 10;

    /**
     * 获取传阅数量
     *
     * @param handler
     */
    public static void loadMailCount(ResponseHandler handler) {
        HttpParams params = new HttpParams();
        params.put("userId", Constants.userId);
        BaseRequest.getInstance()
                .setUrl(HttpApis.MAIL_COUNT)
                .setType(HttpApis.MAIL_COUNT.hashCode())
                .params(params)
                .postExecute(handler);
    }

    /**
     * 获取传阅列表数据
     *
     * @param type
     * @param status
     * @param page
     * @param orderBy
     * @param handler
     */
    public static void loadCYListData(String api, int type, int status, int page, int orderBy,
                                      ResponseHandler handler) {
        HttpParams params = new HttpParams();
        params.put("status", status);
        params.put("page", page);
        params.put("userId", Constants.userId);
        params.put("pageRows", pageRows);
        if (orderBy != -1)
            params.put("orderBy", orderBy);
        BaseRequest.getInstance()
                .setUrl(api)
                .setType(type)
                .params(params)
                .postExecute(handler);
    }

    /**
     * 删除传阅
     *
     * @param mailId
     * @param handler
     */
    public static void deleteMail(List<String> mailId, ResponseHandler handler) {
        HttpParams params = new HttpParams();
        params.put("userId", Constants.userId);
        params.put("mailId", mailId);
        BaseRequest.getInstance()
                .setUrl(HttpApis.DELETE_MAIL)
                .setType(HttpApis.DELETE_MAIL.hashCode())
                .params(params)
                .postExecute(handler);
    }

    /**
     * 关注(已发送)
     *
     * @param mailId
     * @param handler
     */
    public static void focusSentMail(List<String> mailId, boolean attention, ResponseHandler handler) {
        HttpParams params = new HttpParams();
        params.put("userId", Constants.userId);
        params.put("mailId", mailId);
        params.put("attention", attention);
        BaseRequest.getInstance()
                .setUrl(HttpApis.SENT_FOCUS_MAIL)
                .setType(HttpApis.SENT_FOCUS_MAIL.hashCode())
                .params(params)
                .postExecute(handler);
    }

    /**
     * 关注（已收）
     *
     * @param mailId
     * @param attention
     * @param handler
     * @param status 1:已发 3：已收
     */
    public static void focusReceivedMail(List<String> mailId, boolean attention,int status, ResponseHandler handler) {
        HttpParams params = new HttpParams();
        params.put("userId", Constants.userId);
        params.put("mailId", mailId);
        params.put("attention", attention);
        params.put("status", status);
        BaseRequest.getInstance()
                .setUrl(HttpApis.RECEIVED_FOCUS_MAIL)
                .setType(HttpApis.RECEIVED_FOCUS_MAIL.hashCode())
                .params(params)
                .postExecute(handler);
    }

    /**
     * 跳过
     *
     * @param mailId
     * @param handler
     */
    public static void jumpMail(List<String> mailId, ResponseHandler handler) {
        HttpParams params = new HttpParams();
        params.put("userId", Constants.userId);
        params.put("mailId", mailId);
        BaseRequest.getInstance()
                .setUrl(HttpApis.JUMP_MAIL)
                .setType(HttpApis.JUMP_MAIL.hashCode())
                .params(params)
                .postExecute(handler);
    }

    /**
     * 获取已删除列表
     *
     * @param type
     * @param handler
     */
    public static void loadRemovedList(int type, int page, ResponseHandler handler) {
        HttpParams params = new HttpParams();
        params.put("userId", Constants.userId);
        params.put("page", page);
        params.put("pageRows", pageRows);
        BaseRequest.getInstance()
                .setUrl(HttpApis.REMOVED_LIST)
                .setType(type)
                .params(params)
                .postExecute(handler);
    }

    /**
     * 搜索已删除
     *
     * @param type
     * @param page
     * @param handler
     */
    public static void searchCYList(String api, int type, int page, String likeName, ResponseHandler handler) {
        HttpParams params = new HttpParams();
        params.put("userId", Constants.userId);
        params.put("likeName", likeName);
        params.put("page", page);
        params.put("pageRows", pageRows);
        BaseRequest.getInstance()
                .setUrl(api)
                .setType(type)
                .params(params)
                .postExecute(handler);
    }

    /**
     * 标记为已读
     *
     * @param mailId
     * @param handler
     */
    public static void readCY(long mailId, ResponseHandler handler) {
        HttpParams params = new HttpParams();
        params.put("userId", Constants.userId);
        params.put("mailId", mailId);
        BaseRequest.getInstance()
                .setUrl(HttpApis.READ_CY)
                .setType(HttpApis.READ_CY.hashCode())
                .params(params)
                .postExecute(handler);
    }

    /**
     * 发表评论
     *
     * @param mailId
     * @param discussContent
     * @param handler
     */
    public static void postDiscuss(String api, long mailId, String discussContent, ResponseHandler handler) {
        HttpParams params = new HttpParams();
        params.put("userId", Constants.userId);
        params.put("mailId", mailId);
        params.put("discussContent", discussContent);
        BaseRequest.getInstance()
                .setUrl(api)
                .setType(api.hashCode())
                .params(params)
                .postExecute(handler);
    }

    /**
     * 获取评论列表
     *
     * @param type
     * @param handler
     */
    public static void loadDiscussList(int type, long mailId, int page, ResponseHandler handler) {
        HttpParams params = new HttpParams();
        params.put("userId", Constants.userId);
        params.put("mailId", mailId);
        params.put("page", page);
        params.put("pageRows", pageRows);
        BaseRequest.getInstance()
                .setUrl(HttpApis.DISCUSS_LIST)
                .setType(type)
                .params(params)
                .postExecute(handler);
    }

    /**
     * 获取传阅详情
     *
     * @param mailId
     * @param handler
     */
    public static void loadMailDetail(long mailId, int mailStatus, ResponseHandler handler) {
        HttpParams params = new HttpParams();
        params.put("userId", !TextUtils.isEmpty(Constants.userId) ? Constants.userId : Constants.userId);
        params.put("mailId", mailId);
        params.put("mailStatus", mailStatus);
        BaseRequest.getInstance()
                .setUrl(HttpApis.MAIL_DETAIL)
                .setType(HttpApis.MAIL_DETAIL.hashCode())
                .params(params)
                .postExecute(handler);
    }

    /**
     * 传阅对象
     *
     * @param type
     * @param mailId
     * @param handler
     */
    public static void loadObjectList(int type, long mailId, ResponseHandler handler) {
        HttpParams params = new HttpParams();
        params.put("userId", Constants.userId);
        params.put("mailId", mailId);
//        params.put("page",page);
//        params.put("pageRows",pageRows);
        BaseRequest.getInstance()
                .setUrl(HttpApis.OBJECT_LIST)
                .setType(type)
                .params(params)
                .postExecute(handler);
    }

    /**
     * 确认
     *
     * @param mailId
     * @param remark
     * @param statusConfirm 确认：true  重新确认：false
     * @param handler
     */
    public static void confirmCy(long mailId, String remark, boolean statusConfirm, ResponseHandler handler) {
        HttpParams params = new HttpParams();
        params.put("userId", Constants.userId);
        params.put("mailId", mailId);
        params.put("remark", remark);
        params.put("statusConfirm", statusConfirm);
        BaseRequest.getInstance()
                .setUrl(HttpApis.CONFIRM_CY)
                .setType(HttpApis.CONFIRM_CY.hashCode())
                .params(params)
                .postExecute(handler);
    }

    /**
     * 新增传阅对象
     *
     * @param mailId
     * @param receiveUserId
     * @param handler
     */
    public static void addCYObject(long mailId, List<String> receiveUserId, ResponseHandler handler) {
        HttpParams params = new HttpParams();
        params.put("userId", Constants.userId);
        params.put("mailId", mailId);
        params.put("receiveUserId", receiveUserId);
        BaseRequest.getInstance()
                .setUrl(HttpApis.ADD_CY_OBJECT)
                .setType(HttpApis.ADD_CY_OBJECT.hashCode())
                .params(params)
                .postExecute(handler);
    }

    /**
     * 删除传阅对象
     *
     * @param mailId
     * @param receiveUserId
     * @param handler
     */
    public static void removeObject(long mailId, String receiveUserId, ResponseHandler handler) {
        HttpParams params = new HttpParams();
        params.put("userId", Constants.userId);
        params.put("mailId", mailId);
        params.put("receiveUserId", receiveUserId);
        BaseRequest.getInstance()
                .setUrl(HttpApis.REMOVE_OBJECT)
                .setType(HttpApis.REMOVE_OBJECT.hashCode())
                .params(params)
                .postExecute(handler);
    }

    /**
     * 模拟通讯录
     *
     * @param handler
     */
    public static void testContacts(ResponseHandler handler) {
        HttpParams params = new HttpParams();
        params.put("subcompanyid", "");
        params.put("departmentid", "");
        params.put("deptLastChangdate", "");
        params.put("loginid", "");
        params.put("userId", Constants.userId);
        BaseRequest.getInstance()
                .setUrl(HttpApis.TEST_CONTACTS)
                .setType(HttpApis.TEST_CONTACTS.hashCode())
                .params(params)
                .postExecute(handler);
    }

    /**
     * 下载
     *
     * @param handler
     */
    public static void downloadFile(List<String> itemId, ResponseHandler handler) {
        HttpParams params = new HttpParams();
        params.put("itemId", itemId);
        BaseRequest.getInstance()
                .setUrl(HttpApis.DOWNLOAD)
                .setType(HttpApis.DOWNLOAD.hashCode())
                .params(params)
                .postExecute(handler);
    }

    /**
     * 下载
     *
     * @param handler
     */
    public static void downloadFile(int itemId, ResponseHandler handler) {
        HttpParams params = new HttpParams();
        params.put("itemId", itemId);
        BaseRequest.getInstance()
                .setUrl(HttpApis.DOWNLOAD)
                .setType(HttpApis.DOWNLOAD.hashCode())
                .params(params)
                .postExecute(handler);
    }

    /**
     * 草稿列表
     *
     * @param type
     * @param page
     * @param handler
     */
    public static void loadDraftList(int type, int page, ResponseHandler handler) {
        HttpParams params = new HttpParams();
        params.put("userId", Constants.userId);
        params.put("page", page);
        params.put("pageRows", pageRows);
        BaseRequest.getInstance()
                .setUrl(HttpApis.DRAFT_LIST)
                .setType(type)
                .params(params)
                .postExecute(handler);
    }

    /**
     * 删除草稿
     *
     * @param mailId
     * @param handler
     */
    public static void deleteDraft(List<String> mailId, ResponseHandler handler) {
        HttpParams params = new HttpParams();
        params.put("userId", Constants.userId);
        params.put("mailId", mailId);
        BaseRequest.getInstance()
                .setUrl(HttpApis.DELETE_DRAFT)
                .setType(HttpApis.DELETE_DRAFT.hashCode())
                .params(params)
                .postExecute(handler);
    }

    /**
     * 获取附件列表
     *
     * @param type
     * @param page
     * @param path
     * @param pathType
     * @param handler
     */
    public static void loadFileList(int type, int page, String path, String pathType, ResponseHandler handler) {
        HttpParams params = new HttpParams();
        params.put("path", path);
        params.put("pathType", pathType);
        params.put("page",page);
        params.put("pageRows", pageRows);
        params.put("userId", Constants.userId);
        BaseRequest.getInstance()
                .setUrl(HttpApis.LOAD_FILE_LIST)
                .setType(type)
                .params(params)
                .postExecute(handler);
    }

    /**
     * 新建传阅
     *
     * @param transmission
     * @param receiveUserId
     * @param bulkId
     * @param title
     * @param mailContent
     * @param handler
     */
    public static void createMail(boolean transmission, List<String> receiveUserId, List<String> bulkId,
                                  String title, String mailContent,long mailId, ResponseHandler handler) {
        HttpParams params = new HttpParams();
        params.put("transmission", transmission);
        params.put("receiveUserId", receiveUserId);
        if (bulkId != null)
            params.put("bulkId", bulkId);
        params.put("userId", Constants.userId);
        params.put("title", title);
        params.put("mailContent", mailContent);
        params.put("mailId", mailId);
        BaseRequest.getInstance()
                .setUrl(HttpApis.CREATE_MAIL)
                .setType(HttpApis.CREATE_MAIL.hashCode())
                .params(params)
                .postExecute(handler);
    }

    /**
     * 上传附件
     * @param mailId
     * @param neid
     * @param handler
     */
    public static void uploadFile(long mailId,List<String> neid,ResponseHandler handler) {
        HttpParams params = new HttpParams();
        params.put("mailId",mailId);
        params.put("neid",neid);
        params.put("userId",Constants.userId);
        BaseRequest.getInstance()
                .setUrl(HttpApis.UPLOAD_FILE)
                .setType(HttpApis.UPLOAD_FILE.hashCode())
                .params(params)
                .postExecute(handler);

    }

    /**
     * 查询附件
     * @param type
     * @param page
     * @param likeName
     * @param handler
     */
    public static void searchFile(int type,int page,String likeName,String searchPath,String pathType,ResponseHandler handler) {
        HttpParams params = new HttpParams();
        params.put("likeName",likeName);
        params.put("searchPath",searchPath);
        params.put("pathType",pathType);
        params.put("userId",Constants.userId);
        params.put("page",page);
        params.put("pageRows",pageRows);
        BaseRequest.getInstance()
                .setUrl(HttpApis.SEARCH_FILE)
                .setType(type)
                .params(params)
                .postExecute(handler);

    }

    /**
     * 删除附件
     * @param itemId
     * @param mailId
     * @param handler
     */
    public static void deleteFile(long itemId,long mailId,ResponseHandler handler) {
        HttpParams params = new HttpParams();
        params.put("userId",Constants.userId);
        params.put("itemId",itemId);
        params.put("mailId",mailId);
        BaseRequest.getInstance()
                .setUrl(HttpApis.DELETE_FILE)
                .setType(HttpApis.DELETE_FILE.hashCode())
                .params(params)
                .postExecute(handler);
    }

    /**
     * 预览附件
     * @param itemId
     * @param bulkId
     * @param handler
     */
    public static void previewFile(long itemId,String bulkId,ResponseHandler handler) {
        HttpParams params = new HttpParams();
        params.put("itemId",itemId);
        params.put("bulkId",bulkId);
        BaseRequest.getInstance()
                .setUrl(HttpApis.PREVIEW_FILE)
                .setType(HttpApis.PREVIEW_FILE.hashCode())
                .params(params)
                .postExecute(handler);
    }
}
