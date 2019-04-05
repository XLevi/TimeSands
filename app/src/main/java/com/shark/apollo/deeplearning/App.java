package com.shark.apollo.deeplearning;

import android.app.Application;

import com.shark.apollo.deeplearning.data.MyObjectBox;
import com.shark.apollo.deeplearning.util.SoundPoolUtil;
import com.shark.apollo.deeplearning.util.SpUtil;

import io.objectbox.BoxStore;

public class App extends Application {

    public static int DURATION;
    public static int TIMING_COUNT;

    private static App application;
    private BoxStore boxStore;
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        boxStore = MyObjectBox.builder().androidContext(App.this).build();
        SoundPoolUtil.init(getApplicationContext());
        SpUtil.init(getApplicationContext());
    }

    public BoxStore getBoxStore() {
        return boxStore;
    }

    public static App getApplication() {
        return application;
    }
}
