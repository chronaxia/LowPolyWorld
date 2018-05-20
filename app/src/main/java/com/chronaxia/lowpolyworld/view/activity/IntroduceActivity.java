package com.chronaxia.lowpolyworld.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chronaxia.lowpolyworld.R;
import com.chronaxia.lowpolyworld.model.entity.ScenicSpot;

import butterknife.BindView;

public class IntroduceActivity extends BaseActivity {

    @BindView(R.id.iv_introduce_picture)
    ImageView ivIntroducePicture;

    private ScenicSpot scenicSpot;

    public static Intent newInstance(Context context, ScenicSpot scenicSpot) {
        Intent intent = new Intent(context, IntroduceActivity.class);
        intent.putExtra("scenic_spot", scenicSpot);
        return intent;
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_introduce;
    }

    @Override
    protected void initData() {
        scenicSpot = (ScenicSpot) getIntent().getSerializableExtra("scenic_spot");
    }

    @Override
    protected void initView() {
        if (scenicSpot != null) {
            Glide.with(this)
                    .load(getResources().getIdentifier(scenicSpot.getPicture(), "drawable", getPackageName()))
                    .into(ivIntroducePicture);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
