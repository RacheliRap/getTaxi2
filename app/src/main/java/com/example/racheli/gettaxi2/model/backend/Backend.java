package com.example.racheli.gettaxi2.model.backend;

import com.example.racheli.gettaxi2.model.datasource.Action;
import com.example.racheli.gettaxi2.model.entities.Driver;
import com.example.racheli.gettaxi2.model.entities.Ride;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface Backend {

    void addDriver(final Driver driver , final Action<String> action) throws Exception;
    List<Driver> getDrivers();
    List<Ride> getRides();
    ArrayList<Ride> getAvailableRides();
    ArrayList<String> getDriversNames();
    ArrayList<Ride> getUnhandledRides();
    ArrayList<Ride> getFinishedRides();
    ArrayList<Ride> getRidesByDriver(String driverName);
    ArrayList<Ride> getRidesByCity(String city);
    ArrayList<Ride> getRidesByDistance(float distance);
    ArrayList<Ride> getRidesByDate(Date date);
    ArrayList<Ride> getRidesByPayment(float payment);



}
