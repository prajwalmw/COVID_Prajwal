package com.prajwal.covid_prajwal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
private Context mcontext;
ArrayList<Card_DataModel> confirm_arraylist;

    public RecyclerAdapter(Context context, ArrayList<Card_DataModel> confirmed_array) {
        this.mcontext = context;
        this.confirm_arraylist = confirmed_array;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_cardview, parent, false);
        return new MyViewHolder(v/*, parent.getContext()*/);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.state.setText(confirm_arraylist.get(position).getState());
        holder.date.setText(confirm_arraylist.get(position).getDate());
        holder.confirmed.setText(confirm_arraylist.get(position).getConfirm());
        holder.recovered.setText(confirm_arraylist.get(position).getRecover());
        holder.deceased.setText(confirm_arraylist.get(position).getDeath());

    }

    @Override
    public int getItemCount() {
        return confirm_arraylist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView state, date, confirmed, recovered, deceased;

        public MyViewHolder(@NonNull View itemView/*, Context context*/) {
            super(itemView);
            this.cardView = itemView.findViewById(R.id.recycle_card);
            this.state = itemView.findViewById(R.id.StateName);
            this.date = itemView.findViewById(R.id.DateName);
            this.confirmed = itemView.findViewById(R.id.Confirmed);
            this.recovered = itemView.findViewById(R.id.Recovered);
            this.deceased = itemView.findViewById(R.id.Deceased);
//            mcontext = context;
        }
    }
}
