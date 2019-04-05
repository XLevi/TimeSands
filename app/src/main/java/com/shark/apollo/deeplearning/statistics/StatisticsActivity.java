package com.shark.apollo.deeplearning.statistics;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.shark.apollo.deeplearning.R;
import com.shark.apollo.deeplearning.data.Note;
import com.shark.apollo.deeplearning.data.source.TimerDataSource;
import com.shark.apollo.deeplearning.data.source.TimerRepository;
import com.shark.apollo.deeplearning.data.source.local.NoteDao;
import com.shark.apollo.deeplearning.data.source.local.NotesLocalDataSource;
import com.shark.apollo.deeplearning.util.AppExecutors;
import com.shark.apollo.deeplearning.util.ChartUtils;
import com.shark.apollo.deeplearning.util.TransformUtils;

import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    private static final int CHART_DAILY = 0;
    private static final int CHART_WEEKLY = 1;
    private static final int CHART_MONTHLY = 2;

    private LineChart mChart;

    private TextView mTvDaily;

    private TextView mTvWeekly;

    private TextView mTvMonthly;

    private TextView mTvFinishCount;

    private TextView mTvStudyTimeCount;

    private TextView mTvHistoryTimeCount;

    private int mChartStatus;

    private int mColorAccent;
    private int mColorWhite;

    private TimerRepository mRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(v -> finish());
        mChart = findViewById(R.id.chart);
        ChartUtils.initChart(mChart);
        mRepository = TimerRepository.getInstance(NotesLocalDataSource.getInstance(new AppExecutors(),
                NoteDao.getInstance()));
        mRepository.getDailyNotesData(datas -> ChartUtils.notifyDataSetChanged(mChart,
                getDailyDatas(datas), ChartUtils.DAILY));

        PopMarkerView markerView = new PopMarkerView(this, R.layout.marker_layout);
        mChart.setMarker(markerView);

//        ChartUtils.notifyDataSetChanged(mChart, getDailyDatas(), ChartUtils.WEEKLY);
        mChartStatus = CHART_DAILY;
        mColorAccent = getResources().getColor(R.color.colorCyan2);
        mColorWhite = getResources().getColor(R.color.colorWhite);
        initViews();
        mRepository.getTodayNotesData(new TimerDataSource.LoadNotesCallback() {
            @Override
            public void onNotesLoaded(List<Note> notes) {
                if(notes.size() == 0) {
                    mTvFinishCount.setText(String.valueOf(0));
                    mTvStudyTimeCount.setText(String.valueOf(0));
                } else {
                    mTvFinishCount.setText(String.valueOf(notes.size()));
                    int count = 0;
                    for(Note note:notes) {
                        count += note.getMins();
                    }
                    mTvStudyTimeCount.setText(TransformUtils.minutes2String(count));
                }

            }

            @Override
            public void onDataNotAvailable() {

            }
        });

        mRepository.getNotes(new TimerDataSource.LoadNotesCallback() {
            @Override
            public void onNotesLoaded(List<Note> notes) {
                int count = 0;
                for(Note note : notes) {
                    count = count + note.getMins();
                }
                mTvHistoryTimeCount.setText(TransformUtils.minutesToHourString(count));
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    private void initViews() {

        mTvFinishCount = findViewById(R.id.finish_count);
        mTvStudyTimeCount = findViewById(R.id.tv_study_time);
        mTvHistoryTimeCount = findViewById(R.id.tv_history);
        mTvDaily = findViewById(R.id.tv_daily);
        mTvWeekly = findViewById(R.id.tv_weekly);
        mTvMonthly = findViewById(R.id.tv_monthly);

        mTvDaily.setOnClickListener(v -> {
            if(mChartStatus != CHART_DAILY) {
                mRepository.getDailyNotesData(datas -> ChartUtils.notifyDataSetChanged(mChart,
                        getDailyDatas(datas), ChartUtils.DAILY));
            }
            mChartStatus = CHART_DAILY;
            setStatusColor(mChartStatus);
        });

        mTvWeekly.setOnClickListener(v -> {
            if(mChartStatus != CHART_WEEKLY) {
                mRepository.getWeeklyNotesData(datas -> ChartUtils.notifyDataSetChanged(mChart,
                        getWeeklyDatas(datas), ChartUtils.WEEKLY));
            }
            mChartStatus = CHART_WEEKLY;
            setStatusColor(mChartStatus);
        });

        mTvMonthly.setOnClickListener(v -> {
            if(mChartStatus != CHART_MONTHLY) {
                mRepository.getMonthlyNotesData(datas -> ChartUtils.notifyDataSetChanged(mChart,
                        getMonthlyDatas(datas), ChartUtils.MONTHLY));
            }
            mChartStatus = CHART_MONTHLY;
            setStatusColor(mChartStatus);
        });
    }

    private void setStatusColor(int s) {
        switch (s) {
            case CHART_DAILY:
                mTvDaily.setTextColor(mColorAccent);
                mTvWeekly.setTextColor(mColorWhite);
                mTvMonthly.setTextColor(mColorWhite);
                break;
            case CHART_WEEKLY:
                mTvDaily.setTextColor(mColorWhite);
                mTvWeekly.setTextColor(mColorAccent);
                mTvMonthly.setTextColor(mColorWhite);
                break;
            case CHART_MONTHLY:
                mTvDaily.setTextColor(mColorWhite);
                mTvWeekly.setTextColor(mColorWhite);
                mTvMonthly.setTextColor(mColorAccent);
                break;
        }
    }

    private List<Entry> getDailyDatas(int[] datas) {
        List<Entry> values = new ArrayList<>();
        values.add(new Entry(0, datas[0]));
        values.add(new Entry(1, datas[1]));
        values.add(new Entry(2, datas[2]));
        values.add(new Entry(3, datas[3]));
        values.add(new Entry(4, datas[4]));
        values.add(new Entry(5, datas[5]));
        values.add(new Entry(6, datas[6]));
        return values;
    }

    private List<Entry> getWeeklyDatas(int[] datas) {
        List<Entry> values = new ArrayList<>();
        values.add(new Entry(0, datas[0]));
        values.add(new Entry(1, datas[1]));
        values.add(new Entry(2, datas[2]));
        values.add(new Entry(3, datas[3]));
        values.add(new Entry(4, datas[4]));
        values.add(new Entry(5, datas[5]));
        values.add(new Entry(6, datas[6]));
        return values;
    }

    private List<Entry> getMonthlyDatas(int[] datas) {
        List<Entry> values = new ArrayList<>();
        values.add(new Entry(0, datas[0]));
        values.add(new Entry(1, datas[1]));
        values.add(new Entry(2, datas[2]));
        values.add(new Entry(3, datas[3]));
        values.add(new Entry(4, datas[4]));
        values.add(new Entry(5, datas[5]));
        values.add(new Entry(6, datas[6]));
        return values;
    }

}
