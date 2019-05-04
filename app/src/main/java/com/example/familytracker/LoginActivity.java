package com.example.familytracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karan.churi.PermissionManager.PermissionManager;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth auth;
    EditText e1 , e2;
  FirebaseUser user;
  TextView resetpw;
  PermissionManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
resetpw=(TextView)findViewById(R.id.password_reset);
        e1 = (EditText) findViewById(R.id.email);
        e2 = (EditText) findViewById(R.id.password);

        auth = FirebaseAuth.getInstance();


      auth = FirebaseAuth.getInstance();
      user = auth.getCurrentUser();
      if(user == null){
        setContentView(R.layout.activity_login);
        manager = new PermissionManager() {};
        manager.checkAndRequestPermissions(this);
      }else{
        Intent intent = new Intent(LoginActivity.this,UserLocationMainActivity.class);
        startActivity(intent);
        finish();
      }
        //auth = FirebaseAuth.getInstance();




    }
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    manager.checkResult(requestCode,permissions,grantResults);
    ArrayList<String> denied_permissions = manager.getStatus().get(0).denied;
    if(denied_permissions.isEmpty()){
      Toast.makeText(getApplicationContext(),"Permissions enabled",Toast.LENGTH_SHORT).show();        }
  }


    public void login(View v) {
      e1 = (EditText) findViewById(R.id.email);
      e2 = (EditText) findViewById(R.id.password);

      if ((e1.getText().toString().equals(null)) || (e2.getText().toString().equals(null)) || (e2.getText().length()<6) ) {

        Toast.makeText(getApplicationContext(), "Enter valid credentials", Toast.LENGTH_LONG).show();

      }


      else {
        auth.signInWithEmailAndPassword(e1.getText().toString(), e2.getText().toString())
          .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
              if (task.isSuccessful()) {
                //Toast.makeText(getApplicationContext(),"User Logged in Successfully",Toast.LENGTH_LONG).show();

                FirebaseUser user = auth.getCurrentUser();
                if (user.isEmailVerified()) {
                  Intent intent = new Intent(LoginActivity.this, UserLocationMainActivity.class);
                  startActivity(intent);
                  finish();

                } else {
                  Toast.makeText(getApplicationContext(), "Email is not verified yet.", Toast.LENGTH_SHORT).show();
                }
              } else {
                Toast.makeText(getApplicationContext(), "Wrong email or password", Toast.LENGTH_LONG).show();
              }
            }
          });
      }
    }

  public void signUp(View v){
    Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
    startActivity(intent);
  }

  public void resetpw(View v){
    startActivity(new Intent(LoginActivity.this,password_reset.class));

  }




}
