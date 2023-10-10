package com.sliit.travelhelp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sliit.travelhelp.Models.ModelChangePassword;
import com.sliit.travelhelp.Utility.APIServices;
import com.sliit.travelhelp.Utility.SharedPreferenceHelper;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentChangePassoword#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentChangePassoword extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentChangePassoword() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentChangePassoword.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentChangePassoword newInstance(String param1, String param2) {
        FragmentChangePassoword fragment = new FragmentChangePassoword();
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
        View view = inflater.inflate(R.layout.fragment_change_passoword, container, false);

        view.findViewById(R.id.cpi_btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView TVCurrentPW = view.findViewById(R.id.cpi_edt_old_password);
                TextView TVNewPW = view.findViewById(R.id.cpi_edt_password);
                TextView TVConfirmPW = view.findViewById(R.id.cpi_edt_password_confirm);

                FrameLayout LoadingLayout = view.findViewById(R.id.res_sum_loading);


                String currentPWText = TVCurrentPW.getText().toString().trim();
                String newPWText = TVNewPW.getText().toString().trim();
                String confirmPWText = TVConfirmPW.getText().toString().trim();

                if (currentPWText.isEmpty() || newPWText.isEmpty() || confirmPWText.isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!newPWText.equals(confirmPWText)) {
                    Toast.makeText(getActivity(), "The password is not match with confirm password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (newPWText.length() < 6) {
                    Toast.makeText(getActivity(), "Password Should have minimum 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                ModelChangePassword _data = new ModelChangePassword(currentPWText,newPWText);
                LoadingLayout.setVisibility(View.VISIBLE);
                Call<ResponseBody> call = APIServices.apiService.changePW(_data, "Bearer "+SharedPreferenceHelper.token);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        LoadingLayout.setVisibility(View.GONE);
                        try {
                            JSONObject _res = null;
                            if (response.isSuccessful()) {
                                Toast.makeText(getActivity(), "Password Change Successful!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);

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


        view.findViewById(R.id.cpi_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.fragmentAccountDetails);
            }
        });
        return view;
    }
}