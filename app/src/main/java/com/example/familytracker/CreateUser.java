package com.example.familytracker;

import com.google.android.gms.maps.model.LatLng;

public class CreateUser {

    public String name , email , password , date , isSharing , code , imageUri  , userId , lat , lng;
    public CreateUser(){

    }
    
    /////333333

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIsSharing() {
        return isSharing;
    }

    public void setIsSharing(String isSharing) {
        this.isSharing = isSharing;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
