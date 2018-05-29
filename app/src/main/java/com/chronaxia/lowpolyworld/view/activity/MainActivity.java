package com.chronaxia.lowpolyworld.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.chronaxia.lowpolyworld.R;
import com.chronaxia.lowpolyworld.app.LowPolyWorldApp;
import com.chronaxia.lowpolyworld.model.entity.Continent;
import com.chronaxia.lowpolyworld.model.retrofit.AudioNet;
import com.chronaxia.lowpolyworld.presenter.MainPresenter;
import com.chronaxia.lowpolyworld.presenter.contract.MainContract;
import com.chronaxia.lowpolyworld.util.AudioUtil;
import com.chronaxia.lowpolyworld.view.custom.ZoomImageView;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.jakewharton.rxbinding2.view.RxView;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Eases.EaseType;
import com.nightonke.boommenu.Types.BoomType;
import com.nightonke.boommenu.Types.ButtonType;
import com.nightonke.boommenu.Types.ClickEffectType;
import com.nightonke.boommenu.Types.DimType;
import com.nightonke.boommenu.Types.OrderType;
import com.nightonke.boommenu.Types.PlaceType;
import com.nightonke.boommenu.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    @BindView(R.id.btn_record_test)
    Button btnRecordTest;
    @BindView(R.id.boom)
    BoomMenuButton boomMenuButton;

    private String selectContinent = "";
    private MainContract.Presenter presenter;
    private static Drawable[] buttonDrawables = new Drawable[2];
    private static int[][] buttonColors = new int[2][2];

    static {
        for (int i = 0; i < 2; i++) {
            buttonDrawables[i] = LowPolyWorldApp.getInstance().getApplicationContext().getResources().getDrawable(R.drawable.ic_back);
            buttonColors[i][0] = 60;
            buttonColors[i][1] = 150;
        }
    }

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
                                ivWorld.myDoScale(motionEvent, 1.0f);
                            } else if (x >= 23.5 && x <= 43 && y >= 18 && y <= 48.5) {
                                Log.e("asd", x + " " + y + "亚洲");
                                selectContinent = "亚洲";
                                ivWorld.myDoScale(motionEvent, 1.0f);
                            } else if (x >= 36.8 && x <= 47.8 && y >= 62 && y <= 79.1) {
                                Log.e("asd", x + " " + y + "大洋洲");
                                selectContinent = "大洋洲";
                                ivWorld.myDoScale(motionEvent, 1.0f);
                            } else if (x >= 63.2 && x <= 82.8 && y >= 15.2 && y <= 44.4) {
                                Log.e("asd", x + " " + y + "北美洲");
                                selectContinent = "北美洲";
                                ivWorld.myDoScale(motionEvent, 1.0f);
                            } else if (x >= 78.1 && x <= 89.8 && y >= 51.3 && y <= 84.7) {
                                Log.e("asd", x + " " + y + "南美洲");
                                selectContinent = "南美洲";
                                ivWorld.myDoScale(motionEvent, 1.0f);
                            } else if (x >= 7 && x <= 24.3 && y >= 21.3 && y <= 37.3) {
                                Log.e("asd", x + " " + y + "欧洲");
                                selectContinent = "欧洲";
                                ivWorld.myDoScale(motionEvent, 1.0f);
                            } else if (x >= 20.8 && x <= 78.3 && y >= 88.3 && y <= 100) {
                                Log.e("asd", x + " " + y + "南极洲");
                                selectContinent = "南极洲";
                                ivWorld.myDoScale(motionEvent, 1.0f);
                            }
                            if (!"".equals(selectContinent)) {
                                presenter.loadContinent(selectContinent);
                            } else {
                                llContinentMessage.setVisibility(View.INVISIBLE);
                            }
                        } else {
                            selectContinent = "";
                            llContinentMessage.setVisibility(View.INVISIBLE);
                            ivWorld.myDoScale(motionEvent, 0.0f);
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
        RxView.clicks(tvContinentName)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        LowPolyWorldApp.getInstance().getAudioUtil().textToAudio(tvContinentName.getText().toString());
                    }
                });
        RxView.clicks(tvContinentEnglish)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        LowPolyWorldApp.getInstance().getAudioUtil().textToAudio(tvContinentName.getText().toString());
                    }
                });
        RxView.clicks(tvContinentEnglish)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        LowPolyWorldApp.getInstance().getAudioUtil().textToAudio(tvContinentEnglish.getText().toString());
                    }
                });
        RxView.clicks(btnRecordTest)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (btnRecordTest.getText().toString().equals("录音")) {
                            LowPolyWorldApp.getInstance().getAudioUtil().startRecord();
                            btnRecordTest.setText("取消");
                        } else if (btnRecordTest.getText().toString().equals("取消")) {
                            LowPolyWorldApp.getInstance().getAudioUtil().audioToText(new AudioUtil.Callback() {
                                @Override
                                public void callback() {
                                    Toasty.success(MainActivity.this,
                                            LowPolyWorldApp.getInstance().getAudioUtil().getAudioTextList().get(0),
                                            0, true).show();
                                }
                            });
                            btnRecordTest.setText("录音");
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
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        new BoomMenuButton.Builder()
                .addSubButton(this, R.drawable.ic_distinguish, new int[]{131,189,232}, "")
                .addSubButton(this, R.drawable.ic_paint, new int[]{94,188,143}, "")
                .frames(80)
                .duration(800)
                .delay(100)
                .showOrder(OrderType.RANDOM)
                .hideOrder(OrderType.RANDOM)
                .button(ButtonType.CIRCLE)
                .boom(BoomType.PARABOLA_2)
                .place(PlaceType.CIRCLE_2_1)
                .showMoveEase(EaseType.EaseOutBack)
                .hideMoveEase(EaseType.EaseOutCirc)
                .showScaleEase(EaseType.EaseOutBack)
                .hideScaleType(EaseType.EaseOutCirc)
                .rotateDegree(720)
                .showRotateEase(EaseType.EaseOutBack)
                .hideRotateType(EaseType.Linear)
                .autoDismiss(true)
                .cancelable(true)
                .dim(DimType.DIM_6)
                .clickEffect(ClickEffectType.RIPPLE)
                .boomButtonShadow(Util.getInstance().dp2px(2), Util.getInstance().dp2px(2))
                .subButtonsShadow(Util.getInstance().dp2px(2), Util.getInstance().dp2px(2))
                .subButtonTextColor(Color.WHITE)
                .onBoomButtonBlick(null)
                .animator(null)
                .onSubButtonClick(new BoomMenuButton.OnSubButtonClickListener() {
                    @Override
                    public void onClick(int buttonIndex) {
                        if (buttonIndex == 0) {
                            startActivity(new Intent(MainActivity.this, DistinguishActivity.class));
                        } else if (buttonIndex == 1) {
                            startActivity(new Intent(MainActivity.this, PaintActivity.class));
                        }
                    }
                })
                .shareStyle(0, 0, 0)
                .init(boomMenuButton);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.doDestroy();
    }
}
