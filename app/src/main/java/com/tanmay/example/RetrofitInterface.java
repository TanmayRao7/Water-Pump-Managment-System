package com.tanmay.example;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @POST("/login")
    Call<LoginResult> executeLogin(@Body HashMap<String,String> map);
    @POST("/signup")
    Call<Void> executeSignup(@Body HashMap<String,String> map);
    @GET("/water-pump-data")
    Call<List<PumpData>> getPumpData();

    @POST("/water-pump-post")
    Call<PumpData> addData(@Body PumpData data);

    @GET("/water-pump-last")
    Call<PumpData> getData();

}
