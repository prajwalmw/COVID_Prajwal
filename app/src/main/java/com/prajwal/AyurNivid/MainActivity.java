package com.prajwal.AyurNivid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

import com.prajwal.AyurNivid.network.ApiClient;
import com.prajwal.AyurNivid.network.ApiInterface;
import com.prajwal.AyurNivid.network.NetworkConnection;
import com.prajwal.AyurNivid.pojo_model.StatesDaily;
import com.prajwal.AyurNivid.pojo_model.StatesDaily_List;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
    String today_date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("AyurNivid");    //App-name
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


            Calendar calendar = Calendar.getInstance();     //today's date.
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy");
            today_date = simpleDateFormat.format(calendar.getTime());

            Calendar cal_yes = Calendar.getInstance();
            cal_yes.add(Calendar.DATE, -1);     //yesterday's date.
            final String yesterday_date = simpleDateFormat.format(cal_yes.getTime());

            Calendar cal_otherDate = Calendar.getInstance();
            cal_otherDate.add(Calendar.DATE, -2);     //day after yesterday's date.
            final String other_date = simpleDateFormat.format(cal_otherDate.getTime());

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

                        int[] index_array = new int[]{};

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
//                            updateMenuTitles(toolbar_date);
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

                            if(response.body().getStatesDaily().indexOf(statesDaily_a) >= 0 ||
                                    response.body().getStatesDaily().indexOf(statesDaily_b) >= 0 ||
                                    response.body().getStatesDaily().indexOf(statesDaily_c) >= 0)
                            {
                                index_array = new int[]
                                        {response.body().getStatesDaily().indexOf(statesDaily_a),
                                                response.body().getStatesDaily().indexOf(statesDaily_b),
                                                response.body().getStatesDaily().indexOf(statesDaily_c)
                                        };
                                toolbar_date = response.body().getStatesDaily().get(index_array[0]).getDate();
//                            updateMenuTitles(toolbar_date);
                            }
                            else {
                                StatesDaily statesDaily_a1 = new StatesDaily();
                                statesDaily_a1.setDate(other_date);
                                statesDaily_a1.setStatus("Confirmed");

                                StatesDaily statesDaily_b1 = new StatesDaily();
                                statesDaily_b1.setDate(other_date);
                                statesDaily_b1.setStatus("Recovered");

                                StatesDaily statesDaily_c1 = new StatesDaily();
                                statesDaily_c1.setDate(other_date);
                                statesDaily_c1.setStatus("Deceased");

                                if (response.body().getStatesDaily().indexOf(statesDaily_a1) >= 0 ||
                                        response.body().getStatesDaily().indexOf(statesDaily_b1) >= 0 ||
                                        response.body().getStatesDaily().indexOf(statesDaily_c1) >= 0)
                                {
                                    index_array = new int[]
                                            {response.body().getStatesDaily().indexOf(statesDaily_a1),
                                                    response.body().getStatesDaily().indexOf(statesDaily_b1),
                                                    response.body().getStatesDaily().indexOf(statesDaily_c1)
                                            };
                                    toolbar_date = response.body().getStatesDaily().get(index_array[0]).getDate();
//                            updateMenuTitles(toolbar_date);
                                }

                            }


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
                        else{
                            AlertDialog.Builder alerBuilder = new AlertDialog.Builder(context)
                                    .setTitle("Oops!")
                                    .setMessage("Something went wrong. Please try again later!")
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
                        }

                    }
                }


                @Override
                public void onFailure(Call<StatesDaily_List> call, Throwable t) {
                    AlertDialog.Builder alerBuilder = new AlertDialog.Builder(context)
                            .setTitle("Oops!")
                            .setMessage("Something went wrong. Please try again later!")
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



/*    private void updateMenuTitles(String toolbar_data) {
        MenuItem bedMenuItem = menu.findItem(R.id.date_selected);
        if(!TextUtils.isEmpty(toolbar_data))
        {
            bedMenuItem.setTitle(today_date);   //current date.
        }
        else
        {
            bedMenuItem.getTitle();
        }


    }*/

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info:
                Intent new_screen = new Intent(MainActivity.this, Information.class);
                startActivity(new_screen);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }*/
}
