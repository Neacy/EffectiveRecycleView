package com.neacy.effective.click;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by jayuchou on 16/8/6.
 */
public class AppClickListener extends RecyclerView.SimpleOnItemTouchListener {

    private GestureDetectorCompat gestureDetector;

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public AppClickListener(final RecyclerView recyclerView, final ItemClickListener listener) {
        gestureDetector = new GestureDetectorCompat(recyclerView.getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (listener != null) {
                    listener.onItemClick(childView, recyclerView.getChildAdapterPosition(childView));
                }
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetector.onTouchEvent(e);
        return super.onInterceptTouchEvent(rv, e);
    }
}
