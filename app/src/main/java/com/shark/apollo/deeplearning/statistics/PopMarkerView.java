package com.shark.apollo.deeplearning.statistics;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.shark.apollo.deeplearning.R;

public class PopMarkerView extends MarkerView {

    private TextView mTvContent;
    private MPPointF mOffset;

    public PopMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        mTvContent = findViewById(R.id.tv_content);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        mTvContent.setText(String.valueOf((int)e.getY()));
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {

        if(mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(getWidth() / 2), -(getHeight() * 5 / 4));
        }

        return mOffset;
    }

}
