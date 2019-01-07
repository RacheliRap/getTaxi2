package com.example.racheli.gettaxi2.controller;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.example.racheli.gettaxi2.R;

import java.util.ArrayList;
import java.util.List;

public class AvailableRidesFragment extends AppCompatActivity
{
    RecyclerView recyclerView;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ((MyAdapter)recyclerView.getAdapter()).onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.available_rides_layout);

        recyclerView = (RecyclerView) findViewById(R.id.myRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyAdapter adapter = new MyAdapter(this,initData());

        adapter.setParentClickableViewAnimationDefaultDuration();
        adapter.setParentAndIconExpandOnClick(true);

        recyclerView.setAdapter(adapter);
    }

    private List<ParentObject> initData() {
        TitleCreator titleCreator = TitleCreator.get(this);
        List<TitleParent> titels = titleCreator.getAll();
        List<ParentObject> parentObjects = new ArrayList<>();
        for(TitleParent title : titels)
        {
            List<Object> childList = new ArrayList<>();
            childList.add(new TitleChild("Add to contacts", "Send messege"));
            title.setChildObjectList(childList);
            parentObjects.add(title);

        }
        return parentObjects;
    }


}
