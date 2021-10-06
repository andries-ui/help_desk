package com.example.techmaster.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techmaster.HomeActivity;
import com.example.techmaster.R;
import com.example.techmaster.adapters.History;
import com.example.techmaster.model.Query;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HistoryDialog extends DialogFragment {

    View view;

    private RecyclerView history;
    History adapter;
    ArrayList<Query> list;
    AppCompatImageView more;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_history, container, false);
      try {
          Init();

          list = new ArrayList<>();

          history.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

          adapter = new History(getActivity(), list);
      }catch (Exception e){
          Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
      }

      more.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              PopupMenu menu = new PopupMenu(getActivity().getApplicationContext(), more);
              menu.getMenuInflater().inflate(R.menu.user_options, menu.getMenu());
              menu.show();

              menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                  @Override
                  public boolean onMenuItemClick(MenuItem item) {
                      switch (item.getItemId()) {
                          case R.id.mresolved:
                                Resolved();
                              return true;
                          case R.id.munresolved:
                              Unresolved();
                              return true;
                          default:
                              return false;
                      }
                  }
              });
          }
      });

        return view;
    }

    private void Init() {
        history = view.findViewById(R.id.history);
        more = view.findViewById(R.id.more);
    }

    @Override
    public void onStart() {
        super.onStart();
        list.clear();
        FirebaseDatabase.getInstance("https://techmaster-50be9-default-rtdb.firebaseio.com/").getReference("issue").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot snap: snapshot.getChildren()) {
                        try {

                            if (snap.child("uid").getValue().toString().matches(user.getUid())) {


                                String description = snap.child("description").getValue().toString();
                                String key = snap.getKey();
                                String issue_relation = snap.child("issue_relation").getValue().toString();
                                String status = snap.child("status").getValue().toString();
                                String uid = snap.child("uid").getValue().toString();
                                String attended = snap.child("attended").getValue().toString();
                                String date = snap.child("date").getValue().toString();
                                String office_num = snap.child("office_num").getValue().toString();
                                String office_contact = snap.child("office_contact").getValue().toString();
                                String office_holder = snap.child("office_holder").getValue().toString();
                                String url ="url" ;
                                if(snap.hasChild("url")) {
                                     url = snap.child("url").getValue().toString();
                                }

                                list.add(new Query( key,url, office_holder,  office_num,
                                        office_contact,  description, issue_relation,
                                        date,  uid,  status,  attended));
                            }
                        } catch (Exception w) {
                            Toast.makeText(getContext(), w.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    history.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void Resolved(){
        list.clear();
        FirebaseDatabase.getInstance("https://techmaster-50be9-default-rtdb.firebaseio.com/").getReference("issue").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot snap: snapshot.getChildren()) {
                        try {

                            if (snap.child("uid").getValue().toString().matches(user.getUid())
                                    && snap.child("status").getValue().toString().matches("resolved")) {


                                String description = snap.child("description").getValue().toString();
                                String key = snap.getKey();
                                String issue_relation = snap.child("issue_relation").getValue().toString();
                                String status = snap.child("status").getValue().toString();
                                String uid = snap.child("uid").getValue().toString();
                                String attended = snap.child("attended").getValue().toString();
                                String date = snap.child("date").getValue().toString();
                                String office_num = snap.child("office_num").getValue().toString();
                                String office_contact = snap.child("office_contact").getValue().toString();
                                String office_holder = snap.child("office_holder").getValue().toString();
                                String url = "url";
                                if (snap.hasChild("url")) {
                                    url = snap.child("url").getValue().toString();
                                }

                                list.add(new Query(key, url, office_holder, office_num,
                                        office_contact, description, issue_relation,
                                        date, uid, status, attended));

                            }
                        } catch (Exception w) {
                            Toast.makeText(getContext(), w.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    history.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void Unresolved(){
        list.clear();
        FirebaseDatabase.getInstance("https://techmaster-50be9-default-rtdb.firebaseio.com/").getReference("issue").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot snap: snapshot.getChildren()) {
                        try {

                            if (snap.child("uid").getValue().toString().matches(user.getUid())
                                    && snap.child("status").getValue().toString().matches("unresolved")) {


                                String description = snap.child("description").getValue().toString();
                                String key = snap.getKey();
                                String issue_relation = snap.child("issue_relation").getValue().toString();
                                String status = snap.child("status").getValue().toString();
                                String uid = snap.child("uid").getValue().toString();
                                String attended = snap.child("attended").getValue().toString();
                                String date = snap.child("date").getValue().toString();
                                String office_num = snap.child("office_num").getValue().toString();
                                String office_contact = snap.child("office_contact").getValue().toString();
                                String office_holder = snap.child("office_holder").getValue().toString();
                                String url = "url";
                                if (snap.hasChild("url")) {
                                    url = snap.child("url").getValue().toString();
                                }

                                list.add(new Query(key, url, office_holder, office_num,
                                        office_contact, description, issue_relation,
                                        date, uid, status, attended));

                            }
                        } catch (Exception w) {
                            Toast.makeText(getContext(), w.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    history.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
