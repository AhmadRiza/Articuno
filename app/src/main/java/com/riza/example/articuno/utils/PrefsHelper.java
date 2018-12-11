package com.riza.example.articuno.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.riza.example.articuno.model.User;

public class PrefsHelper {

    private SharedPreferences prefs;


    public PrefsHelper(Context context) {
        prefs = context.getSharedPreferences("Articuno", Context.MODE_PRIVATE);
    }

    public void putString(String key, String value){
        prefs.edit().putString(key,value).apply();
    }

    public void putInt(String key, int value){
        prefs.edit().putInt(key,value).apply();
    }

    public int getInt(String key){
        return prefs.getInt(key, -1);
    }

    public String  getString(String key){
        return prefs.getString(key, null);
    }

    public void saveUser(User user){

        putString("id",user.getId());
        putString("nama",user.getNama());
        putString("token",user.getApiToken());
        putString("alamat",user.getAlamat());
        putString("pass",user.getPassword());
        putString("telp",user.getNoTelp());
        putString("kerja",user.getPekerjaan());
        putString("gender",user.getGender());
        putString("email",user.getEmail());

    }

    public User getUser(){

        User user = new User();
        user.setId(getString("id"));
        user.setNama(getString("nama"));
        user.setApiToken(getString("token"));
        user.setAlamat(getString("alamat"));
        user.setPassword(getString("pass"));
        user.setNoTelp(getString("telp"));
        user.setPekerjaan(getString("kerja"));
        user.setGender(getString("gender"));
        user.setEmail(getString("email"));

        return user;
    }

    public void logout(){
        prefs.edit().clear().apply();
    }



}
