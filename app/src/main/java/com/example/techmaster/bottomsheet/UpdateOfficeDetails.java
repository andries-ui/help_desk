package com.example.techmaster.bottomsheet;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.techmaster.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.regex.Pattern;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class UpdateOfficeDetails extends BottomSheetDialogFragment {

    private CircularProgressButton upload;
    private Spinner departments;
    private TextView departments_text;
    private TextInputLayout office_number, office_Contact,office_email;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable android.os.Bundle savedInstanceState) {

    view = inflater.inflate(R.layout.update_details, container, false);

            Init();

        ArrayAdapter<CharSequence> _departments = ArrayAdapter.createFromResource(getContext(),R.array.department_list, android.R.layout.simple_spinner_dropdown_item);
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
            Toast.makeText(getActivity().getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Validate()) {

                    upload.startAnimation();
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    FirebaseUser user = auth.getCurrentUser();

                    HashMap _office = new HashMap();
                    _office.put("office_number", office_number.getEditText().getText().toString().trim());
                    _office.put("office_email", office_email.getEditText().getText().toString().trim());
                    _office.put("office_Contact", office_Contact.getEditText().getText().toString().trim());
                    _office.put("department", departments_text.getText().toString().trim());

                    FirebaseDatabase.getInstance("https://techmaster-50be9-default-rtdb.firebaseio.com/").getReference("user")
                            .child(user.getUid()).child("office").setValue(_office).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                dismiss();
                                upload.revertAnimation();
                                Toast.makeText(getContext(), "Office details updated.", Toast.LENGTH_SHORT).show();
                            } else {
                                upload.revertAnimation();
                                Toast.makeText(getContext(), "Unsuccessful process, please try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


    return view;
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

       departments = view.findViewById(R.id.departmentlist);
       departments_text = view.findViewById(R.id.departmentText);
       office_Contact = view.findViewById(R.id.contact);
       office_email = view.findViewById(R.id.office_email);
       office_number = view.findViewById(R.id.officeNumber);
       upload = view.findViewById(R.id.update);
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
