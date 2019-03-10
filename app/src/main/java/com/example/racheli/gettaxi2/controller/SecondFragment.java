package com.example.racheli.gettaxi2.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.example.racheli.gettaxi2.R;
import com.example.racheli.gettaxi2.model.backend.Backend;
import com.example.racheli.gettaxi2.model.backend.BackendFactory;
import com.example.racheli.gettaxi2.model.datasource.Firebase_DBManager;
import com.example.racheli.gettaxi2.model.entities.Driver;
import com.example.racheli.gettaxi2.model.entities.Ride;

import java.util.ArrayList;
import java.util.List;

/**
 * The class handle the second fragment - specific rides
 */
public class SecondFragment extends android.app.Fragment {
    View view;
    RecyclerView recyclerView;
    static Context context;
    SearchView searchView;
    ExpendableAdapter adapter;
    Ride tmpRide;
    List<Ride> rideList = new ArrayList<>();
    List<Driver> driverList = new ArrayList<>();
    Backend instance;
    Driver mDriver;

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, @NonNull ViewGroup container, Bundle savedInstanceState) {
         mDriver = (Driver)getArguments().getSerializable("myDriver");
        view = inflater.inflate(R.layout.fragment_search, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView();
        activeSearchView();
    }
    /**
     * Find the Views in the layout
     */
    private void findView() {
        instance = BackendFactory.getInstance(context);
        recyclerView = (RecyclerView) getView().findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new ExpendableAdapter(initItems(), new ListItemClickListener() {
            @Override
            public void onItemClicked(ExpendableItem item, int position) {
                showDialog(item , position);
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

    /**
     * The function init the items for the recycle view
     * @return list with the items
     */
    private List<Ride> initItems() {
        List<Ride> result = new ArrayList<>(1000);
        driverList = ((Firebase_DBManager)instance).getDriverList();
        rideList = instance.getRidesByDriver(mDriver.getFullName());

        for (int i = 0; i < rideList.size(); i++) {
            ExpendableItem item = new ExpendableItem();
            item.setDestination(rideList.get(i).getDestination());
            LocationClass locationClass = new LocationClass(context);
            Location origin = locationClass.addressToLocation(rideList.get(i).getOrigin());
            Location destination = locationClass.addressToLocation(rideList.get(i).getDestination());
            float distance = (float)(Math.round(locationClass.calculateDistance(origin, destination)*100))/100;
            item.setDistance(distance);
            item.setRideDate(rideList.get(i).getRideDate());
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
     * The function show the dialog after the driver chose to save the passanger as a contact
     * @param item the chosen expandable item
     * @param position the position of the recycle view
     */
    public void showDialog(ExpendableItem item, int position)
    {

        tmpRide = rideList.get(position);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("ADD CONTACT");
        String message = "Do you want to add the passenger to your contacts?";
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("Yes",onClickListener);
        alertDialogBuilder.setNegativeButton("Cancel ",onClickListener);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }
    //listener for the dialog that set on showDialog()
    AlertDialog.OnClickListener onClickListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case Dialog.BUTTON_NEGATIVE: {
                    break;
                }
                //Thee driver wants to add the contract
                case Dialog.BUTTON_POSITIVE: {

                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(Intent.ACTION_INSERT);
                            intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                            intent.putExtra(ContactsContract.Intents.Insert.NAME, tmpRide.getPassengerName());
                            intent.putExtra(ContactsContract.Intents.Insert.PHONE,tmpRide.getPhoneNumber());
                            intent.putExtra(ContactsContract.Intents.Insert.EMAIL, tmpRide.getPassengerMail());
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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expendable2, parent, false);
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
                exHolder.distance_txt.setText(Float.toString(((ExpendableItem)item).getDistance()) + " km ride");
                exHolder.date_txt.setText("At " + item.getRideDate());
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

        /**
         * implement the data filter for the recycle view
         */
        @Override
        public Filter getFilter() {
            return dataFilter;
        }
        /**
         * Filter the data by the constraint
         * @param constraint the char the user typed in the search view
         * @return list with filter results
         */
        private Filter dataFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Ride> filteredList = new ArrayList<>();
                //if there is no constraint or the constraint wa deleted
                if(constraint == null || constraint.length() == 0)
                {
                    filteredList.addAll(dataFull);// suppose to be dataFull - check if error accurs
                }
                else { //filter the list
                    String filterPattern = constraint.toString().toLowerCase().trim(); //make sure the search is not case sensitivity
                    for (Ride r : dataFull) {
                        if (r.getDestination().toLowerCase().contains(filterPattern)
                                || Float.toString(r.getDistance()).contains(filterPattern)
                                || r.getRideDate().contains(filterPattern)) {
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
            return data.get(position) instanceof ExpendableItem ? ExpendableAdapter.TYPES.EXPENDABLE.value
                    : ExpendableAdapter.TYPES.CHILD.value;
        }

        /**
         * The class handle the child view holder
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
            TextView date_txt;
            ImageView arrow;
            ImageView addContact;
            /**
             * ctor
             * @param itemView the layout with the parent view
             */
            public ExpendableViewHolder(View itemView) {
                super(itemView);
                location_txt = itemView.findViewById(R.id.parent_location);
                distance_txt = itemView.findViewById(R.id.parent_dist);
                date_txt = itemView.findViewById(R.id.parent_date);
                arrow = itemView.findViewById(R.id.expendable_btn);
                arrow.setOnClickListener(this);
                addContact = itemView.findViewById(R.id.addContact_btn);
                addContact.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                //the button for expend the list view
                if (v == arrow) {
                    ExpendableItem item = ((ExpendableItem) data.get(getAdapterPosition()));
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
                //the button to get the contact
                if (v == addContact) {
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
        public boolean isOpen;//Is the item expand or not
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
