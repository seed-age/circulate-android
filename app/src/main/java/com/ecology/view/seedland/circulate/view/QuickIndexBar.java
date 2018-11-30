package com.ecology.view.seedland.circulate.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.ecology.view.seedland.circulate.R;
import com.ecology.view.seedland.circulate.global.Global;

/**
 * Created by ${Ho} on 2017/8/15.
 */

public class QuickIndexBar extends View {
    /**
     * 索引字母颜色
     */
    private static final int LETTER_COLOR = 0xff595959;

    /**
     * 字母索引条背景颜色
     */
    private static final int BG_COLOR = 0xffB0B0B0;

    /**
     * 26个字母 + #
     */
    public static final String[] LETTERS = {
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
            "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"
    };
    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    private float mCellHeight;

    public QuickIndexBar(Context context) {
        this(context, null);
    }

    public QuickIndexBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setTextSize(Global.getDimen(R.dimen.sp_13));
        mPaint.setColor(Global.getColor(R.color.color_22262A));
        mPaint.setAntiAlias(true);// 去锯齿，让字体的边缘变得平滑
    }

    private int getTextWidth(String text) {
        Rect bounds = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.width();
    }

    private int getTextHeight(String text) {
        Rect bounds = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.height();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < LETTERS.length; i++) {
            String letter = LETTERS[i];

            float x = mWidth / 2 - getTextWidth(letter) / 2;
            float y = mCellHeight / 2 + getTextHeight(letter) / 2 + mCellHeight * i;
            canvas.drawText(letter, x, y, mPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //测量宽高
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mCellHeight = 1f * mHeight / LETTERS.length;
    }

    int index = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 按下的是第几个字母
                this.setBackgroundColor(BG_COLOR);
                index = (int) (event.getY() / mCellHeight);
                if (index >= 0 && index < LETTERS.length) {
                    // 用户选中的字母
                    String letter = LETTERS[index];
                    // System.out.println("-----------------------" + letter);

                    // 3. 当事件发生时，调用监听器中的方法
                    if (mOnLetterSelectedListener != null) {
                        mOnLetterSelectedListener.onLetterSelected(letter);
                    }

                    // 显示首字母
                    tvLetter.setVisibility(View.VISIBLE);
                    tvLetter.setText(letter);
                }

                // 按下需要返回true，以便持续地接收到后续的move和up事件
                return true;
            case MotionEvent.ACTION_MOVE:
                // 按下的是第几个字母
                int tempIndex = (int) (event.getY() / mCellHeight);
                if (tempIndex >= 0 && tempIndex < LETTERS.length) {

                    // 用户当前选中的字母与上一次不一样时，才打印
                    if (tempIndex != index) {
                        // 用户选中的字母
                        String letter = LETTERS[tempIndex];
                        // System.out.println("-----------------------" + letter);
                        // 3. 当事件发生时，调用监听器中的方法
//                        if (mOnLetterSelectedListener != null) {
//                            mOnLetterSelectedListener.onLetterSelected(letter);
//                        }

                        // 显示首字母
                        tvLetter.setVisibility(View.VISIBLE);
                        tvLetter.setText(letter);
                    }

                    index = tempIndex;
                }
                break;
            case MotionEvent.ACTION_UP:
                this.setBackgroundResource(Color.alpha(100));
                tvLetter.setVisibility(View.GONE);
                String letter = LETTERS[index];
                // 3. 当事件发生时，调用监听器中的方法
                if (mOnLetterSelectedListener != null) {
                    mOnLetterSelectedListener.onLetterSelected(letter);
                }
                break;
        }
        return super.onTouchEvent(event);
    }


    public interface OnLetterSelectedListener {
        /**
         * 字母发生了改变
         *
         * @param letter 用户选中的字母
         */
        void onLetterSelected(String letter);
    }

    private OnLetterSelectedListener mOnLetterSelectedListener;
    /**
     * 设置显示首字母的TextView
     */
    private TextView tvLetter;

    public void setOnLetterSelectedListener(OnLetterSelectedListener listener) {
        this.mOnLetterSelectedListener = listener;
    }

    /**
     * 设置显示首字母的TextView
     *
     * @param tvLetter
     */
    public void setTextView(TextView tvLetter) {
        this.tvLetter = tvLetter;
    }
}
