package com.example.familytracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RetreiveMembersActivity extends AppCompatActivity {

    ListView listView;
    FirebaseDatabase db;
    FirebaseAuth auth;
    DatabaseReference reference, ref2;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    CreateUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retreive_members);

        auth = FirebaseAuth.getInstance();
        user = new CreateUser();
        listView = (ListView) findViewById(R.id.listView);
        db = FirebaseDatabase.getInstance();
        reference = db.getInstance().getReference().child("users").child(auth.getCurrentUser().getUid()).child("CircleMembers");
        list = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this,R.layout.activity_userinfo,R.id.userinfo,list);
        ref2 = db.getInstance().getReference().child("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dss : dataSnapshot.getChildren()){
                    CreateUser members = dss.getValue(CreateUser.class);
                    String i = String.valueOf(dss.getValue());
                    String a = String.valueOf(dss.child(auth.getCurrentUser().getUid()).getKey());
                    final String cId = String.valueOf(dss.child("circleMemberId").getValue());
                    ref2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot d : dataSnapshot.getChildren()){
                                //String a = String.valueOf(d.child(auth.getCurrentUser().getUid()).getValue());
                                String u = String.valueOf(d.child("userId").getValue());
                                if(u.equals(cId)){
                                     String name = String.valueOf(d.child("name").getValue());
                                     String email = String.valueOf(d.child("email").getValue());
                                     list.add(name + " : " + email);
                                }else{
                                    //Toast.makeText(getApplicationContext(),"Not equal",Toast.LENGTH_SHORT).show();
                                }
                            }
                            listView.setAdapter(adapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
