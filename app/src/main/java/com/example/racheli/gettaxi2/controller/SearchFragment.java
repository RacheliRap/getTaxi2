package com.example.racheli.gettaxi2.controller;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
}