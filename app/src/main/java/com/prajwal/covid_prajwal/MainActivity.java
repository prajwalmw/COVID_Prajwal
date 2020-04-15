package com.prajwal.covid_prajwal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.util.Function;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.prajwal.covid_prajwal.network.ApiClient;
import com.prajwal.covid_prajwal.network.ApiInterface;
import com.prajwal.covid_prajwal.network.NetworkConnection;
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
    String toolbar_date = "";
    Menu menu;
    CustomProgressDialog customProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Home");
        context = MainActivity.this;
        state = findViewById(R.id.StateName);
        date = findViewById(R.id.DateName);
        confirmed = findViewById(R.id.Confirmed);
        recovered = findViewById(R.id.Recovered);
        deceased = findViewById(R.id.Deceased);
        recyclerView = findViewById(R.id.recyclerview);
        array_list = new ArrayList<Card_DataModel>();

        customProgressDialog = new CustomProgressDialog(context);


        if (!NetworkConnection.isOnline(context) || !NetworkConnection.isConnecting(context)) {
            customProgressDialog.dismiss();
            AlertDialog.Builder alerBuilder = new AlertDialog.Builder(context)
                    .setTitle("No Internet Connection")
                    .setMessage("Please check your internet connection and try again later.")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            moveTaskToBack(true);
                            System.exit(1);   //non-zero states that the JVM has to be killed.
                        }
                    });
            AlertDialog alertDialog = alerBuilder.create();
            alertDialog.show();
            Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setTypeface(Typeface.DEFAULT, Typeface.BOLD);


        } else {
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    customProgressDialog.show();
                }
            });


            array_statenames = new String[]
                    {"Andaman & Nicobar Island", "Andhra Pradesh", "Arunachal Pradesh",
                            "Assam", "Bihar", "Chandigarh", "Chattisgarh", "Diu & Daman",
                            "Delhi", "Dadra & Nagar Haveli", "Goa", "Gujarat", "Himachal Pradesh", "Haryana",
                            "Jharkhand", "Jammu & Kashmir", "Karnataka", "Kerala", "Ladakh",
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


                    if (response.isSuccessful()) {

                        StatesDaily statesDaily = new StatesDaily();
                        statesDaily.setDate(today_date);
                        statesDaily.setStatus("Confirmed");

                        StatesDaily statesDaily_1 = new StatesDaily();
                        statesDaily_1.setDate(today_date);
                        statesDaily_1.setStatus("Recovered");

                        StatesDaily statesDaily_2 = new StatesDaily();
                        statesDaily_2.setDate(today_date);
                        statesDaily_2.setStatus("Deceased");

                    /*int[] index_array = new int[]
                            {response.body().getStatesDaily().indexOf(statesDaily),
                                    response.body().getStatesDaily().indexOf(statesDaily_1),
                                    response.body().getStatesDaily().indexOf(statesDaily_2)
                            };*/

                        int[] index_array;

                        if (response.body().getStatesDaily().indexOf(statesDaily) >= 0 ||
                                response.body().getStatesDaily().indexOf(statesDaily_1) >= 0 ||
                                response.body().getStatesDaily().indexOf(statesDaily_2) >= 0)
                        {
                            index_array = new int[]
                                    {response.body().getStatesDaily().indexOf(statesDaily),
                                            response.body().getStatesDaily().indexOf(statesDaily_1),
                                            response.body().getStatesDaily().indexOf(statesDaily_2)
                                    };
                            toolbar_date = response.body().getStatesDaily().get(index_array[0]).getDate();
                            updateMenuTitles(toolbar_date);
                        }
                        else {
                            StatesDaily statesDaily_a = new StatesDaily();
                            statesDaily_a.setDate(yesterday_date);
                            statesDaily_a.setStatus("Confirmed");

                            StatesDaily statesDaily_b = new StatesDaily();
                            statesDaily_b.setDate(yesterday_date);
                            statesDaily_b.setStatus("Recovered");

                            StatesDaily statesDaily_c = new StatesDaily();
                            statesDaily_c.setDate(yesterday_date);
                            statesDaily_c.setStatus("Deceased");

                            index_array = new int[]
                                    {response.body().getStatesDaily().indexOf(statesDaily_a),
                                            response.body().getStatesDaily().indexOf(statesDaily_b),
                                            response.body().getStatesDaily().indexOf(statesDaily_c)
                                    };
                            toolbar_date = response.body().getStatesDaily().get(index_array[0]).getDate();
                            updateMenuTitles(toolbar_date);

                        }


                        StatesDaily statesDaily_new = new StatesDaily();
                        for (int i = 0; i < statesDaily_new.All_States_Length(); i++) {
                            int c_confirm = Integer.parseInt
                                    (response.body().getStatesDaily().get(index_array[0]).All_States(i));

                            int c_recover = Integer.parseInt
                                    (response.body().getStatesDaily().get(index_array[1]).All_States(i));

                            int c_death = Integer.parseInt
                                    (response.body().getStatesDaily().get(index_array[2]).All_States(i));

                            String total = String.valueOf(c_confirm + c_recover + c_death);

                            array_list.add(
                                    new Card_DataModel(array_statenames[i],
                                            total,
                                            response.body().getStatesDaily().get(index_array[0]).All_States(i),
                                            response.body().getStatesDaily().get(index_array[1]).All_States(i),
                                            response.body().getStatesDaily().get(index_array[2]).All_States(i)));
                        }

                        recyclerAdapter = new RecyclerAdapter(context, array_list);
                        recyclerView.setAdapter(recyclerAdapter);
                        if (recyclerAdapter.getItemCount() != 0) {
                            customProgressDialog.dismiss();
                        }

                    }
                }


                @Override
                public void onFailure(Call<StatesDaily_List> call, Throwable t) {

                }
            });
//comments added.
        }
    }


  /*  @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.date_selected);
        if(!TextUtils.isEmpty(toolbar_date))
        {
            item.setTitle(toolbar_date);
        }


        return super.onPrepareOptionsMenu(menu);
    }*/

       @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    private void updateMenuTitles(String toolbar_data) {
        MenuItem bedMenuItem = menu.findItem(R.id.date_selected);
        if(!TextUtils.isEmpty(toolbar_data))
        {
            bedMenuItem.setTitle(toolbar_data);
        }
        else
        {
            bedMenuItem.getTitle();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      /*  switch (item.getItemId()) {
            case R.id.date_selected:
                return true;

            default:

        }*/
        return super.onOptionsItemSelected(item);
    }
}
