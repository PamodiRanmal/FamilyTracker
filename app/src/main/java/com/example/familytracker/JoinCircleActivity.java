package com.example.familytracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class JoinCircleActivity extends AppCompatActivity {

    Pinview pin;
    DatabaseReference reference , currentreference , circlereference;
    FirebaseUser user;
    FirebaseAuth auth;
    String current_user_id , join_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_circle);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("users");
        currentreference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        pin = (Pinview) findViewById(R.id.pinview);
        current_user_id = user.getUid();

    }

    public void submit(View v){
        Query query = reference.orderByChild("code").equalTo(pin.getValue());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    CreateUser createUser = null;
                    for(DataSnapshot childdss : dataSnapshot.getChildren()){
                        createUser = childdss.getValue(CreateUser.class);
                        join_user_id = createUser.userId;
                        circlereference = FirebaseDatabase.getInstance().getReference().child("users").child(join_user_id).child("CircleMembers");

                        CircleJoin circleJoin = new CircleJoin(current_user_id);
                        CircleJoin circleJoin1 = new CircleJoin(join_user_id);

                        circlereference.child(user.getUid()).setValue(circleJoin)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(getApplicationContext(),"User Joined Circle Succesfully",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    }
                }else {
                    Toast.makeText(getApplicationContext(),"Circle code is not present",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
