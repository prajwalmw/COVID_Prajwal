package com.prajwal.covid_prajwal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.util.Function;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.prajwal.covid_prajwal.network.ApiClient;
import com.prajwal.covid_prajwal.network.ApiInterface;
import com.prajwal.covid_prajwal.pojo_model.StatesDaily;
import com.prajwal.covid_prajwal.pojo_model.StatesDaily_List;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
    ArrayList<Card_DataModel> array_list, recovered_array, deceased_array;
    String[] array_statenames;

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
        array_list = new ArrayList<Card_DataModel>();

       array_statenames = new String[]
               {"Andaman & Nicobar Island", "Andhra Pradesh", "Arunachal Pradesh",
                       "Assam", "Bihar", "Chandigarh", "Chattisgarh", "Diu & Daman",
                       "Delhi", "Dadra & Nagar Haveli", "Goa", "Gujarat", "Himachal Pradesh", "Haryana",
               "Jharkhand", "Jammu & Kashmir", "Karnataka", "Kerala", "Ladakh" ,
                       "Lakshadweep", "Maharashtra", "Meghalaya", "Manipur",
                       "Madhya Pradesh", "Mizoram", "Nagaland", "Orissa", "Punjab", "Puducherry",
               "Rajasthan", "Sikkim", "Telangana", "Tamil Nadu", "Tripura",
                       "Uttar Pradesh", "Uttarakhand", "West Bengal"};



        recyclerView.setLayoutManager(
                new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));


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

                    StatesDaily statesDaily = new StatesDaily();
                    statesDaily.setDate(yesterday_date);
                    statesDaily.setStatus("Confirmed");

                    StatesDaily statesDaily_1 = new StatesDaily();
                    statesDaily_1.setDate(yesterday_date);
                    statesDaily_1.setStatus("Recovered");

                    StatesDaily statesDaily_2 = new StatesDaily();
                    statesDaily_2.setDate(yesterday_date);
                    statesDaily_2.setStatus("Deceased");

                    int[] index_array = new int[]
                            {response.body().getStatesDaily().indexOf(statesDaily),
                                    response.body().getStatesDaily().indexOf(statesDaily_1),
                                    response.body().getStatesDaily().indexOf(statesDaily_2)
                            };


                    if(!Arrays.asList(index_array).contains(-1))
                    {
                        StatesDaily statesDaily_new = new StatesDaily();
                        for (int i = 0; i < statesDaily_new.All_States_Length(); i++)
                        {
                            array_list.add(
                                    new Card_DataModel(array_statenames[i],
                                            response.body().getStatesDaily().get(index_array[0]).getDate(),
                                            response.body().getStatesDaily().get(index_array[0]).All_States(i),
                                            response.body().getStatesDaily().get(index_array[1]).All_States(i),
                                            response.body().getStatesDaily().get(index_array[2]).All_States(i)));
                        }

                        recyclerAdapter = new RecyclerAdapter(context, array_list);
                        recyclerView.setAdapter(recyclerAdapter);

                    }
                }
            }


            @Override
            public void onFailure(Call<StatesDaily_List> call, Throwable t) {

            }
        });


    }
}
