package com.example.racheli.gettaxi2.model.backend;

import com.example.racheli.gettaxi2.model.datasource.Action;
import com.example.racheli.gettaxi2.model.entities.Driver;
import com.example.racheli.gettaxi2.model.entities.Ride;

import java.util.List;

public interface Backend {

    void addDriver(final Driver driver , final Action<String> action) throws Exception;
    List<Driver> getDrivers();
    List<Ride> getRides();



}
