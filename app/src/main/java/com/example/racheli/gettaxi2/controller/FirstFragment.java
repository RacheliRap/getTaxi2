package com.example.racheli.gettaxi2.controller;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.example.racheli.gettaxi2.model.entities.Ride;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.content.ContextCompat.checkSelfPermission;


public class FirstFragment extends android.app.Fragment {
    View view;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    RecyclerView recyclerView;
    static Context context;
    SearchView searchView;
    ExpendableAdapter adapter;
    List<Ride> rideList = new ArrayList<>();
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
        adapter = new ExpendableAdapter(initDemoItems(), new ListItemClickListener() {
            @Override
            public void onItemClicked(ExpendableItem item) {
                FirstFragment firstFragment = new FirstFragment();
                firstFragment.showDialog(item);
            }
        });
        recyclerView.setAdapter(adapter);
        searchView = (SearchView) getView().findViewById(R.id.simpleSearchView);
        activeSearchView();
    }
    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        context=getActivity();
    }

    private class SendSmsTask extends AsyncTask<Integer, Integer, Boolean> {
        protected Boolean doInBackground(Integer... urls) {
            Log.i("Send SMS", "");
            Intent smsIntent = new Intent(Intent.ACTION_VIEW);

            smsIntent.setData(Uri.parse("smsto:"));
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("address"  , new String ("01234"));
            smsIntent.putExtra("sms_body"  , "Test ");

            try {
                startActivity(smsIntent);
                //finish();
                Log.i("Finished sending SMS...", "");
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(context, "SMS failed, please try again later.", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        protected void onProgressUpdate(Integer... progress) {
        }
        protected void onPostExecute(Long result) {
        }
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
            item.setDestination(rideList.get(i).getDestination().toString());
            LocationHandle locationHandle = new LocationHandle(context);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);
                // has no permission it will crash if we will try to access it
            }
            locationHandle.getMyLocation();
            Location driverLocation = locationHandle.getMyLocation();
            Location passengerLocation = locationHandle.addressToLocation(rideList.get(i).getOrigin().toString());
            float distance = Math.round(locationHandle.calculateDistance(driverLocation, passengerLocation));
            item.setDistance(distance);
            ChildItem child = new ChildItem();
            child.setOrigin(rideList.get(i).getOrigin().toString());
            child.setStartingTime(rideList.get(i).getStartingTime());
            child.setPhoneNumber(rideList.get(i).getPhoneNumber());
            item.childs.add(child);
            result.add(item);
        }
        return result;
    }
    public void showDialog(ExpendableItem item)
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "567")
                .setSmallIcon(R.drawable.transparent_icon)
                .setContentTitle("My notification")
                .setContentText("Much longer text that cannot fit one line...")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        //context = getActivity().getBaseContext();
        //Toast.makeText(context, "hi", Toast.LENGTH_LONG).show();
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
                case Dialog.BUTTON_POSITIVE: {
                    //TODO change in ride the driver name
                    /*Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + "+972586367706"));
                    intent.putExtra("sms_body", "hi");
                    startActivity(intent);*/
                    /*SmsManager sms = SmsManager.getDefault();
                    sms.sendTextMessage("+972586367706", null, "hi", null, null);
                    Toast.makeText(context, "3", Toast.LENGTH_LONG).show();*/
                    /*SmsManager sms = SmsManager.getDefault();
                    PendingIntent sentPI;
                    String SENT = "SMS_SENT";
                    sentPI = PendingIntent.getBroadcast(context, 0,new Intent(SENT), 0);
                    sms.sendTextMessage("+972596367706", null, "hi ", sentPI, null);*/
                    //sendSMS();

                    /*Uri uri = Uri.parse("smsto:+972586367706");
                    Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                    intent.putExtra("sms_body", "hi");
                    startActivity(intent);*/
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Uri uri = Uri.parse("smsto:+972586367706");
                            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                            intent.putExtra("sms_body", "hi");
                            context.startActivity(intent);
                            }
                    }
                    );

//                    new SendSmsTask().execute(1, 2, 3);
                    break;
                }
            }
        }
    };

    protected void sendSMS() {
        Log.i("Send SMS", "");
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);

        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address"  , new String ("01234"));
        smsIntent.putExtra("sms_body"  , "Test ");

        try {
            startActivity(smsIntent);
            //finish();
            Log.i("Finished sending SMS...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "SMS failed, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    public void activeSearchView()
    {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }


    private static class ExpendableAdapter extends RecyclerView.Adapter implements Filterable{
        List<Ride> data;
        List<Ride> dataFull;
        ListItemClickListener listener;


        public ExpendableAdapter(List<Ride> data,ListItemClickListener listener) {
            this.data = data;
            this.listener = listener;
            dataFull = new ArrayList<>(this.data);
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == FirstFragment.ExpendableAdapter.TYPES.EXPENDABLE.value) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expendable1, parent, false);
                return new FirstFragment.ExpendableAdapter.ExpendableViewHolder(view);
            } else {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child, parent, false);
                return new FirstFragment.ExpendableAdapter.ChildViewHolder(view);
            }

        }


        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Ride item = data.get(position);
            if (item instanceof FirstFragment.ExpendableItem) {
                FirstFragment.ExpendableAdapter.ExpendableViewHolder exHolder = (FirstFragment.ExpendableAdapter.ExpendableViewHolder) holder;
                exHolder.location_txt.setText(item.getDestination());
                exHolder.distance_txt.setText(Float.toString(((ExpendableItem)item).getDistance()));
                // exHolder.distance_txt.setText((ExpendableItem) item.getDistance());
            } else {
                FirstFragment.ExpendableAdapter.ChildViewHolder chHolder = (FirstFragment.ExpendableAdapter.ChildViewHolder) holder;
                chHolder.dest.setText(item.getDestination());
                chHolder.phoneNumber.setText(item.getPhoneNumber());
                chHolder.time.setText(item.getStartingTime());
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        @Override
        public Filter getFilter() {
            return dataFilter;
        }
        private Filter dataFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Ride> filteredList = new ArrayList<>();
                //if there is no constraint or the constraint wa deleted
                if(constraint == null || constraint.length() == 0)
                {
                    filteredList.addAll(dataFull);// suppose to be dataFull - check if error accurs
                }
                else {
                    String filterPattern = constraint.toString().toLowerCase().trim(); //make sure the search is not case sensitivity
                    for (Ride r : dataFull) {
                        if (r.getDestination().toLowerCase().contains(filterPattern)) {
                            filteredList.add(r);
                        }

                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                data.clear();
                data.addAll((List)results.values);
                notifyDataSetChanged();
            }
        };

        enum TYPES {
            EXPENDABLE(1), CHILD(2);

            private final int value;

            TYPES(int i) {
                value = i;
            }
        }

        @Override
        public int getItemViewType(int position) {
            return data.get(position) instanceof FirstFragment.ExpendableItem ? FirstFragment.ExpendableAdapter.TYPES.EXPENDABLE.value : FirstFragment.ExpendableAdapter.TYPES.CHILD.value;
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
            ImageView arrow;
            ImageView plus;
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
                    listener.onItemClicked((ExpendableItem) data.get(getAdapterPosition()));
                }
            }
        }
    }
    interface ListItemClickListener{
        void onItemClicked(FirstFragment.ExpendableItem item);
    }

    private class ChildItem extends Ride { }

    private class ExpendableItem extends Ride {
        public boolean isOpen;
        float distance;
        List<FirstFragment.ChildItem> childs = new ArrayList<>();

        public void setDistance(Float distance) {
            this.distance = distance;
        }

        public float getDistance() {
            return distance;
        }
    }
}
