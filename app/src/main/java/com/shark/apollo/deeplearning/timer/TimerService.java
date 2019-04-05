package com.shark.apollo.deeplearning.timer;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.shark.apollo.deeplearning.App;
import com.shark.apollo.deeplearning.R;
import com.shark.apollo.deeplearning.util.TransformUtils;

public class TimerService extends Service {

    private static final String TAG = "Deep";
    private static final int NOTIFICATION_ID = 1;
    public interface TimeCallback {
        void onGetTime(int time);

        void onTimingFinish();
    }

    TimeCallback timeCallback;
    NotificationManager mNotificationManager;
    NotificationCompat.Builder notificationBuilder;
    private static final String CHANNEL_ID = "channel_0";
    private static final String CHANNEL_NAME = "Message";
    private LocalBinder mBinder = new LocalBinder();
    private boolean isStartForeground;
    private CountDownTimer mTimer;

    public void setTimeCallback(TimeCallback timeCallback) {
        this.timeCallback = timeCallback;
    }

    public TimerService() {}

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Service", "TimerService - onCreate()");
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_av_timer)
                .setContentTitle("Time")
                .setContentText(" ");
        Intent intent = new Intent(this, TimerActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent,PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_LOW);
            mNotificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public TimerService getService() {
            return TimerService.this;
        }
    }

    public void startTiming() {
        long timeDuration = App.DURATION * 60 * 1000;
        mTimer = new CountDownTimer(timeDuration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int t = (int) (millisUntilFinished / 1000);
                timeCallback.onGetTime(t);
                if(isStartForeground) {
                    updateForeground(t);
                }
            }

            @Override
            public void onFinish() {
                //to finish progress
                timeCallback.onGetTime(0);
                timeCallback.onTimingFinish();
                if(isStartForeground) {
                    updateForeground(-1);
                }
            }
        };
        mTimer.start();
    }

    public void stopTiming() {
        mTimer.cancel();
    }

    public void updateForeground(int time) {
        if(time == -1) {
            notificationBuilder.setContentText(getString(R.string.victory));
            mNotificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
            return;
        }
        notificationBuilder.setContentText(TransformUtils.time2String(time));
        mNotificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    public void regulateForeground(boolean b) {
        if(b) {
            if(!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
                Toast.makeText(this, "Blocked", Toast.LENGTH_LONG).show();
                return;
            }
            isStartForeground = b;
            startForeground(NOTIFICATION_ID, notificationBuilder.build());
        } else {
            isStartForeground = b;
            stopForeground(true);
        }
    }
}
