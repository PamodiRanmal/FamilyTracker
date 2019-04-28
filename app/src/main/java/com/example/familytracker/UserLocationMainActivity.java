package com.example.familytracker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class UserLocationMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    FirebaseAuth auth;
    FirebaseUser user;
    GoogleApiClient client;
    LocationRequest request;
    LatLng latLng , latLng1;
    DatabaseReference reference;
    String current_user_name;
    String current_user_email;
    String current_user_imageurl;
    String Current_user_code;
    String current_user_location;
    TextView t1_current_name,t2_current_email;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_location_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        t1_current_name = header.findViewById(R.id.titletext);
        t2_current_email = header.findViewById(R.id.nametext);
        iv = header.findViewById(R.id.imageView);
        reference = FirebaseDatabase.getInstance().getReference().child("users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                current_user_name = dataSnapshot.child(user.getUid()).child("name").getValue(String.class);
                current_user_email = dataSnapshot.child(user.getUid()).child("email").getValue(String.class);
                current_user_imageurl = dataSnapshot.child(user.getUid()).child("imageUri").getValue(String.class);

                t1_current_name.setText(current_user_name);
                t2_current_email.setText(current_user_email);

                Current_user_code = dataSnapshot.child(user.getUid()).child("code").getValue(String.class);
                Picasso.get().load(current_user_imageurl).into(iv);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_location_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_joincircle) {
            Intent intent = new Intent(UserLocationMainActivity.this,JoinCircleActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_mycircle) {
            Intent intent = new Intent(UserLocationMainActivity.this,MyCircleActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_joinedcircle) {

        } else if (id == R.id.nav_invitemembers) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT,current_user_name + "Invite you to Share your location. You can join circle by using "+Current_user_code + " invite code");
            startActivity(intent.createChooser(intent,"Share using : "));
        } else if (id == R.id.nav_shareLocation){
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT,"My Location is : "+"https://www.google.com/maps/@"+latLng.latitude+","+latLng.longitude+",17z");
            startActivity(intent.createChooser(intent,"Share using : "));
        }else if (id == R.id.nav_signOut) {
            FirebaseUser user = auth.getCurrentUser();
            if (user != null) {
                auth.signOut();
                Intent intent = new Intent(UserLocationMainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        client = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        client.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        request = new LocationRequest().create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(1000);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(client, request, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if(location == null){
            Toast.makeText(getApplicationContext(),"Could not get location",Toast.LENGTH_SHORT).show();
        }else{
            latLng = new LatLng(location.getLatitude(),location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Location");
            mMap.addMarker(markerOptions);
            String s = String.valueOf(location.getLatitude());
            String sl = String.valueOf(location.getLongitude());
                reference.child(auth.getCurrentUser().getUid()).child("lat").setValue(s);
                reference.child(auth.getCurrentUser().getUid()).child("lng").setValue(sl);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null) {
                        String uid;
                        uid = String.valueOf(dataSnapshot.child(auth.getUid()).getValue());
                        CreateUser cu = dataSnapshot.getValue(CreateUser.class);

                        for(DataSnapshot dss : dataSnapshot.getChildren()){
                            String id = String.valueOf(dataSnapshot.child(auth.getCurrentUser().getUid()).child("CircleMembers").child("tTSb31kTG5UHZuUTbhDpY7V34P82").child("circleMemberId").getValue());
                            String userId = dss.child("userId").getValue(String.class);
                            if(userId.equals(id)){
                                Double la = Double.parseDouble(String.valueOf(dataSnapshot.child(userId).child("lat").getValue()));
                                Double lo = Double.parseDouble(String.valueOf(dataSnapshot.child(userId).child("lng").getValue()));
                                latLng = new LatLng(la,lo);
                                MarkerOptions memberMarkerOptions = new MarkerOptions();
                                memberMarkerOptions.position(latLng);
                                memberMarkerOptions.title("Friends Location");
                                memberMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                                mMap.addMarker(memberMarkerOptions);

                            }else {

                            }
                        }
                    }else {
                        Toast.makeText(getBaseContext(), "NULLL", Toast.LENGTH_SHORT).show();
                    }
                    }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
