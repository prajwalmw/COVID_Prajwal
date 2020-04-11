package com.prajwal.covid_prajwal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.prajwal.covid_prajwal.network.ApiClient;
import com.prajwal.covid_prajwal.network.ApiInterface;
import com.prajwal.covid_prajwal.pojo_model.StatesDaily;
import com.prajwal.covid_prajwal.pojo_model.StatesDaily_List;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    ApiInterface apiInterface;
    TextView state, date, confirmed, recovered, deceased;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;
        state = findViewById(R.id.StateName);
        date = findViewById(R.id.DateName);
        confirmed = findViewById(R.id.Confirmed);
        recovered = findViewById(R.id.Recovered);
        deceased = findViewById(R.id.Deceased);
        recyclerView = findViewById(R.id.recyclerview);

        recyclerAdapter = new RecyclerAdapter(context);
        recyclerView.setAdapter(recyclerAdapter);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy");
        final String today_date = simpleDateFormat.format(calendar.getTime());

        Calendar cal_yes = Calendar.getInstance();
        cal_yes.add(Calendar.DATE, -1);
        final String yesterday_date = simpleDateFormat.format(cal_yes.getTime());

        apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<StatesDaily_List> statesDaily_listCall = apiInterface.STATES_DAILY_LIST_CALL();
        statesDaily_listCall.enqueue(new Callback<StatesDaily_List>() {
            @Override
            public void onResponse(Call<StatesDaily_List> call, Response<StatesDaily_List> response) {
                if(response.isSuccessful())
                {
                    StatesDaily st = new StatesDaily();
                    st.setDate(today_date);
                    st.setDate(yesterday_date);
                    st.setStatus("Confirmed");


//                    Log.e("PRAJWAL","EQUALL: "+response.body().getStatesDaily().contains(st));
                    List<StatesDaily> listOfState = response.body().getStatesDaily();
                    int indexOfObj = listOfState.indexOf(st);
                    if(indexOfObj >= 0){
                       StatesDaily actualObj = listOfState.get(indexOfObj);
                        Log.e("PRAJWAL","actualObj: "+actualObj);
                        confirmed.setText(actualObj.getMh() + "\n Confirm");
                        date.setText(actualObj.getDate());
                        state.setText("Maharashtra");
                    }

                    StatesDaily st_1 = new StatesDaily();
                    st_1.setDate(today_date);
                    st_1.setDate(yesterday_date);
                    st_1.setStatus("Recovered");


//                    Log.e("PRAJWAL","EQUALL: "+response.body().getStatesDaily().contains(st));
                    List<StatesDaily> listOfState_1 = response.body().getStatesDaily();
                    int indexOfObj_1 = listOfState_1.indexOf(st_1);
                    if(indexOfObj_1 >= 0){
                        StatesDaily actualObj_1 = listOfState_1.get(indexOfObj_1);
                        recovered.setText(actualObj_1.getMh() + "\n Recovered");
                    }

                    StatesDaily st_2 = new StatesDaily();
                    st_2.setDate(today_date);
                    st_2.setDate(yesterday_date);
                    st_2.setStatus("Deceased");


//                    Log.e("PRAJWAL","EQUALL: "+response.body().getStatesDaily().contains(st));
                    List<StatesDaily> listOfState_2 = response.body().getStatesDaily();
                    int indexOfObj_2 = listOfState_2.indexOf(st_2);
                    if(indexOfObj_2 >= 0){
                        StatesDaily actualObj_2 = listOfState_1.get(indexOfObj_2);
                        deceased.setText(actualObj_2.getMh() + "\n Death");
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
