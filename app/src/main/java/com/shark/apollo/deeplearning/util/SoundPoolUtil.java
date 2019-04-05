package com.shark.apollo.deeplearning.util;

import android.content.Context;
import android.media.SoundPool;

import com.shark.apollo.deeplearning.R;

public class SoundPoolUtil {

    public static final int TICK = 1;
    public static final int FIRST_BLOOD = 2;
    public static final int DOUBLE_KILL = 3;
    public static final int TRIPLE_KILL = 4;
    public static final int QUADRA_KILL = 5;
    public static final int PENTA_KILL = 6;
    public static final int GODLIKE = 7;
    public static final int LEGENDARY = 8;

    private static SoundPool soundPool;

    public static void init(Context context) {
        soundPool = new SoundPool.Builder()
                .setMaxStreams(8)
                .build();
        soundPool.load(context, R.raw.tick, 1);
        soundPool.load(context, R.raw.first_blood, 1);
        soundPool.load(context, R.raw.double_kill, 1);
        soundPool.load(context, R.raw.triple_kill, 1);
        soundPool.load(context, R.raw.quadra_kill, 1);
        soundPool.load(context, R.raw.penta_kill, 1);
        soundPool.load(context, R.raw.godlike, 1);
        soundPool.load(context, R.raw.legendary, 1);
    }

    public static void play(int choice) {
        soundPool.play(choice, 1, 1, 0, 0, 1);
    }

}
