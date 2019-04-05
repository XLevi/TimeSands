package com.shark.apollo.deeplearning.util;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

public class DownloadUtils {

    private static final String TAG = "update";

    private static final String APK_MIME_TYPE = "application/vnd.android.package-archive";

    private static final String APK_PATH = "/app.apk";

    public static void downloadAPK(Context context, String url) {
        if(TextUtils.isEmpty(url)) {
            return;
        }

        final DownloadManager downloadManager = (DownloadManager) context.getSystemService(
                Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.allowScanningByMediaScanner();
        request.setVisibleInDownloadsUi(true);
        request.setTitle("Crazy Master");
        request.setDescription("Downloading Apk.");
        request.setNotificationVisibility(DownloadManager.Request
                .VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setMimeType(APK_MIME_TYPE);
        request.setDestinationInExternalPublicDir("/update",
                APK_PATH);
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                .concat("/update").concat(APK_PATH));
        if(file.exists()) {
            Log.d(TAG, "文件已存在，删除ing");
            file.delete();
        }
        downloadManager.enqueue(request);
    }

    public static boolean isConnectServer(String urlStr) {
//        InetAddress inetAddress;
//        boolean isOnline;
//        try {
//            inetAddress = InetAddress.getByName(urlStr);
//            isOnline = inetAddress.isReachable(1000);
//            return isOnline;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return false;
        HttpURLConnection connection;
        try {
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(1000);
            return connection.getResponseCode() == 200;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}