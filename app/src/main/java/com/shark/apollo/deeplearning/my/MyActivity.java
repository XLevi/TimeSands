package com.shark.apollo.deeplearning.my;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.shark.apollo.deeplearning.R;
import com.shark.apollo.deeplearning.util.ActivityUtils;

public class MyActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        MainFragment mainFragment = MainFragment.newInstance();
        mainFragment.setCallBack(fragment -> ActivityUtils.replaceFragment (
                getSupportFragmentManager(), fragment, R.id.my_container));
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mainFragment,
                R.id.my_container);
    }
}
