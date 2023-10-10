package com.sliit.travelhelp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.sliit.travelhelp.Models.ModelLogin;
import com.sliit.travelhelp.Utility.APIServices;
import com.sliit.travelhelp.Utility.SharedPreferenceHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSignIn#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSignIn extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentSignIn() {
        // Required empty public constructor
    }


    public static FragmentSignIn newInstance(String param1, String param2) {
        FragmentSignIn fragment = new FragmentSignIn();
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
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);


        view.findViewById(R.id.fsi_btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText EDTUsername = view.findViewById(R.id.fsi_edt_username);
                EditText EDTPassword = view.findViewById(R.id.fsi_edt_password);
                FrameLayout LoadingUI = view.findViewById(R.id.sign_in_loading);

                String username = EDTUsername.getText().toString();
                String password = EDTPassword.getText().toString();



                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.length()<6){
                    Toast.makeText(getActivity(), "Password Should have minimum 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                LoadingUI.setVisibility(View.VISIBLE);

                Call<ResponseBody> call = APIServices.apiService.login(new ModelLogin(username, password));
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        LoadingUI.setVisibility(View.GONE);
                        try {
                                JSONObject _res = null;
                            if (response.isSuccessful()) {
                                _res = new JSONObject(response.body().string());
                                SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(getActivity());
                                sharedPreferenceHelper.saveToken(_res.getString("token"));

                                if (!sharedPreferenceHelper.getToken().isEmpty()) {
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);
                                }

                            } else {
                                if ( response.errorBody().contentLength() == 0) {
                                    Toast.makeText(getActivity(), "Request Failed! \n" + "UnAuthorized request.", Toast.LENGTH_SHORT).show();

                                } else {

                                    _res = new JSONObject(response.errorBody().string());
                                    Toast.makeText(getActivity(), "Request Failed! \n" + _res.getString("details"), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                                Toast.makeText(getActivity().getApplicationContext(), "System Error \n"+e.getMessage() , Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getActivity().getApplicationContext(), "Request Failed! " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });


        view.findViewById(R.id.fsi_txt_acc_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.fragmentSignUp);
            }
        });
        return view;
    }
}