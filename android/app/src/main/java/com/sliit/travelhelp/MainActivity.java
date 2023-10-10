package com.sliit.travelhelp;

import static com.google.android.material.internal.ContextUtils.getActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sliit.travelhelp.Models.ModelLogin;
import com.sliit.travelhelp.Models.ModelReservation;
import com.sliit.travelhelp.Models.ModelTrain;
import com.sliit.travelhelp.Models.ModelUser;
import com.sliit.travelhelp.Utility.APIServices;
import com.sliit.travelhelp.Utility.Miscellaneous;
import com.sliit.travelhelp.Utility.ReservationListAdapter;
import com.sliit.travelhelp.Utility.ReservationListAdapterMain;
import com.sliit.travelhelp.Utility.SharedPreferenceHelper;

import org.bson.BsonArray;
import org.bson.BsonDateTime;
import org.bson.BsonDocument;
import org.bson.BsonObjectId;
import org.bson.BsonValue;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ReservationListAdapterMain adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar_main_activity);
        toolbar.setTitle("Travel Help");
        setSupportActionBar(toolbar);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ReservationActivity.class);
                startActivity(intent);
            }
        };
        getList(listener);

        findViewById(R.id.btn_home_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ReservationActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_home) {
            // Handle the menu item click
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_account) {
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_reservation) {
            Intent intent = new Intent(this, ReservationActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }

    public void getList(View.OnClickListener listener) {
        LinearLayout layout = findViewById(R.id.home_loading);
        layout.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = APIServices.apiService.getReservation("Bearer " + SharedPreferenceHelper.token, "upcoming");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        layout.setVisibility(View.GONE);

                        String bsonDocumentString = response.body().string();
                        BsonArray bsonDocumentList = BsonArray.parse(bsonDocumentString);

                        List<ModelReservation> reservationList = new ArrayList<>();

                        if (bsonDocumentList.isEmpty()) {
                            findViewById(R.id.home_list_place_holder).setVisibility(View.VISIBLE);
                            return;
                        }

                        for (BsonValue bsonValue : bsonDocumentList) {
                            if (bsonValue instanceof BsonDocument) {
                                BsonDocument bsonDocument = bsonValue.asDocument();
                                BsonObjectId id = bsonDocument.get("_id").asObjectId();
                                BsonDateTime date = bsonDocument.get("date").asDateTime();
                                String str = bsonDocument.getString("status").getValue();

                                ModelReservation reservation = new ModelReservation();

                                if (!bsonDocument.get("train").asArray().isEmpty()) {

                                    BsonDocument train = bsonDocument.get("train").asArray().get(0).asDocument();
                                    reservation.setTrain(new ModelTrain(train.getString("from").getValue(), train.getString("to").getValue()));
                                } else {
                                    reservation.setTrain(new ModelTrain("", ""));

                                }
                                reservation.setId(id);
                                reservation.setDate(date);
                                reservation.setStatus(str);

                                reservationList.add(reservation);
                            }
                        }


                        recyclerView = findViewById(R.id.rv_upcoming_list);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


                        adapter = new ReservationListAdapterMain(reservationList, listener);
                        recyclerView.setAdapter(adapter);


                    } else {
                        layout.setVisibility(View.GONE);

                        if (response.code() == 401 || response.code() == 403 || response.errorBody().contentLength() == 0) {
                            Toast.makeText(getApplicationContext(), "Request Failed! \n" + "UnAuthorized request.", Toast.LENGTH_SHORT).show();

                        } else {
                            JSONObject __res = new JSONObject();
                            __res = new JSONObject(response.errorBody().string());
                            Toast.makeText(getApplicationContext(), "Request Failed! \n" + __res.getString("details"), Toast.LENGTH_SHORT).show();
                        }

                    }
                } catch (Exception e) {
                    layout.setVisibility(View.GONE);

                    Toast.makeText(getApplicationContext(), "System Error \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                layout.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Request Failed! " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}