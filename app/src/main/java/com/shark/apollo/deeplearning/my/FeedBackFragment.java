package com.shark.apollo.deeplearning.my;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.shark.apollo.deeplearning.R;

public class FeedBackFragment extends Fragment {

    private Button mBtnCommit;
    private EditText mEdtInput;

    private Toolbar mToolbar;
    private View mViewRoot;

    private View mViewToast;

    public FeedBackFragment() {
        // Required empty public constructor
    }
    public static FeedBackFragment newInstance() {
        return new FeedBackFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewRoot = container;
        View root = inflater.inflate(R.layout.fragment_feed_back, container, false);
        mViewToast = inflater.inflate(R.layout.toast_commit_success, container, false);
        initView(root);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initEvent(getActivity());
    }

    private void initView(View parent) {
        mBtnCommit = parent.findViewById(R.id.btn_commit);
        mEdtInput = parent.findViewById(R.id.edt_input);
        mToolbar = parent.findViewById(R.id.toolbar);
    }

    private void initEvent(FragmentActivity activity) {
        mBtnCommit.setOnClickListener(v -> {
            if(TextUtils.isEmpty(mEdtInput.getText().toString())) {
                Snackbar.make(mViewRoot, "Please input content !", Snackbar.LENGTH_SHORT).show();
            } else {
                mEdtInput.setText("");
//                Snackbar.make(mViewRoot, "Commit success", Snackbar.LENGTH_SHORT).show();
                Toast toast = new Toast(getContext());
                toast.setView(mViewToast);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });

        mToolbar.setNavigationOnClickListener(v ->
                activity.getSupportFragmentManager().popBackStack());
    }

}
