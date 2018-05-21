package com.chronaxia.lowpolyworld.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chronaxia.lowpolyworld.R;
import com.chronaxia.lowpolyworld.model.entity.ScenicSpot;
import com.chronaxia.lowpolyworld.presenter.IntroducePresenter;
import com.chronaxia.lowpolyworld.presenter.contract.IntroduceContract;
import com.chronaxia.lowpolyworld.util.BitmapUtils;
import com.chronaxia.lowpolyworld.util.LowPoly;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class IntroduceActivity extends BaseActivity implements IntroduceContract.View{

    @BindView(R.id.iv_introduce_back)
    ImageView ivIntroduceBack;
    @BindView(R.id.iv_introduce_picture)
    ImageView ivIntroducePicture;
    @BindView(R.id.iv_introduce_back_space)
    ImageView ivIntroduceBackSpace;
    @BindView(R.id.iv_introduce_forward)
    ImageView ivIntroduceForward;
    @BindView(R.id.iv_introduce_magic)
    ImageView ivIntroduceMagic;
    @BindView(R.id.tv_introduce_upper)
    TextView tvIntroduceUpper;
    @BindView(R.id.tv_introduce_middle)
    TextView tvIntroduceMiddle;
    @BindView(R.id.tv_introduce_lower)
    TextView tvIntroduceLower;

    private ScenicSpot scenicSpot;
    private List<ScenicSpot> scenicSpotList;
    private ScenicSpot selectScenicSpot;
    private IntroduceContract.Presenter presenter;

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
        presenter = new IntroducePresenter(this, this);
        scenicSpotList = new ArrayList<>();
        int id = getResources().getIdentifier(scenicSpot.getPicture(), "xml", getPackageName());
        presenter.loadScenicSpots(getResources().getXml(id));
    }

    @Override
    protected void initView() {
        if (scenicSpot != null) {
            RxView.clicks(ivIntroduceBack)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .compose(this.bindToLifecycle())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            onBackPressed();
                        }
                    });
            RxView.clicks(ivIntroduceForward)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .compose(this.bindToLifecycle())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            ivIntroduceForward.setVisibility(View.VISIBLE);
                            ivIntroduceBackSpace.setVisibility(View.VISIBLE);
                            int next = scenicSpotList.indexOf(selectScenicSpot) + 1;
                            if (next < scenicSpotList.size()) {
                                selectScenicSpot = scenicSpotList.get(next);
                                Glide.with(IntroduceActivity.this)
                                        .load(IntroduceActivity.this.getResources().getIdentifier(selectScenicSpot.getPicture(),
                                                "mipmap", IntroduceActivity.this.getPackageName()))
                                        .into(ivIntroducePicture);
                                setIntroduce(selectScenicSpot.getPhonetic(), selectScenicSpot.getName(), selectScenicSpot.getEnglish());
                            }
                            if (next == scenicSpotList.size() - 1) {
                                ivIntroduceForward.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
            RxView.clicks(ivIntroduceBackSpace)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .compose(this.bindToLifecycle())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            ivIntroduceForward.setVisibility(View.VISIBLE);
                            ivIntroduceBackSpace.setVisibility(View.VISIBLE);
                            int next = scenicSpotList.indexOf(selectScenicSpot) - 1;
                            if (next >= 0) {
                                selectScenicSpot = scenicSpotList.get(next);
                                Glide.with(IntroduceActivity.this)
                                        .load(IntroduceActivity.this.getResources().getIdentifier(selectScenicSpot.getPicture(),
                                                "mipmap", IntroduceActivity.this.getPackageName()))
                                        .into(ivIntroducePicture);
                                setIntroduce(selectScenicSpot.getPhonetic(), selectScenicSpot.getName(), selectScenicSpot.getEnglish());
                            }
                            if (next == 0) {
                                ivIntroduceBackSpace.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
            RxView.clicks(ivIntroduceMagic)
                    .throttleFirst(2, TimeUnit.SECONDS)
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .compose(this.bindToLifecycle())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            lowpolyPicture();
                        }
                    });
        }
    }

    public void setIntroduce(String upper, String middle, String lower) {
        if (upper != null) {
            tvIntroduceUpper.setText(upper);
        }
        if (middle != null) {
            tvIntroduceMiddle.setText(middle);
        }
        if (lower != null) {
            tvIntroduceLower.setText(lower);
        }
    }

    public void lowpolyPicture() {
        LowPoly.createLowPoly(IntroduceActivity.this,
                BitmapUtils.loadBitmapRes(IntroduceActivity.this, IntroduceActivity.this.getResources().getIdentifier(selectScenicSpot.getPicture(),
                        "mipmap", IntroduceActivity.this.getPackageName())),
                6,
                0,
                new LowPoly.Callback() {
                    @Override
                    public void lowpoly() {
                        ivIntroducePicture.setImageBitmap(LowPoly.bmpRendered);
                        System.gc();
                    }
                });
    }

    @Override
    public void updateScenicSpots(List<ScenicSpot> scenicSpotList) {
        this.scenicSpotList = scenicSpotList;
        if (scenicSpotList.size() != 0) {
            this.selectScenicSpot = scenicSpotList.get(0);
            setIntroduce(selectScenicSpot.getPhonetic(), selectScenicSpot.getName(), selectScenicSpot.getEnglish());
            Glide.with(this)
                    .load(getResources().getIdentifier(selectScenicSpot.getPicture(), "mipmap", getPackageName()))
                    .into(ivIntroducePicture);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.doDestroy();
    }
}
