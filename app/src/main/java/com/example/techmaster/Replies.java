package com.example.techmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techmaster.adapters.History;
import com.example.techmaster.adapters.ReplyList;
import com.example.techmaster.model.Query;
import com.example.techmaster.model.Reply;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Replies extends AppCompatActivity {

    TextView date,description,attended,status;
    AppCompatImageView indicator, image;
    RecyclerView replys;
    RelativeLayout query;

    Uri selectedimage;

    private RecyclerView history;
    ReplyList adapter;
    ArrayList<Reply> list;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_reply);

        try {
            Init();
            list = new ArrayList<>();

            replys.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));

            adapter = new ReplyList(getApplicationContext(), list);

        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!status.getText().toString().matches("solved")) {
                    FirebaseDatabase.getInstance("https://techmaster-50be9-default-rtdb.firebaseio.com/").getReference("issue").
                            child(getIntent().getStringExtra("key")).child("attended").setValue("No").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                            FirebaseDatabase.getInstance("https://techmaster-50be9-default-rtdb.firebaseio.com/").getReference("issue").
                                    child(getIntent().getStringExtra("key")).child("date").setValue(date).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(), "This issue has been resent to the technician.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Resubmittion Failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Resubmittion Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(Replies.this, "This issue is resolved already", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void Init(){

        date = findViewById(R.id.date);
        description = findViewById(R.id.description);
        attended = findViewById(R.id.attended);
        status = findViewById(R.id.status);
        indicator = findViewById(R.id.statusInducator);
        image = findViewById(R.id.image);
        replys = findViewById(R.id.replies);
        query = findViewById(R.id.query);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance("https://techmaster-50be9-default-rtdb.firebaseio.com/").getReference("issue")
                .child(getIntent().getStringExtra("key")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    description.setText(snapshot.child("description").getValue().toString());
                    status.setText(snapshot.child("status").getValue().toString());
                    attended.setText(snapshot.child("attended").getValue().toString());
                    date.setText(snapshot.child("date").getValue().toString());

                    if(snapshot.hasChild("url")) {
                        Picasso.get().load(snapshot.child("url").getValue().toString()).fit().centerCrop().into(image);
                    }

                    if(snapshot.child("status").getValue().toString().matches("unresolved")){
                        indicator.setImageResource(R.drawable.ic_baseline_error_outline_24);
                    }
                    if(snapshot.child("status").getValue().toString().matches("resolved")){
                       indicator.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance("https://techmaster-50be9-default-rtdb.firebaseio.com/").getReference("issue").child(getIntent().getStringExtra("key"))
                .child("reply").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for(DataSnapshot snap:  snapshot.getChildren()) {
                        try {
                            String key = snap.getKey();
                            String reply = snap.child("description").getValue().toString();
                            String status = snap.child("status").getValue().toString();
                            String image = snap.child("url").getValue().toString();
                            String date = snap.child("date").getValue().toString();
                            String uid = snap.child("uid").getValue().toString();

                            list.add(new Reply(getIntent().getStringExtra("key"),key,reply, date, uid, status,image));

                        }catch (Exception e){
                            Toast.makeText(Replies.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    replys.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}