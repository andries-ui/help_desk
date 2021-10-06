package com.example.techmaster.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techmaster.R;
import com.example.techmaster.model.Query;
import com.example.techmaster.model.Reply;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReplyList extends RecyclerView.Adapter<ReplyList.ViewHolder> {

    private List<Reply> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Context context;

    // data is passed into the constructor
    public ReplyList(Context context, List<Reply> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.reply_item, parent, false);
        context = view.getContext();
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reply rp = mData.get(position);
        holder.reply.setText(rp.getDescription());
        holder.status.setText(rp.getStatus());

        Picasso.get().load(rp.getUrl()).fit().centerCrop().into(holder.images);

        holder.success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance("https://techmaster-50be9-default-rtdb.firebaseio.com/").getReference("issue").child(rp.getPostKey())
                        .child("reply").child(rp.getKey()).child("status").setValue("resolved").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()){
                            FirebaseDatabase.getInstance("https://techmaster-50be9-default-rtdb.firebaseio.com/").getReference("issue")
                                    .child(rp.getPostKey()).child("status").setValue("resolved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(context, "Successful solution", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(context, "Proccess failed", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                        }else {
                            FirebaseDatabase.getInstance("https://techmaster-50be9-default-rtdb.firebaseio.com/").getReference("issue")
                                    .child(rp.getPostKey()).child("reply").child(rp.getKey()).child("status").setValue("unresolved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(context, "Proccess failed ==", Toast.LENGTH_SHORT).show();

                                    }else {
                                        Toast.makeText(context, "Proccess failed", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                        }
                    }
                });
            }
        });
        holder.unsuccessful.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance("https://techmaster-50be9-default-rtdb.firebaseio.com/").getReference("issue")
                        .child(rp.getPostKey()).child("replies").child(rp.getKey()).child("status").setValue("unresolved").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()){
                            FirebaseDatabase.getInstance("https://techmaster-50be9-default-rtdb.firebaseio.com/").getReference("issue")
                                    .child(rp.getPostKey()).child("status").setValue("unresolved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(context, "Unsuccessful solution", Toast.LENGTH_SHORT).show();
                                    }else {
                                        FirebaseDatabase.getInstance("https://techmaster-50be9-default-rtdb.firebaseio.com/").getReference("issue")
                                                .child(rp.getPostKey()).child("replies").child(rp.getKey()).child("status").setValue("unresolved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(context, "Proccess failed", Toast.LENGTH_SHORT).show();
                                                }else {
                                                    Toast.makeText(context, "Proccess failed", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    }
                                }
                            });
                        }else {
                            Toast.makeText(context, "Proccess failed", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView reply, status;
        AppCompatImageView images;
        AppCompatButton success, unsuccessful;
        ViewHolder(View itemView) {
            super(itemView);
            reply = itemView.findViewById(R.id.reply);
            status = itemView.findViewById(R.id.status);
            images = itemView.findViewById(R.id.images);
            unsuccessful = itemView.findViewById(R.id.unresolved);
            success = itemView.findViewById(R.id.resolved);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());



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
