package com.example.racheli.gettaxi2.model.datasource;

public interface NotifyDataChange<T> {

    void OnDataChanged(T obj);

    void onFailure(Exception exception);
}
