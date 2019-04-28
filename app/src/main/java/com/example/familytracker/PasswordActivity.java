package com.example.familytracker;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordActivity extends AppCompatActivity {

    String email;
    EditText e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        e = (EditText) findViewById(R.id.editText);

        Intent intent = getIntent();
        if(intent!=null)
        {
            email = intent.getStringExtra("email");

        }
    }

    public void goToNamePickedActivity(View v)
    {
        if(e.getText().toString().length() > 6)
        {
            Intent intent = new Intent(PasswordActivity.this,NameActivity.class);
            intent.putExtra("email",email);
            intent.putExtra("password",e.getText().toString());
            startActivity(intent);
            finish();
        }
        else {
            Toast.makeText(getApplicationContext(),"Password length should be more than 6 characters",Toast.LENGTH_LONG).show();
        }
    }
}
