package com.example.racheli.gettaxi2.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.racheli.gettaxi2.R;
import com.example.racheli.gettaxi2.model.backend.Backend;
import com.example.racheli.gettaxi2.model.backend.BackendFactory;
import com.example.racheli.gettaxi2.model.datasource.Firebase_DBManager;
import com.example.racheli.gettaxi2.model.entities.Driver;
import com.example.racheli.gettaxi2.model.entities.Ride;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends android.app.Fragment {
    View view;
    RecyclerView recyclerView;
    Context context;
    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, @NonNull ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) getView().findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new ExpendableAdapter(initDemoItems()));
        context = getActivity();
       // List<Ride> myRides = Firebase_DBManager.getRideList();
    }
    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        context=activity;
    }

    private List<Ride> initDemoItems() {
        List<Ride> result = new ArrayList<>(1000);
        Backend fb = BackendFactory.getInstance();
        Firebase_DBManager db = new Firebase_DBManager();
        List<Ride> rideList = Firebase_DBManager.getRideList();
        List<Driver> driverList = Firebase_DBManager.getDriverList();
        for(int i = 0; i < 3; i++)
        {
            Ride ride = new Ride();
            ride.setDestination(i + " Avraham shiff St, Jerusalem");
            ride.setPhoneNumber("0507270820");
            ride.setOrigin(i + "Beit hadfus, Jerusalem");
            ride.setStartingTime("15:00");
            rideList.add(ride);
        }


        for (int i = 0; i < 3; i++) {
            ExpendableItem item = new SearchFragment.ExpendableItem();
            item.setDestination(rideList.get(i).getDestination().toString());
            //getLocation();
            //addressToLocation(rideList.get(i).getDestination().toString());
            //float distance =  calculateDistance(locationA, location);
            //item.setDistance(distance);
            //item.time = rideList.get(i).getStartingTime();
            ChildItem child = new ChildItem();
            child.setDestination(rideList.get(i).getOrigin().toString());
            child.setStartingTime(rideList.get(i).getStartingTime());
            child.setPhoneNumber(rideList.get(i).getPhoneNumber());
            item.childs.add(child);
            result.add(item);
        }
        return result;
    }
   public void showToast()
    {
        Toast.makeText(context, "hi", Toast.LENGTH_LONG).show();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("dialog title");
        alertDialogBuilder.setMessage("dialog message ....");
        alertDialogBuilder.setPositiveButton("Ok",onClickListener);
        alertDialogBuilder.setNegativeButton("Cancel ",onClickListener);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    AlertDialog.OnClickListener onClickListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case Dialog.BUTTON_NEGATIVE: {

                }
            }
        }
    };

    private static class ExpendableAdapter extends RecyclerView.Adapter {
        List<Ride> data;

        public ExpendableAdapter(List<Ride> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == SearchFragment.ExpendableAdapter.TYPES.EXPENDABLE.value) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expendable, parent, false);
                return new SearchFragment.ExpendableAdapter.ExpendableViewHolder(view);
            } else {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child, parent, false);
                return new SearchFragment.ExpendableAdapter.ChildViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Ride item = data.get(position);
            if (item instanceof SearchFragment.ExpendableItem) {
                SearchFragment.ExpendableAdapter.ExpendableViewHolder exHolder = (SearchFragment.ExpendableAdapter.ExpendableViewHolder) holder;
                exHolder.location_txt.setText(item.getDestination());
                // exHolder.distance_txt.setText((ExpendableItem) item.getDistance());
            } else {
                SearchFragment.ExpendableAdapter.ChildViewHolder chHolder = (SearchFragment.ExpendableAdapter.ChildViewHolder) holder;
                chHolder.dest.setText(item.getDestination());
                chHolder.phoneNumber.setText(item.getPhoneNumber());
                chHolder.time.setText(item.getStartingTime());
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        enum TYPES {
            EXPENDABLE(1), CHILD(2);

            private final int value;

            TYPES(int i) {
                value = i;
            }
        }

        @Override
        public int getItemViewType(int position) {
            return data.get(position) instanceof SearchFragment.ExpendableItem ? SearchFragment.ExpendableAdapter.TYPES.EXPENDABLE.value : SearchFragment.ExpendableAdapter.TYPES.CHILD.value;
        }


        class ChildViewHolder extends RecyclerView.ViewHolder {
            TextView dest;
            TextView time;
            TextView phoneNumber;

            public ChildViewHolder(View itemView) {
                super(itemView);
                dest = itemView.findViewById(R.id.dest_textView);
                time = itemView.findViewById(R.id.time_textView);
                phoneNumber = itemView.findViewById(R.id.phoneNumber_textView);
            }
        }


        class ExpendableViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView location_txt;
            TextView distance_txt;
            ImageView btn;
            ImageButton arrow;
            ImageButton plus;
            Context context;


            public ExpendableViewHolder(View itemView) {
                super(itemView);
                location_txt = itemView.findViewById(R.id.parent_location);
                distance_txt = itemView.findViewById(R.id.parent_dist);
                //btn = itemView.findViewById(R.id.expendable_btn);
                // btn.setOnClickListener(this);
                arrow = itemView.findViewById(R.id.expendable_btn);
                arrow.setOnClickListener(this);
                plus = itemView.findViewById(R.id.getRide_btn);
                plus.setOnClickListener(this);

            }

            @Override
            public void onClick(View v) {
                //the button for expend the list view
                if (v == arrow) {
                    SearchFragment.ExpendableItem item = ((SearchFragment.ExpendableItem) data.get(getAdapterPosition()));
                    int numOfChildes = item.childs.size();
                    if (item.isOpen) {
                        data.removeAll(item.childs);
                        notifyItemRangeRemoved(getAdapterPosition() + 1, numOfChildes);
                        item.isOpen = false;
                    } else {
                        data.addAll(getAdapterPosition() + 1, item.childs);
                        notifyItemRangeInserted(getAdapterPosition() + 1, numOfChildes);
                        item.isOpen = true;
                    }
                }
                //the button to get the ride
                if (v == plus) {
                    SearchFragment searchFragment = new SearchFragment();
                    searchFragment.showToast();
                    // Toast.makeText(context, "hi", Toast.LENGTH_LONG).show();
                      /*  AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                        alertDialogBuilder.setTitle("dialog title");
                        alertDialogBuilder.setMessage("dialog message ....");
                        alertDialogBuilder.setPositiveButton("Ok",onClickListener);
                        alertDialogBuilder.setNegativeButton("Cancel ",onClickListener);
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();*/
                }

            }
        }
    }

    private class ChildItem extends Ride { }

    private class ExpendableItem extends Ride {
        public boolean isOpen;
        Float distance;
        List<SearchFragment.ChildItem> childs = new ArrayList<>();

        public void setDistance(Float distance) {
            this.distance = distance;
        }

        public Float getDistance() {
            return distance;
        }
    }
}
