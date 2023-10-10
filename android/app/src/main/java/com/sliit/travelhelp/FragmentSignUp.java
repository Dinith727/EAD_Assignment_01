package com.sliit.travelhelp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sliit.travelhelp.Models.ModelRegister;
import com.sliit.travelhelp.Utility.APIServices;
import com.sliit.travelhelp.Utility.RegexHelper;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSignUp#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSignUp extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentSignUp() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSignUp.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSignUp newInstance(String param1, String param2) {
        FragmentSignUp fragment = new FragmentSignUp();
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
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        view.findViewById(R.id.fsu_btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView TVEmail = view.findViewById(R.id.fsu_edt_username);
                TextView TVFullName = view.findViewById(R.id.fsu_edt_full_name);
                TextView TVNIC = view.findViewById(R.id.fsu_edt_nic);
                TextView TVPassword = view.findViewById(R.id.fsu_edt_password);
                FrameLayout LoadingComp = view.findViewById(R.id.res_sum_loading);


                String emailText = TVEmail.getText().toString().trim();
                String fullNameText = TVFullName.getText().toString().trim();
                String nicText = TVNIC.getText().toString().trim();
                String passwordText = TVPassword.getText().toString().trim();

                if (emailText.isEmpty() || fullNameText.isEmpty() || nicText.isEmpty() || passwordText.isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }if(passwordText.length()<6){
                    Toast.makeText(getActivity(), "Password Should have minimum 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!( RegexHelper.matchNicOld(nicText) || RegexHelper.matchNicNew(nicText))){
                    Toast.makeText(getActivity(), "Invalid NIC format, please check and try again.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!RegexHelper.matchEmail(emailText) ){
                    Toast.makeText(getActivity(), "Invalid email format, please check and try again.", Toast.LENGTH_SHORT).show();
                    return;
                }


                ModelRegister user = new ModelRegister(nicText, fullNameText, passwordText, emailText);
                LoadingComp.setVisibility(View.VISIBLE);
                Call<ResponseBody> call = APIServices.apiService.addTraveller(user);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        LoadingComp.setVisibility(View.GONE);

                        try {
                            JSONObject _res = null;
                            if (response.isSuccessful()) {
                                Toast.makeText(getActivity(), "Registration Successful!", Toast.LENGTH_SHORT).show();

                                Navigation.findNavController(view).navigate(R.id.fragmentSignIn);

                            } else {
                                if (response.code() == 401 || response.code() == 403 || response.errorBody().contentLength() == 0) {
                                    Toast.makeText(getActivity(), "Request Failed! \n" + "UnAuthorized request.", Toast.LENGTH_SHORT).show();

                                } else {

                                    _res = new JSONObject(response.errorBody().string());
                                    Toast.makeText(getActivity(), "Request Failed! \n" + _res.getString("details"), Toast.LENGTH_SHORT).show();
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
        view.findViewById(R.id.fsu_already_acc_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.fragmentSignIn);
            }
        });
        return view;
    }
}