package com.chronaxia.lowpolyworld.model.retrofit;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by 一非 on 2018/5/28.
 */

public interface AudioNet {
    @Streaming
    @GET
    Observable<ResponseBody> textToAudio(@Url String url);

    @Headers("Content-Type: audio/pcm;rate=16000")
    @POST
    Observable<ResponseBody> audioToText(@Url String url, @Body byte[] fileData);
}
