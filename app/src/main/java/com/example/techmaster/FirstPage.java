package com.example.techmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;

import com.example.techmaster.dialog.SignUpPage;
import com.example.techmaster.dialog.SigninPage;
import com.google.android.material.button.MaterialButton;

public class FirstPage extends AppCompatActivity {

    AppCompatButton signin, signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_page);
        Init();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SigninPage dialog = new SigninPage();
                dialog.setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dialog.show(getSupportFragmentManager(),"dialog");
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpPage dialog = new SignUpPage();
                dialog.setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dialog.show(getSupportFragmentManager(),"dialog");
            }
        });

    }

    private void Init(){
        signin = findViewById(R.id.signin_dialog);
        signup = findViewById(R.id.signup_dialog);
    }
}