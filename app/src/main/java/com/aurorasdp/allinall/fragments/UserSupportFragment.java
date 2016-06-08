package com.aurorasdp.allinall.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.aurorasdp.allinall.R;
import com.aurorasdp.allinall.activities.LoginActivity;
import com.aurorasdp.allinall.helper.RESTClient;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserSupportFragment extends Fragment {
    private Button signoutButton;
    private SharedPreferences allinallSharedPref;

    public UserSupportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_user_support, container, false);
        allinallSharedPref = getActivity().getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        signoutButton = (Button) view.findViewById(R.id.fragment_user_support_signout_button);
        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDialog();
            }
        });
        return view;
    }

    private void showConfirmDialog() {
        new android.support.v7.app.AlertDialog.Builder(getContext())
                //set message, title, and icon
                .setTitle("Sign out")
                .setMessage("Do you want to sign out?")
                .setIcon(R.drawable.ic_launcher)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        SharedPreferences.Editor editor = allinallSharedPref.edit();
                        editor.putBoolean("firstLaunch", true);
                        editor.remove("userId");
                        editor.apply();
                        RESTClient.ID = null;
                        Intent loginIntent = new Intent(getContext(), LoginActivity.class);
                        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(loginIntent);
                        getActivity().finish();

                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
}
