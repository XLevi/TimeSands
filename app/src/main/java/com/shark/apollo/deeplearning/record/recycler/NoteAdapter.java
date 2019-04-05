package com.shark.apollo.deeplearning.record.recycler;

import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shark.apollo.deeplearning.App;
import com.shark.apollo.deeplearning.R;
import com.shark.apollo.deeplearning.data.Note;
import com.shark.apollo.deeplearning.data.source.TimerRepository;
import com.shark.apollo.deeplearning.util.TimeUtils;
import com.shark.apollo.deeplearning.util.TransformUtils;

import java.util.Collections;
import java.util.List;

public class NoteAdapter extends BaseQuickAdapter<Note, BaseViewHolder> implements
        ItemTouchHelperCallback.OnMoveAndSwipedListener {

    private List<Note> mDatas;
    private TimerRepository mRepository;

    public NoteAdapter(int layoutResId, @Nullable List<Note> data) {
        super(layoutResId, data);
        mDatas = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, Note item) {
        if(helper == null) {
            return;
        }
        TextView textView = helper.getView(R.id.tv_mins);
        textView.getPaint().setFakeBoldText(true);
        helper.setText(R.id.tv_mins, TransformUtils.Minute2String(item.getMins()));
        helper.setText(R.id.tv_item_date, TransformUtils.date2String(item.getDate()));
    }

    public void setRepository(TimerRepository repository) {
        mRepository = repository;
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mDatas, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemSwiped(int position) {
        Note note = mDatas.remove(position);
        notifyItemRemoved(position);
        mRepository.removeNote(note);
        App.TIMING_COUNT--;
    }
}
