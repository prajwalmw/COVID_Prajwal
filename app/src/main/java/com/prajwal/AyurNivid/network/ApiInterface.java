package com.prajwal.AyurNivid.network;

import com.prajwal.AyurNivid.pojo_model.StatesDaily_List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("states_daily.json")
    Call<StatesDaily_List> STATES_DAILY_LIST_CALL();

}
