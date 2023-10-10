//this code is for add reservations
package com.sliit.travelhelp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.sliit.travelhelp.Models.ModelAddReservation;
import com.sliit.travelhelp.Utility.APIServices;
import com.sliit.travelhelp.Utility.DataHolder;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAddReservation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAddReservation extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private boolean is_call_1_done = false;
    private boolean is_call_2_done = false;
    ArrayList<String> trainIDList = new ArrayList<>();
    ArrayList<String> agentIdList = new ArrayList<>();

    private String trainID = null;
    private String agentID = null;
    private String classSelected = null;

    public FragmentAddReservation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentAddReservation.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAddReservation newInstance(String param1, String param2) {
        FragmentAddReservation fragment = new FragmentAddReservation();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_reservation, container, false);

        LinearLayout loadingSpinner = view.findViewById(R.id.rsa_loading);

        Spinner spinnerTrain = view.findViewById(R.id.rsa_spin_train);
        Spinner spinnerAgent = view.findViewById(R.id.rsa_spin_agent);
        Spinner spinnerClass = view.findViewById(R.id.rsa_spin_train_class);
        EditText edtDate = view.findViewById(R.id.rsa_edt_date);

        loadingSpinner.setVisibility(View.VISIBLE);


        Call<ResponseBody> call1 = APIServices.apiService.getTrains("Bearer " + SharedPreferenceHelper.token);
        call1.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        is_call_1_done = true;
                        loadingSpinner.setVisibility(is_call_1_done && is_call_2_done ? View.GONE : View.VISIBLE);

                        String bsonDocumentString = response.body().string();
                        BsonDocument _bsonDocument = BsonDocument.parse(bsonDocumentString);
                        BsonArray bsonDocumentList = _bsonDocument.getArray("data");


                        ArrayList<String> trainList = new ArrayList<>();


                        for (BsonValue bsonValue : bsonDocumentList) {
                            if (bsonValue instanceof BsonDocument) {
                                BsonDocument bsonDocument = bsonValue.asDocument();
                                String id = bsonDocument.getString("_id").getValue();
                                String str = bsonDocument.getString("trainName").getValue();
                                String strFrom = bsonDocument.getString("from").getValue();
                                String strTo = bsonDocument.getString("to").getValue();

                                trainList.add(str + " [" + strFrom + " to " + strTo + "]" + "\n");
                                trainIDList.add(id);

                            }
                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, trainList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerTrain.setAdapter(adapter);


                    } else {
                        loadingSpinner.setVisibility(View.GONE);

                        if (response.code() == 401 || response.code() == 403 || response.errorBody().contentLength() == 0) {
                            Toast.makeText(getActivity(), "Request Failed! \n" + "UnAuthorized request.", Toast.LENGTH_SHORT).show();

                        } else {
                            JSONObject __res = new JSONObject();
                            __res = new JSONObject(response.errorBody().string());
                            Toast.makeText(getActivity(), "Request Failed! \n" + __res.getString("details"), Toast.LENGTH_SHORT).show();
                        }

                    }
                } catch (Exception e) {
                    loadingSpinner.setVisibility(View.GONE);

                    Toast.makeText(getActivity(), "System Error \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "Request Failed! " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        Call<ResponseBody> call2 = APIServices.apiService.getAgents("Bearer " + SharedPreferenceHelper.token);
        call2.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        is_call_2_done = true;
                        loadingSpinner.setVisibility(is_call_1_done && is_call_2_done ? View.GONE : View.VISIBLE);


                        String bsonDocumentString = response.body().string();
                        BsonDocument _bsonDocument = BsonDocument.parse(bsonDocumentString);
                        BsonArray bsonDocumentList = _bsonDocument.getArray("data");


                        ArrayList<String> agentList = new ArrayList<>();


                        for (BsonValue bsonValue : bsonDocumentList) {
                            if (bsonValue instanceof BsonDocument) {
                                BsonDocument bsonDocument = bsonValue.asDocument();
                                String id = bsonDocument.getString("_id").getValue();
                                String str = bsonDocument.getString("username").getValue();

                                agentList.add(str);
                                agentIdList.add(id);

                            }
                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, agentList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerAgent.setAdapter(adapter);


                    } else {
                        loadingSpinner.setVisibility(View.GONE);

                        if (response.code() == 401 || response.code() == 403 || response.errorBody().contentLength() == 0) {
                            Toast.makeText(getActivity(), "Request Failed! \n" + "UnAuthorized request.", Toast.LENGTH_SHORT).show();

                        } else {
                            JSONObject __res = new JSONObject();
                            __res = new JSONObject(response.errorBody().string());
                            Toast.makeText(getActivity(), "Request Failed! \n" + __res.getString("details"), Toast.LENGTH_SHORT).show();
                        }

                    }
                } catch (Exception e) {
                    loadingSpinner.setVisibility(View.GONE);

                    Toast.makeText(getActivity(), "System Error \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "Request Failed! " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        spinnerTrain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTrain = parent.getItemAtPosition(position).toString();
                if (!trainIDList.isEmpty()) trainID = trainIDList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where nothing is selected
            }
        });

        spinnerAgent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTrain = parent.getItemAtPosition(position).toString();
                // Handle the selected train
                if (!agentIdList.isEmpty()) agentID = agentIdList.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where nothing is selected
            }
        });
        spinnerClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                classSelected = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where nothing is selected
            }
        });


        view.findViewById(R.id.rsa_btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy/MM/dd");
                    inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    Date date = null;
                    date = inputFormat.parse(edtDate.getText().toString());

                    SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    outputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                    String formattedDate = outputFormat.format(date);

                    ModelAddReservation reservation = new ModelAddReservation(formattedDate, trainID, agentID, classSelected);


                    Call<ResponseBody> call = APIServices.apiService.addReservation("Bearer " + SharedPreferenceHelper.token, reservation);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                JSONObject _res = null;
                                if (response.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Reservation added Successful!", Toast.LENGTH_SHORT).show();

                                    _res = new JSONObject(response.body().string());

                                    DataHolder.setReservationId(_res.getJSONObject("data").getString("_id"));
                                    Navigation.findNavController(view).navigate(R.id.fragmentReservationSummery);

                                } else {
                                    if (response.code() == 401 || response.code() == 403 || response.errorBody().contentLength() == 0) {
                                        Toast.makeText(getActivity(), "Request Failed! \n" + "UnAuthorized request.", Toast.LENGTH_SHORT).show();

                                    } else {

                                        _res = new JSONObject(response.errorBody().string());
                                        Toast.makeText(getActivity(), "Request Failed! \n", Toast.LENGTH_SHORT).show();
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

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }
}