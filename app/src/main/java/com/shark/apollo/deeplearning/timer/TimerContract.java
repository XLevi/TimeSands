package com.shark.apollo.deeplearning.timer;

import android.app.Activity;

import com.shark.apollo.deeplearning.BasePresenter;
import com.shark.apollo.deeplearning.BaseView;

public interface TimerContract {
    interface View extends BaseView<Presenter> {

        void showTimerStatus(int time);

        void showStopAlert();

        void showBackPressedToast();

        void showTimerStopClue();

        void showTimerFinishedAlert();

        void showTimerSetting();

        void showTimerRecords();

        void showUserInfo();

        void showSettingDurationValue(int value);

        void showTimingNotification(boolean b);
    }

    interface Presenter extends BasePresenter {

        void checkPermission(Activity activity);

        void doOnBackPressed(Activity activity);

        void startTimer(TimerService.TimeCallback callback);

        void stopTimer();

        void saveTimer(int duration);

        void setTimerDuration(int duration);

        void setTimerTick(boolean isOpen);

        boolean getTickStatus();

        void getTimingCount();

        void setProgressBarDiffusing(boolean isOpen);

        boolean getDiffusionStatus();

        void checkTimerRecord();

        void playTick();

        void playFinishSound();

        String getFinishMessage();

        void regulateTimingNotification(boolean b);
    }
}
