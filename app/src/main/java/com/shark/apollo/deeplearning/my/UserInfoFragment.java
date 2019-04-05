package com.shark.apollo.deeplearning.my;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shark.apollo.deeplearning.R;
import com.shark.apollo.deeplearning.util.SpUtil;
import com.wildma.pictureselector.ImageUtils;
import com.wildma.pictureselector.PictureSelector;

import java.io.File;
import java.util.Objects;

public class UserInfoFragment extends Fragment {

    private static final String PICTURE_NAME = "/PictureSelector.temp.jpg";

    private static final String PICTURE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
            .concat(PICTURE_NAME);

    private Toolbar mToolbar;

    private ImageView mIvUser;

    private TextView mTvUserName;

    private TextView mTvUserMotto;

    private View mViewRoot;

    public UserInfoFragment() {
        // Required empty public constructor
    }

    public static UserInfoFragment newInstance() {
        return new UserInfoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewRoot = inflater.inflate(R.layout.fragment_user_info, container, false);
        initView(mViewRoot);
        return mViewRoot;
    }

    private void initView(View parent) {
        Log.d("UserInfo", "initView(View parent)");
        mToolbar = parent.findViewById(R.id.toolbar);
        mIvUser = parent.findViewById(R.id.iv_user);
        if(new File(PICTURE_PATH).exists()) {
            mIvUser.setImageBitmap(ImageUtils.getBitmap(PICTURE_PATH));
        }
        mTvUserName = parent.findViewById(R.id.tv_user_name);
        mTvUserName.setText(SpUtil.getString(SpUtil.SP_USER_NAME, getString(R.string.nickname_def)));
        mTvUserMotto = parent.findViewById(R.id.tv_user_motto);
        mTvUserMotto.setText(SpUtil.getString(SpUtil.SP_USER_MOTTO, getString(R.string.motto_def)));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initEvent(Objects.requireNonNull(getActivity()));
    }

    private void initEvent(FragmentActivity activity) {
        mToolbar.setNavigationOnClickListener(v ->
                activity.getSupportFragmentManager().popBackStack());
        mIvUser.setOnClickListener(v ->
                PictureSelector.create(UserInfoFragment.this,
                        1).selectPicture());
        mViewRoot.setOnClickListener(v -> {
            InputMethodManager methodManager = (InputMethodManager) getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            methodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        });

        mTvUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                SpUtil.putString(SpUtil.SP_USER_NAME, s.toString());
            }
        });

        mTvUserMotto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                SpUtil.putString(SpUtil.SP_USER_MOTTO, s.toString());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (data != null) {
                String picturePath = data.getStringExtra(PictureSelector.PICTURE_PATH);
                mIvUser.setImageBitmap(ImageUtils.getBitmap(picturePath));
            }
        }
    }

}
