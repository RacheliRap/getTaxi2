package com.example.racheli.gettaxi2.controller;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.racheli.gettaxi2.R;


public class SearchFragment extends android.app.Fragment {
    View view;
    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater,@NonNull ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_search, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
