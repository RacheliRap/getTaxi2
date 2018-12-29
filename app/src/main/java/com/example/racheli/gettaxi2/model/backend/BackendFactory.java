package com.example.racheli.gettaxi2.model.backend;

import com.example.racheli.gettaxi2.model.datasource.Firebase_DBManager;

public class BackendFactory {
    static Backend instance = null;

    static public final Backend getInstance(){
        if(instance == null){
            instance = new Firebase_DBManager();
        }
        return instance;
    }
}
