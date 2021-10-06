package com.example.techmaster.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.DialogFragment;

import com.example.techmaster.OfficeUpdate;
import com.example.techmaster.R;
import com.example.techmaster.Replies;
import com.example.techmaster.UserUpdate;
import com.example.techmaster.bottomsheet.UpdateOfficeDetails;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class Profile extends DialogFragment {

    View view;
    MaterialTextView name, id, contact, email,officeNum;
    AppCompatImageView edtOffice, edtUser;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_profile,container,false);
        Init();
        try{
        edtUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(getContext(), UserUpdate.class);
                getActivity().startActivity(intent);

            }
        });

        edtOffice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), OfficeUpdate.class);
                startActivity(intent);

            }
        });

    }catch (Exception e){
        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
    }
        return view;
    }

    private void Init(){

         name  = view.findViewById(R.id.names);
         id = view.findViewById(R.id.id);
         contact = view.findViewById(R.id.contacts);
        email = view.findViewById(R.id.email);
        officeNum = view.findViewById(R.id.officeNum);
        edtOffice = view.findViewById(R.id.edtOfficeDetails);
        edtUser = view.findViewById(R.id.edtUserDetails);

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance("https://techmaster-50be9-default-rtdb.firebaseio.com/").getReference("user")
                .child(user.getUid()).child("office").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                     officeNum.setText(dataSnapshot.child("office_number").getValue().toString());

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

                    name.setText(dataSnapshot.child("names").getValue().toString());
                    email.setText(dataSnapshot.child("email").getValue().toString());
                    id.setText(dataSnapshot.child("id").getValue().toString());
                    contact.setText(dataSnapshot.child("contact").getValue().toString());

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });

    }
}
