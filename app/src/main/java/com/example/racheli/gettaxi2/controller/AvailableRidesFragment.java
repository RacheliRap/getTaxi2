package com.example.racheli.gettaxi2.controller;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.example.racheli.gettaxi2.R;
import com.example.racheli.gettaxi2.model.backend.Backend;
import com.example.racheli.gettaxi2.model.backend.BackendFactory;
import com.example.racheli.gettaxi2.model.datasource.Firebase_DBManager;
import com.example.racheli.gettaxi2.model.entities.Driver;
import com.example.racheli.gettaxi2.model.entities.Ride;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

public class AvailableRidesFragment extends Activity
{
    RecyclerView recyclerView;
    LocationListener locationListener;
    LocationManager locationManager;
    ArrayList<Ride> rideList = new ArrayList<Ride>() {};


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.available_rides_layout);

        recyclerView = (RecyclerView) findViewById(R.id.myRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ExpendableAdapter(initDemoItems()));
       // getLocation();
    }
    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.available_rides_layout, null);
        recyclerView = (RecyclerView) getView().findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(new ExpendableAdapter(initDemoItems()));
        return root;
    }*/
    /*@Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        recyclerView = (RecyclerView) getView().findViewById(R.id.myRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(new ExpendableAdapter(initDemoItems()));
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }*/




    private List<Ride> initDemoItems() {
       List<Ride> result = new ArrayList<>(1000);
       Backend fb  = BackendFactory.getInstance();
       List<Driver> driverList = Firebase_DBManager.getDriverList();
       List<Ride> rlist = Firebase_DBManager.getRideList();
        for(int i=0;i<20;i++)
       {
           Ride ride = new Ride();
           ride.setCreditCard(i+" 111222233334444");
           ride.setDestination(i+" avraham shiif, jerusalem");
           ride.setDriverName(i+" choen");
           ride.setOrigin(i + "mhalh mall");
           ride.setPassengerMail(i+" ride@gmail.com");
           ride.setPhoneNumber(i+"77777777");
           rideList.add(ride);
       }

       for (int i=0;i<20;i++) {
           ExpendableItem item = new ExpendableItem();
           item.setDestination(rideList.get(i).getOrigin().toString());
           //item.time = rideList.get(i).getStartingTime();
           ChildItem child = new ChildItem();
           //child.location = "Hi, im: "+ i +" and I'm a child of " + i;
           child.setDestination( rideList.get(i).getOrigin().toString());
           item.childs.add(child);
           result.add(item);
       }
       return result;
   }

    private static class ExpendableAdapter extends RecyclerView.Adapter {
        List<Ride> data;
        public ExpendableAdapter(List<Ride> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if(viewType == TYPES.EXPENDABLE.value){
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expendable,parent,false);
                return new ExpendableViewHolder(view);
            }else{
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child,parent,false);
                return new ChildViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Ride item = data.get(position);
            if (item instanceof ExpendableItem){
                ExpendableViewHolder exHolder = (ExpendableViewHolder) holder;
                exHolder.txt.setText(item.getDestination());
            }else {
                ChildViewHolder chHolder = (ChildViewHolder) holder;
                chHolder.txt.setText(item.getDestination());
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        enum TYPES{
            EXPENDABLE(1),CHILD(2);

            private final int value;
            TYPES(int i) {
                value = i;
            }
        }
        @Override
        public int getItemViewType(int position) {
            return data.get(position) instanceof ExpendableItem ? TYPES.EXPENDABLE.value : TYPES.CHILD.value;
        }


        class ChildViewHolder extends RecyclerView.ViewHolder{
            TextView txt;
            public ChildViewHolder(View itemView) {
                super(itemView);
                txt = itemView.findViewById(R.id.dest_textView);
            }
        }


        class ExpendableViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView txt;
            ImageView btn;

            public ExpendableViewHolder(View itemView) {
                super(itemView);
                txt = itemView.findViewById(R.id.parent_location);
                btn = itemView.findViewById(R.id.expendable_btn);
                btn.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                ExpendableItem item = ((ExpendableItem)data.get(getAdapterPosition()));
                int numOfChildes = item.childs.size();
                if (item.isOpen){
                    data.removeAll(item.childs);
                    notifyItemRangeRemoved(getAdapterPosition()+1, numOfChildes);
                    item.isOpen = false;
                }else {
                    data.addAll(getAdapterPosition()+1, item.childs);
                    notifyItemRangeInserted(getAdapterPosition()+1, numOfChildes);
                    item.isOpen = true;
                }
            }
        }
    }

    private class ExpendableItem extends Ride {
        public boolean isOpen;
        List<ChildItem> childs = new ArrayList<>();
    }
    private class ChildItem extends Ride  {

    }



}
