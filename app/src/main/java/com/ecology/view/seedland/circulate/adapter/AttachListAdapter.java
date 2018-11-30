package com.ecology.view.seedland.circulate.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ecology.view.seedland.circulate.R;
import com.ecology.view.seedland.circulate.modle.bean.FileInfo;

import java.util.List;

/**
 * Created by Administrator on 2018/1/25 0025.
 */

public class AttachListAdapter extends BaseMultiItemQuickAdapter<FileInfo, BaseViewHolder> {


    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public AttachListAdapter(List<FileInfo> data) {
        super(data);
        addItemType(FileInfo.TYPE_DIR, R.layout.item_dbank_dir);
        addItemType(FileInfo.TYPE_FILE, R.layout.item_search_attach);
    }

    @Override
    protected void convert(BaseViewHolder helper, FileInfo item) {
        int itemType = item.getItemType();
        String path = item.path;
        String name = path.substring(path.lastIndexOf("/") + 1);
        if (itemType == FileInfo.TYPE_DIR) {
//            helper.setText(R.id.tv_dir_name, TextUtils.isEmpty(item.desc) ? item.path : item.desc);
            helper.setText(R.id.tv_dir_name, name);
        } else if (itemType == FileInfo.TYPE_FILE) {
//            helper.setText(R.id.tv_file_title, TextUtils.isEmpty(item.desc) ? item.path : item.desc);
            helper.setText(R.id.tv_file_title, name);
            helper.setText(R.id.tv_file_size, item.size);
            ImageView ivSelector = helper.getView(R.id.iv_selector);
            ImageView ivType = helper.getView(R.id.iv_type);
            ivSelector.setSelected(item.isSelected);
            int dotIndex = item.path.lastIndexOf(".");
            String suffix = item.path.substring(dotIndex + 1);
            switch (suffix) {
                case "txt":
                    ivType.setImageResource(R.drawable.icon_txt);
                    break;
                case "pdf":
                    ivType.setImageResource(R.drawable.icon_pdf);
                    break;
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
        }

    }
}
