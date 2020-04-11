package com.prajwal.covid_prajwal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.prajwal.covid_prajwal.network.ApiClient;
import com.prajwal.covid_prajwal.network.ApiInterface;
import com.prajwal.covid_prajwal.pojo_model.StatesDaily;
import com.prajwal.covid_prajwal.pojo_model.StatesDaily_List;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    ApiInterface apiInterface;
    TextView state, date, confirmed, recovered, deceased;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        state = findViewById(R.id.StateName);
        date = findViewById(R.id.DateName);
        confirmed = findViewById(R.id.Confirmed);
        recovered = findViewById(R.id.Recovered);
        deceased = findViewById(R.id.Deceased);

        apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<StatesDaily_List> statesDaily_listCall = apiInterface.STATES_DAILY_LIST_CALL();
        statesDaily_listCall.enqueue(new Callback<StatesDaily_List>() {
            @Override
            public void onResponse(Call<StatesDaily_List> call, Response<StatesDaily_List> response) {
                if(response.isSuccessful())
                {
                    StatesDaily st = new StatesDaily();
                    st.setDate("14-Mar-20");
                    st.setStatus("Recovered");
//                    Log.e("PRAJWAL","EQUALL: "+response.body().getStatesDaily().contains(st));
                    List<StatesDaily> listOfState = response.body().getStatesDaily();
                    int indexOfObj = listOfState.indexOf(st);
                    if(indexOfObj >= 0){
                       StatesDaily actualObj = listOfState.get(indexOfObj);
                        Log.e("PRAJWAL","actualObj: "+actualObj);
                        state.setText(actualObj.getMh());
                    }
/*
                    for (int i = 0; i < response.body().getStatesDaily().size(); i++) {
                        String date_string = response.body().getStatesDaily().get(i).getDate();
//                        String confirm_string = response.body().getStatesDaily().get(i).;
                        date.setText(date_string);
                        confirmed.setText(response.body().getStatesDaily().get(i).getMh() + "\n Confirm");
                        recovered.setText(response.body().getStatesDaily().get(i+1).getMh() + "\n Recover");
                        deceased.setText(response.body().getStatesDaily().get(i+2).getMh() + "\n Death");
                        state.setText("Maharashtra");
                    }
*/


                }
            }

            @Override
            public void onFailure(Call<StatesDaily_List> call, Throwable t) {

            }
        });
    }
}
