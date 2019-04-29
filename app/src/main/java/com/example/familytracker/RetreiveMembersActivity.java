package com.example.familytracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RetreiveMembersActivity extends AppCompatActivity {

    ListView listView;
    FirebaseDatabase db , db1;
    FirebaseAuth auth;
    DatabaseReference reference, ref2 , ref3 , ref4;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    CreateUser user;

    /*public void displayAlart(final String li){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you shure, You want to delete this user?");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ref3 = FirebaseDatabase.getInstance().getReference().child("users");
                ref3.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot d : dataSnapshot.getChildren()){
                           String currentuser = auth.getCurrentUser().getUid();
                           Query q = d.child(currentuser).child("CircleMembers").child("circleMemberId").equals(d.child("userId"));
                           db1.getInstance().getReference().child("users");
                           ref4 = FirebaseDatabase.getInstance().getReference().child("users");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //Toast.makeText(getApplicationContext(),""+li,Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }*/

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retreive_members);

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        //getSupportActionBar().setElevation(0);
        View view = getSupportActionBar().getCustomView();
        TextView name = view.findViewById(R.id.name);

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
                    //Toast.makeText(getApplicationContext(),""+cId,Toast.LENGTH_SHORT);
                    ref2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot d : dataSnapshot.getChildren()){
                                //String a = String.valueOf(d.child(auth.getCurrentUser().getUid()).getValue());
                                String u = String.valueOf(d.child("userId").getValue());
                                if(u.equals(cId)){
                                     String name = String.valueOf(d.child("name").getValue());
                                     String email = String.valueOf(d.child("email").getValue());
                                     list.add(name);
                                }else{
                                    //Toast.makeText(getApplicationContext(),"Not equal",Toast.LENGTH_SHORT).show();
                                }
                            }
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String li = String.valueOf(listView.getItemAtPosition(position));
                                    //displayAlart(li);
                                }
                            });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        //SearchView searchView = (SearchView) findViewById(R.id.search);
        return true;
    }
}
