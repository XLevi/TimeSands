package com.shark.apollo.deeplearning.timer;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.shark.apollo.deeplearning.App;
import com.shark.apollo.deeplearning.data.Note;
import com.shark.apollo.deeplearning.data.source.TimerDataSource;
import com.shark.apollo.deeplearning.data.source.TimerRepository;
import com.shark.apollo.deeplearning.util.SoundPoolUtil;
import com.shark.apollo.deeplearning.util.SpUtil;
import com.shark.apollo.deeplearning.util.TransformUtils;

import java.util.Date;
import java.util.List;

public class TimerPresenter implements TimerContract.Presenter {

    private static final int SECOND_TO_MSECOND = 1000;

    private long mLastBackPressedTime;
    private TimerContract.View mTimerView;
    private TimerRepository mTimerRepository;
    private TimerService mService;

    TimerPresenter(@NonNull TimerRepository timerRepository,
                   @NonNull TimerContract.View timerView, @NonNull TimerService service) {
        mTimerView = timerView;
        mTimerRepository = timerRepository;
        mService = service;
        mTimerView.setPresenter(this);
    }

    @Override
    public void checkPermission(Activity activity) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        if(ContextCompat.checkSelfPermission(activity.getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }

    @Override
    public void doOnBackPressed(Activity activity) {
        long time = System.currentTimeMillis();
        if(time - mLastBackPressedTime > 2000) {
            mLastBackPressedTime = time;
            mTimerView.showBackPressedToast();
        } else {
            activity.finish();
        }
    }

    @Override
    public void startTimer(TimerService.TimeCallback timeCallback) {
        mService.setTimeCallback(timeCallback);
        mService.startTiming();
    }

    @Override
    public void stopTimer() {
        mService.stopTiming();
    }

    @Override
    public void saveTimer(int duration) {
        App.TIMING_COUNT++;
        Note note = new Note();
        note.setTimeKeeping(duration * SECOND_TO_MSECOND);
        note.setMins(duration / 60);
        note.setDate(new Date());
        mTimerRepository.saveNote(note);
    }

    @Override
    public void setTimerDuration(int duration) {
        //SeekBar的起始数值是0，规定Duration最小为5分钟
        SpUtil.putInt(SpUtil.SP_SETTINGS_DURATION, duration);
        App.DURATION = duration;
    }

    @Override
    public void setTimerTick(boolean isOpen) {
        SpUtil.putBoolean(SpUtil.SP_SETTINGS_TICK, isOpen);
    }

    @Override
    public boolean getTickStatus() {
        return SpUtil.getBoolean(SpUtil.SP_SETTINGS_TICK, true);
    }

    @Override
    public void getTimingCount() {
        mTimerRepository.getTodayNotesData(new TimerDataSource.LoadNotesCallback() {
            @Override
            public void onNotesLoaded(List<Note> notes) {
                App.TIMING_COUNT = notes.size();
                Log.d("Crazy", App.TIMING_COUNT + "");
            }

            @Override
            public void onDataNotAvailable() {

            }
        });

    }

    @Override
    public void setProgressBarDiffusing(boolean isOpen) {
        SpUtil.putBoolean(SpUtil.SP_SETTINGS_DIFFUSION, isOpen);
    }

    @Override
    public boolean getDiffusionStatus() {
        return SpUtil.getBoolean(SpUtil.SP_SETTINGS_DIFFUSION, true);
    }

    @Override
    public void checkTimerRecord() {
        mTimerView.showTimerRecords();
    }

    @Override
    public void playTick() {
        SoundPoolUtil.play(SoundPoolUtil.TICK);
    }

    @Override
    public void playFinishSound() {
        switch (App.TIMING_COUNT + 1) {
            case 1:
                SoundPoolUtil.play(SoundPoolUtil.FIRST_BLOOD);
                break;
            case 2:
                SoundPoolUtil.play(SoundPoolUtil.DOUBLE_KILL);
                break;
            case 3:
                SoundPoolUtil.play(SoundPoolUtil.TRIPLE_KILL);
                break;
            case 4:
                SoundPoolUtil.play(SoundPoolUtil.QUADRA_KILL);
                break;
            case 5:
                SoundPoolUtil.play(SoundPoolUtil.PENTA_KILL);
                break;
            case 7:
                SoundPoolUtil.play(SoundPoolUtil.GODLIKE);
                break;
            case 8:
                SoundPoolUtil.play(SoundPoolUtil.LEGENDARY);
                break;
            default:
                SoundPoolUtil.play(SoundPoolUtil.LEGENDARY);
        }
    }

    @Override
    public String getFinishMessage() {
        switch (App.TIMING_COUNT + 1) {
            case 1:
                return "First Blood";
            case 2:
                return "Double Kill";
            case 3:
                return "Triple Kill";
            case 4:
                return "Quadra kill";
            case 5:
                return "Penta kill";
            case 7:
                return "God Like";
            case 8:
                return "Legendary";
            default:
                return "Victory";
        }
    }

    @Override
    public void regulateTimingNotification(boolean b) {
        mService.regulateForeground(b);
    }

    @Override
    public void start() {}
}
