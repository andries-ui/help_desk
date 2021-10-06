package com.example.techmaster.dialog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.techmaster.HomeActivity;
import com.example.techmaster.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class Problem extends DialogFragment {

    View view;
    Spinner deficulty;
    TextView issueText;
    TextInputLayout office_num, office_holder, office_contact,description;
    ImageView capture, imageView;
    CircularProgressButton submit;

    Uri selectedimage= null;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    String key;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_problem,container,false);
       try {
           Init();

           ArrayAdapter<CharSequence> deficulties = ArrayAdapter.createFromResource(getContext(), R.array.deficulties, android.R.layout.simple_spinner_dropdown_item);
           deficulties.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
           deficulty.setAdapter(deficulties);
           Retrieve();
           deficulty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
               @Override
               public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                   issueText.setText(parent.getItemAtPosition(position).toString());
               }

               @Override
               public void onNothingSelected(AdapterView<?> parent) {
                   issueText.setText(null);
               }
           });

           submit.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                    try{
                        key = UUID.randomUUID().toString();

                        String date =new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                       if (Validator()) {
                           submit.startAnimation();
                           try {

                           HashMap _user = new HashMap();
                           _user.put("office_holder", office_holder.getEditText().getText().toString().trim());
                           _user.put("office_num", office_num.getEditText().getText().toString().trim());
                           _user.put("office_contact", office_contact.getEditText().getText().toString().trim());
                           _user.put("description", description.getEditText().getText().toString().trim());
                           _user.put("issue_relation", issueText.getText().toString().trim());
                               _user.put("date", date);
                               _user.put("uid", user.getUid());
                               _user.put("status", "unresolved");
                               _user.put("attended", "No");
                               _user.put("url", "null");

                               FirebaseDatabase.getInstance("https://techmaster-50be9-default-rtdb.firebaseio.com/")
                                       .getReference("issue").child(key).setValue(_user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull @NotNull Task<Void> task) {
                                       if (task.isSuccessful()) {
                                           if(selectedimage == null){
                                               submit.revertAnimation();
                                               Toast.makeText(getContext(), "Process successful.", Toast.LENGTH_SHORT).show();

                                           }else{  try {
                                               Upload(selectedimage);
                                           }catch (Exception e){

                                           }
                                           }
                                       } else {
                                           submit.revertAnimation();
                                           Toast.makeText(getContext(), "Process unsuccessfuk.", Toast.LENGTH_SHORT).show();
                                       }
                                   }
                               });

                           } catch (Exception e) {
                               submit.revertAnimation();
                               Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                           }

                       }
                    } catch (Exception e) {
                       submit.revertAnimation();
                       Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                   }
               }
           });

           capture.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                   intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                   startActivityForResult(intent, 2);
               }
           });
       }catch (Exception e){
           Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
       }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == HomeActivity.RESULT_OK)
        {

                        Bitmap bitmap;
                        selectedimage = data.getData();
                        BitmapFactory.Options biOptions = new BitmapFactory.Options();
                        bitmap = (Bitmap) data.getExtras().get("data");

                        imageView.setImageBitmap(bitmap);


        }


    }


    private void Upload(Uri uri){
            if(!TextUtils.isEmpty(uri.toString())){
                FirebaseStorage.getInstance().getReference("Issues").child(key).child("description").putFile(selectedimage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        if(taskSnapshot.getMetadata().getReference().getDownloadUrl() != null){
                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    FirebaseDatabase.getInstance("https://techmaster-50be9-default-rtdb.firebaseio.com/").getReference("issue").
                                            child(key).child("url").setValue(uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            dismiss();
                                            Toast.makeText(getContext(), "Submittion Successsful", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            submit.revertAnimation();
                                            Toast.makeText(getContext(), "Submittion Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
    }


    private TextWatcher contactValidate = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String _contact= office_contact.getEditText().getText().toString().trim();
            if(!Verifycontact(_contact)){
                office_contact.setError("Invalid contact");

            }else{
                office_contact.setError(null);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher nNumber = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String _confirmPassword = office_num.getEditText().getText().toString().trim();
            if(!TextUtils.isEmpty(_confirmPassword)){
                office_num.setError(null);
            }else{
                office_num.setError("Invalid ID");
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
            String _confirmPassword = office_holder.getEditText().getText().toString().trim();
            if(!TextUtils.isEmpty(_confirmPassword)){
                office_holder.setError(null);
            }else{
                office_holder.setError("Invalid names");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private boolean VerifyEmail(String email){

        Pattern emailpartten = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");

        return  !TextUtils.isEmpty(email) && emailpartten.matcher(email).matches();

    }

    private boolean Verifypassword(String passw){

        Pattern passwordpartten = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&*+=!])(?=\\S+$).{8,}");

        return  !TextUtils.isEmpty(passw) && passwordpartten.matcher(passw).matches();

    }

    private void Retrieve(){

        FirebaseDatabase.getInstance("https://techmaster-50be9-default-rtdb.firebaseio.com/").getReference("user")
                .child(user.getUid()).child("office").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    office_num.setEnabled(false);
                    office_contact.setEnabled(false);
                    office_contact.getEditText().setText(dataSnapshot.child("office_Contact").getValue().toString());
                    office_num.getEditText().setText(dataSnapshot.child("office_number").getValue().toString());
                  // office_contact.getEditText().setText(dataSnapshot.child("office_holder").getValue().toString());
//                    office_contact.getEditText().setText(dataSnapshot.child("office_Contact").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });
    }


        private boolean Verifycontact(String cntr){

        Pattern contactpartten = Pattern.compile("^(27|0)[6-8][0-8]{8}");

        return  !TextUtils.isEmpty(cntr) && contactpartten.matcher(cntr).matches();

    }

    private boolean Validator(){
        if(TextUtils.isEmpty(office_num.getEditText().getText().toString().trim())){
            return false;
        }

        if(TextUtils.isEmpty(office_holder.getEditText().getText().toString().trim())){
            return false;
        }
        if(TextUtils.isEmpty(office_contact.getEditText().getText().toString().trim())){
            return false;
        }

        if(TextUtils.isEmpty(issueText.getText().toString())){
            return false;
        }
        if(TextUtils.isEmpty(description.getEditText().getText().toString().trim())){
            return false;
        }
        return true;
    }

    private void Init(){
        deficulty = view.findViewById(R.id.IssueType);
        issueText = view.findViewById(R.id.issueText);

        office_contact = view.findViewById(R.id.officeContact);
        office_holder = view.findViewById(R.id.officeHolder);
        office_num = view.findViewById(R.id.officeNumber);
        description = view.findViewById(R.id.description);

        capture = view.findViewById(R.id.capture);
        imageView = view.findViewById(R.id.preview);
        submit = view.findViewById(R.id.submit);
    }
}
