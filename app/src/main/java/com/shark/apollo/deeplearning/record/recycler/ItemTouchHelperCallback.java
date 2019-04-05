package com.shark.apollo.deeplearning.record.recycler;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseViewHolder;
import com.shark.apollo.deeplearning.R;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private static final String TAG = "ItemTouchHelperCallback";
    private static final float MAX_INCREASED_SIZE = 50;

    private int iconInvisibleDefaultWidth;

    private int iconInvisibleDefaultHeight;

    private boolean isFirstShowIcon = true;

    private OnMoveAndSwipedListener mListener;

    public ItemTouchHelperCallback(OnMoveAndSwipedListener listener) {
        mListener = listener;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        Log.d(TAG, "onChildDraw: actionState = " + actionState);
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            int recyclerViewWidth = recyclerView.getMeasuredWidth();
            int deleteFrameWidth = getDeleteFrameWidth(viewHolder);
            BaseViewHolder itemViewHolder = (BaseViewHolder) viewHolder;
            View ivInvisible = itemViewHolder.getView(R.id.iv_invisible);
            View tvDelete = itemViewHolder.getView(R.id.tv_delete);
            if(Math.abs(dX) <= deleteFrameWidth) {
                viewHolder.itemView.scrollTo(-(int) dX, 0);
            } else {
                viewHolder.itemView.scrollTo(deleteFrameWidth, 0);
                if(isFirstShowIcon) {
                    isFirstShowIcon = false;
                    iconInvisibleDefaultWidth = ivInvisible.getMeasuredWidth();
                    iconInvisibleDefaultHeight = ivInvisible.getMeasuredHeight();
                }
                int distanceOdd = recyclerViewWidth / 2 - deleteFrameWidth;
                float factor = MAX_INCREASED_SIZE / distanceOdd;
                float sizeIncreased = (int) ((Math.abs(dX) - deleteFrameWidth) * factor);

                if(sizeIncreased > MAX_INCREASED_SIZE) {
                    sizeIncreased = MAX_INCREASED_SIZE;
                }
                tvDelete.setVisibility(View.INVISIBLE);
                ivInvisible.setVisibility(View.VISIBLE);

                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ivInvisible
                        .getLayoutParams();
                params.width = (int) (iconInvisibleDefaultWidth + sizeIncreased);
                params.height = (int) (iconInvisibleDefaultHeight + sizeIncreased);
                ivInvisible.setLayoutParams(params);
            }
        }
        else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setScrollX(0);
        BaseViewHolder itemViewHolder = (BaseViewHolder) viewHolder;
        View ivDelete = itemViewHolder.getView(R.id.iv_invisible);
        ivDelete.setVisibility(View.INVISIBLE);
        itemViewHolder.getView(R.id.tv_delete).setVisibility(View.VISIBLE);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ivDelete.getLayoutParams();
        params.width = iconInvisibleDefaultWidth;
        params.height = iconInvisibleDefaultHeight;
        ivDelete.setLayoutParams(params);
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.LEFT;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mListener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mListener.onItemSwiped(viewHolder.getAdapterPosition());
    }

    private int getDeleteFrameWidth(RecyclerView.ViewHolder viewHolder) {
        return ((BaseViewHolder)viewHolder).getView(R.id.frame_delete).getMeasuredWidth();
    }

    interface OnMoveAndSwipedListener {

        void onItemMove(int fromPosition, int toPosition);

        void onItemSwiped(int position);
    }

}
