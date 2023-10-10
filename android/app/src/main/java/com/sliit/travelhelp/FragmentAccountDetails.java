package com.sliit.travelhelp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sliit.travelhelp.Models.ModelLogin;
import com.sliit.travelhelp.Utility.APIServices;
import com.sliit.travelhelp.Utility.Miscellaneous;
import com.sliit.travelhelp.Utility.SharedPreferenceHelper;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAccountDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAccountDetails extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentAccountDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentAccountDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAccountDetails newInstance(String param1, String param2) {
        FragmentAccountDetails fragment = new FragmentAccountDetails();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_account_details, container, false);

        TextView greetingText = view.findViewById(R.id.tv_account_greeting);
        greetingText.setText(Miscellaneous.getGreeting());

        TextView TVEmail=view.findViewById(R.id.txt_account_email);
        TextView TVFullName=view.findViewById(R.id.txt_account_full_name);
        TextView TVNIC=view.findViewById(R.id.txt_account_nic_number);

        TVFullName.setText(SharedPreferenceHelper.fullName);
        TVEmail.setText(SharedPreferenceHelper.email);
        TVNIC.setText(SharedPreferenceHelper.NIC);


        //password change
        view.findViewById(R.id.btn_account_change_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.fragmentChangePassoword);
            }
        });
        //logout
        view.findViewById(R.id.btn_account_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to logout?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(getActivity());
                        sharedPreferenceHelper.deleteToken();
                        sharedPreferenceHelper.deleteUser();
                        Intent intent = new Intent(getActivity(), Authentication.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();



            }
        });
        //deactivate user
        view.findViewById(R.id.btn_account_deactivate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to deactivate?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Call<ResponseBody> call = APIServices.apiService.deactivate("Bearer "+SharedPreferenceHelper.token);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                try {
                                    JSONObject _res = null;
                                    if (response.isSuccessful()) {
                                        Toast.makeText(getActivity().getApplicationContext(), "Your account successfully deactivated!" , Toast.LENGTH_SHORT).show();
                                        SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(getActivity());
                                        sharedPreferenceHelper.deleteToken();
                                        sharedPreferenceHelper.deleteUser();
                                        Intent intent = new Intent(getActivity(), Authentication.class);
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
        return view;
    }
}