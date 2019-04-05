package com.shark.apollo.deeplearning.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtil {

    public static final String SP_SETTINGS = "settings";
    public static final String SP_SETTINGS_DURATION = "settings_duration";
    public static final String SP_SETTINGS_DIFFUSION = "settings_diffusion";
    public static final String SP_SETTINGS_TICK = "settings_tick";

    public static final String SP_USER_NAME = "user_name";
    public static final String SP_USER_MOTTO = "user_motto";

    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    public static void init(Context context) {
        preferences = context.getSharedPreferences(SP_SETTINGS, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }

    public static void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void putString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public static int getInt(String key, int def) {
        return preferences.getInt(key, def);
    }

    public static boolean getBoolean(String key, boolean def) {
        return preferences.getBoolean(key, def);
    }
    public static String getString(String key, String def) {
        return preferences.getString(key, def);
    }

}
