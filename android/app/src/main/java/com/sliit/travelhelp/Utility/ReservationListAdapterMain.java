package com.sliit.travelhelp.Utility;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.sliit.travelhelp.Authentication;
import com.sliit.travelhelp.Models.ModelReservation;
import com.sliit.travelhelp.R;
import com.sliit.travelhelp.ReservationActivity;

import org.bson.BsonDateTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReservationListAdapterMain extends RecyclerView.Adapter<ReservationListAdapterMain.MyViewHolder>  {
        private List<ModelReservation> dataList;
        private  View.OnClickListener listener;
        public ReservationListAdapterMain(List<ModelReservation> dataList, View.OnClickListener listener) {
            this.dataList = dataList;
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            final BsonDateTime date = dataList.get(position).getDate();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            holder.tvDate.setText(dateFormat.format(new Date(date.getValue())));
            holder.tvFrom.setText(dataList.get(position).getTrain().getFrom());
            holder.tvTo.setText(dataList.get(position).getTrain().getTo());

            holder.btnView.findViewById(R.id.btn_item_event_view).setOnClickListener(listener);

        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate;
        TextView tvFrom;
        TextView tvTo;
        Button btnView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.txt_item_event_date);
            tvFrom = itemView.findViewById(R.id.txt_item_event_from);
            tvTo = itemView.findViewById(R.id.txt_item_event_to);
            btnView = itemView.findViewById(R.id.btn_item_event_view);
        }
    }
}
