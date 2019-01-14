package com.example.racheli.gettaxi2.controller;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

//import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.example.racheli.gettaxi2.R;
import com.example.racheli.gettaxi2.model.entities.Ride;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class AvailableRidesFragment extends AppCompatActivity
{
    RecyclerView recyclerView;
    ArrayList<Ride> rideList = new ArrayList<Ride>() {};

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        ((MyAdapter)recyclerView.getAdapter()).onSaveInstanceState(outState);
    }
   /* @Override
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
   @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.available_rides_layout);

        recyclerView = (RecyclerView) findViewById(R.id.myRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ExpendableAdapter(initDemoItems()));
//        MyAdapter adapter = new MyAdapter(this,initData());

//        adapter.setParentClickableViewAnimationDefaultDuration();
//        adapter.setParentAndIconExpandOnClick(true);

//        recyclerView.setAdapter(adapter);
    }

   /* private List<AbstractItem> initDemoItems() {
        List<AbstractItem> result = new ArrayList<>(1000);
        for (int i=0;i<100;i++) {
            ExpendableItem item = new ExpendableItem();
            item.name = "Im " + i + " click here -> ";
            for (int j = 0; j < 10; j++) {
                ChildItem child = new ChildItem();
                child.name = "Hi, im: "+ j +" and I'm a child of " + i;
                item.childs.add(child);
            }
            result.add(item);
        }
        return result;
    }*/
   private List<AbstractItem> initDemoItems() {
       List<AbstractItem> result = new ArrayList<>(1000);
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
           item.location = rideList.get(i).getOrigin().toString();
           //item.time = rideList.get(i).getStartingTime();
           ChildItem child = new ChildItem();
           //child.location = "Hi, im: "+ i +" and I'm a child of " + i;
           child.location = rideList.get(i).getOrigin().toString();
           item.childs.add(child);
           result.add(item);
       }
       return result;
   }

    private static class ExpendableAdapter extends RecyclerView.Adapter {
        List<AbstractItem> data;
        public ExpendableAdapter(List<AbstractItem> data) {
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
            AbstractItem item = data.get(position);
            if (item instanceof ExpendableItem){
                ExpendableViewHolder exHolder = (ExpendableViewHolder) holder;
                exHolder.txt.setText(item.location);
            }else {
                ChildViewHolder chHolder = (ChildViewHolder) holder;
                chHolder.txt.setText(item.location);
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
                    notifyItemRangeInserted(getAdapterPosition(), numOfChildes);
                    item.isOpen = true;
                }
            }
        }
    }

    private abstract class AbstractItem {
        String location;
        //String time;
    }
    private class ExpendableItem extends AbstractItem {
        public boolean isOpen;
        List<ChildItem> childs = new ArrayList<>();
    }
    private class ChildItem extends AbstractItem  {

    }


//    private List<ParentObject> initData() {
//        TitleCreator titleCreator = TitleCreator.get(this);
//        List<TitleParent> titels = titleCreator.getAll();
//        List<ParentObject> parentObjects = new ArrayList<>();
//        for(TitleParent title : titels)
//        {
//            List<Object> childList = new ArrayList<>();
//            childList.add(new TitleChild("Add to contacts", "Send messege"));
//            title.setChildObjectList(childList);
//            parentObjects.add(title);
//
//        }
//        return parentObjects;
//    }


}
