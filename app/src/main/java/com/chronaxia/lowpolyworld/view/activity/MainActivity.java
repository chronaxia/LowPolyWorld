package com.chronaxia.lowpolyworld.view.activity;

import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chronaxia.lowpolyworld.R;
import com.chronaxia.lowpolyworld.app.LowPolyWorldApp;
import com.chronaxia.lowpolyworld.model.entity.Continent;
import com.chronaxia.lowpolyworld.presenter.MainPresenter;
import com.chronaxia.lowpolyworld.presenter.contract.MainContract;
import com.chronaxia.lowpolyworld.view.custom.ZoomImageView;
import com.jakewharton.rxbinding2.view.RxView;

import java.text.NumberFormat;
import java.util.Observable;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity implements MainContract.View{

    @BindView(R.id.iv_world)
    ZoomImageView ivWorld;
    @BindView(R.id.ll_continent_message)
    LinearLayout llContinentMessage;
    @BindView(R.id.tv_continent_phonetic)
    TextView tvContinentPhonetic;
    @BindView(R.id.tv_continent_name)
    TextView tvContinentName;
    @BindView(R.id.tv_continent_english)
    TextView tvContinentEnglish;
    @BindView(R.id.btn_test)
    Button btnTest;

    private String selectContinent = "";
    private MainContract.Presenter presenter;

    @Override
    protected int setContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        presenter = new MainPresenter(this, this);
    }

    @Override
    protected void initView() {
        /*Glide.with(MainActivity.this)
                .load(R.drawable.start_background)
                .into(ivWorld);*/
        RxView.touches(ivWorld)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(this.<MotionEvent>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MotionEvent>() {
                    @Override
                    public void accept(MotionEvent motionEvent) throws Exception {
                        NumberFormat num = NumberFormat.getPercentInstance();
                        num.setMaximumIntegerDigits(3);
                        num.setMaximumFractionDigits(1);
                        String strX = num.format(motionEvent.getX() / LowPolyWorldApp.getInstance().getX()).replace("%", "");
                        String strY = num.format(motionEvent.getY() / LowPolyWorldApp.getInstance().getY()).replace("%", "");
                        float x = Float.parseFloat(strX);
                        float y = Float.parseFloat(strY);
                        if ("".equals(selectContinent)) {
                            if (x >= 5 && x <= 22.2 && y >= 37.5 && y <= 75) {
                                Log.e("asd", x + " " + y + "非洲");
                                selectContinent = "非洲";
                                ivWorld.test(motionEvent, 1.0f);
                            } else if (x >= 23.5 && x <= 43 && y >= 18 && y <= 48.5) {
                                Log.e("asd", x + " " + y + "亚洲");
                                selectContinent = "亚洲";
                                ivWorld.test(motionEvent, 1.0f);
                            } else if (x >= 36.8 && x <= 47.8 && y >= 62 && y <= 79.1) {
                                Log.e("asd", x + " " + y + "大洋洲");
                                selectContinent = "大洋洲";
                                ivWorld.test(motionEvent, 1.0f);
                            } else if (x >= 63.2 && x <= 82.8 && y >= 15.2 && y <= 44.4) {
                                Log.e("asd", x + " " + y + "北美洲");
                                selectContinent = "北美洲";
                                ivWorld.test(motionEvent, 1.0f);
                            } else if (x >= 78.1 && x <= 89.8 && y >= 51.3 && y <= 84.7) {
                                Log.e("asd", x + " " + y + "南美洲");
                                selectContinent = "南美洲";
                                ivWorld.test(motionEvent, 1.0f);
                            } else if (x >= 7 && x <= 24.3 && y >= 21.3 && y <= 37.3) {
                                Log.e("asd", x + " " + y + "欧洲");
                                selectContinent = "欧洲";
                                ivWorld.test(motionEvent, 1.0f);
                            } else if (x >= 20.8 && x <= 78.3 && y >= 88.3 && y <= 100) {
                                Log.e("asd", x + " " + y + "南极洲");
                                selectContinent = "南极洲";
                                ivWorld.test(motionEvent, 1.0f);
                            }
                            if (!"".equals(selectContinent)) {
                                presenter.loadContinent(selectContinent);
                            } else {
                                llContinentMessage.setVisibility(View.INVISIBLE);
                            }
                        } else {
                            selectContinent = "";
                            llContinentMessage.setVisibility(View.INVISIBLE);
                            ivWorld.test(motionEvent, 0.0f);
                        }
                    }
                });
        RxView.touches(llContinentMessage)
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(this.<MotionEvent>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MotionEvent>() {
                    @Override
                    public void accept(MotionEvent motionEvent) throws Exception {
                    }
                });
        RxView.clicks(btnTest)
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (!"".equals(selectContinent)) {
                            Intent intent = new Intent(MainActivity.this, ScenicSpotsActivity.class);
                            intent.putExtra("continent", selectContinent);
                            startActivity(intent);
                        }
                    }
                });
    }

    @Override
    public void showContinent(Continent continent) {
        llContinentMessage.setVisibility(View.VISIBLE);
        tvContinentPhonetic.setText(continent.getPhonetic());
        tvContinentName.setText(continent.getName());
        tvContinentEnglish.setText(continent.getEnglish());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.doDestroy();
    }
}
