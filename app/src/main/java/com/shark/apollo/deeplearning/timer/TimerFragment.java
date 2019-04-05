package com.shark.apollo.deeplearning.timer;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.shark.apollo.deeplearning.App;
import com.shark.apollo.deeplearning.R;
import com.shark.apollo.deeplearning.data.Note;
import com.shark.apollo.deeplearning.my.MyActivity;
import com.shark.apollo.deeplearning.record.HistoryActivity;
import com.shark.apollo.deeplearning.top_snackbar.BaseTransientBottomBar;
import com.shark.apollo.deeplearning.top_snackbar.TopSnackBar;
import com.shark.apollo.deeplearning.util.SpUtil;
import com.shark.apollo.deeplearning.util.TransformUtils;

import java.util.Locale;

public class TimerFragment extends Fragment implements TimerContract.View {

    private static final String TAG = "DL";
    private static final int MIN_DURATION = 5;
    private static final int MINUTE_TO_SECONDS = 60;
    private static final int DEFAULT_DURATION = 25;

    private int mDuration;
    private boolean mIsTiming;
    private boolean mIsOpenTick = true;

    private TimerContract.Presenter mPresenter;

    private SeekBar mSBDuration;

    private OProgressBar mPgBar;

    private View mViewPop;
    private ViewGroup mVgParent;
    private TextView mTvSettingDurationValue;

    private ImageView mIvSetting;

    private ImageView mIvRecord;

    private ImageView mIvMy;

    private PopupWindow mPopSetting;

    private AlertDialog mDialogStop;

    private AlertDialog mDialogFinish;

    private Switch mSwTick;

    private Switch mSwDiffusion;

    public TimerFragment() {}

    public static TimerFragment newInstance() {
        return new TimerFragment();
    }

    @Override
    public void setPresenter(TimerContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mVgParent = container;
        mViewPop = inflater.inflate(R.layout.pop_setting, container, false);
        View root = inflater.inflate(R.layout.frag_timer, container, false);

        mPgBar = root.findViewById(R.id.progressBar);
        mIvRecord = root.findViewById(R.id.iv_record);
        mIvSetting = root.findViewById(R.id.iv_setting);
        mIvMy = root.findViewById(R.id.iv_user_info);
        mSBDuration = mViewPop.findViewById(R.id.sbar_duration);
        mTvSettingDurationValue = mViewPop.findViewById(R.id.tv_duration_value);
        mSwDiffusion = mViewPop.findViewById(R.id.switch1);
        mSwTick = mViewPop.findViewById(R.id.switch2);

        initEvent();
        return root;
    }

    private void initEvent() {
        mPgBar.openDiffusing();
        mPgBar.setOnClickListener(v -> {
            if(mIsTiming) {
                showStopAlert();
            } else {
                mIsTiming = true;
                mPresenter.startTimer(new TimerService.TimeCallback() {
                    @Override
                    public void onGetTime(int time) {
                        showTimerStatus(time);
                    }

                    @Override
                    public void onTimingFinish() {
                        showTimerFinishedAlert();
                    }
                });
                mIvRecord.setVisibility(View.INVISIBLE);
                mIvSetting.setVisibility(View.INVISIBLE);
                mIvMy.setVisibility(View.INVISIBLE);
            }
        });

        mIvSetting.setOnClickListener(v -> showTimerSetting());

        mIvRecord.setOnClickListener(v -> mPresenter.checkTimerRecord());

        mIvMy.setOnClickListener(v -> showUserInfo());

        mSBDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                showSettingDurationValue(progress + MIN_DURATION);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int p = seekBar.getProgress() + MIN_DURATION;
                mDuration = p * MINUTE_TO_SECONDS;
                mPresenter.setTimerDuration(p);
                mPgBar.setMax(mDuration);
            }
        });

        mSwDiffusion.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mPresenter.setProgressBarDiffusing(isChecked);
            if (isChecked) {
                mPgBar.openDiffusing();
            } else {
                mPgBar.closeDiffusing();
            }
        });

        mSwTick.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mIsOpenTick = isChecked;
            mPresenter.setTimerTick(isChecked);
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(mDialogStop == null) {
            mDialogStop = new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.cancel_timer)
                    .setPositiveButton("Yes", (dialog, which) -> {
                        mIsTiming = false;
                        mPresenter.stopTimer();
                        mPgBar.stopProgressAnim();
                        mPgBar.clearProgress();
                        mPgBar.endDiffusionAnim();
                        mIvRecord.setVisibility(View.VISIBLE);
                        mIvSetting.setVisibility(View.VISIBLE);
                        mIvMy.setVisibility(View.VISIBLE);
                        showTimerStopClue();
                    })
                    .setNegativeButton("No", (dialog, which) -> {

                    })
                    .create();
        }

        if(mDialogFinish == null) {
            mDialogFinish = new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.congratulations)
                    .setPositiveButton("ok", (dialog, which) -> {
                        mPresenter.saveTimer(mDuration);
                        mDialogFinish.dismiss();
                        mPgBar.clearProgress();
                        mIvRecord.setVisibility(View.VISIBLE);
                        mIvSetting.setVisibility(View.VISIBLE);
                        mIvMy.setVisibility(View.VISIBLE);
                        })
                    .create();
        }
        mDialogFinish.setCanceledOnTouchOutside(false);

        App.DURATION = SpUtil.getInt(SpUtil.SP_SETTINGS_DURATION, DEFAULT_DURATION);
        mDuration = App.DURATION * MINUTE_TO_SECONDS;
        mPgBar.setMax(mDuration);
    }

    @Override
    public void onPause() {
        if(mIsTiming) {
            showTimingNotification(true);
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        if(mIsTiming) {
            showTimingNotification(false);
        }
        super.onResume();
    }

    @Override
    public void showTimerStatus(int time) {
        mPgBar.setProgress(mDuration - time);
        mPgBar.setText(TransformUtils.time2String(time));
        if(mIsOpenTick) mPresenter.playTick();
    }

    @Override
    public void showStopAlert() {
        mDialogStop.show();
    }

    @Override
    public void showBackPressedToast() {
        Toast.makeText(getContext(), R.string.press_toast, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showTimerStopClue() {
        TopSnackBar.make(mVgParent, getString(R.string.celled),
                BaseTransientBottomBar.LENGTH_SHORT).show();
    }

    @Override
    public void showTimerFinishedAlert() {
        mPresenter.playFinishSound();
        mIsTiming = false;
        mPgBar.endDiffusionAnim();
        mDialogFinish.setMessage(mPresenter.getFinishMessage());
        mDialogFinish.show();
    }

    @Override
    public void showTimerSetting() {
        if(mPopSetting == null) {
            mPopSetting = new PopupWindow(mViewPop, mVgParent.getMeasuredWidth() - 100,
                    mVgParent.getHeight() / 2, true);
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                mPopSetting.setBackgroundDrawable(new ColorDrawable());
                mPopSetting.setOutsideTouchable(true);
            }
            mSBDuration.setProgress(App.DURATION - MIN_DURATION);
            mTvSettingDurationValue.setText(String.format(Locale.ENGLISH, "%d%s", App.DURATION,
                    getString(R.string.mins)));
            mSwDiffusion.setChecked(mPresenter.getDiffusionStatus());
            mSwTick.setChecked(mPresenter.getTickStatus());
        }
        mPopSetting.showAsDropDown(mIvSetting, 0, 0);
    }

    @Override
    public void showTimerRecords() {
        Activity parentActivity = getActivity();
        Intent intent = new Intent(parentActivity, HistoryActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(parentActivity)
                .toBundle());
    }

    @Override
    public void showUserInfo() {
        Activity activity = getActivity();
        Intent intent = new Intent(activity, MyActivity.class);
        startActivity(intent);
    }

    @Override
    public void showSettingDurationValue(int value) {
        mTvSettingDurationValue.setText(String.format(Locale.ENGLISH, "%d%s", value,
                getString(R.string.mins)));
    }

    @Override
    public void showTimingNotification(boolean b) {
        mPresenter.regulateTimingNotification(b);
    }

}
