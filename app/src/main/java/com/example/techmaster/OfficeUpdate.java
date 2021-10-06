package com.example.techmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.regex.Pattern;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class OfficeUpdate extends AppCompatActivity {

    private CircularProgressButton upload;
    private Spinner departments;
    private TextView departments_text;
    private TextInputLayout office_number, office_Contact,office_email;

    String _office_number, _contact, _email, _dept;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.office_edit);

    try{
        Init();

        ArrayAdapter<CharSequence> _departments = ArrayAdapter.createFromResource(getApplicationContext(),R.array.department_list, android.R.layout.simple_spinner_dropdown_item);
        _departments.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departments.setAdapter(_departments);

        departments.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                departments_text.setText(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                departments_text.setText(null);
            }
        });
        office_number.getEditText().addTextChangedListener(Office_number);


        office_email.getEditText().addTextChangedListener(emailValidate);


        try{
            office_Contact.getEditText().addTextChangedListener(contactValidate);
        }catch (Exception e){
            Toast.makeText(getApplicationContext().getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(departments_text.getText())){
                    _dept = departments_text.getText().toString();
                }

                if(!TextUtils.isEmpty(office_email.getEditText().getText())){
                    _email = office_email.getEditText().getText().toString();
                }
                if(!TextUtils.isEmpty(office_Contact.getEditText().getText())){
                    _contact = office_Contact.getEditText().getText().toString();
                }
                if(!TextUtils.isEmpty(office_number.getEditText().getText())){
                    _contact = office_number.getEditText().getText().toString();
                }

                if(TextUtils.isEmpty(office_number.getEditText().getText())
                        && TextUtils.isEmpty(office_Contact.getEditText().getText()) &&
                        TextUtils.isEmpty(office_email.getEditText().getText())&&
                        TextUtils.isEmpty(departments_text.getText())){
                    Toast.makeText(getApplicationContext(), "There is nothng to upload yet", Toast.LENGTH_SHORT).show();
                }else {

                    upload.startAnimation();
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    FirebaseUser user = auth.getCurrentUser();

                    HashMap _office = new HashMap();
                    _office.put("office_number", _office_number);
                    _office.put("office_email",_email);
                    _office.put("office_Contact", _contact);
                    _office.put("department", _dept);

                    FirebaseDatabase.getInstance("https://techmaster-50be9-default-rtdb.firebaseio.com/").getReference("user")
                            .child(user.getUid()).child("office").updateChildren(_office).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                upload.revertAnimation();
                                Toast.makeText(getApplicationContext(), "Updated successfully.", Toast.LENGTH_SHORT).show();
                            } else {
                                upload.revertAnimation();
                                Toast.makeText(getApplicationContext(), "Unsuccessful process, please try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }catch (Exception e){
        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
    }

}

    @Override
    public void onStart() {
        super.onStart();
        Retrieve();
    }

    private void Retrieve(){
        FirebaseDatabase.getInstance("https://techmaster-50be9-default-rtdb.firebaseio.com/").getReference("user")
                .child(user.getUid()).child("office").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    _contact = dataSnapshot.child("office_Contact").getValue().toString();
                    _office_number = (dataSnapshot.child("office_number").getValue().toString());
                    _email =(dataSnapshot.child("office_email").getValue().toString());
                    _dept = (dataSnapshot.child("department").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });
    }
    private TextWatcher emailValidate = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String _email= office_email.getEditText().getText().toString().trim();
            if(!VerifyEmail(_email)){
                office_email.setError("Invalid email");

            }else{
                office_email.setError(null);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher contactValidate = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String _contact= office_Contact.getEditText().getText().toString().trim();
            if(!Verifycontact(_contact)){
                office_Contact.setError("Invalid contact");

            }else{
                office_Contact.setError(null);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher Office_number = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String _confirmPassword = office_number.getEditText().getText().toString().trim();
            if(!TextUtils.isEmpty(_confirmPassword)){
                office_number.setError(null);
            }else{
                office_number.setError("Invalid Office number");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private boolean Validate(){

        if(TextUtils.isEmpty(departments_text.getText())){
            return false;
        }

        if(!Verifycontact(office_Contact.getEditText().getText().toString())){
            return false;
        }
        if(!VerifyEmail(office_email.getEditText().getText().toString())){
            return false;
        }
        if(TextUtils.isEmpty(office_number.getEditText().getText())){
            return false;
        }

        return true;
    }

    private  void Init(){

        departments = findViewById(R.id.departmentlist);
        departments_text = findViewById(R.id.departmentText);
        office_Contact = findViewById(R.id.contact);
        office_email = findViewById(R.id.office_email);
        office_number = findViewById(R.id.officeNumber);
        upload =findViewById(R.id.update);
    }

    private boolean VerifyEmail(String email){

        Pattern emailpartten = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");

        return  !TextUtils.isEmpty(email) && emailpartten.matcher(email).matches();

    }


    private boolean Verifycontact(String cntr){

        Pattern contactpartten = Pattern.compile("^(27|0)[6-8][0-8]{8}");

        return  !TextUtils.isEmpty(cntr) && contactpartten.matcher(cntr).matches();

    }

}