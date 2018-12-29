package cc.seedland.oa.circulate.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
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
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import cc.seedland.oa.circulate.R;
import cc.seedland.oa.circulate.adapter.CYDetailAdapter;
import cc.seedland.oa.circulate.base.CirculateBaseActivity;
import cc.seedland.oa.circulate.global.Global;
import cc.seedland.oa.circulate.modle.bean.AttachInfo;
import cc.seedland.oa.circulate.modle.bean.CYDetailInfo;
import cc.seedland.oa.circulate.modle.bean.DiscussListInfo;
import cc.seedland.oa.circulate.modle.bean.DiscussesInfo;
import cc.seedland.oa.circulate.modle.bean.FileInfo;
import cc.seedland.oa.circulate.modle.bean.MailInfo;
import cc.seedland.oa.circulate.modle.bean.UserInfo;
import cc.seedland.oa.circulate.modle.net.BaseResponse;
import cc.seedland.oa.circulate.modle.net.DownloadUtil;
import cc.seedland.oa.circulate.modle.net.HttpApis;
import cc.seedland.oa.circulate.modle.net.HttpService;
import cc.seedland.oa.circulate.modle.net.ResponseHandler;
import cc.seedland.oa.circulate.utils.LogUtil;
import cc.seedland.oa.circulate.utils.NetWorkSpeedUtils;
import cc.seedland.oa.circulate.utils.OADownloadUtil;
import cc.seedland.oa.circulate.utils.ReceivessCache;
import cc.seedland.oa.circulate.utils.UISkipUtils;
import cc.seedland.oa.circulate.utils.Utils;
import cc.seedland.oa.circulate.view.BottomDialog;
import cc.seedland.oa.circulate.view.DownLoadDialog;
import cc.seedland.oa.circulate.view.EditTextDialog;
import cc.seedland.oa.circulate.view.MyToolbar;

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
    private DownLoadDialog mDownLoadDialog;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};


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
                if (id == R.id.tv_open) {
                    mAdapter.openDetail();
                } else if (id == R.id.tv_close) {
                    mAdapter.closeDetail();
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
                commentApi = HttpApis.getPostSentDiscuss();
                break;
            case 2: // 删除
                mLlBottombar.setVisibility(View.GONE);
                mToolbar.isShowRightFirstIv(false);
                break;
            case 3: //收
                commentApi = HttpApis.getPostReceiveDiscuss();
                break;
        }
    }


    @Override
    public void onClick(View v, int id) {
        if (id == R.id.fl_right_first_ic) {//弹框
            showEditMenu();
        } else if (id == R.id.fl_comment) {
            UISkipUtils.skipToCommentListActivity(this, mMailInfo, commentApi);
        } else if (id == R.id.fl_object) {//人员
            UISkipUtils.skipToObjectListActivity(this, mMailId);
        } else if (id == R.id.fl_focus) {
            boolean receiveAttention = mFlag == 3 ? mMailInfo.receiveAttention : mMailInfo
                    .attention;
            List<String> mailId = new ArrayList<>();
            mailId.add(String.valueOf(mMailInfo.mailId));
            HttpService.focusReceivedMail(mailId, !receiveAttention, mFlag, this);
        } else if (id == R.id.tv_confirm) {
            showConfirmDialog();
        } else if (id == R.id.tv_cancel) {
            mEditDialog.dismiss();
        } else if (id == R.id.tv_add_object) {//新增人员
            //TODO 530 新增人员这里receivess size过大，需要进行缓存
//            if (mMailInfo.receivess != null && !mMailInfo.receivess.isEmpty()) {
//                ReceivessCache.clear();
//                boolean status = ReceivessCache.pullReceivessObj(mMailInfo.receivess);
//                if (status) {
//                    //UISkipUtils.skipToContactsActivity(this, mMailInfo.receivess,mMailId);
//                    UISkipUtils.skipToContactsActivity(this, mMailId);
//                    mEditDialog.dismiss();
//                }
//            }
            UISkipUtils.skipToContactsActivity(this, mMailInfo.receivess, mMailId);
        } else if (id == R.id.tv_add_file) {
            UISkipUtils.skipToDBankActivity(this);
            mEditDialog.dismiss();
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
                    List<UserInfo> userInfos = data.getParcelableArrayListExtra("USER");
                    if (userInfos != null) {
                        List<String> receiveUserId = new ArrayList<>();
                        for (UserInfo userInfo : userInfos) {
                            receiveUserId.add(String.valueOf(userInfo.userId));
                        }
                        if (receiveUserId.size() != 0) {
                            HttpService.addCYObject(mMailId, receiveUserId, this);
                        }
                    }
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
    public void onError(String msg, String code) {
        if ("205".equals(code)) {
            if (mDownLoadDialog != null && mDownLoadDialog.isShowing()) {
                mDownLoadDialog.dismiss();
            }
        }
        hideDelayDialog();
        showToast(msg);
    }

    @Override
    public void onSuccess(String json, JSONObject jsonObject, BaseResponse response) {
        hideDelayDialog();
        if (response != null) {
            int type = response.getType();
            String dataStr = jsonObject.optString("data");
            if (type == HttpApis.getMailDetail().hashCode()) {
                if (!TextUtils.isEmpty(dataStr) && !"null".equalsIgnoreCase(dataStr))
                    refreshData(dataStr);
            } else if (type == HttpApis.getReceivedFocusMail().hashCode()) {
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
            } else if (type == HttpApis.getConfirmCy().hashCode()) {
                showToast(response.getMsg());
                HttpService.loadMailDetail(mMailId, mFlag, this);
            } else if (type == HttpApis.getAddCyObject().hashCode()) {
                showToast(response.getMsg());
                HttpService.loadMailDetail(mMailId, mFlag, this);
            } else if (type == HttpApis.getUploadFile().hashCode()) {
                showToast(response.getMsg());
                HttpService.loadMailDetail(mMailId, mFlag, this);
            } else if (type == HttpApis.getDeleteFile().hashCode()) {
                showToast(response.getMsg());
                HttpService.loadMailDetail(mMailId, mFlag, this);
            } else if (type == HttpApis.getPreviewFile().hashCode()) {
                JSONObject data = jsonObject.optJSONObject("data");
                String url = data.optString("url");
                if (TextUtils.isEmpty(url)) {
                    showToast(response.getMsg());
                    return;
                }
                UISkipUtils.skipToWebActivity(this, url);
            } else if (type == HttpApis.getDOWNLOAD().hashCode()) {
                String previewStr = jsonObject.optString("data");
                if (TextUtils.isEmpty(previewStr)) {
                    showToast(response.getMsg());
                    return;
                }
                final String downloadUrl = Global.sKnife.getHost() + previewStr;
                if (mDownLoadDialog != null && mDownLoadDialog.isShowing()) {
                    TextView tvCancel = mDownLoadDialog.findViewById(R.id.tv_cancel);
                    tvCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mDownLoadDialog.dismiss();
                            OADownloadUtil.get().cancelTag(downloadUrl);
                        }
                    });
                    final TextView tvSpeed = mDownLoadDialog.findViewById(R.id.tv_speed);
                    final ProgressBar progressBar = mDownLoadDialog.findViewById(R.id.progress_bar);
                    progressBar.setMax(100);
//                progressBar.setProgress(40);
//                LogUtil.e("url = " + downloadUrl);
//                String str = HttpApis.HostApi + "/file/8d09513f-d567-44b6-b7e1-132001b3f2b3.png";
                    final Handler handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            switch (msg.what) {
                                case 100:
                                    tvSpeed.setText(msg.obj.toString());
                                    break;
                            }
                            super.handleMessage(msg);
                        }
                    };
                    final NetWorkSpeedUtils netWorkSpeedUtils = new NetWorkSpeedUtils(this, handler);
                    netWorkSpeedUtils.startShowNetSpeed();
                    OADownloadUtil.get().download(downloadUrl, Global.sFileFolder, Global
                            .sPreViewFileName, new OADownloadUtil.OnDownloadListener() {

                        @Override
                        public void onDownloadSuccess() {
                            netWorkSpeedUtils.stop();
                            handler.removeCallbacksAndMessages(null);
                            CYDetailActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mDownLoadDialog.dismiss();
                                    showToast("下载成功");
                                    File file = new File(Global.sFileFolder + "/" + Global
                                            .sPreViewFileName);
                                    if (file.exists()) {
                                        try {
                                            Intent intent = new Intent();
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            //设置intent的Action属性
                                            intent.setAction(Intent.ACTION_VIEW);
                                            //获取文件file的MIME类型
                                            String type = getMIMEType(file);
                                            //设置intent的data和Type属性。
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                intent.setDataAndType(getUriForFile(file), type);
                                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//注意加上这句话
                                            } else {
                                                intent.setDataAndType(Uri.fromFile(file), type);
                                            }
                                            //跳转
                                            CYDetailActivity.this.startActivity(intent);
                                        } catch (ActivityNotFoundException e) {
                                            Toast.makeText(CYDetailActivity.this,
                                                    "附件不能打开，请下载相关软件！", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
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
//                                    tvSpeed.setText(speed + "MB/S");
//                                LogUtil.e("progress = " + progress);
//                                LogUtil.e("speed = " + speed);
                                }
                            });
                        }

                        @Override
                        public void onDownloadFailed() {
                            netWorkSpeedUtils.stop();
                            handler.removeCallbacksAndMessages(null);
                            CYDetailActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mDownLoadDialog.dismiss();
                                    showToast("下载失败");
                                }
                            });
                        }
                    });
                }
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
//                        UISkipUtils.skipToWebActivity(this, HttpApis.PREVIEW_API + HttpApis
// .HostApi + previewStr);
//                        break;
//                }
            }
        }
    }

    public Uri getUriForFile(File file) {
        return FileProvider.getUriForFile(this, Global.sContext.getPackageName() + ".fileprovider", file);
    }

    public void showDownloadDialog(int itemId) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE);
            } else {
                mDownLoadDialog = new DownLoadDialog(this);
                mDownLoadDialog.show();
                HttpService.downloadFile(itemId, this);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    private String getMIMEType(File file) {

        String type = "*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
        /* 获取文件的后缀名*/
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "") return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) {
            //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    private final String[][] MIME_MapTable = {
            //{后缀名，MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},
            {"", "*/*"}
    };

}

