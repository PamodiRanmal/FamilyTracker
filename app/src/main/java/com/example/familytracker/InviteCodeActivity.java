package com.example.familytracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class InviteCodeActivity extends AppCompatActivity {

    String name, email, password, date, isSharing, code;
    Uri imageUri;
    TextView t1;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    ProgressDialog progressDialog;
    String userId;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_code);

        t1 = (TextView) findViewById(R.id.textview);
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        reference = FirebaseDatabase.getInstance().getReference().child("users");
        storageReference = FirebaseStorage.getInstance().getReference().child("user_images");
        Intent intent = getIntent();
        if (intent != null) {
            name = intent.getStringExtra("name");
            email = intent.getStringExtra("email");
            password = intent.getStringExtra("password");
            date = intent.getStringExtra("date");
            isSharing = intent.getStringExtra("isSharing");
            code = intent.getStringExtra("code");
            imageUri = intent.getParcelableExtra("imageUri");
        }

        t1.setText(code);
    }

    public void registerUser(View v) {
        progressDialog.setMessage("Please wait while we creating an account for you.");
        progressDialog.show();
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                user = auth.getCurrentUser();
                                CreateUser createUser = new CreateUser(name,email,password, date,"false",code,"na",null,null,user.getUid());
                                user = auth.getCurrentUser();
                                userId = user.getUid();

                                reference.child(userId).setValue(createUser)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    StorageReference sr = storageReference.child(user.getUid() + ".jpg");
                                                    sr.putFile(imageUri)
                                                            .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                                    if(task.isSuccessful()){
                                                                        String download_image_path = task.getResult().getUploadSessionUri().toString();
                                                                        reference.child(user.getUid()).child("imageUri").setValue(download_image_path)
                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        if(task.isSuccessful()){
                                                                                            progressDialog.dismiss();
                                                                                            //Toast.makeText(getApplicationContext(),"Email sent for varification",Toast.LENGTH_SHORT).show();
                                                                                            //finish();
                                                                                            sendVarificationEmail();
                                                                                            //Intent intent = new Intent(InviteCodeActivity.this,MainActivity.class);
                                                                                            //startActivity(intent);
                                                                                        }else {
                                                                                            Toast.makeText(getApplicationContext(),"An error occured while creating an account",Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    }
                                                                                });
                                                                    }
                                                                }
                                                            });

                                                }
                                                else {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getApplicationContext(),"Could not insert values in a database",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }
                    });

    }

    public void sendVarificationEmail(){
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Email Sent for varification",Toast.LENGTH_SHORT).show();
                            finish();
                            auth.signOut();
                        }else{
                            Toast.makeText(getApplicationContext(),"Could not send email",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
