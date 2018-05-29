package com.chronaxia.lowpolyworld.util;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Vibrator;
import android.util.Log;

import com.chronaxia.lowpolyworld.app.LowPolyWorldApp;
import com.chronaxia.lowpolyworld.model.entity.ATTResponse;
import com.chronaxia.lowpolyworld.model.entity.Token;
import com.chronaxia.lowpolyworld.model.entity.TokenResponse;
import com.chronaxia.lowpolyworld.model.retrofit.AudioNet;
import com.chronaxia.lowpolyworld.model.retrofit.TokenNet;
import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    private String path;
    private String fileName = "audio.mp3";
    private String recordFileName = "record.pcm";
    private MediaPlayer mediaPlayer;
    private String token;
    private String mac;
    private boolean isRecording;
    public List<String> audioTextList;

    public AudioUtil() {
        path = Environment.getExternalStorageDirectory().getPath() + File.separator + "LowPolyWorld" + File.separator;
        audioTextList = new ArrayList<>();
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
                        Retrofit retrofit = new Retrofit.Builder()
                                .addConverterFactory(GsonConverterFactory.create())//请求的结果转为实体类
                                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())  //适配RxJava2.0, RxJava1.x则为RxJavaCallAdapterFactory.create()
                                .baseUrl("http://tsn.baidu.com/")
                                .build();
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

    public void startRecord(){
        Observable.just(0)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        int frequency = 16000;
                        int channelConfiguration = 1;
                        int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
                        File file = new File(path + recordFileName);
                        if (file.exists()) {
                            file.delete();
                        }
                        OutputStream os = new FileOutputStream(file);
                        BufferedOutputStream bos = new BufferedOutputStream(os);
                        DataOutputStream dos = new DataOutputStream(bos);
                        int bufferSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding) * 4;
                        AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, frequency, channelConfiguration, audioEncoding, bufferSize);
                        byte[] buffer = new byte[bufferSize / 2];
                        Vibrator vibrator = (Vibrator)LowPolyWorldApp.getInstance().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(100);
                        audioRecord.startRecording();
                        isRecording = true;
                        while (isRecording) {
                            int bufferReadResult = audioRecord.read(buffer, 0, bufferSize / 2);
                            for (int i = 0; i < bufferReadResult; i++) {
                                dos.write(buffer[i]);
                            }
                        }
                        audioRecord.stop();
                        audioRecord.release();
                        dos.close();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("asd", throwable.getMessage());
                    }
                });

    }

    public void setRecording(boolean recording) {
        isRecording = recording;
    }

    public String connectForResult() throws Exception{
        String url = "http://vop.baidu.com/server_api?cuid=" + getLocalMac() + "&dev_pid=1537&token=" + token;
        byte[] content = getFileContent(path+recordFileName);
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setConnectTimeout(3000);
        conn.setRequestProperty("Content-Type", "audio/pcm; rate=16000");
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.getOutputStream().write(content);
        conn.getOutputStream().close();
        String result = ConnUtil.getResponseString(conn);
        return result;
    }

    public void audioToText(final Callback callback) {
        Observable.just(0)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        isRecording = false;
                    }
                })
                .delay(250, TimeUnit.MILLISECONDS)
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return connectForResult();
                    }
                })
                .map(new Function<String, List<String>>() {
                    @Override
                    public List<String> apply(String s) throws Exception {
                        return new Gson().fromJson(s, ATTResponse.class).getResult();
                    }
                })
                .doOnNext(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> stringList) throws Exception {
                        audioTextList = stringList;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> stringList) throws Exception {
                        callback.callback();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("asd", throwable.getMessage());
                    }
                });
    }

    public void audioToTextByRxJava(Callback callback) {
        String url = "http://vop.baidu.com/server_api?cuid=" + getLocalMac() + "&dev_pid=1537&token=" + token;
        Observable.just(url)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        isRecording = false;
                    }
                })
                .delay(250, TimeUnit.MILLISECONDS)
                .flatMap(new Function<String, ObservableSource<ResponseBody>>() {
                    @Override
                    public ObservableSource<ResponseBody> apply(String string) throws Exception {
                        Retrofit retrofit = new Retrofit.Builder()
                                .addConverterFactory(GsonConverterFactory.create())//请求的结果转为实体类
                                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())  //适配RxJava2.0, RxJava1.x则为RxJavaCallAdapterFactory.create()
                                .baseUrl("http://vop.baidu.com/")
                                .build();
                        AudioNet net = retrofit.create(AudioNet.class);
                        byte[] bytes = File2Bytes();
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i=0;i<bytes.length;i++) {
                            stringBuilder.append(" "+ bytes[i]);
                        }
                        Log.e("asd", stringBuilder.toString());
                        return net.audioToText(string, bytes);
                    }
                })
                .doOnNext(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        Log.e("asd", responseBody.string().toString());
                    }
                })
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("asd", throwable.getMessage());
                    }
                });
    }

    public interface Callback {
        void callback();
    }

    public void playRecord() {
        Observable.just(0)
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        isRecording = false;
                    }
                })
                .delay(250, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        byte[] music = getFileContent(path + recordFileName);
                        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                                16000, 1,
                                AudioFormat.ENCODING_PCM_16BIT,
                                music.length,
                                AudioTrack.MODE_STREAM);
                        audioTrack.play();
                        audioTrack.write(music, 0, music.length);
                        audioTrack.stop();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("asd", throwable.getMessage());
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

    public List<String> getAudioTextList() {
        return audioTextList;
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

    public byte[] File2Bytes() {
        File file = new File(path + recordFileName);
        int byte_size = 1024;
        byte[] b = new byte[byte_size];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(
                    byte_size);
            for (int length; (length = fileInputStream.read(b)) != -1;) {
                outputStream.write(b, 0, length);
            }
            fileInputStream.close();
            outputStream.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] getFileContent(String filename) throws IOException {
        File file = new File(filename);
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
            return ConnUtil.getInputStreamContent(is);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
