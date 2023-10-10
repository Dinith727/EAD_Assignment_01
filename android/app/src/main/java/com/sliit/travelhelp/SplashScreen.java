package com.sliit.travelhelp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.sliit.travelhelp.Models.ModelUser;
import com.sliit.travelhelp.Utility.APIServices;
import com.sliit.travelhelp.Utility.Miscellaneous;
import com.sliit.travelhelp.Utility.SharedPreferenceHelper;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {
    private static final int SPLASH_DISPLAY_TIME = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Animation animationUp = AnimationUtils.loadAnimation(this, R.anim.translate_animation);

        // Find the object to animate
        View animatedObject = findViewById(R.id.splash_image);

        // Apply the animation to the object
        animatedObject.startAnimation(animationUp);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the main activity


                // Close this activity


                SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(getApplicationContext());

                if (sharedPreferenceHelper.getToken().isEmpty()) {
                    Intent intent = new Intent(getApplicationContext(), Authentication.class);
                    startActivity(intent);
                } else {
                    try {


                    Call<ResponseBody> call = APIServices.apiService.getTraveller("Bearer " + SharedPreferenceHelper.token);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                JSONObject _res = null;
                                if (response.isSuccessful()) {
                                    _res = new JSONObject(response.body().string());
                                    JSONObject data = _res.getJSONObject("data");
                                    ModelUser user = new ModelUser(data.getString("_id"), data.getString("email"), data.getString("nic"), data.getString("fullName"));



                                    sharedPreferenceHelper.saveUser(user);

                                    Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                                    startActivity(mainIntent);


                                } else {
                                    if (response.code() == 401 || response.code() == 403 || response.errorBody().contentLength() == 0) {
                                        Toast.makeText(getApplicationContext(), "Request Failed! \n" + "UnAuthorized request.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), Authentication.class);
                                        startActivity(intent);
                                        return;
                                    } else {

                                        _res = new JSONObject(response.errorBody().string());
                                        Toast.makeText(getApplicationContext(), "Request Failed! \n" + _res.getString("details"), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "System Error \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }


                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Request Failed! " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(), "System Error \n" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
                finish();

            }
        }, SPLASH_DISPLAY_TIME);
    }
}