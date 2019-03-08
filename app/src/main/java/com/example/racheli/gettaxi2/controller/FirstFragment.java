package com.example.racheli.gettaxi2.controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SearchView;
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

import static android.support.v4.content.ContextCompat.checkSelfPermission;

/**
 * The class handle the first fragment - available rides
 */
public class FirstFragment extends android.app.Fragment {
    View view;
    RecyclerView recyclerView;
    static Context context;
    SearchView searchView;
    ExpendableAdapter adapter;
    List<Ride> rideList = new ArrayList<>();
    List<Driver> ls = new ArrayList<>();

    Ride tmpRide;



    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, @NonNull ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews();
        activeSearchView();
    }
    /**
     * Find the Views in the layout
     */
    private void findViews() {
        recyclerView = (RecyclerView) getView().findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new ExpendableAdapter(initDemoItems(), new ListItemClickListener() {
            @Override
            public void onItemClicked(ExpendableItem item, int position) {
                showDialog(item, position);
            }
        });
        recyclerView.setAdapter(adapter);
        searchView = (SearchView) getView().findViewById(R.id.simpleSearchView);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context=getActivity();
    }

    private List<Ride> initDemoItems() {
        List<Ride> result = new ArrayList<>(1000);
        for(int i = 0; i < 3; i++)
        {
            Ride ride = new Ride();
            ride.setDestination( " Sderot Golda Me'ir 45, Jerusalem");
            ride.setPhoneNumber("0507270820");
            ride.setOrigin(" Beit HaDfus Street 20, jerusalem");
            ride.setStartingTime("15:00");
            rideList.add(ride);
        }


        for (int i = 0; i < 3; i++) {
            ExpendableItem item = new FirstFragment.ExpendableItem();
            LocationClass locationClass = new LocationClass(context);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);
                // has no permission it will crash if we will try to access it
            }
            item.setDestination(rideList.get(i).getDestination());
            Location driverLocation = locationClass.getMyLocation();
            Location passengerLocation = locationClass.addressToLocation(rideList.get(i).getOrigin());
            float distance = Math.round(locationClass.calculateDistance(driverLocation, passengerLocation));
            item.setDistance(distance);
            ChildItem child = new ChildItem();
            child.setOrigin(rideList.get(i).getOrigin());
            child.setStartingTime(rideList.get(i).getStartingTime());
            child.setPhoneNumber(rideList.get(i).getPhoneNumber());
            item.childs.add(child);
            result.add(item);
        }
        return result;
    }

    /**
     * The function show the dialog after the driver pick a ride
     * @param item the chosen expandable item
     * @param position the position of the recycle view
     */
    public void showDialog(ExpendableItem item, int position)
    {
        tmpRide = rideList.get(position);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("GET RIDE");
        String destination = item.getDestination();
        String message = "Are you sure you want to continue with the ride to " + destination + "?";
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("Yes,I'm sure!",onClickListener);
        alertDialogBuilder.setNegativeButton("Cancel ",onClickListener);
        alertDialogBuilder.setNeutralButton("Ride is done!", onClickListener);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
    //listener for the dialog that set on showDialog()
    AlertDialog.OnClickListener onClickListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case Dialog.BUTTON_NEGATIVE: {
                    Toast.makeText(context, "1", Toast.LENGTH_LONG).show();
                    break;
                }
                case Dialog.BUTTON_NEUTRAL: {
                    //TODO change in ride status to done
                    Toast.makeText(context, "2", Toast.LENGTH_LONG).show();
                    break;
                }
                //the driver picked the ride
                case Dialog.BUTTON_POSITIVE: {
                    //TODO change in ride the driver name
                    showNotification();
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Uri uri = Uri.parse("smsto:+972586367706");
                            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                            String message = "Hi,\n you ordered a ride from" + tmpRide.getOrigin() +
                                    "to" + tmpRide.getDestination()+ "\nour driver will be in touch:)" ;
                            intent.putExtra("sms_body", message);
                            context.startActivity(intent);
                            }
                    }
                    );

                    break;
                }
            }
        }
    };

    /**
     * The function show notification after the driver picked a ride
     */
    private void showNotification() {

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context,MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.CYAN);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder b = new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID);

        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.transparent_icon)
                .setContentTitle("New Ride")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("You just added a new ride to" + tmpRide.getDestination())) //expand
                .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent)
                .setContentInfo("Info");

        notificationManager.notify(1, b.build());
    }

    /**
     * set listener for the search view
     */
    public void activeSearchView()
    {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            /**
             * When the text change in the search view
             * @param newText the text the user typed into the search view
             * @return false
             */
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    /**
     * The class handle the adapter for the recycle view
     */
    private static class ExpendableAdapter extends RecyclerView.Adapter implements Filterable
    {
        List<Ride> data;
        List<Ride> dataFull;
        ListItemClickListener listener;

        /**
         * ctor
         * @param data the data to present on recycle view
         * @param listener listener for the button click on the recycle view
         */
        public ExpendableAdapter(List<Ride> data,ListItemClickListener listener) {
            this.data = data;
            this.listener = listener;
            dataFull = new ArrayList<>(this.data);
        }

        /**
         * create the expandable view holder or its child view holder
         */
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == ExpendableAdapter.TYPES.EXPENDABLE.value) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expendable1, parent, false);
                return new ExpendableAdapter.ExpendableViewHolder(view);
            } else {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child, parent, false);
                return new ExpendableAdapter.ChildViewHolder(view);
            }

        }

        /**
         * bind the view holder
         * @param holder recycle view to bind
         * @param position position in the view where the view holder should be bind
         */
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Ride item = data.get(position);
            if (item instanceof ExpendableItem) {
                //set view holder text for the recycle view
                ExpendableAdapter.ExpendableViewHolder exHolder = (ExpendableAdapter.ExpendableViewHolder) holder;
                exHolder.location_txt.setText(item.getDestination());
                exHolder.distance_txt.setText(Float.toString(((ExpendableItem)item).getDistance())+ " km");

            } else {
                //set child view holder text for the recycle view
                ExpendableAdapter.ChildViewHolder chHolder = (ExpendableAdapter.ChildViewHolder) holder;
                chHolder.origin.setText(item.getOrigin());
                chHolder.phoneNumber.setText(item.getPhoneNumber());
                chHolder.time.setText(item.getStartingTime());
            }
        }

        /**
         * return the size of the recycle view
         * @return size
         */
        @Override
        public int getItemCount() {
            return data.size();
        }

        @Override
        public Filter getFilter() {
            return dataFilter;
        }

        /**
         * implement the data filter for the recycle view
         */
        private Filter dataFilter = new Filter() {
            /**
             * Filter the data by the constraint
             * @param constraint the char the user typed in the search view
             * @return list with filter results
             */
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Ride> filteredList = new ArrayList<>();
                //if there is no constraint or the constraint wa deleted
                if(constraint == null || constraint.length() == 0)
                {
                    filteredList.addAll(dataFull);
                }
                else {
                    //filter the list
                    String filterPattern = constraint.toString().toLowerCase().trim(); //make sure the search is not case sensitivity
                    for (Ride r : dataFull) {
                        if (r.getDestination().toLowerCase().contains(filterPattern)
                                || Float.toString(r.getDistance()).contains(filterPattern)) {
                            filteredList.add(r);
                        }

                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            /**
             * publish the filtered results
             * @param constraint the char the user typed in the search view
             * @param results the results from performFiltering()
             */
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                data.clear();
                data.addAll((List)results.values);
                notifyDataSetChanged();
            }
        };
        //enum with types for the recycle view(Expandable(parent), child)
        enum TYPES {
            EXPENDABLE(1), CHILD(2);

            private final int value;

            TYPES(int i) {
                value = i;
            }
        }

        /**
         * retrurn if the item is expandable or child
         * @param position
         * @return enum TYPES value
         */
        @Override
        public int getItemViewType(int position) {
            return data.get(position) instanceof ExpendableItem ? FirstFragment.ExpendableAdapter.TYPES.EXPENDABLE.value : FirstFragment.ExpendableAdapter.TYPES.CHILD.value;
        }

        /**
         * The class handle the child view
         */
        class ChildViewHolder extends RecyclerView.ViewHolder {
            TextView origin;
            TextView time;
            TextView phoneNumber;

            /**
             * ctor
             * @param itemView the layout with the child view
             */
            public ChildViewHolder(View itemView) {
                super(itemView);
                origin = itemView.findViewById(R.id.dest_textView);
                time = itemView.findViewById(R.id.time_textView);
                phoneNumber = itemView.findViewById(R.id.phoneNumber_textView);
            }
        }

        /**
         * The class handle the Expandable item(the parent)
         */
        class ExpendableViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView location_txt;
            TextView distance_txt;
            ImageView arrow;
            ImageView plus;

            /**
             * ctor
             * @param itemView the layout with the parent view
             */
            public ExpendableViewHolder(View itemView) {
                super(itemView);
                location_txt = itemView.findViewById(R.id.parent_location);
                distance_txt = itemView.findViewById(R.id.parent_dist);
                arrow = itemView.findViewById(R.id.expendable_btn);
                arrow.setOnClickListener(this);
                plus = itemView.findViewById(R.id.getRide_btn);
                plus.setOnClickListener(this);

            }

            @Override
            public void onClick(View v) {
                //the button for expend the list view
                if (v == arrow) {
                    FirstFragment.ExpendableItem item = ((FirstFragment.ExpendableItem) data.get(getAdapterPosition()));
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
                    listener.onItemClicked((ExpendableItem) data.get(getAdapterPosition()), getAdapterPosition());
                }
            }
        }
    }

    /**
     * Interface to handle the button click on the recycle view
     */
    interface ListItemClickListener{
        void onItemClicked(ExpendableItem item, int position);
    }

    /**
     * child item class, extends ride for use in the recycle view
     */
    private class ChildItem extends Ride { }

    /**
     * Expandable item class, extends ride for use in the recycle view
     */
    private class ExpendableItem extends Ride {
        public boolean isOpen;//if the item is expand or not
        //float distance;
        List<ChildItem> childs = new ArrayList<>();

        /*public void setDistance(Float distance) {
            this.distance = distance;
        }

        public float getDistance() {
            return distance;
        }*/
    }
}
