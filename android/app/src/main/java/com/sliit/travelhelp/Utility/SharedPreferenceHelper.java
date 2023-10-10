package com.sliit.travelhelp.Utility;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.content.SharedPreferences;

import com.sliit.travelhelp.MainActivity;
import com.sliit.travelhelp.Models.ModelUser;

public class SharedPreferenceHelper {
    public static String token;
    public static String id;
    public static String fullName;
    public static String NIC;
    public static String email;
    private  SharedPreferences.Editor editor ;
    private  SharedPreferences sharedPreferences;

    public SharedPreferenceHelper(Context context) {
        sharedPreferences = context.getSharedPreferences("travel_helper", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public  void saveToken(String _token){
        editor.putString("token", _token);
        token =_token;
        editor.apply();
    }
    public  void saveUser(ModelUser user){
        editor.putString("id", user.getId());
        editor.putString("fullName", user.getFullName());
        editor.putString("NIC", user.getNIC());
        editor.putString("email", user.getEmail());

        id=user.getId();
        fullName=user.getFullName();
        NIC=user.getNIC();
        email=user.getEmail();

        editor.apply();
    }
    public  String getToken(){
        token = sharedPreferences.getString("token", "");
        return token;
    }
    public  ModelUser getUser(){
        id = sharedPreferences.getString("id", "");
        fullName = sharedPreferences.getString("fullName", "");
        NIC = sharedPreferences.getString("NIC", "");
        email = sharedPreferences.getString("email", "");
        ModelUser user = new ModelUser(id,email,NIC,fullName);
        return user;
    }

    public  void deleteToken(){
        editor.putString("token", null);
        token =null;
        editor.apply();
    }
    public  void deleteUser(){
        editor.putString("id", null);
        editor.putString("fullName", null);
        editor.putString("NIC", null);
        editor.putString("email", null);

        id=null;
        fullName=null;
        NIC=null;
        email=null;

        editor.apply();
    }

}
