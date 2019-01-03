package com.example.racheli.gettaxi2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.racheli.gettaxi2.model.datasource.Firebase_DBManager;
import com.example.racheli.gettaxi2.model.entities.Ride;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {

    Button btn;

    public BlankFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false);

    }

    void initItemByListView(int size) {
        final ArrayList<Ride> list = Firebase_DBManager.getRideList();
        ListView listView = new ListView(getContext());
        ArrayAdapter<Ride> adapter = new ArrayAdapter<Ride>(this,
                R.layout.ride_button,
                list)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                if (convertView == null)
                {
                    convertView = View.inflate(getContext(),
                            R.layout.ride_button,null);
                }
                btn = (Button) getView().findViewById(R.id.rideButton);
                String buttonText = list.get(position).getOrigin() + " " +list.get(position).getStartingTime();
                btn.setText(buttonText);
                return convertView;
            }
        };
        listView.setAdapter(adapter);
        this.setContentView(listView);
    }
}
