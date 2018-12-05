package cc.seedland.oa.circulate.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;

import cc.seedland.oa.circulate.R;
import cc.seedland.oa.circulate.global.Global;

/**
 * Created by Granden on 2017/8/26.
 */

public class SimpleItemDecoration extends RecyclerView.ItemDecoration {


    private Bitmap bitmap;
    private Paint.FontMetrics fontMetrics;
    private int wight;
    private int itemDecorationHeight;
    private Paint paint;
    private ObtainTextCallback callback;
    private float itemDecorationPadding;
    private TextPaint textPaint;
    private Rect text_rect = new Rect();

    public SimpleItemDecoration(Context context, ObtainTextCallback callback) {

        wight = context.getResources().getDisplayMetrics().widthPixels;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint.setColor(Global.getColor(R.color.color_bg));
        itemDecorationHeight = Global.dp2px(25);
        itemDecorationPadding = Global.dp2px(15);
        this.callback = callback;


        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        textPaint.setColor(Global.getColor(R.color.color_22262A));
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextSize(Global.dp2px(14));
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        fontMetrics = new Paint.FontMetrics();
        textPaint.getFontMetrics(fontMetrics);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = parent.getChildAt(i);
            int top = view.getTop() - itemDecorationHeight;
            int bottom = top + itemDecorationHeight;


            int position = parent.getChildAdapterPosition(view);
            String content = callback.getText(position);
            if (!TextUtils.isEmpty(content)) {
                textPaint.getTextBounds(content, 0, content.length(), text_rect);

                if (isFirstInGroup(position)) {
                    c.drawRect(0, top, wight, bottom, paint);
                    c.drawText(content, itemDecorationPadding, bottom - fontMetrics.descent, textPaint);

                }
            }
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        View child0 = parent.getChildAt(0);
        int position = parent.getChildAdapterPosition(child0);
        String content = callback.getText(position);
        if (TextUtils.isEmpty(content))
            return;
        if (child0.getBottom() <= itemDecorationHeight && isFirstInGroup(position + 1)) {
            c.drawRect(0, 0, wight, child0.getBottom(), paint);
            c.drawText(content, itemDecorationPadding, child0.getBottom() - fontMetrics.descent,
                    textPaint);
        } else {
            c.drawRect(0, 0, wight, itemDecorationHeight, paint);
            c.drawText(content, itemDecorationPadding, itemDecorationHeight - fontMetrics
                    .descent, textPaint);
        }
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State
            state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        //除了first item以外,当前item和下一个item的文字比比较,只要不一样就马上要腾出一条bar的位置
        if (isFirstInGroup(position)) {
            outRect.top = itemDecorationHeight;
        }

    }

    public interface ObtainTextCallback {
        String getText(int position);
    }

    private boolean isFirstInGroup(int pos) {
        if (pos == 0) {
            return true;
        } else {
            String prevGroupId = callback.getText(pos - 1);
            String groupId = callback.getText(pos);
            if (prevGroupId.equals(groupId)) {
                return false;
            } else {
                return true;
            }
        }
    }
}
