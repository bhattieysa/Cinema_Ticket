package com.example.cinematicket;

import com.example.cinematicket.Model.FCMModel;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

public interface FcmRetrofitDAO {
    Retrofit RETROFIT=new Retrofit.Builder()
            .baseUrl("https://fcm.googleapis.com/fcm/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @POST("send")
    Call<Void> sendNotification(@HeaderMap HashMap<String,String> headers, @Body FCMModel fcmModel);
}
