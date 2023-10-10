//This code is for reservation list view
package com.sliit.travelhelp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sliit.travelhelp.Models.ModelReservation;
import com.sliit.travelhelp.Models.ModelTrain;
import com.sliit.travelhelp.Utility.APIServices;
import com.sliit.travelhelp.Utility.Miscellaneous;
import com.sliit.travelhelp.Utility.ReservationListAdapter;
import com.sliit.travelhelp.Utility.SharedPreferenceHelper;

import org.bson.BsonArray;
import org.bson.BsonDateTime;
import org.bson.BsonDocument;
import org.bson.BsonObjectId;
import org.bson.BsonValue;
import org.bson.Document;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmnetListReservation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmnetListReservation extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String typeOfReservation = "upcoming";
    private RecyclerView recyclerView;
    private ReservationListAdapter adapter;

    public FragmnetListReservation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmnetListReservation.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmnetListReservation newInstance(String param1, String param2) {
        FragmnetListReservation fragment = new FragmnetListReservation();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragmnet_list_reservation, container, false);

        Button btnPast = view.findViewById(R.id.btn_reservation_list_past);
        Button btnAdd = view.findViewById(R.id.btn_reservation_list_add);
        TextView emptyText=  view.findViewById(R.id.reservation_list_empty_text);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.fragmentAddReservation);
            }
        });

        btnPast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (typeOfReservation.equals("upcoming")) {
                    typeOfReservation = "past";
                } else if (typeOfReservation.equals("past")) {
                    typeOfReservation = "draft";
                } else if (typeOfReservation.equals("draft")) {
                    typeOfReservation = "upcoming";
                }

                btnPast.setText(typeOfReservation);
                emptyText.setText("No "+ Miscellaneous.capitalizeFirstLetter( typeOfReservation) +" Reservations");
                getList(view);
            }
        });


        getList(view);

        return view;
    }

    public void getList(View view) {
        FrameLayout loadingLayout = view.findViewById(R.id.reservation_list_loading);
        LinearLayout emptyHolder = view.findViewById(R.id.reservation_list_holder);
        recyclerView = view.findViewById(R.id.rv_reservation_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new ReservationListAdapter( new ArrayList<>(), view);
        recyclerView.setAdapter(adapter);

        loadingLayout.setVisibility(View.VISIBLE);
        emptyHolder.setVisibility(View.GONE);
        Call<ResponseBody> call = APIServices.apiService.getReservation("Bearer " + SharedPreferenceHelper.token, typeOfReservation);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loadingLayout.setVisibility(View.GONE);
                try {
                    if (response.isSuccessful()) {

                        String bsonDocumentString = response.body().string();
                        BsonArray bsonDocumentList = BsonArray.parse(bsonDocumentString);

                        List<ModelReservation> reservationList = new ArrayList<>();

                        if (bsonDocumentList.isEmpty()) {
                            emptyHolder.setVisibility(View.VISIBLE);

                            adapter = new ReservationListAdapter(reservationList, view);
                            recyclerView.setAdapter(adapter);
                            return;
                        } else {
                            emptyHolder.setVisibility(View.GONE);

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



                            adapter = new ReservationListAdapter(reservationList, view);
                            recyclerView.setAdapter(adapter);
                        }


                    } else {
                        if (response.code() == 401 || response.code() == 403 || response.errorBody().contentLength() == 0) {
                            Toast.makeText(getActivity(), "Request Failed! \n" + "UnAuthorized request.", Toast.LENGTH_SHORT).show();

                        } else {
                            JSONObject __res = new JSONObject();
                            __res = new JSONObject(response.errorBody().string());
                            Toast.makeText(getActivity(), "Request Failed! \n" + __res.getString("details"), Toast.LENGTH_SHORT).show();
                        }

                    }
                } catch (Exception e) {

                    Toast.makeText(getActivity(), "System Error \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "Request Failed! " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}