package com.shark.apollo.deeplearning.my;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shark.apollo.deeplearning.R;

public class AboutFragment extends Fragment {


    private Toolbar mToolbar;

    public AboutFragment() {
        // Required empty public constructor
    }
    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_about, container, false);
        mToolbar = root.findViewById(R.id.toolbar);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initEvent(getActivity());
    }

    private void initEvent(FragmentActivity activity) {
        mToolbar.setNavigationOnClickListener(v ->
                activity.getSupportFragmentManager().popBackStack());
    }

}
