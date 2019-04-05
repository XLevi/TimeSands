package com.shark.apollo.deeplearning.my;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shark.apollo.deeplearning.R;
import com.shark.apollo.deeplearning.data.Note;
import com.shark.apollo.deeplearning.data.source.TimerDataSource;
import com.shark.apollo.deeplearning.data.source.TimerRepository;
import com.shark.apollo.deeplearning.data.source.local.NoteDao;
import com.shark.apollo.deeplearning.data.source.local.NotesLocalDataSource;
import com.shark.apollo.deeplearning.util.AppExecutors;
import com.shark.apollo.deeplearning.util.DownloadUtils;
import com.shark.apollo.deeplearning.util.TransformUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainFragment extends Fragment {

    private static final String UPDATE_URL = "http://192.168.0.106:8080/CrazyMaster/app.apk";

    interface CallBack {
        void startFragment(Fragment fragment);
    }

    private TextView mTvUpdate;
    private TextView mTvAbout;
    private TextView mTvFeedback;
    private TextView mTvLevel;
    private TextView mTvClock;
    private TextView mTvLevelProgress;

    private TimerRepository mRepository;

    private FloatingActionButton mFabInfo;

    private CallBack mCallBack;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepository = TimerRepository.getInstance(NotesLocalDataSource.getInstance(new AppExecutors(),
                NoteDao.getInstance()));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        initView(root);
        return root;
    }

    private void initView(View parent) {
//        Toolbar toolbar = parent.findViewById(R.id.toolbar);
        mTvUpdate = parent.findViewById(R.id.tv_update);
        mTvClock = parent.findViewById(R.id.tv_clock);
        mTvAbout = parent.findViewById(R.id.tv_about);
        mTvFeedback = parent.findViewById(R.id.tv_feedback);
        mTvLevel = parent.findViewById(R.id.tv_level);
        mTvLevelProgress = parent.findViewById(R.id.tv_level_progress);
        mFabInfo = parent.findViewById(R.id.fab);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initEvent(Objects.requireNonNull(getActivity()));
    }

    private void initEvent(Activity activity) {
//        View view = getLayoutInflater().inflate(R.layout.dialog_update,
//                (ViewGroup) activity.getWindow().getDecorView(), false);
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setView(R.layout.dialog_update)
                .create();
        dialog.setCanceledOnTouchOutside(false);
        mTvUpdate.setOnClickListener(v -> {
            dialog.show();
            CountDownTimer countDownTimer = new CountDownTimer(3000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    dialog.dismiss();
                    Snackbar.make(v, "Server sneak off", Snackbar.LENGTH_LONG).show();
                }
            };
            countDownTimer.start();
            Flowable.fromCallable(() -> DownloadUtils.isConnectServer(UPDATE_URL))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aBoolean -> {
                        Log.d("update", "isConnectServer = " + aBoolean);
                        if(aBoolean) {
                            dialog.dismiss();
                            countDownTimer.cancel();
                            DownloadUtils.downloadAPK(getContext(), UPDATE_URL);
                        } else {
                            if(dialog.isShowing()) {
                                countDownTimer.onFinish();
                                countDownTimer.cancel();
                            }
                        }
                    });
        });
        mTvAbout.setOnClickListener(v -> mCallBack.startFragment(AboutFragment.newInstance()));

        mTvFeedback.setOnClickListener(v -> mCallBack.startFragment(FeedBackFragment.newInstance()));

        mTvClock.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ClockActivity.class);
            startActivity(intent);
        });

        mFabInfo.setOnClickListener(v -> mCallBack.startFragment(UserInfoFragment.newInstance()));

        mRepository.getNotes(new TimerDataSource.LoadNotesCallback() {
            @Override
            public void onNotesLoaded(List<Note> notes) {
                int count = 0;
                for(Note note : notes) {
                    count += note.getMins();
                }
                mTvLevelProgress.setText(TransformUtils.minutesToPercent(count, 1200));
            }

            @Override
            public void onDataNotAvailable() {

            }
        });

    }

    public void setCallBack(CallBack callBack) {
        mCallBack = callBack;
    }
}
