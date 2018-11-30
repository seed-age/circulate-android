package com.ecology.view.seedland.circulate.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.ecology.view.seedland.circulate.activity.CYDetailActivity;
import com.ecology.view.seedland.circulate.activity.CommentListActivity;
import com.ecology.view.seedland.circulate.activity.ContactsActivity;
import com.ecology.view.seedland.circulate.activity.CreateMailActivity;
import com.ecology.view.seedland.circulate.activity.DBankActivity;
import com.ecology.view.seedland.circulate.activity.DraftListActivity;
import com.ecology.view.seedland.circulate.activity.EditDraftCYListActivity;
import com.ecology.view.seedland.circulate.activity.EditReceiveCYListActivity;
import com.ecology.view.seedland.circulate.activity.EditSentCYListActivity;
import com.ecology.view.seedland.circulate.activity.FileListActivity;
import com.ecology.view.seedland.circulate.activity.ObjectListActivity;
import com.ecology.view.seedland.circulate.activity.OrganizationActivity;
import com.ecology.view.seedland.circulate.activity.ReceiveListActivity;
import com.ecology.view.seedland.circulate.activity.RemovedListActivity;
import com.ecology.view.seedland.circulate.activity.SearchCYActivity;
import com.ecology.view.seedland.circulate.activity.SearchFileActivity;
import com.ecology.view.seedland.circulate.activity.SearchPeopleActivity;
import com.ecology.view.seedland.circulate.activity.SelectedContactsActivity;
import com.ecology.view.seedland.circulate.activity.SentListActivity;
import com.ecology.view.seedland.circulate.activity.WebActivity;
import com.ecology.view.seedland.circulate.modle.bean.MailInfo;
import com.ecology.view.seedland.circulate.modle.bean.UserInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/2/20 0020.
 */

public class UISkipUtils {
    public final static int TO_EDIT = 0x001;
    public final static int FROM_EDIT = 0x002;
    public final static int FROM_SELECTED = 0x003;
    public final static int TO_DBANK = 0x004;
    public final static int FROM_SEARCH = 0x005;
    public final static int TO_COMMENT = 0x006;
    public static void skipToCreateMailActivity(Context context) {
        Intent intent = new Intent(context,CreateMailActivity.class);
        context.startActivity(intent);
    }

    public static void skipToCreateMailActivity(Activity context, MailInfo mailInfo) {
        Intent intent = new Intent(context,CreateMailActivity.class);
        intent.putExtra("INFO",mailInfo);
        context.startActivityForResult(intent,TO_EDIT);
    }
    public static void skipToReceiveListActivity(Context context, int type,String title,boolean isShowTab) {
        Intent intent = new Intent(context,ReceiveListActivity.class);
        intent.putExtra("TYPE",type);
        intent.putExtra("TITLE",title);
        intent.putExtra("IS_SHOW_TAB",isShowTab);
        context.startActivity(intent);
    }

    public static void skipToSentListActivity(Context context, int type,String title) {
        Intent intent = new Intent(context,SentListActivity.class);
        intent.putExtra("TYPE",type);
        intent.putExtra("TITLE",title);
        context.startActivity(intent);
    }

    public static void skipToEditReceiveCYListActivity(Activity context, int status) {
        Intent intent = new Intent(context,EditReceiveCYListActivity.class);
        intent.putExtra("STATUS",status);
        context.startActivityForResult(intent,TO_EDIT);
    }

    public static void skipToEditSentCYListActivity(Activity context, int status) {
        Intent intent = new Intent(context,EditSentCYListActivity.class);
        intent.putExtra("STATUS",status);
        context.startActivityForResult(intent,TO_EDIT);
    }

    public static void skipToContactsActivity(Activity context) {
        Intent intent = new Intent(context,ContactsActivity.class);
        context.startActivityForResult(intent,TO_EDIT);
    }

    public static void skipToContactsActivity(Activity context, List<UserInfo> userInfos,long mailId) {
        Intent intent = new Intent(context,ContactsActivity.class);
        intent.putExtra("USER_LIST", (Serializable) userInfos);
        intent.putExtra("MAIL_ID",mailId);
        context.startActivityForResult(intent,TO_EDIT);
    }

    public static void skipToSelectedContactsActivity(Activity context, List<UserInfo>
            selectedUserList,long mailId) {
        Intent intent = new Intent(context,SelectedContactsActivity.class);
        intent.putExtra("SELECTED_USER", (Serializable) selectedUserList);
        intent.putExtra("MAIL_ID",mailId);
        context.startActivityForResult(intent,TO_EDIT);
    }
    //flag 1:发 2:删 3:收
    public static void skipToCYDetailActivity(Activity context, long mailId,int flag) {
        Intent intent = new Intent(context,CYDetailActivity.class);
        intent.putExtra("DATA",mailId);
        intent.putExtra("FLAG",flag);
        context.startActivityForResult(intent,TO_EDIT);
    }

    public static void skipToCommentListActivity(Activity context, MailInfo mailInfo,String api) {
        Intent intent = new Intent(context,CommentListActivity.class);
        intent.putExtra("DATA",mailInfo);
        intent.putExtra("API",api);
        context.startActivityForResult(intent,TO_COMMENT);
    }

    public static void skipToObjectListActivity(Context context, long mailId) {
        Intent intent = new Intent(context,ObjectListActivity.class);
        intent.putExtra("MAILID",mailId);
        context.startActivity(intent);
    }

    public static void skipToSearchPeopleActivity(Activity context, List<UserInfo> selected) {
        Intent intent = new Intent(context,SearchPeopleActivity.class);
        intent.putExtra("SELECTED_DATA", (Serializable) selected);
        context.startActivityForResult(intent,TO_EDIT);
    }
    //type 1:发 2:删 3:收 4:草稿
    public static void skipToSearchCYActivity(Context context, String api,int type) {
        Intent intent = new Intent(context,SearchCYActivity.class);
        intent.putExtra("API",api);
        intent.putExtra("TYPE",type);
        context.startActivity(intent);
    }

    public static void skipToWebActivity(Context context, String url) {
        Intent intent = new Intent(context,WebActivity.class);
        intent.putExtra("URL",url);
        context.startActivity(intent);
    }

    public static void skipToRemovedListActivity(Context context) {
        Intent intent = new Intent(context, RemovedListActivity.class);
        context.startActivity(intent);
    }
    public static void skipToDraftListActivity(Context context) {
        Intent intent = new Intent(context, DraftListActivity.class);
        context.startActivity(intent);
    }

    public static void skipToFileListActivity(Activity context) {
        Intent intent = new Intent(context,FileListActivity.class);
        context.startActivityForResult(intent,TO_EDIT);
    }

    public static void skipToFileListActivity(Activity context, String name, String type,String path) {
        Intent intent = new Intent(context,FileListActivity.class);
        intent.putExtra("dir_name",name);
        intent.putExtra("type",type);
        intent.putExtra("path",path);
        context.startActivityForResult(intent,TO_EDIT);
    }

    public static void skipToEditDraftListActivity(Activity context) {
        Intent intent = new Intent(context,EditDraftCYListActivity.class);
        context.startActivityForResult(intent,TO_EDIT);
    }

    public static void skipToDBankActivity(Activity context) {
        Intent intent = new Intent(context, DBankActivity.class);
        context.startActivityForResult(intent,TO_DBANK);
    }

    public static void skipToDBankActivity(Context context,int flag) {
        Intent intent = new Intent(context, DBankActivity.class);
        intent.putExtra("flag",flag);
        context.startActivity(intent);
    }

    public static void skipToOrganizationActivity(Activity activity, List<UserInfo> userInfos,int type) {
        Intent intent = new Intent(activity,OrganizationActivity.class);
        intent.putExtra("DATA", (Serializable) userInfos);
        intent.putExtra("TYPE",type);
        activity.startActivityForResult(intent,TO_EDIT);
    }

    public static void skipToSearchFileActivity(Activity activity,String path,String pathType) {
        Intent intent = new Intent(activity,SearchFileActivity.class);
        intent.putExtra("PATH",path);
        intent.putExtra("PATH_TYPE",pathType);
        activity.startActivityForResult(intent,TO_EDIT);
    }
}
