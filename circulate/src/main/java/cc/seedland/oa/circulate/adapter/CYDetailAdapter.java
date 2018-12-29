package cc.seedland.oa.circulate.adapter;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import cc.seedland.oa.circulate.R;
import cc.seedland.oa.circulate.activity.CYDetailActivity;
import cc.seedland.oa.circulate.global.Global;
import cc.seedland.oa.circulate.modle.bean.AttachInfo;
import cc.seedland.oa.circulate.modle.bean.CYDetailInfo;
import cc.seedland.oa.circulate.modle.bean.DiscussesInfo;
import cc.seedland.oa.circulate.modle.bean.MailInfo;
import cc.seedland.oa.circulate.modle.net.HttpApis;
import cc.seedland.oa.circulate.modle.net.HttpService;
import cc.seedland.oa.circulate.utils.Utils;
import cc.seedland.oa.circulate.view.BottomDialog;
import com.zzhoujay.richtext.RichText;

import java.util.ArrayList;
import java.util.List;

import cn.carbs.android.expandabletextview.library.ExpandableTextView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018/1/23 0023.
 */

public class CYDetailAdapter extends BaseMultiItemQuickAdapter<CYDetailInfo, BaseViewHolder> {

    private LayoutInflater mInflater;
    private int mFlag;

    private RelativeLayout mRlOmit;
    private LinearLayout mLlDetail;
    private BottomDialog mFileMenu;
    private final CYDetailActivity mCyDetailActivity;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public CYDetailAdapter(Context context, List<CYDetailInfo> data) {
        super(data);
        mCyDetailActivity = (CYDetailActivity) context;
        mInflater = LayoutInflater.from(context);
        addItemType(CYDetailInfo.MAIL_DETAIL, R.layout.item_mail_detail);
        addItemType(CYDetailInfo.MAIL_DISCUSS, R.layout.item_detail_discuss);
    }

    @Override
    protected void convert(BaseViewHolder helper, CYDetailInfo item) {
        int itemType = item.getItemType();
        switch (itemType) {
            case CYDetailInfo.MAIL_DETAIL:
                MailInfo mailInfo = item.getMailInfo();
                putDetailData(helper, mailInfo);
                helper.addOnClickListener(R.id.tv_open);
                helper.addOnClickListener(R.id.tv_close);
                mLlDetail = helper.getView(R.id.ll_detail);
                mRlOmit = helper.getView(R.id.rl_omit);
                break;
            case CYDetailInfo.MAIL_DISCUSS:
                List<DiscussesInfo> discussesInfos = item.getDiscussesList();
                LinearLayout llDiscussesContainer = helper.getView(R.id.ll_discuss_container);
                if (discussesInfos == null || discussesInfos.size() == 0) {
                    llDiscussesContainer.setVisibility(View.GONE);
                } else {
                    llDiscussesContainer.setVisibility(View.VISIBLE);
                    helper.setText(R.id.tv_comment_count, "评论(" + item.getTotalRecord() + ")");
                    llDiscussesContainer.removeViews(1, llDiscussesContainer.getChildCount() - 1);
//            mLlDiscussesContainer.removeAllViews();
                    for (DiscussesInfo discussesInfo : discussesInfos) {
                        View view = mInflater.inflate(R.layout.item_comment_list, llDiscussesContainer,
                                false);
                        TextView tvName = view.findViewById(R.id.tv_name);
                        TextView tvDepartment = view.findViewById(R.id.tv_department);
                        TextView tvTime = view.findViewById(R.id.tv_time);
                        ExpandableTextView etv = view.findViewById(R.id.etv);
                        CircleImageView civHead = view.findViewById(R.id.civ_head);
                        //普通视图中的更新
                        etv.setText(discussesInfo.discussContent);
                        //在ListView/RecyclerView中的应用
                        etv.updateForRecyclerView(discussesInfo.discussContent, etv.getWidth(),
                                ExpandableTextView.STATE_SHRINK);
                        //etvWidth为控件的真实宽度，state是控件所处的状态，“收缩”/“伸展”状态
                        tvName.setText(discussesInfo.lastName);
                        tvDepartment.setText(discussesInfo.loginId);
                        String dateStr = Utils.formatDate(discussesInfo.createTime);
                        dateStr = dateStr.substring(dateStr.indexOf("-") + 1, dateStr.length());
                        tvTime.setText(discussesInfo.time);
//                Utils.displayDrawable(this,discussesInfo.);
                        llDiscussesContainer.addView(view);
                    }
                }
                break;
        }
    }

    public void openDetail() {
        mRlOmit.setVisibility(View.GONE);
        mLlDetail.setVisibility(View.VISIBLE);
    }

    public void closeDetail() {
        mLlDetail.setVisibility(View.GONE);
        mRlOmit.setVisibility(View.VISIBLE);
    }


    private void putDetailData(BaseViewHolder helper, final MailInfo mailInfo) {
        List<AttachInfo> attachmentItems = mailInfo.attachmentItemss;
        helper.setText(R.id.tv_mail_title, mailInfo.title);
        TextView tvContent = helper.getView(R.id.tv_content);
        WebView webView = helper.getView(R.id.web_content);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); //取消滚动条白边效果
        webView.getSettings().setDefaultTextEncodingName("UTF-8") ;
        webView.getSettings().setBlockNetworkImage(false);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            webView.getSettings().setMixedContentMode(webView.getSettings().MIXED_CONTENT_ALWAYS_ALLOW);  //注意安卓5.0以上的权限
        }
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        //https://cy.seedland.cc/web/rich_text.htm?userId=4129&mailId=532用这个吗？
//        webView.loadDataWithBaseURL(Global.sKnife.getHost(),"/web/rich_text.htm?userId="+mailInfo.userId+"&mailId="+mailInfo.mailId,"text/html", "UTF-8", null);
        String url = Global.sKnife.getHost()+"/web/rich_text.htm?userId="+mailInfo.userId+"&mailId="+mailInfo.mailId;
        Log.i("CYDetailAdapter", "show mail content with " + url);
        webView.loadUrl(url);
        // 设置为Html
//        RichText.fromHtml(mailInfo.mailContent).autoPlay(true).into(tvContent);
        helper.setText(R.id.tv_sender, mailInfo.lastName);
        TextView tvAttachNum = helper.getView(R.id.tv_attach_num);
        TextView tvAccessoryNum = helper.getView(R.id.tv_accessory_num);
        if (attachmentItems == null || attachmentItems.size() == 0) {
            tvAttachNum.setVisibility(View.GONE);
            tvAccessoryNum.setVisibility(View.GONE);
        } else {
            tvAttachNum.setVisibility(View.VISIBLE);
            tvAccessoryNum.setVisibility(View.VISIBLE);
            tvAttachNum.setText(String.valueOf(attachmentItems.size()));
            tvAccessoryNum.setText("附件数:  " + attachmentItems.size() + "个");
        }
//        helper.setText(R.id.tv_attach_num, attachmentItems == null ? "0" : String.valueOf(attachmentItems.size()));
        helper.setText(R.id.tv_deliver, Html.fromHtml("发件人:  <font color ='#008cd6'>" + mailInfo.lastName + "</font>"));
        if (mFlag == 3) {
            helper.setText(R.id.tv_time, "传阅时间:  " + mailInfo.receiveTime);
        }else {
            helper.setText(R.id.tv_time, "传阅时间:  " + Utils.formatDate(mailInfo.createTime));
        }
        helper.setText(R.id.tv_receiver, Html.fromHtml("接收人:  <font color ='#22262A'>" + mailInfo
                .allReceiveName + "</font>"));
        if (attachmentItems == null || attachmentItems.size() == 0)
            helper.setGone(R.id.view_divider, false);
        else
            helper.setGone(R.id.view_divider, true);
//        helper.setText(R.id.tv_accessory_num, "附件数:  " + (attachmentItems == null ? 0 : attachmentItems.size()) +
// "个");
        LinearLayout llAccessoryContainer = helper.getView(R.id.ll_accessory_container);
        llAccessoryContainer.removeAllViews();
        if (attachmentItems != null) {
            for (int i = 0; i < attachmentItems.size(); i++) {
                View view = mInflater.inflate(R.layout.item_accessory, llAccessoryContainer, false);
                ImageView ivType = view.findViewById(R.id.iv_type);
                TextView tvFileTitle = view.findViewById(R.id.tv_file_title);
                TextView tvFileSize = view.findViewById(R.id.tv_file_size);
                View vDivider = view.findViewById(R.id.view_divider);
                final AttachInfo attachBean = attachmentItems.get(i);
                String fileName = attachBean.fileName;
                tvFileTitle.setText(fileName);
                tvFileSize.setText(attachBean.itemSize);
                vDivider.setVisibility(i == attachmentItems.size() - 1 ? View.GONE : View.VISIBLE);
                String fileCategory = attachBean.fileCategory;
                switch (fileCategory) {
                    case "txt":
                        ivType.setImageResource(R.drawable.icon_txt);
                        break;
                    case "pdf":
                        ivType.setImageResource(R.drawable.icon_pdf);
                        break;
                    case "pptx":
                    case "ppt":
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
//                    UISkipUtils.skipToWebActivity(CYDetailActivity.this, HttpApis.HostApi +
// attachBean.urlPath);

                        showMenu(attachBean, mailInfo.mailId, mailInfo.stepStatus);
                    }
                });
                llAccessoryContainer.addView(view);
            }
        }
    }

    private void showMenu(final AttachInfo attachBean, final int mailId, int stepStatus) {
        if (mFileMenu == null)
            mFileMenu = new BottomDialog(mContext, R.layout.dialog_read_file);
        mFileMenu.show();
        TextView tvDelete = mFileMenu.findViewById(R.id.tv_delete);
        if (mFlag == 2 || stepStatus == 3) {
            tvDelete.setVisibility(View.GONE);
        } else {
            tvDelete.setVisibility(View.VISIBLE);
        }
        TextView tvDownload = mFileMenu.findViewById(R.id.tv_download);
        tvDownload.setText(attachBean.fileName + "(" + attachBean.itemSize + ")");
//        tvDownload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                List<String> itemId = new ArrayList<>();
//                itemId.add(String.valueOf(attachBean.itemId));
//                HttpService.downloadFile(itemId, new ResponseHandler() {
//                    @Override
//                    public void onError(String msg) {
//
//                    }
//
//                    @Override
//                    public void onSuccess(String json, JSONObject jsonObject, BaseResponse response) {
//                        int type = response.getType();
//                        if (type == HttpApis.DOWNLOAD.hashCode()) {
//                            CYDetailActivity cyDetailActivity = (CYDetailActivity) mContext;
//                            String dataStr = jsonObject.optString("data");
//                            cyDetailActivity.parseFileJson(dataStr, attachBean);
//                        }
//                    }
//                });
//            }
//        });
        mFileMenu.findViewById(R.id.tv_preview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.sPreViewFileName = attachBean.fileName;
                mCyDetailActivity.showDownloadDialog(attachBean.itemId);
                mFileMenu.dismiss();
//                HttpService.previewFile(attachBean.itemId,attachBean.bulkId,mCyDetailActivity);
            }
        });

        mFileMenu.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFileMenu.dismiss();
            }
        });
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFileMenu.dismiss();
                mCyDetailActivity.showDelayDialog();
                HttpService.deleteFile(attachBean.itemId, mailId, mCyDetailActivity);
            }
        });

    }

    public void setFlag(int flag) {
        mFlag = flag;
    }
}
