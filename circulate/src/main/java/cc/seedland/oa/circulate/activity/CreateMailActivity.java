package cc.seedland.oa.circulate.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import cc.seedland.oa.circulate.R;
import cc.seedland.oa.circulate.adapter.FilteredArrayAdapter;
import cc.seedland.oa.circulate.base.CirculateBaseActivity;
import cc.seedland.oa.circulate.global.Global;
import cc.seedland.oa.circulate.modle.bean.AttachInfo;
import cc.seedland.oa.circulate.modle.bean.CYDetailInfo;
import cc.seedland.oa.circulate.modle.bean.FileInfo;
import cc.seedland.oa.circulate.modle.bean.MailInfo;
import cc.seedland.oa.circulate.modle.bean.UserInfo;
import cc.seedland.oa.circulate.modle.net.BaseResponse;
import cc.seedland.oa.circulate.modle.net.HttpApis;
import cc.seedland.oa.circulate.modle.net.HttpService;
import cc.seedland.oa.circulate.modle.net.ResponseHandler;
import cc.seedland.oa.circulate.utils.PinyinUtils;
import cc.seedland.oa.circulate.utils.ReceivessCache;
import cc.seedland.oa.circulate.utils.UISkipUtils;
import cc.seedland.oa.circulate.utils.Utils;
import cc.seedland.oa.circulate.view.BottomDialog;
import cc.seedland.oa.circulate.view.MyToolbar;
import cc.seedland.oa.circulate.view.ResultDialog;
import cc.seedland.oa.circulate.view.contacts.ContactsCompletionView;
import cc.seedland.oa.circulate.view.contacts.TokenCompleteTextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2018/1/5 0005.
 */

public class CreateMailActivity extends CirculateBaseActivity implements /*TokenCompleteTextView
        .TokenListener<UserInfo>,*/
        ResponseHandler {

    private BottomDialog mBottomDialog;
    private LinearLayout mLlAccessoryContainer;
    private LayoutInflater mInflater;
    //    private ChipsInput chipsInput;
    private ScrollView mScrollView;

    private TextView receiverV;
//    private ContactsCompletionView mCompletionView;
//    private List<UserInfo> filterData = new ArrayList<>();
//    private FilteredArrayAdapter<UserInfo> mFilterAdapter;

    private EditText mEdtTheme;
    private EditText mEdtContent;
    private View mViewDivider;
    private List<UserInfo> mUserInfos = new ArrayList<>();
    private ResultDialog mResultDialog;
    private List<String> mBulkids;
    private List<AttachInfo> mAttachInfos = new ArrayList<>();
    private BottomDialog mFileMenu;
    private int mMailId;
    private MailInfo mMailInfo;

    private UserInfo currentUser;
  @Override
    public int getLayoutRes() {
        return R.layout.activity_create_mail;
    }

    @Override
    public void initView() {

      currentUser = new UserInfo();
      currentUser.userId = Global.sKnife.getCurrentUserId();

        mInflater = LayoutInflater.from(this);
        initStatusBar();
        initToolbar();
        mLlAccessoryContainer = findView(R.id.ll_accessory_container);
//        chipsInput = findView(R.id.chips_input);
        mScrollView = findView(R.id.scrollView);

//        mCompletionView = findView(R.id.searchView);
        receiverV = findView(R.id.receiver);
        receiverV.setMovementMethod(ScrollingMovementMethod.getInstance());

//        filterData = Global.sUserInfo;
//        if (filterData != null) {
//            mFilterAdapter = new FilteredArrayAdapter<UserInfo>(this, R.layout
//                    .item_search_contacts, filterData) {
//                @NonNull
//                @Override
//                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup
//                        parent) {
//                    if (convertView == null) {
//
//                        LayoutInflater l = (LayoutInflater) getContext().getSystemService(Activity
//                                .LAYOUT_INFLATER_SERVICE);
//                        convertView = l.inflate(R.layout.item_search_contacts, parent, false);
//                    }
//
//                    final UserInfo u = getItem(position);
//                    ((TextView) convertView.findViewById(R.id.tv_name)).setText(u.lastName);
//                    ((TextView) convertView.findViewById(R.id.tv_company)).setText(u.subcompanyName);
//                    ((TextView) convertView.findViewById(R.id.tv_department)).setText(u.departmentName);
//
//                    convertView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            for (UserInfo userInfo : mUserInfos) {
//                                if (u.userId.equals(userInfo.userId)) {
//                                    return;
//                                }
//                            }
//
////                            mCompletionView.replaceText(mCompletionView.convertSelectionToString(u));
//
////                        mCompletionView.addObject(u);
//                        }
//                    });
//                    return convertView;
//                }
//
//
//                @Override
//                protected boolean keepObject(UserInfo obj, String mask) {
//                    String lastName = obj.lastName;
//                    if (mask.length() > lastName.length()) {
//                        return false;
//                    }
//                    if (mask.matches("[0-9]+") || (mask.getBytes().length != mask.length())) {//当输入数字或中文的时候
//                        for (int i = 0; i < mask.length(); i++) {
//                            String maskStr = String.valueOf(mask.charAt(i));
//                            String nameStr = String.valueOf(lastName.charAt(i));
//                            if (!maskStr.equals(nameStr)) {
//                                return false;
//                            }
//                        }
//                        return true;
////                        if (lastName.contains(mask)) {
////                            return true;
////                        } else {
////                            return false;
////                        }
//                    } else {
//                        StringBuilder nameFront = new StringBuilder();
//                        StringBuilder namePinyin = new StringBuilder();
//                        StringBuilder maskPinyin = new StringBuilder();
//                        for (int i = 0; i < lastName.length(); i++) {
//                            nameFront.append(lastName.charAt(i));
//                        }
//                        for (int i = 0; i < nameFront.length(); i++) {
//                            namePinyin.append(PinyinUtils.getPinyin(String.valueOf(nameFront
//                                    .charAt(i))).toLowerCase().charAt(0));
//                        }
//                        for (int i = 0; i < mask.length(); i++) {
//                            maskPinyin.append(PinyinUtils.getPinyin(String.valueOf(mask.charAt(i)
//                            )).toLowerCase());
//                        }
//                        if (maskPinyin.length() > namePinyin.length()) {
//                            return false;
//                        }
//                        for (int i = 0; i < maskPinyin.length(); i++) {
//                            String marskStr = String.valueOf(maskPinyin.charAt(i));
//                            String nameStr = String.valueOf(namePinyin.charAt(i));
//                            if (!marskStr.equals(nameStr)) {
//                                return false;
//                            }
//                        }
//                        return true;
//                    }
//                }
//
//            };
//        }
//        mCompletionView.setAdapter(mFilterAdapter);
//        mCompletionView.setTokenListener(this);
//        mCompletionView.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Select);

        mEdtTheme = findView(R.id.edt_theme);
        mEdtContent = findView(R.id.edt_content);
        mViewDivider = findView(R.id.view_divider);
    }

    private void initToolbar() {
        MyToolbar toolbar = findView(R.id.toolbar);
        toolbar.setBackClickListener(this);
        toolbar.setTitle("新建传阅");
        toolbar.setRightText("发送");
        toolbar.setOnRightTextClickListener(this);
    }

    private void initStatusBar() {
        FrameLayout flTopBar = findView(R.id.fl_top_bar);
        int statusBarHeight = Global.getStatusBarHeight(this);
        flTopBar.setPadding(0, statusBarHeight, 0, 0);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        mMailId = intent.getIntExtra("MAIL_ID", -1);
        if (mMailId != -1) {
            showDelayDialog();
            HttpService.loadMailDetail(mMailId, 0, this);
        }
    }

    @Override
    public void onClick(View v, int id) {
        if (id == R.id.fl_add) {
            //TODO 530 新增人员这里receivess size过大，需要进行缓存
//            if (mUserInfos != null && !mUserInfos.isEmpty()) {
//                ReceivessCache.clear();
//                boolean status = ReceivessCache.pullReceivessObj(mUserInfos);
//                if (status) {
////                    UISkipUtils.skipToContactsActivity(this, mUserInfos, -1);
//                    UISkipUtils.skipToContactsActivity(this, -1);
//                }
//            }
            UISkipUtils.skipToContactsActivity(this, mUserInfos, -1, 0);
        } else if (id == R.id.fl_accessory) {
            showMenu();
        } else if (id == R.id.tv_pick_file) {
            mBottomDialog.dismiss();
            UISkipUtils.skipToDBankActivity(this);
        } else if (id == R.id.tv_cancel) {
            mBottomDialog.dismiss();
        } else if (id == R.id.tv_right) {
            send(true);
        } else if (id == R.id.ll_back) {
            finish();
            if (mMailInfo == null)
                send(false);
        }
    }

    private void send(boolean isSend) {
//        List<ChipInfo> chipInfos = (List<ChipInfo>) chipsInput.getSelectedChipList();
//        for (ChipInfo chipInfo : chipInfos) {
//            LogUtil.e("name = " + chipInfo.getLabel());
//        }
        String themeStr = mEdtTheme.getText().toString();
        String contentStr = mEdtContent.getText().toString();
        if (isSend) {
            if (TextUtils.isEmpty(contentStr)) {
                showToast("传阅内容不能为空");
                return;
            }
        } else {
            if (TextUtils.isEmpty(contentStr) && mUserInfos.size() == 0 && TextUtils.isEmpty
                    (themeStr) && mBulkids ==
                    null) {
                return;
            }
        }
        if (TextUtils.isEmpty(themeStr)) {
            themeStr = "（无主题）";
        }
        List<String> receiveUserId = new ArrayList<>();
        if (mUserInfos != null) {
            for (UserInfo userInfo : mUserInfos) {
                receiveUserId.add(String.valueOf(userInfo.userId));
            }
        }
        HttpService.createMail(isSend, receiveUserId, mBulkids, themeStr, contentStr, mMailId,
                this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UISkipUtils.TO_EDIT) {
            if (resultCode == UISkipUtils.FROM_EDIT) {
                List<UserInfo> userInfoList = data.getParcelableArrayListExtra("USER");
                if (userInfoList != null) {
//                    mUserInfos.clear();
//                    mCompletionView.clear();
                mUserInfos.addAll(userInfoList);
                updateContent(receiverV, mUserInfos);
//                    for (UserInfo userInfo : userInfoList) {
//                        mCompletionView.addObject(userInfo);
//                    }
                }
            }
        } else if (requestCode == UISkipUtils.TO_DBANK) {
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
            HttpService.uploadFile(0, neid, this);
        }
    }

    private void showMenu() {
        if (mBottomDialog == null)
            mBottomDialog = new BottomDialog(this, R.layout.dialog_pick_menu);
        mBottomDialog.show();
        TextView tvPickFile = mBottomDialog.findViewById(R.id.tv_pick_file);
        TextView tvCancel = mBottomDialog.findViewById(R.id.tv_cancel);
        tvPickFile.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }

//    @Override
//    public void onTokenAdded(UserInfo token) {
//        mUserInfos.add(token);
//    }
//
//    @Override
//    public void onTokenRemoved(UserInfo token) {
//        mUserInfos.remove(token);
//    }
//
//    @Override
//    public void onDuplicateRemoved(UserInfo token) {
//
//    }

    @Override
    public void onError(String msg, String code) {
        hideDelayDialog();
        showToast(msg);
    }

    @Override
    public void onSuccess(String json, JSONObject jsonObject, BaseResponse response) {
        hideDelayDialog();
        if (response != null) {
            int type = response.getType();
            if (type == HttpApis.getCreateMail().hashCode()) {
                JSONObject data = jsonObject.optJSONObject("data");
                if (data != null) {
                    boolean transmission = data.optBoolean("transmission");
                    if (transmission) {
                        showSuccessDialog();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mResultDialog.dismiss();
                                finish();
                            }
                        }, 1000);
                    } else {
                        showToast(response.getMsg());
                    }
                }
            } else if (type == HttpApis.getUploadFile().hashCode()) {
                String dataStr = jsonObject.optString("data");
                mBulkids = new ArrayList<>();
                List<AttachInfo> attachInfos = Utils.parseJsonArray(dataStr, AttachInfo.class);
                mAttachInfos.addAll(attachInfos);
                mLlAccessoryContainer.removeAllViews();
                if (mAttachInfos != null && mAttachInfos.size() > 0) {
                    refreshFile();
                } else {
                    mViewDivider.setVisibility(View.GONE);
                }
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
                String category = previewStr.substring(previewStr.lastIndexOf(".") + 1);
                switch (category) {
                    case "txt":
                        UISkipUtils.skipToWebActivity(this, Global.sKnife.getHost() + previewStr);
                        break;
                    case "pdf":
                        UISkipUtils.skipToWebActivity(this, Global.sKnife.getHost() + previewStr);
                        break;
                    case "rar":
                        UISkipUtils.skipToWebActivity(this, Global.sKnife.getHost() + previewStr);
                        break;
                    case "zip":
                        UISkipUtils.skipToWebActivity(this, Global.sKnife.getHost() + previewStr);
                        break;
                    case "dwg":
                        UISkipUtils.skipToWebActivity(this, Global.sKnife.getHost() + previewStr);
                        break;
                    case "png":
                        UISkipUtils.skipToWebActivity(this, Global.sKnife.getHost() + previewStr);
                        break;
                    case "jpg":
                        UISkipUtils.skipToWebActivity(this, Global.sKnife.getHost() + previewStr);
                        break;
                    default:
                        UISkipUtils.skipToWebActivity(this, HttpApis.PREVIEW_API + Global.sKnife.getHost() +
                                previewStr);
                        break;
                }
            } else if (type == HttpApis.getMailDetail().hashCode()) {
                String dataStr = jsonObject.optString("data");
                if (!TextUtils.isEmpty(dataStr) && !"null".equalsIgnoreCase(dataStr)) {
                    refreshData(dataStr);
                }
            }
        }
    }


    private void refreshData(String dataStr) {
        mMailInfo = Utils.parseJson(dataStr, MailInfo.class);
        if (mMailInfo != null) {
            mMailId = mMailInfo.mailId;
            List<UserInfo> receives = mMailInfo.receivess;
            if (receives != null) {
//                for (UserInfo receive : receives) {
//                    mCompletionView.addObject(receive);

//                }
                mUserInfos.addAll(receives);
                updateContent(receiverV, mUserInfos);
            }
            if (mMailInfo.title.equals("（无主题）")) {
                mEdtTheme.setHint(mMailInfo.title);
            } else {
                mEdtTheme.setText(mMailInfo.title);
            }
            mEdtContent.setText(mMailInfo.mailContent);
            List<AttachInfo> attachmentItemss = mMailInfo.attachmentItemss;
            if (attachmentItemss != null) {
                mAttachInfos.addAll(attachmentItemss);
                if (attachmentItemss != null && attachmentItemss.size() > 0) {
                    refreshFile();
                } else {
                    mViewDivider.setVisibility(View.GONE);
                }
            }
        }
    }

    private void refreshFile() {
        mLlAccessoryContainer.removeAllViews();
        mViewDivider.setVisibility(View.VISIBLE);
        for (final AttachInfo itemss : mAttachInfos) {
            if (mBulkids != null)
                mBulkids.add(itemss.bulkId);
            View view = mInflater.inflate(R.layout.item_accessory, mLlAccessoryContainer, false);
            TextView tvFileTitle = view.findViewById(R.id.tv_file_title);
            TextView tvFileSize = view.findViewById(R.id.tv_file_size);
            ImageView ivType = view.findViewById(R.id.iv_type);
            String fileName = itemss.fileName;
            tvFileTitle.setText(fileName == null ? itemss.path : fileName);
            tvFileSize.setText(itemss.itemSize);
//            String fileType = itemss.fileName.substring(fileName.lastIndexOf("."));
            switch (itemss.fileCategory) {
                case "txt":
                    ivType.setImageResource(R.drawable.icon_txt);
                    break;
                case "pdf":
                    ivType.setImageResource(R.drawable.icon_pdf);
                    break;
                case "ppt":
                    ivType.setImageResource(R.drawable.icon_ppt);
                    break;
                case "pptx":
                    ivType.setImageResource(R.drawable.icon_ppt);
                    break;
                case "xlsx":
                    ivType.setImageResource(R.drawable.icon_excel);
                    break;
                case "docx":
                    ivType.setImageResource(R.drawable.icon_word);
                    break;
                case "jpg":
                    ivType.setImageResource(R.drawable.icon_pic);
                    break;
                case "png":
                    ivType.setImageResource(R.drawable.icon_pic);
                    break;
                case "gif":
                    ivType.setImageResource(R.drawable.icon_pic);
                    break;
                case "rar":
                    ivType.setImageResource(R.drawable.icon_zip);
                    break;
                case "zip":
                    ivType.setImageResource(R.drawable.icon_zip);
                    break;
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showFileMenu(itemss);
                }
            });
            mLlAccessoryContainer.addView(view);
        }
    }

    private void showFileMenu(final AttachInfo itemss) {
        if (mFileMenu == null)
            mFileMenu = new BottomDialog(this, R.layout.dialog_read_file);
        mFileMenu.show();
        TextView tvDownload = mFileMenu.findViewById(R.id.tv_download);
        tvDownload.setText(itemss.fileName + "(" + itemss.itemSize + ")");
        mFileMenu.findViewById(R.id.tv_preview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDelayDialog();
                mFileMenu.dismiss();
//                HttpService.previewFile(itemss.itemId, itemss.bulkId, CreateMailActivity.this);
                Global.sPreViewFileName = itemss.fileName;
                HttpService.downloadFile(itemss.itemId, CreateMailActivity.this);
            }
        });

        mFileMenu.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFileMenu.dismiss();
            }
        });
        mFileMenu.findViewById(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFileMenu.dismiss();
                mAttachInfos.remove(itemss);
                refreshFile();
            }
        });

    }

    private void showSuccessDialog() {
        if (mResultDialog == null)
            mResultDialog = new ResultDialog(this);
        mResultDialog.show();
    }

    private void updateContent(TextView v, List<UserInfo> list) {

        // 去重
        Set<UserInfo> set = new HashSet<>(list);
        list.clear();
        list.addAll(set);
        list.remove(currentUser);

        String result = "";
        if(list != null && !list.isEmpty()) {
            String raw = list.toString();
            result = raw.substring(1, raw.length() - 1);
        }
        v.setText(result);
        int offset = (v.getLineCount() - 1) * v.getLineHeight();
        if(offset > (v.getHeight() - v.getLineHeight())){
            v.scrollTo(0,offset - v.getHeight() + v.getLineHeight());
        }
    }
}
