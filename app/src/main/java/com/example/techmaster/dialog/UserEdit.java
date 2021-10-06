package com.example.techmaster.dialog;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.techmaster.HomeActivity;
import com.example.techmaster.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
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

public class UserEdit extends DialogFragment {

    TextInputLayout names, contact, id, email;
    CircularProgressButton signup;

    String _name, _contact, _email, _id;
    FirebaseAuth auth;
    FirebaseUser user;

    View view;
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.user_edit,container,false);

      try {
          Init();
          FirebaseApp.getInstance();
          email.getEditText().addTextChangedListener(emailValidate);
          contact.getEditText().addTextChangedListener(contactValidate);
          names.getEditText().addTextChangedListener(Names);

          signup.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  signup.startAnimation();
                  auth = FirebaseAuth.getInstance();

                  if (!TextUtils.isEmpty(names.getEditText().getText())) {
                      _name = names.getEditText().getText().toString();
                  }

                  if (!TextUtils.isEmpty(email.getEditText().getText())) {
                      _email = email.getEditText().getText().toString();
                  }
                  if (!TextUtils.isEmpty(contact.getEditText().getText())) {
                      _contact = contact.getEditText().getText().toString();
                  }

                  if (TextUtils.isEmpty(contact.getEditText().getText())
                          && TextUtils.isEmpty(email.getEditText().getText()) &&
                          TextUtils.isEmpty(names.getEditText().getText())) {
                      Toast.makeText(getContext(), "There is nothng to upload yet", Toast.LENGTH_SHORT).show();
                  } else {

                      HashMap _user = new HashMap();
                      _user.put("id", id.getEditText().getText().toString().trim());
                      _user.put("contact", contact.getEditText().getText().toString().trim());
                      _user.put("email", email.getEditText().getText().toString().trim());
                      _user.put("names", id.getEditText().getText().toString().trim());
                      try {
                          FirebaseDatabase.getInstance("https://techmaster-50be9-default-rtdb.firebaseio.com/").getReference("user").child(user.getUid()).updateChildren(_user).addOnCompleteListener(new OnCompleteListener<Void>() {
                              @Override
                              public void onComplete(@NonNull @NotNull Task<Void> _task) {
                                  if (_task.isSuccessful()) {
                                      Snackbar.make(signup, "Updated successfully", Snackbar.LENGTH_SHORT).show();
                                      Intent intent = new Intent(getContext(), HomeActivity.class);
                                      signup.revertAnimation();
                                      startActivity(intent);

                                  } else {
                                      signup.revertAnimation();
                                      Snackbar.make(signup, "Update unsuccessful", Snackbar.LENGTH_SHORT).show();
                                      auth.getCurrentUser().delete();
                                  }
                              }
                          });
                      } catch (Exception e) {
                          signup.revertAnimation();
                          Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                      }
                  }

              }
          });

      }catch (Exception e){
          Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
      }
        return view;
    }

    private TextWatcher emailValidate = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String _email= email.getEditText().getText().toString().trim();
            if(!VerifyEmail(_email)){
                email.setError("Invalid email");

            }else{
                email.setError(null);
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
            String _contact= contact.getEditText().getText().toString().trim();
            if(!Verifycontact(_contact)){
                contact.setError("Invalid contact");

            }else{
                contact.setError(null);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher IDValidator = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String _id= id.getEditText().getText().toString().trim();
            if(!Verifyid1(_id)){
                id.setError("Invalid ID");

            }else{
                id.setError(null);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    private TextWatcher Names = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String _confirmPassword = names.getEditText().getText().toString().trim();
            if(!TextUtils.isEmpty(_confirmPassword)){
                names.setError(null);
            }else{
                names.setError("Invalid names");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void Init(){
        // text Input

        names = view.findViewById(R.id.username);
        contact = view.findViewById(R.id.contact);
        id = view.findViewById(R.id.id);
        email = view.findViewById(R.id.email);
        signup = view.findViewById(R.id.sign_up);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance("https://techmaster-50be9-default-rtdb.firebaseio.com/").getReference("user")
                .child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    _name = (dataSnapshot.child("names").getValue().toString());
                    _email = (dataSnapshot.child("email").getValue().toString());
                    _id = (dataSnapshot.child("id").getValue().toString());
                    _contact = (dataSnapshot.child("contact").getValue().toString());

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });

    }

    private boolean VerifyEmail(String email){

        Pattern emailpartten = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");

        return  !TextUtils.isEmpty(email) && emailpartten.matcher(email).matches();

    }

    private boolean Verifypassword(String passw){

        Pattern passwordpartten = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&*+=!])(?=\\S+$).{8,}");

        return  !TextUtils.isEmpty(passw) && passwordpartten.matcher(passw).matches();

    }

    private boolean Verifycontact(String cntr){

        Pattern contactpartten = Pattern.compile("^(27|0)[6-8][0-8]{8}");

        return  !TextUtils.isEmpty(cntr) && contactpartten.matcher(cntr).matches();

    }

    private boolean Verifyid1(String id){

        String IdExpression = "(((\\d{2}((0[13578]|1[02])(0[1-9]|[12]\\d|3[01])|(0[13456789]|1[012])(0[1-9]|[12]\\d|30)|02(0[1-9]|1\\d|2[0-8])))|([02468][048]|[13579][26])0229))(( |)(\\d{4})( |)(\\d{3})|(\\d{7}))";

        Pattern idpartten = Pattern.compile(IdExpression);

        return  !TextUtils.isEmpty(id) && idpartten.matcher(id).matches();

    }
}
