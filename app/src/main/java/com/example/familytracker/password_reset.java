package com.example.familytracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class password_reset extends AppCompatActivity {



EditText userEmail;
Button sendlink;
ProgressBar progressBar;
  FirebaseAuth firebaseAuth;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_password_reset);

    userEmail=findViewById(R.id.password_reset_email);
    sendlink=findViewById(R.id.send_link_button);
    progressBar=findViewById(R.id.progressbar);


    firebaseAuth =FirebaseAuth.getInstance();
    sendlink.setOnClickListener(new View.OnClickListener() {
  @Override
  public void onClick(View v) {

progressBar.setVisibility(View.VISIBLE);

    firebaseAuth.sendPasswordResetEmail(userEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
      @Override
      public void onComplete(@NonNull Task<Void> task) {

if(task.isSuccessful()){
  progressBar.setVisibility(View.GONE);

  Toast.makeText(password_reset.this, "password reset link was sent to your email", Toast.LENGTH_LONG).show();

}


else {

  Toast.makeText(password_reset.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();



}

      }
    });

  }
});



  }
}
