package com.sliit.travelhelp.Utility;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.sliit.travelhelp.Models.ModelReservation;
import com.sliit.travelhelp.R;

import org.bson.BsonDateTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReservationListAdapter extends RecyclerView.Adapter<ReservationListAdapter.MyViewHolder>  {
        private List<ModelReservation> dataList;
        private  View view;
        public ReservationListAdapter(List<ModelReservation> dataList,View view) {
            this.dataList = dataList;
            this.view = view;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_small, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            final BsonDateTime date = dataList.get(position).getDate();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            holder.tvDate.setText(dateFormat.format(new Date(date.getValue())));
            holder.tvFrom.setText(dataList.get(position).getTrain().getFrom());
            holder.tvTo.setText(dataList.get(position).getTrain().getTo());

            int pos = position;
            holder.btnView.findViewById(R.id.item_view_reservation_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataHolder.setReservationId(dataList.get(pos).getId().getValue().toString());
                    Navigation.findNavController(view).navigate(R.id.fragmentReservationSummery);

                }
            });

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
            tvDate = itemView.findViewById(R.id.txt_item_event_small_date);
            tvFrom = itemView.findViewById(R.id.txt_item_event_small_from);
            tvTo = itemView.findViewById(R.id.txt_item_event_small_to);
            btnView = itemView.findViewById(R.id.item_view_reservation_btn);
        }
    }
}
