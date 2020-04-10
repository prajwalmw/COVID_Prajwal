package com.prajwal.covid_prajwal.pojo_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StatesDaily_List {


    @SerializedName("states_daily")
    @Expose
    private List<StatesDaily> statesDaily = null;

    public List<StatesDaily> getStatesDaily() {
        return statesDaily;
    }

    public void setStatesDaily(List<StatesDaily> statesDaily) {
        this.statesDaily = statesDaily;
    }
}
