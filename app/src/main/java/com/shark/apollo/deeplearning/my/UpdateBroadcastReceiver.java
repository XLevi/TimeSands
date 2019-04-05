package com.shark.apollo.deeplearning.my;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

public class UpdateBroadcastReceiver extends BroadcastReceiver {

    private static final String APK_PATH = "/update/app.apk";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent();
        intent1.setAction("android.intent.action.VIEW");
        intent1.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + APK_PATH)),
                "application/vnd.android.package-archive");
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
    }
}
