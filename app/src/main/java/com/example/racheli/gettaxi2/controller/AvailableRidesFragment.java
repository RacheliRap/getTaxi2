package com.example.racheli.gettaxi2.controller;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.racheli.gettaxi2.BlankFragment;
import com.example.racheli.gettaxi2.R;

public class AvailableRidesFragment extends Fragment {
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.available_rides_layout , container , false);
        BlankFragment fragment = new BlankFragment();
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().replace(R.id.fragment_container,fragment, fragment.getTag()).commit();

        return v;
    }
}

