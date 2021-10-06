package com.example.techmaster.dialog;

import android.app.Dialog;
import android.content.Context;
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
import com.example.techmaster.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
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

public class
SignUpPage extends DialogFragment {

    TextInputLayout id, email,password, confirmPassword;
    MaterialTextView hasCaps, hasSmallCaps, hasNumber, hasChar,MinLength;
    CircularProgressButton signup, verify;
    MaterialCardView createAccount;
    ImageButton viewhide,viewhide1;

    FirebaseAuth auth;
    FirebaseUser user;

    String names = null , contact = null;

    View view;
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.signup,container,false);

       try {
           Init();
           FirebaseApp.getInstance();
           email.getEditText().addTextChangedListener(emailValidate);
           id.getEditText().addTextChangedListener(IDValidator);
           password.getEditText().addTextChangedListener(PasswordValidator);
           confirmPassword.getEditText().addTextChangedListener(ConfirmPasswod);

           signup.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if ( Verifypassword(password.getEditText().getText().toString().trim()) &&
                           password.getEditText().getText().toString().trim().matches(confirmPassword.getEditText().getText().toString().trim())) {
                       signup.startAnimation();
                       auth = FirebaseAuth.getInstance();

                       auth.createUserWithEmailAndPassword(email.getEditText().getText().toString().trim(),
                               password.getEditText().getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                           @Override
                           public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                               user = auth.getCurrentUser();
                               if (task.isSuccessful()) {
                                   HashMap _user = new HashMap();
                                   _user.put("id", id.getEditText().getText().toString().trim());
                                   _user.put("contact", contact);
                                   _user.put("email", email.getEditText().getText().toString().trim());
                                   _user.put("names", names);
                                   try {
                                       FirebaseDatabase.getInstance("https://techmaster-50be9-default-rtdb.firebaseio.com/").getReference("user").child(user.getUid()).setValue(_user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull @NotNull Task<Void> _task) {
                                               if (_task.isSuccessful()) {
                                                   Snackbar.make(signup, "=User registered successfully", Snackbar.LENGTH_SHORT).show();
                                                   Intent intent = new Intent(getContext(), HomeActivity.class);
                                                   signup.revertAnimation();
                                                   startActivity(intent);

                                               } else {
                                                   signup.revertAnimation();
                                                   Snackbar.make(signup, "==User registration unsuccessfully", Snackbar.LENGTH_SHORT).show();
                                                   auth.getCurrentUser().delete();
                                               }
                                           }
                                       });

                                   } catch (Exception e) {
                                       signup.revertAnimation();
                                       Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                   }
                               } else {
                                   signup.revertAnimation();
                                   Snackbar.make(signup, "===User registration unsuccessfully", Snackbar.LENGTH_SHORT).show();
                               }
                           }
                       });

                   } else {

                       if (!VerifyEmail(email.getEditText().getText().toString().trim())) {
                           email.setError("This field is reqired");
                       }
                       if (!Verifyid1(id.getEditText().getText().toString().trim())) {
                           id.setError("This field is reqired");
                       }
                       if (!Verifypassword(password.getEditText().getText().toString().trim())) {
                           password.setError("This field is reqired");
                       }
                       if (!Verifypassword(confirmPassword.getEditText().getText().toString().trim())) {
                           confirmPassword.setError("This field is reqired");
                       }
                   }
               }
           });

           verify.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if(VerifyEmail(email.getEditText().getText().toString().trim()) &&
                           Verifyid1(id.getEditText().getText().toString().trim()) ) {

                       verify.startAnimation();
                       try{
                       FirebaseDatabase.getInstance("https://techmaster-50be9-default-rtdb.firebaseio.com/").getReference("emp").child(id.getEditText().getText().toString()).addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                               if (dataSnapshot.exists()) {
                                   names = dataSnapshot.child("names").getValue().toString();
                                   email.getEditText().setText(dataSnapshot.child("email").getValue().toString());
                                   id.getEditText().setText(dataSnapshot.child("id").getValue().toString());
                                   contact = dataSnapshot.child("contact").getValue().toString();

                                   createAccount.setVisibility(View.VISIBLE);
                                   verify.setEnabled(false);
                               } else {
                                   verify.revertAnimation();
                                   Toast.makeText(getContext(), "You are not a valid user , please contact the admin!", Toast.LENGTH_LONG).show();
                               }
                           }

                           @Override
                           public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                                verify.revertAnimation();
                           }
                       });

                       }catch (Exception e){
                           verify.revertAnimation();
                           Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                       }
                   }else{
                       if(!VerifyEmail(email.getEditText().getText().toString().trim())){
                           email.setError("Invalid error");

                       }

                       if(!Verifyid1(id.getEditText().getText().toString().trim())){
                           id.setError("Invalid error");
                       }

                       verify.revertAnimation();
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
           viewhide1.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   try {
                       if (confirmPassword.getEditText().getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                           viewhide1.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                           confirmPassword.getEditText().setSelection(password.getEditText().getText().toString().length());
                           confirmPassword.getEditText().setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                       } else {
                           viewhide1.setImageResource(R.drawable.ic_baseline_visibility_24);
                           confirmPassword.getEditText().setTransformationMethod(PasswordTransformationMethod.getInstance());
                           confirmPassword.getEditText().setSelection(confirmPassword.getEditText().getText().toString().length());
                       }
                   } catch (Exception e) {
                       Snackbar.make(viewhide1, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                   }
               }
           });
       }catch (Exception e){
           Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private TextWatcher PasswordValidator = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String _password= s.toString();

            if(TextUtils.isEmpty(_password)){
                password.setError(null);
                //validation helper
                hasCaps.setTextColor(Color.RED);
                hasChar.setTextColor(Color.RED);
                hasSmallCaps.setTextColor(Color.RED);
                MinLength.setTextColor(Color.RED);
            }

            char chr[] = s.toString().toCharArray();
            for(Character c:chr)
            {
                if (Character.isDigit(c)) {
                    hasNumber.setTextColor(Color.GREEN);
                }

                if (Pattern.matches("[a-z]", c.toString())) {
                    hasSmallCaps.setTextColor(Color.GREEN);
                }
                if (Pattern.matches("[A-Z]+", c.toString())) {
                    hasCaps.setTextColor(Color.GREEN);
                }
                if (Pattern.matches("(?=.*[@#$%^&*+=!])(?=\\S+$)", c.toString())) {
                    hasChar.setTextColor(Color.GREEN);
                }
                if (chr.length > 7) {
                    MinLength.setTextColor(Color.GREEN);
                }
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher ConfirmPasswod = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String _confirmPassword = confirmPassword.getEditText().getText().toString().trim();
            if(password.getEditText().getText().toString().trim().matches(_confirmPassword)){
                confirmPassword.setError(null);
            }else{
                confirmPassword.setError("Password does not match");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void Init(){
        // text Input

        id = view.findViewById(R.id.id);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        confirmPassword = view.findViewById(R.id.confirmPassword);
        signup = view.findViewById(R.id.sign_up);
        viewhide = view.findViewById(R.id.viewhide);
        viewhide1 = view.findViewById(R.id.confirmviewhide);
        verify = view.findViewById(R.id.verify);
        createAccount = view.findViewById(R.id.create_password);

        //textview
        hasChar = view.findViewById(R.id.passwordHasCharacter);
        hasCaps = view.findViewById(R.id.passwordHasCapital);
        hasSmallCaps = view.findViewById(R.id.passwordHasNoCapital);
        hasNumber = view.findViewById(R.id.passwordHasNumer);
        MinLength = view.findViewById(R.id.passwordHaEightCharacters);
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
