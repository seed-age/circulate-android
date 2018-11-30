package com.ecology.view.seedland.circulate.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecology.view.seedland.circulate.R;


/**
 * Created by Administrator on 2018/1/5 0005.
 */

public class MyToolbar extends RelativeLayout {

    private LinearLayout mLlBack;
    private TextView mTvTitle;
    private TextView mTvRight;
    private ImageView mIvRightFirst;
    private ImageView mIvRightSecond;
    private TextView mTvBack;
    private ImageView mIvBack;
    private FrameLayout mFlRightSecondIc;
    private FrameLayout mFlRightFirstIc;

    public MyToolbar(Context context) {
        this(context, null);
    }

    public MyToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = View.inflate(getContext(), R.layout.toolbar, this);
        mLlBack = view.findViewById(R.id.ll_back);
        mTvTitle = view.findViewById(R.id.tv_title);
        mTvRight = view.findViewById(R.id.tv_right);
        mIvRightFirst = view.findViewById(R.id.iv_right_first);
        mIvRightSecond = view.findViewById(R.id.iv_right_second);
        mTvBack = view.findViewById(R.id.tv_back);
        mIvBack = view.findViewById(R.id.iv_back);
        mFlRightSecondIc = view.findViewById(R.id.fl_right_second_ic);
        mFlRightFirstIc = view.findViewById(R.id.fl_right_first_ic);
    }

    public void setRightFirstIvImg(int res) {
        if (!mFlRightFirstIc.isShown())
            mFlRightFirstIc.setVisibility(VISIBLE);
        mIvRightFirst.setImageResource(res);
    }

    public void isShowRightFirstIv(boolean isShow) {
        mFlRightFirstIc.setVisibility(isShow ? VISIBLE : GONE);
    }

    public void setRightFirstIvOnClickListener(OnClickListener onClickListener) {
        mFlRightFirstIc.setOnClickListener(onClickListener);
    }


    public void setRightSecondIvImg(int res) {
        if (!mFlRightSecondIc.isShown())
            mFlRightSecondIc.setVisibility(VISIBLE);
        mIvRightSecond.setImageResource(res);
    }

    public void isShowRightSecondIv(boolean isShow) {
        mFlRightSecondIc.setVisibility(isShow ? VISIBLE : GONE);
    }

    public void setRightSecondIvOnClickListener(OnClickListener onClickListener) {
        mFlRightSecondIc.setOnClickListener(onClickListener);
    }

    public void setOnBackClickListener(final Activity activity) {
        if (!mLlBack.isShown())
            mLlBack.setVisibility(VISIBLE);
        mLlBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
    }

    public void setBackClickListener(OnClickListener onClickListener) {
        if (!mLlBack.isShown())
            mLlBack.setVisibility(VISIBLE);
        mLlBack.setOnClickListener(onClickListener);
    }

    public void setOnBackClickListenerWithResult(final Activity activity, final int resultCode,
                                                 final Intent intent) {
        if (!mLlBack.isShown())
            mLlBack.setVisibility(VISIBLE);
        mLlBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (intent != null)
                    activity.setResult(resultCode, intent);
                else
                    activity.setResult(resultCode);
                activity.finish();
            }
        });
    }

    public void setTitle(String title) {
        if (!mTvTitle.isShown())
            mTvTitle.setVisibility(VISIBLE);
        mTvTitle.setText(title);
    }

    public void setTitleColor(int color) {
        mTvTitle.setTextColor(color);
    }

    public void setRightText(String str) {
        if (!mTvRight.isShown())
            mTvRight.setVisibility(VISIBLE);
        mTvRight.setText(str);
    }

    public void setOnRightTextClickListener(OnClickListener onClickListener) {
        if (!mTvRight.isShown())
            mTvRight.setVisibility(VISIBLE);
        mTvRight.setOnClickListener(onClickListener);
    }

    public void showTextBack() {
        mIvBack.setVisibility(GONE);
        mTvBack.setVisibility(VISIBLE);
    }
}
