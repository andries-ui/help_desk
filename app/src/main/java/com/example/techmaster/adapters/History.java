package com.example.techmaster.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;


import com.example.techmaster.R;
import com.example.techmaster.Replies;
import com.example.techmaster.model.Query;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class History extends RecyclerView.Adapter<History.ViewHolder> {

    private List<Query> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Context context;

    // data is passed into the constructor
    public History(Context context, List<Query> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.history_item, parent, false);
        context = view.getContext();
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Query qr = mData.get(position);
        holder.date.setText(qr.getDate());
        holder.description.setText(qr.getDescription());
        holder.status.setText(qr.getStatus());
        holder.attended.setText(qr.getAttended());

        if(qr.getAttended().matches("No")){
            holder.attended.setTextColor(Color.RED);
        }
        if(qr.getAttended().matches("Yes")){
            holder.attended.setTextColor(Color.GREEN);
        }

        if(qr.getStatus().matches("unresolved")){
            holder.indicator.setImageResource(R.drawable.ic_baseline_error_outline_24);
        }
        if(qr.getStatus().matches("resolved")){
            holder.indicator.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
        }


            holder.image.setVisibility(View.VISIBLE);
            Picasso.get().load(qr.getUrl()).error(R.drawable.ic_baseline_image_24black).centerCrop().fit().into(holder.image);

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView date,description,attended,status;
        AppCompatImageView indicator, image;
        ViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            description = itemView.findViewById(R.id.description);
            attended = itemView.findViewById(R.id.attended);
            status = itemView.findViewById(R.id.status);
            indicator = itemView.findViewById(R.id.statusInducator);
            image = itemView.findViewById(R.id.image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());

            try {
                Intent intent = new Intent(context, Replies.class);
                intent.putExtra("key",mData.get(getAdapterPosition()).getKey());
                context.startActivity(intent);

            }catch (Exception e){
                Toast.makeText(context, e.getMessage()+ "==" + e.getCause(), Toast.LENGTH_LONG).show();
            }

        }
    }

    // convenience method for getting data at click position
    public String getKey(int id) {
        return mData.get(id).getKey();
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
