package com.shark.apollo.deeplearning.record;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shark.apollo.deeplearning.R;
import com.shark.apollo.deeplearning.data.Note;
import com.shark.apollo.deeplearning.data.source.TimerDataSource;
import com.shark.apollo.deeplearning.data.source.TimerRepository;
import com.shark.apollo.deeplearning.data.source.local.NoteDao;
import com.shark.apollo.deeplearning.data.source.local.NotesLocalDataSource;
import com.shark.apollo.deeplearning.record.recycler.ItemTouchHelperCallback;
import com.shark.apollo.deeplearning.record.recycler.NoteAdapter;
import com.shark.apollo.deeplearning.statistics.StatisticsActivity;
import com.shark.apollo.deeplearning.util.AppExecutors;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView mRvContent;
    private Toolbar mToolbar;
    private TimerRepository mRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        findViewById(R.id.tv_statistics).setOnClickListener(v -> {
            Intent intent = new Intent(HistoryActivity.this, StatisticsActivity.class);
            startActivity(intent);
        });
        mRvContent = findViewById(R.id.recycler_content);
        mRvContent.setHasFixedSize(true);
        mRvContent.setLayoutManager(new LinearLayoutManager(this));
        mRepository = TimerRepository.getInstance(NotesLocalDataSource.getInstance(new AppExecutors(),
                NoteDao.getInstance()));
        mRepository.getNotes(new TimerDataSource.LoadNotesCallback() {
            @Override
            public void onNotesLoaded(List<Note> notes) {
                initAdapter(notes);
            }

            @Override
            public void onDataNotAvailable() {
            }
        });

        initEvent();
    }

    private void initEvent() {
        mToolbar.setNavigationOnClickListener(v -> finishAfterTransition());
    }

    private void initAdapter(List<Note> notes) {
        NoteAdapter adapter = new NoteAdapter(R.layout.note_item, notes);
        adapter.setRepository(mRepository);
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        adapter.isFirstOnly(false);
//        adapter.setOnItemClickListener((adapter1, view, position) -> Toast.makeText(
//                HistoryActivity.this, (position + 1) + "was Clicked", Toast.LENGTH_SHORT).show());
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelperCallback(adapter));
        helper.attachToRecyclerView(mRvContent);
        mRvContent.setAdapter(adapter);
    }
}
