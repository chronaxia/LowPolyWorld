package com.chronaxia.lowpolyworld.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.chronaxia.lowpolyworld.R;

public class MainActivity extends BaseActivity {

    @Override
    protected int setContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        Log.e("asd", "main");
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
