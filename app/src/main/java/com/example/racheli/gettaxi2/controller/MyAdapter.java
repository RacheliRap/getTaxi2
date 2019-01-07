package com.example.racheli.gettaxi2.controller;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.example.racheli.gettaxi2.R;
import com.example.racheli.gettaxi2.controller.TitleParentViewHolder;
import com.example.racheli.gettaxi2.controller.TitleChildViewHolder;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.List;


public class MyAdapter extends ExpandableRecyclerAdapter<TitleParentViewHolder, TitleChildViewHolder> {


    LayoutInflater inflater;
    public MyAdapter(Context context, List<ParentObject> parentItemList) {
        super(context, parentItemList);
        inflater = LayoutInflater.from(context);

    }

    @Override
    public TitleParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.list_parent, viewGroup, false);
        return new TitleParentViewHolder(view);
    }

    @Override
    public TitleChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.list_child, viewGroup, false);
        return new TitleChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(TitleParentViewHolder titleParentViewHolder, int i, Object o) {
        TitleParent title = (TitleParent) o;
        titleParentViewHolder._textview.setText(title.getTitle());
    }

    @Override
    public void onBindChildViewHolder(TitleChildViewHolder titleChildViewHolder, int i, Object o) {

        TitleChild title = (TitleChild)o;
        titleChildViewHolder.option1.setText(title.getOption1());
        titleChildViewHolder.option2.setText(title.getOption2());



    }
}
