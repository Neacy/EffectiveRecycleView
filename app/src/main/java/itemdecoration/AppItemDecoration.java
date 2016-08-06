package itemdecoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by jayuchou on 16/8/6.
 */
public class AppItemDecoration extends RecyclerView.ItemDecoration {

    // divider的颜色Drawable
    private Drawable mDivider;
    // divider的高度
    private int dividerHeight;

    public AppItemDecoration(Context context, int dividerHeight, int color) {
        mDivider = new ColorDrawable(context.getResources().getColor(color));
        this.dividerHeight = dividerHeight;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        drawVertical(c, parent);
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int itemCount = parent.getChildCount();
        for (int i = 0; i < itemCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + lp.bottomMargin;
            final int bottom = top + dividerHeight;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0, 0, 0, dividerHeight);// 设置这个ItemView的哪个方向的padding值。
    }
}
