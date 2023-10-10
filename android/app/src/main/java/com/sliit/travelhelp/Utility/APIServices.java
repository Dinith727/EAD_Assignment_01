package com.sliit.travelhelp.Utility;

import android.content.Context;
import android.os.NetworkOnMainThreadException;
import android.util.Log;

import com.sliit.travelhelp.Interfaces.AppAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIServices {
    public static AppAPI apiService;

    static {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:5299/api/").addConverterFactory(GsonConverterFactory.create()).build();
        apiService = retrofit.create(AppAPI.class);
    }



}
