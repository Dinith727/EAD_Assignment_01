package com.sliit.travelhelp.Interfaces;

import com.sliit.travelhelp.Models.ModelAddReservation;
import com.sliit.travelhelp.Models.ModelChangePassword;
import com.sliit.travelhelp.Models.ModelEditReservation;
import com.sliit.travelhelp.Models.ModelLogin;
import com.sliit.travelhelp.Models.ModelRegister;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.http.Query;

public interface AppAPI {

    @GET("traveller")
    Call<ResponseBody> getTraveller(@Header("Authorization") String token);

    @GET("trainschedule/active-trains")
    Call<ResponseBody> getTrains(@Header("Authorization") String token);

    @GET("admin/agents")
    Call<ResponseBody> getAgents(@Header("Authorization") String token);

    @GET("reservation/all")
    Call<ResponseBody> getReservation(@Header("Authorization") String token,@Query("type") String type);

    @GET("reservation")
    Call<ResponseBody> getReservationSingle(@Header("Authorization") String token, @Query("id") String id);

    @POST("traveller/add")
    Call<ResponseBody> addTraveller(@Body ModelRegister requestBody);

    @POST("reservation/add")
    Call<ResponseBody> addReservation(@Header("Authorization") String token,@Body ModelAddReservation requestBody);

    @POST("reservation/update")
    Call<ResponseBody> editReservation(@Header("Authorization") String token,@Body ModelEditReservation requestBody);

     @DELETE("reservation/delete")
    Call<ResponseBody> deleteReservation(@Header("Authorization") String token,@Query("id") String id);

    @POST("traveller/change-pw")
    Call<ResponseBody> changePW(@Body ModelChangePassword requestBody, @Header("Authorization") String token);

    @POST("traveller/deactivate")
    Call<ResponseBody> deactivate(@Header("Authorization") String token);

    @GET("admin/test")
    Call<ResponseBody> test();

    @POST("auth/login")
    Call<ResponseBody> login(@Body ModelLogin requestBody);
}

