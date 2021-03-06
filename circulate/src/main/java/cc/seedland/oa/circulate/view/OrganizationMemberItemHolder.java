package cc.seedland.oa.circulate.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cc.seedland.oa.circulate.R;
import cc.seedland.oa.circulate.global.Global;
import cc.seedland.oa.circulate.modle.bean.UserInfo;
import cc.seedland.oa.circulate.utils.BizUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.unnamed.b.atv.model.TreeNode;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by hch on 2018/2/2.
 */

public class OrganizationMemberItemHolder extends TreeNode.BaseNodeViewHolder<UserInfo> {
    private OnNodeSelectListener mOnNodeSelectListener;

    public OrganizationMemberItemHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(final TreeNode node, final UserInfo value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.item_contacts_contact, null, false);
        view.findViewById(R.id.tv_letter).setVisibility(View.GONE);
        TextView tvName = view.findViewById(R.id.tv_name);
        tvName.setText(value.lastName);
        TextView tvDepartment = view.findViewById(R.id.tv_department);
        tvDepartment.setText(BizUtils.formatOrganInfo(value.subcompanyName, value.departmentName));
        final ImageView ivSelector = view.findViewById(R.id.iv_selector);
        ivSelector.setSelected(node.isSelected());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                node.setSelected(!node.isSelected());
//                ivSelector.setSelected(node.isSelected());
                mOnNodeSelectListener.onNodeSelectListener(ivSelector,node,value);
            }
        });

        if (!TextUtils.isEmpty(value.headerUrl)) {
            Global.sKnife.loadImage(value, (ImageView) view.findViewById(R.id.civ_head));
        }

        return view;
    }

    public void setOnNodeSelectListener(OnNodeSelectListener onNodeSelectListener) {
        mOnNodeSelectListener = onNodeSelectListener;
    }

    public interface OnNodeSelectListener {
        void onNodeSelectListener(ImageView ivSelector,TreeNode node,UserInfo value);
    }
}
