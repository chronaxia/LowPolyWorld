package com.chronaxia.lowpolyworld.model.retrofit;

import com.chronaxia.lowpolyworld.model.entity.TokenResponse;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by 一非 on 2018/5/28.
 */

public interface TokenNet {
    @FormUrlEncoded
    @POST("oauth/2.0/token")
    Observable<TokenResponse> updateToken(@Field("grant_type") String grant_type, @Field("client_id") String client_id, @Field("client_secret") String client_secret);
}
