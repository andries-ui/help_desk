package com.example.techmaster.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.example.techmaster.FirstPage;
import com.example.techmaster.HomeActivity;
import com.example.techmaster.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import br.com.simplepass.loadingbutton.customViews.ProgressButton;

public class SigninPage extends DialogFragment {

    View view;
    CircularProgressButton signin;
    TextInputLayout email, password;
    TextView signup, forgot_password;
    ImageButton viewhide;
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.signin,container,false);

       try {
           Init();

           signin.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   FirebaseAuth auth = FirebaseAuth.getInstance();
                   FirebaseUser user = auth.getCurrentUser();
                   if (Validator()) {
                       signin.startAnimation();
                       auth.signInWithEmailAndPassword(email.getEditText().getText().toString(),
                               password.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                           @Override
                           public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                               if (task.isSuccessful()) {
                                   signin.revertAnimation();
                                   Intent intent = new Intent(getContext(), HomeActivity.class);
                                   startActivity(intent);
                               }
                               if(!task.isSuccessful()){
                                   signin.revertAnimation();
                                   Toast.makeText(getContext(), "Invalid email or password.", Toast.LENGTH_SHORT).show();
                               }
                           }
                       });
                   } else {
                       Toast.makeText(getContext(), "Enter email and password.", Toast.LENGTH_SHORT).show();
                   }

               }
           });

           viewhide.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   try {
                       if (password.getEditText().getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                           viewhide.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                           password.getEditText().setSelection(password.getEditText().getText().toString().length());
                           password.getEditText().setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                       } else {
                           viewhide.setImageResource(R.drawable.ic_baseline_visibility_24);
                           password.getEditText().setTransformationMethod(PasswordTransformationMethod.getInstance());
                           password.getEditText().setSelection(password.getEditText().getText().toString().length());
                       }
                   } catch (Exception e) {
                       Snackbar.make(viewhide, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                   }
               }
           });

           signup.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   dismiss();
                   SignUpPage dialog = new SignUpPage();
                   dialog.setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                   dialog.show(getActivity().getSupportFragmentManager(), "dialog");
               }
           });

           forgot_password.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   dismiss();
                   ResetPassword dialog = new ResetPassword();
                   dialog.setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                   dialog.show(getActivity().getSupportFragmentManager(), "dialog");
               }
           });
       }catch (Exception e){
           Snackbar.make(signin, e.getMessage(), Snackbar.LENGTH_INDEFINITE).show();
       }
        return view;
    }

    private void Init(){
        signin = view.findViewById(R.id.sign_in);
        password = view.findViewById(R.id.password);
        email = view.findViewById(R.id.username);
        viewhide = view.findViewById(R.id.viewhide);
        signup = view.findViewById(R.id.signup);
        forgot_password = view.findViewById(R.id.reset_password);
    }

    private boolean Validator(){
        if(TextUtils.isEmpty(email.getEditText().getText().toString().trim())){
            return false;
        }

        if(TextUtils.isEmpty(password.getEditText().getText().toString().trim())){
            return false;
        }

        return true;
    }
}
