package com.aurorasdp.allinall.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aurorasdp.allinall.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserSupportFragment extends Fragment {


    public UserSupportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_support, container, false);
    }

}