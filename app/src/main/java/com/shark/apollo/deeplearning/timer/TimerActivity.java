package com.shark.apollo.deeplearning.timer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;

import com.shark.apollo.deeplearning.R;
import com.shark.apollo.deeplearning.data.source.TimerRepository;
import com.shark.apollo.deeplearning.data.source.local.NoteDao;
import com.shark.apollo.deeplearning.data.source.local.NotesLocalDataSource;
import com.shark.apollo.deeplearning.util.ActivityUtils;
import com.shark.apollo.deeplearning.util.AppExecutors;

public class TimerActivity extends AppCompatActivity {

    private TimerService mTimerService;
    private TimerFragment mTimerFragment;
    private boolean mShouldUnbind;
    private TimerContract.Presenter mPresenter;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TimerService.LocalBinder binder = (TimerService.LocalBinder) service;
            mTimerService = binder.getService();
            mPresenter = new TimerPresenter(TimerRepository.getInstance(
                    NotesLocalDataSource.getInstance(new AppExecutors(), NoteDao.getInstance())),
                    mTimerFragment, mTimerService);
            mPresenter.getTimingCount();
            mPresenter.checkPermission(TimerActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mTimerService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        bindService(new Intent(this, TimerService.class), connection, Context.BIND_AUTO_CREATE);
        mShouldUnbind = true;
        mTimerFragment = (TimerFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if(mTimerFragment == null) {
            mTimerFragment = TimerFragment.newInstance();
        }
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mTimerFragment,
                R.id.contentFrame);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mTimerService != null) {
            mTimerService.regulateForeground(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mShouldUnbind) {
            unbindService(connection);
        }
    }

    @Override
    public void onBackPressed() {
        mPresenter.doOnBackPressed(this);
    }

}
