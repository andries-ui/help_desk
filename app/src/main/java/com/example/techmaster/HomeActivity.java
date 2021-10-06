package com.example.techmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techmaster.bottomsheet.UpdateOfficeDetails;
import com.example.techmaster.dialog.OfficeEdit;
import com.example.techmaster.dialog.HistoryDialog;
import com.example.techmaster.dialog.Problem;
import com.example.techmaster.dialog.Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class HomeActivity extends AppCompatActivity {

    CardView profile,query, history, signout;
    TextView names;
    AppCompatImageView more;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Init();

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Profile dialog = new Profile();
                dialog.setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dialog.show(getSupportFragmentManager(),"dialog");
            }
        });

        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Problem dialog = new Problem();
                dialog.setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dialog.show(getSupportFragmentManager(),"dialog");
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryDialog dialog = new HistoryDialog();
                dialog.setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dialog.show(getSupportFragmentManager(),"dialog");
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                finish();
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(getApplicationContext().getApplicationContext(), more);
                menu.getMenuInflater().inflate(R.menu.menu, menu.getMenu());
                menu.show();

                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.support:
                                Toast.makeText(HomeActivity.this, "The related website is not yet availabe!", Toast.LENGTH_SHORT).show();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
            }
        });

    }

    private void Init(){
        profile = findViewById(R.id.profile);
        query = findViewById(R.id.query);
        history = findViewById(R.id.history);
        signout = findViewById(R.id.signout);
        names = findViewById(R.id.names);
        more = findViewById(R.id.more);

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseDatabase.getInstance("https://techmaster-50be9-default-rtdb.firebaseio.com/").getReference("user")
                .child(user.getUid()).child("office").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    UpdateOfficeDet();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance("https://techmaster-50be9-default-rtdb.firebaseio.com/").getReference("user")
                .child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    names.setText(dataSnapshot.child("names").getValue().toString());


                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });

    }

    private void UpdateOfficeDet(){
        Thread myThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(900);
                    UpdateOfficeDetails bottomsheet = new UpdateOfficeDetails();
                    bottomsheet.show(getSupportFragmentManager(), "Bottomsheet signin");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        myThread.start();
    }
}