package com.ecology.view.seedland.circulate.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.ecology.view.seedland.circulate.R;
import com.ecology.view.seedland.circulate.adapter.CYDetailAdapter;
import com.ecology.view.seedland.circulate.base.CirculateBaseActivity;
import com.ecology.view.seedland.circulate.global.Global;
import com.ecology.view.seedland.circulate.modle.bean.AttachInfo;
import com.ecology.view.seedland.circulate.modle.bean.CYDetailInfo;
import com.ecology.view.seedland.circulate.modle.bean.DiscussListInfo;
import com.ecology.view.seedland.circulate.modle.bean.DiscussesInfo;
import com.ecology.view.seedland.circulate.modle.bean.FileInfo;
import com.ecology.view.seedland.circulate.modle.bean.MailInfo;
import com.ecology.view.seedland.circulate.modle.bean.UserInfo;
import com.ecology.view.seedland.circulate.modle.net.BaseResponse;
import com.ecology.view.seedland.circulate.modle.net.DownloadUtil;
import com.ecology.view.seedland.circulate.modle.net.HttpApis;
import com.ecology.view.seedland.circulate.modle.net.HttpService;
import com.ecology.view.seedland.circulate.modle.net.ResponseHandler;
import com.ecology.view.seedland.circulate.utils.LogUtil;
import com.ecology.view.seedland.circulate.utils.OADownloadUtil;
import com.ecology.view.seedland.circulate.utils.UISkipUtils;
import com.ecology.view.seedland.circulate.utils.Utils;
import com.ecology.view.seedland.circulate.view.BottomDialog;
import com.ecology.view.seedland.circulate.view.DownLoadDialog;
import com.ecology.view.seedland.circulate.view.EditTextDialog;
import com.ecology.view.seedland.circulate.view.MyToolbar;
import com.zzhoujay.richtext.RichText;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by Administrator on 2018/1/9 0009.
 */
@RuntimePermissions
public class CYDetailActivity extends CirculateBaseActivity implements ResponseHandler,
        BaseQuickAdapter
                .RequestLoadMoreListener {

    private LayoutInflater mInflater;
    private ImageView mIvFocus;
    private MailInfo mMailInfo;
    private RecyclerView mRvList;
    private CYDetailAdapter mAdapter;
    private List<CYDetailInfo> mData = new ArrayList<>();
    private List<DiscussesInfo> mCommentData = new ArrayList<>();
    private int page = 1;
    private boolean mLoadMoreEnd = false;
    private boolean mLoadMoreFail = false;
    private long mMailId;
    private TextView mTvConfirm;
    private BottomDialog mEditDialog;
    private ArrayList<String> filePaths = new ArrayList<>();
    private ArrayList<String> photoPaths;
    private ArrayList<String> docPaths;
    private EditTextDialog mEditTextDialog;
    private String commentApi;
    private LinearLayout mLlBottombar;
    private int mFlag;
    private MyToolbar mToolbar;
    private List<AttachInfo> mAttachInfos;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_cy_detail;
    }

    @Override
    public void initView() {
        mInflater = LayoutInflater.from(this);
        initToolbar();
        initStatusBar();
        initRv();
        mIvFocus = findView(R.id.iv_focus);
        mTvConfirm = findView(R.id.tv_confirm);
        mLlBottombar = findView(R.id.ll_bottombar);
    }

    private void initRv() {
        mRvList = findView(R.id.rv_list);
        mRvList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CYDetailAdapter(this, mData);
        mRvList.setAdapter(mAdapter);
    }

    private void initToolbar() {
        mToolbar = findView(R.id.toolbar);
//        toolbar.setOnBackClickListenerWithResult(this,UISkipUtils.FROM_EDIT,null);
        mToolbar.setOnBackClickListener(this);
        mToolbar.setTitle("传阅详情");
        mToolbar.setRightFirstIvImg(R.drawable.icon_edit);
        mToolbar.setRightFirstIvOnClickListener(this);
    }

    private void initStatusBar() {
        FrameLayout flTopBar = findView(R.id.fl_top_bar);
        int statusBarHeight = Global.getStatusBarHeight(this);
        flTopBar.setPadding(0, statusBarHeight, 0, 0);
    }

    @Override
    public void initListener() {
        mRvList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                switch (id) {
                    case R.id.tv_open:
                        mAdapter.openDetail();
                        break;
                    case R.id.tv_close:
                        mAdapter.closeDetail();
                        break;
                }
            }
        });
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        mMailId = intent.getLongExtra("DATA", 0);
        mFlag = intent.getIntExtra("FLAG", 0);
        mAdapter.setFlag(mFlag);
        showDelayDialog();
        HttpService.loadMailDetail(mMailId, mFlag, this);
        switch (mFlag) {
            case 1: // 发
                mTvConfirm.setVisibility(View.GONE);
                commentApi = HttpApis.POST_SENT_DISCUSS;
                break;
            case 2: // 删除
                mLlBottombar.setVisibility(View.GONE);
                mToolbar.isShowRightFirstIv(false);
                break;
            case 3: //收
                commentApi = HttpApis.POST_RECEIVE_DISCUSS;
                break;
        }
    }


    @Override
    public void onClick(View v, int id) {
        switch (id) {
            case R.id.fl_right_first_ic:
                showEditMenu();
                break;
            case R.id.fl_comment:
                UISkipUtils.skipToCommentListActivity(this, mMailInfo, commentApi);
                break;
            case R.id.fl_object:
                UISkipUtils.skipToObjectListActivity(this, mMailId);
                break;
            case R.id.fl_focus:
                boolean receiveAttention = mFlag == 3 ? mMailInfo.receiveAttention : mMailInfo.attention;
                List<String> mailId = new ArrayList<>();
                mailId.add(String.valueOf(mMailInfo.mailId));
                HttpService.focusReceivedMail(mailId, !receiveAttention, mFlag, this);
                break;
            case R.id.tv_confirm:
                showConfirmDialog();
                break;
            case R.id.tv_cancel:
                mEditDialog.dismiss();
                break;
            case R.id.tv_add_object:
                UISkipUtils.skipToContactsActivity(this, mMailInfo.receivess, mMailId);
                mEditDialog.dismiss();
                break;
            case R.id.tv_add_file:
//                CYDetailActivityPermissionsDispatcher.pickFileWithPermissionCheck(this);
//                UISkipUtils.skipToFileListActivity(this);
                UISkipUtils.skipToDBankActivity(this);
                mEditDialog.dismiss();
                break;
        }
    }

    private void showConfirmDialog() {
        mEditTextDialog = new EditTextDialog(this);
        mEditTextDialog.setYesOnclickListener("确定", new EditTextDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                String message = mEditTextDialog.getMessage();
                boolean confirm = false;
                if (!mMailInfo.ifConfirmss) {
                    confirm = true;
                } else if (mMailInfo.afreshConfimss) {
                    confirm = false;
                }
                if (TextUtils.isEmpty(message)) {
                    showToast("确认信息不能为空...");
                    return;
                }
                HttpService.confirmCy(mMailId, message, confirm, CYDetailActivity.this);
                mEditTextDialog.dismiss();
            }
        });
        mEditTextDialog.setNoOnclickListener("取消", new EditTextDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                mEditTextDialog.dismiss();
            }
        });
        mEditTextDialog.show();
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void pickFile() {
        FilePickerBuilder.getInstance().setMaxCount(10)
                .setSelectedFiles(filePaths)
                .setActivityTheme(R.style.AppTheme)
                .pickFile(this);
    }


    public void parseFileJson(String dataStr, AttachInfo attachBean) {
//        List<DownInfo> downInfos = Utils.parseJsonArray(dataStr, DownInfo.class);
//        String urlPath = downInfos.get(0).urlPath;
//        CYDetailActivityPermissionsDispatcher.downloadFileWithPermissionCheck(this, attachBean,
//                HttpApis.HostApi + dataStr);
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void downloadFile(AttachInfo attachBean, String urlPath) {
        DownloadUtil.getInstance().download(urlPath, Environment.getExternalStorageDirectory()
                .toString(), attachBean.fileName, new DownloadUtil.OnDownloadListener() {

            @Override
            public void onDownloadSuccess(String path) {
                LogUtil.e("path= " + path);
            }

            @Override
            public void onDownloading(int progress, float obj) {
                LogUtil.e("progress= " + progress);
            }

            @Override
            public void onDownloadFailed() {
                LogUtil.e("failed");
            }
        });
    }

    private void showEditMenu() {
        if (mEditDialog == null)
            mEditDialog = new BottomDialog(this, R.layout.dialog_edit_menu);
        mEditDialog.show();
        mEditDialog.findViewById(R.id.tv_cancel).setOnClickListener(this);
        mEditDialog.findViewById(R.id.tv_add_object).setOnClickListener(this);
        mEditDialog.findViewById(R.id.tv_add_file).setOnClickListener(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FilePickerConst.REQUEST_CODE_PHOTO:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    photoPaths = new ArrayList<>();
                    photoPaths.addAll(data.getStringArrayListExtra(FilePickerConst
                            .KEY_SELECTED_MEDIA));
                }
                break;
            case FilePickerConst.REQUEST_CODE_DOC:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    docPaths = new ArrayList<>();
                    docPaths.addAll(data.getStringArrayListExtra(FilePickerConst
                            .KEY_SELECTED_DOCS));
                }
                break;
            case UISkipUtils.TO_EDIT:
                if (resultCode == UISkipUtils.FROM_EDIT && data != null) {
                    List<UserInfo> userInfos = (List<UserInfo>) data.getSerializableExtra("USER");
                    List<String> receiveUserId = new ArrayList<>();
                    for (UserInfo userInfo : userInfos) {
                        receiveUserId.add(String.valueOf(userInfo.userId));
                    }
                    HttpService.addCYObject(mMailId, receiveUserId, this);
                }
                break;
            case UISkipUtils.TO_DBANK:
                List<FileInfo> selectedFiles = Global.sSelectedFiles;
                List<String> neid = new ArrayList<>();
                if (selectedFiles == null || selectedFiles.size() == 0)
                    return;
                outer:
                for (FileInfo selectedFile : selectedFiles) {
                    if (mAttachInfos != null && mAttachInfos.size() > 0) {
                        for (AttachInfo attachInfo : mAttachInfos) {
                            if (attachInfo.itemNeid == selectedFile.neid) {
                                continue outer;
                            }
                        }
                    }
                    neid.add(String.valueOf(selectedFile.neid));
                }
                if (neid.size() == 0) {
                    return;
                }
//                for (FileInfo selectedFile : selectedFiles) {
//                    LogUtil.e("neid = " + selectedFile.neid);
//                    neid.add(String.valueOf(selectedFile.neid));
//                }
//                LogUtil.e("mMailId = " + mMailId);
                HttpService.uploadFile(mMailId, neid, this);
                break;
            case UISkipUtils.TO_COMMENT:
                showDelayDialog();
                HttpService.loadMailDetail(mMailId, mFlag, this);
                break;
        }
//        addThemToView(photoPaths, docPaths);
    }


    private void addThemToView(ArrayList<String> imagePaths, ArrayList<String> docPaths) {
        ArrayList<String> filePaths = new ArrayList<>();
        if (imagePaths != null)
            filePaths.addAll(imagePaths);

        if (docPaths != null)
            filePaths.addAll(docPaths);
    }

    @Override
    public void onError(String msg) {
        hideDelayDialog();
        showToast(msg);
    }

    @Override
    public void onSuccess(String json, JSONObject jsonObject, BaseResponse response) {
        hideDelayDialog();
        if (response != null) {
            int type = response.getType();
            String dataStr = jsonObject.optString("data");
            if (type == HttpApis.MAIL_DETAIL.hashCode()) {
                if (!TextUtils.isEmpty(dataStr) && !"null".equalsIgnoreCase(dataStr))
                    refreshData(dataStr);
            } else if (type == HttpApis.RECEIVED_FOCUS_MAIL.hashCode()) {
                showToast(response.getMsg());
                if (mFlag == 3) {
                    mMailInfo.receiveAttention = !mMailInfo.receiveAttention;
                    mIvFocus.setSelected(mMailInfo.receiveAttention);
                } else {
                    mMailInfo.attention = !mMailInfo.attention;
                    mIvFocus.setSelected(mMailInfo.attention);
                }
                setResult(UISkipUtils.FROM_EDIT);
            } else if (type == INIT_DATA) {
                mCommentData.clear();
                refreshDiscuss(dataStr);
            } else if (type == LOAD_MORE) {
                refreshDiscuss(dataStr);
            } else if (type == HttpApis.CONFIRM_CY.hashCode()) {
                showToast(response.getMsg());
                HttpService.loadMailDetail(mMailId, mFlag, this);
            } else if (type == HttpApis.ADD_CY_OBJECT.hashCode()) {
                showToast(response.getMsg());
                HttpService.loadMailDetail(mMailId, mFlag, this);
            } else if (type == HttpApis.UPLOAD_FILE.hashCode()) {
                showToast(response.getMsg());
                HttpService.loadMailDetail(mMailId, mFlag, this);
            } else if (type == HttpApis.DELETE_FILE.hashCode()) {
                showToast(response.getMsg());
                HttpService.loadMailDetail(mMailId, mFlag, this);
            } else if (type == HttpApis.PREVIEW_FILE.hashCode()) {
                JSONObject data = jsonObject.optJSONObject("data");
                String url = data.optString("url");
                if (TextUtils.isEmpty(url)) {
                    showToast(response.getMsg());
                    return;
                }
                UISkipUtils.skipToWebActivity(this, url);
            } else if (type == HttpApis.DOWNLOAD.hashCode()) {
                String previewStr = jsonObject.optString("data");
                if (TextUtils.isEmpty(previewStr)) {
                    showToast(response.getMsg());
                    return;
                }
                final String downloadUrl = HttpApis.HostApi + previewStr;
                final DownLoadDialog downLoadDialog = new DownLoadDialog(this);
                downLoadDialog.show();
                TextView tvCancel = downLoadDialog.findViewById(R.id.tv_cancel);
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        downLoadDialog.dismiss();
                        OADownloadUtil.get().cancelTag(downloadUrl);
                    }
                });
                final TextView tvSpeed = downLoadDialog.findViewById(R.id.tv_speed);
                final ProgressBar progressBar = downLoadDialog.findViewById(R.id.progress_bar);
                progressBar.setMax(100);
//                progressBar.setProgress(40);
//                LogUtil.e("url = " + downloadUrl);
//                String str = HttpApis.HostApi + "/file/8d09513f-d567-44b6-b7e1-132001b3f2b3.png";
                OADownloadUtil.get().download(downloadUrl, Global.sFileFolder, Global.sPreViewFileName, new OADownloadUtil.OnDownloadListener() {

                    @Override
                    public void onDownloadSuccess() {
                        CYDetailActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                downLoadDialog.dismiss();
                                showToast("下载成功");
                                File file = new File(Global.sFileFolder + "/" + Global.sPreViewFileName);
                                if (file.exists()) {
                                    Uri uri = Uri.fromFile(file);
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setDataAndType(uri, "text/plain");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }else{
                                    showToast("文件不存在或已被删除");
                                }
                            }
                        });
                    }

                    @Override
                    public void onDownloading(final int progress, final float speed) {
                        CYDetailActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(progress);
                                tvSpeed.setText(speed + "MB/S");
//                                LogUtil.e("progress = " + progress);
//                                LogUtil.e("speed = " + speed);
                            }
                        });
                    }

                    @Override
                    public void onDownloadFailed() {
                        CYDetailActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                downLoadDialog.dismiss();
                                showToast("下载失败");
                            }
                        });
                    }
                });
//                String category = previewStr.substring(previewStr.lastIndexOf(".") + 1);
//                switch (category) {
//                    case "txt":
//                        UISkipUtils.skipToWebActivity(this, HttpApis.HostApi + previewStr);
//                        break;
//                    case "pdf":
//                        UISkipUtils.skipToWebActivity(this, HttpApis.HostApi + previewStr);
//                        break;
//                    case "rar":
//                        UISkipUtils.skipToWebActivity(this, HttpApis.HostApi + previewStr);
//                        break;
//                    case "zip":
//                        UISkipUtils.skipToWebActivity(this, HttpApis.HostApi + previewStr);
//                        break;
//                    case "dwg":
//                        UISkipUtils.skipToWebActivity(this, HttpApis.HostApi + previewStr);
//                        break;
//                    case "png":
//                        UISkipUtils.skipToWebActivity(this, HttpApis.HostApi + previewStr);
//                        break;
//                    case "jpg":
//                        UISkipUtils.skipToWebActivity(this, HttpApis.HostApi + previewStr);
//                        break;
//                    default:
//                        UISkipUtils.skipToWebActivity(this, HttpApis.PREVIEW_API + HttpApis.HostApi + previewStr);
//                        break;
//                }
            }
        }
    }

    private void refreshDiscuss(String dataStr) {
        DiscussListInfo data = Utils.parseJson(dataStr, DiscussListInfo.class);
        if (data != null) {
            mLoadMoreEnd = data.lastPage;
            if (mLoadMoreEnd) {
                mAdapter.loadMoreEnd();
            } else {
                if (data.list.size() == 10) {
                    mAdapter.setOnLoadMoreListener(this, mRvList);
                }
            }
            List<DiscussesInfo> discussInfo = data.list;
            List<CYDetailInfo> data1 = mAdapter.getData();
            CYDetailInfo cyDetailInfo = data1.get(1);
            mCommentData.addAll(discussInfo);
            cyDetailInfo.setDiscussesList(mCommentData);
            cyDetailInfo.setTotalRecord(data.totalRecord);
//            mData.get(1).setTotalRecord(data.totalRecord);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void refreshData(String dataStr) {
        mMailInfo = Utils.parseJson(dataStr, MailInfo.class);
        mAttachInfos = mMailInfo.attachmentItemss;
        if (mMailInfo.stepStatus == 3) {
            mToolbar.isShowRightFirstIv(false);
        }
        CYDetailInfo cyDetailInfo = new CYDetailInfo(CYDetailInfo.MAIL_DETAIL);
        cyDetailInfo.setMailInfo(mMailInfo);
        if (mData.size() > 0)
            mData.clear();
        mData.add(0, cyDetailInfo);
//        mTvConfirm.setText(mMailInfo.);
        CYDetailInfo cyCommentInfo = new CYDetailInfo(CYDetailInfo.MAIL_DISCUSS);
        cyDetailInfo.setDiscussesList(mCommentData);
        mData.add(cyCommentInfo);
//        putDetailData(mMail);
        if (mFlag == 3) {
            mIvFocus.setSelected(mMailInfo.receiveAttention);
        } else {
            mIvFocus.setSelected(mMailInfo.attention);
        }
        page = 1;
        HttpService.loadDiscussList(INIT_DATA, mMailId, page, this);
        refreshConfirm();
    }

    private void refreshConfirm() {
        if (mMailInfo.ifConfirmss) {
            mTvConfirm.setVisibility(View.GONE);
            if (mMailInfo.afreshConfimss) {
                mTvConfirm.setText("重新确认");
                mTvConfirm.setVisibility(View.VISIBLE);
            } else {
                mTvConfirm.setVisibility(View.GONE);
            }
        } else {
            mTvConfirm.setVisibility(View.VISIBLE);
            mTvConfirm.setText("确认");
        }
        if (mFlag != 3)
            mTvConfirm.setVisibility(View.GONE);
    }

    @Override
    public void onLoadMoreRequested() {
        if (!mLoadMoreFail) {
            if (!mLoadMoreEnd) {
                page++;
                HttpService.loadDiscussList(LOAD_MORE, mMailId, page, CYDetailActivity.this);
            } else {
                mAdapter.loadMoreEnd();
            }
        } else {
            mLoadMoreFail = false;
            mAdapter.loadMoreFail();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RichText.recycle();
    }

    //    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        CYDetailActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode,
// grantResults);
//    }
}