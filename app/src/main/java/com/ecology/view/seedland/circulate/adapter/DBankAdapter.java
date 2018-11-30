package com.ecology.view.seedland.circulate.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ecology.view.seedland.circulate.R;
import com.ecology.view.seedland.circulate.modle.bean.DBankDirInfo;

import java.util.List;

/**
 * Created by Administrator on 2018/1/30 0030.
 */

public class DBankAdapter extends BaseQuickAdapter<DBankDirInfo,BaseViewHolder>{
    public DBankAdapter(int layoutResId, @Nullable List<DBankDirInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DBankDirInfo item) {
        helper.setText(R.id.tv_dir_name,item.name);
    }
}
