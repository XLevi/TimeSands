package com.shark.apollo.deeplearning.my;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.shark.apollo.deeplearning.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ClockActivity extends AppCompatActivity {

    private TextView mTvSecond;

    private SimpleDateFormat mSimpleDateFormat;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            mTvSecond.setText(mSimpleDateFormat.format(new Date()));
            mHandler.sendEmptyMessageDelayed(0, 1000);
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
        mSimpleDateFormat = new SimpleDateFormat("ss", Locale.getDefault());
        mTvSecond = findViewById(R.id.tv_second);
        mHandler.sendEmptyMessage(0);
    }
}
