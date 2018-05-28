package com.chronaxia.lowpolyworld.util;

import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;

import com.chronaxia.lowpolyworld.app.LowPolyWorldApp;
import com.chronaxia.lowpolyworld.model.entity.Token;
import com.chronaxia.lowpolyworld.model.entity.TokenResponse;
import com.chronaxia.lowpolyworld.model.retrofit.AudioNet;
import com.chronaxia.lowpolyworld.model.retrofit.TokenNet;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

/**
 * Created by 一非 on 2018/5/28.
 */

public class AudioUtil {

    private Retrofit retrofit;
    private String path;
    private String fileName = "audio.mp3";
    private MediaPlayer mediaPlayer;
    private String token;
    private String mac;

    public AudioUtil() {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())//请求的结果转为实体类
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())  //适配RxJava2.0, RxJava1.x则为RxJavaCallAdapterFactory.create()
                .baseUrl("http://tsn.baidu.com/")
                .build();
        path = Environment.getExternalStorageDirectory().getPath() + File.separator + "LowPolyWorld" + File.separator;
        mediaPlayer = new MediaPlayer();
        mac = getLocalMac();
        init();
    }

    private void init() {
        Token token = LowPolyWorldApp.getInstance().getDaoSession().getTokenDao().load(1L);
        SimpleDateFormat simFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        try {
            if (token == null || ValueUnit.daysBetween(simFormat.parse(token.getOpdate()), new Date()) > 20) {
                updateToken();
            } else {
                this.token = token.getToken();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void updateToken() {
        final Token token = new Token();
        token.setId(1L);
        token.setAppId("11312449");
        token.setApiKey("GWS9tLk7leEOaSWhjA6uGEig");
        token.setSecretKey("ykqfa4EHV3GIOHBzV7gSopDLdyi2YwtF");
        SimpleDateFormat simFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        token.setOpdate(simFormat.format(new Date()));
        Observable.just(token)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Function<Token, ObservableSource<TokenResponse>>() {
                    @Override
                    public ObservableSource<TokenResponse> apply(Token token) throws Exception {
                        Retrofit retrofit = new Retrofit.Builder()
                                .addConverterFactory(GsonConverterFactory.create())//请求的结果转为实体类
                                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())  //适配RxJava2.0, RxJava1.x则为RxJavaCallAdapterFactory.create()
                                .baseUrl("http://openapi.baidu.com/")
                                .build();
                        TokenNet net = retrofit.create(TokenNet.class);
                        return net.updateToken("client_credentials", token.getApiKey(), token.getSecretKey());
                    }
                })
                .doOnNext(new Consumer<TokenResponse>() {
                    @Override
                    public void accept(TokenResponse tokenResponse) throws Exception {
                        token.setToken(tokenResponse.getAccess_token());
                        LowPolyWorldApp.getInstance().getDaoSession().getTokenDao().update(token);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<TokenResponse>() {
                    @Override
                    public void accept(TokenResponse tokenResponse) throws Exception {
                        AudioUtil.this.token = token.getToken();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("asd", throwable.getMessage());
                    }
                });
    }

    public void textToAudio(String text) {
        try {
            String url = "http://tsn.baidu.com/text2audio?lan=zh&ctp=1&cuid=" + mac + "&tok=" +
                    token + "&tex=" + gbkToUtf8(text) + "&vol=9&per=0&spd=5&pit=5";
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void textToAudioByRetrofit(String text) {
        String url = "http://tsn.baidu.com/text2audio?lan=zh&ctp=1&cuid=" + mac + "&tok=" +
                token + "&tex=" + gbkToUtf8(text) + "&vol=9&per=0&spd=5&pit=5";
        mediaPlayer.stop();
        mediaPlayer.reset();
        Observable.just(url)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Function<String, ObservableSource<ResponseBody>>() {
                    @Override
                    public ObservableSource<ResponseBody> apply(String string) throws Exception {
                        AudioNet net = retrofit.create(AudioNet.class);
                        return net.textToAudio(string);
                    }
                })
                .map(new Function<ResponseBody, InputStream>() {
                    @Override
                    public InputStream apply(ResponseBody responseBody) throws Exception {
                        return responseBody.byteStream();
                    }
                })
                .map(new Function<InputStream, File>() {
                    @Override
                    public File apply(InputStream inputStream) throws Exception {
                        File file = new File(path + fileName);
                        if (file.exists()) {
                            file.delete();
                        }
                        FileOutputStream fos = new FileOutputStream(file);
                        byte[] b = new byte[1024];
                        int len;
                        while ((len = inputStream.read(b)) != -1) {
                            fos.write(b,0,len);
                        }
                        inputStream.close();
                        fos.close();
                        return file;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File file) throws Exception {
                        FileInputStream fis = new FileInputStream(file);
                        mediaPlayer.setDataSource(fis.getFD());
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toasty.error(LowPolyWorldApp.getInstance().getApplicationContext(),
                                "音频加载错误", 0, true).show();
                    }
                });
    }

    private String gbkToUtf8(String gbk) {
        try {
            gbk = new String(gbk.getBytes("utf-8"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return gbk;
    }

    public static String  getLocalMac() {
        String mac=null;
        String str = "";
        try
        {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (; null != str;)
            {
                str = input.readLine();
                if (str != null)
                {
                    mac = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return mac;
    }

}
