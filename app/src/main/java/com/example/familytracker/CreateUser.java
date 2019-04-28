package com.example.familytracker;

import com.google.android.gms.maps.model.LatLng;

public class CreateUser {

    public String name , email , password , date , isSharing , code , imageUri  , userId , lat , lng;
    public CreateUser(){

    }

    public CreateUser(String name, String email, String password, String date, String isSharing, String code, String imageUri,String lat,String lng, String userId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.date = date;
        this.isSharing = isSharing;
        this.code = code;
        this.imageUri = imageUri;
        this.lat = lat;
        this.lng = lng;
        this.userId = userId;
    }

}
