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
import android.widget.TextView;
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
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.regex.Pattern;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class
ResetPassword extends DialogFragment {

    TextInputLayout email;
    CircularProgressButton signup;
    TextView signin;

    FirebaseAuth auth;
    FirebaseUser user;

    View view;
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.reset_password,container,false);
        Init();
        FirebaseApp.getInstance();
        email.getEditText().addTextChangedListener(emailValidate);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( VerifyEmail(email.getEditText().getText().toString().trim()) )
                {
                    signup.startAnimation();
                    auth = FirebaseAuth.getInstance();

                    auth.sendPasswordResetEmail(email.getEditText().getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if(task.isSuccessful()){
                                signup.revertAnimation();
                                Snackbar.make(signup, "Password reset link sent successfully",Snackbar.LENGTH_SHORT).show();

                            }else{
                                signup.revertAnimation();
                                Snackbar.make(signup, "Process unsuccessfully",Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                else{

                    if(!VerifyEmail(email.getEditText().getText().toString().trim())){
                        email.setError("This field is reqired");
                    }

                }
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                SigninPage dialog = new SigninPage();
                dialog.setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dialog.show(getActivity().getSupportFragmentManager(), "dialog");
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


    private void Init(){
        // text Input

        email = view.findViewById(R.id.email);
        signup = view.findViewById(R.id.sign_up);
        signin = view.findViewById(R.id.signin);
    }

    private boolean VerifyEmail(String email){

        Pattern emailpartten = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");

        return  !TextUtils.isEmpty(email) && emailpartten.matcher(email).matches();

    }


}
