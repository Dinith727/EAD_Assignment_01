package com.sliit.travelhelp;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sliit.travelhelp.Models.ModelAddReservation;
import com.sliit.travelhelp.Models.ModelEditReservation;
import com.sliit.travelhelp.Models.ModelReservation;
import com.sliit.travelhelp.Models.ModelTrain;
import com.sliit.travelhelp.Utility.APIServices;
import com.sliit.travelhelp.Utility.DataHolder;
import com.sliit.travelhelp.Utility.Miscellaneous;
import com.sliit.travelhelp.Utility.ReservationListAdapter;
import com.sliit.travelhelp.Utility.SharedPreferenceHelper;

import org.bson.BsonArray;
import org.bson.BsonDateTime;
import org.bson.BsonDocument;
import org.bson.BsonObjectId;
import org.bson.BsonValue;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentReservationSummery#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentReservationSummery extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String resID;

    public FragmentReservationSummery() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentReservationSummery.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentReservationSummery newInstance(String param1, String param2) {
        FragmentReservationSummery fragment = new FragmentReservationSummery();
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
        View view = inflater.inflate(R.layout.fragment_reservation_summery, container, false);

        // Inside your Fragment class or Activity class, where you have access to the layout
        TextView txtDate = view.findViewById(R.id.txt_res_sum_date);
        TextView txtFrom = view.findViewById(R.id.txt_res_sum_from);
        TextView txtTo = view.findViewById(R.id.txt_res_sum_to);
        TextView txtAgent = view.findViewById(R.id.txt_res_sum_agent);
        TextView txtTrain = view.findViewById(R.id.txt_res_sum_train);
        TextView txtTrainClass = view.findViewById(R.id.txt_res_sum_train_class);
        TextView txtTrainPrice = view.findViewById(R.id.txt_res_sum_train_price);
        TextView txtAgentNote = view.findViewById(R.id.txt_res_sum_agent_note);

        Button btnCancel = view.findViewById(R.id.btn_res_sum_delete);
        Button btnEdit = view.findViewById(R.id.btn_res_sum_edit);
        Button btnDays = view.findViewById(R.id.btn_res_sum_days);
        Button btnSubmit = view.findViewById(R.id.btn_res_sum_submit);

        FrameLayout loadingLayout = view.findViewById(R.id.res_sum_loading);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModelEditReservation reservation = new ModelEditReservation();
                reservation.set_id(resID);
                reservation.setStatus("upcoming");

                Call<ResponseBody> call = APIServices.apiService.editReservation("Bearer " + SharedPreferenceHelper.token, reservation);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            JSONObject _res = null;
                            if (response.isSuccessful()) {
                                Toast.makeText(getActivity(), "Reservation Updated Successful!", Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(view).navigate(R.id.fragmnetListReservation);

                            } else {
                                if (response.code() == 401 || response.code() == 403 || response.errorBody().contentLength() == 0) {
                                    Toast.makeText(getActivity(), "Request Failed! \n" + "UnAuthorized request.", Toast.LENGTH_SHORT).show();

                                } else {

                                    _res = new JSONObject(response.errorBody().string());
                                    Toast.makeText(getActivity(), "Request Failed! \n"+_res.getString("details"), Toast.LENGTH_SHORT).show();
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
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to proceed?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Call<ResponseBody> call = APIServices.apiService.deleteReservation("Bearer " + SharedPreferenceHelper.token, resID);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                try {
                                    JSONObject _res = null;
                                    if (response.isSuccessful()) {
                                        Toast.makeText(getActivity(), "Reservation Delete Successful!", Toast.LENGTH_SHORT).show();
                                        Navigation.findNavController(view).navigate(R.id.fragmnetListReservation);

                                    } else {
                                        if (response.code() == 401 || response.code() == 403 || response.errorBody().contentLength() == 0) {
                                            Toast.makeText(getActivity(), "Request Failed! \n" + "UnAuthorized request.", Toast.LENGTH_SHORT).show();

                                        } else {

                                            _res = new JSONObject(response.errorBody().string());
                                            Toast.makeText(getActivity(), "Request Failed! \n"+_res.getString("details"), Toast.LENGTH_SHORT).show();
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
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Handle the "No" button click
                        // You can perform your desired action here, or simply close the dialog
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.fragmentEditReservation);
            }
        });


        loadingLayout.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = APIServices.apiService.getReservationSingle("Bearer " + SharedPreferenceHelper.token, DataHolder.getReservationId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loadingLayout.setVisibility(View.GONE);
                try {
                    if (response.isSuccessful()) {

                        String bsonDocumentString = response.body().string();
                        BsonDocument bsonDocument = BsonDocument.parse(bsonDocumentString);

                        BsonObjectId id = bsonDocument.get("_id").asObjectId();
                        BsonDateTime date = bsonDocument.get("date").asDateTime();
                        String str = bsonDocument.getString("agentNote").getValue();
                        String trainClass = bsonDocument.getString("trainClass").getValue();

                        resID = id.getValue().toString();
                        txtTrainClass.setText(trainClass);

                        if (!bsonDocument.get("train").asArray().isEmpty()) {
                            BsonDocument train = bsonDocument.get("train").asArray().get(0).asDocument();
                            txtFrom.setText(train.getString("from").getValue());
                            txtTo.setText(train.getString("to").getValue());
                            txtTrain.setText(train.getString("trainName").getValue());


                            txtTrainPrice.setText("Rs."+train.get("price").asDocument().getInt32(trainClass.equals("Business")?"bussiness":"economic").getValue());

                        }
                        if (!bsonDocument.get("agent").asArray().isEmpty()) {
                            BsonDocument agent = bsonDocument.get("agent").asArray().get(0).asDocument();
                            txtAgent.setText(agent.getString("username").getValue());

                        }

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");



                        txtDate.setText(dateFormat.format(new Date(date.getValue())));

                        LocalDate localDate  = new Date(date.getValue()).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                        btnDays.setText(Miscellaneous.getDaysLeftString(localDate));


                        txtAgentNote.setText(str);

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


        return view;
    }
}