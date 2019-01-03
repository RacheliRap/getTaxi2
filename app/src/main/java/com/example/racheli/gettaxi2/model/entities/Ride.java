package com.example.racheli.gettaxi2.model.entities;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

//Ride class.
public class Ride implements java.io.Serializable{
    enum  Status{
        AVAILABLE, ON, DONE
    }

    //private static final long serialVersionUID = 1L;
    //private Long ID;
    private String driverName;
    private String status;
    private String origin;
    private String destination;
    private String startingTime = "";
    private String endingTime = "";
    private String passengerName;
    private String passengerMail;
    private String phoneNumber;
    private String creditCard;

    public Ride(String origin, String destination, String endingTime, String passengerName, String passengerMail, String phoneNumber, String creditCard) {
        this.origin = origin;
        this.destination = destination;
        this.endingTime = endingTime;
        this.passengerName = passengerName;
        this.passengerMail = passengerMail;
        this.phoneNumber = phoneNumber;
        this.creditCard = creditCard;
    }


    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status.toString();
    }

    public String getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(String startingTime) {
        this.startingTime = startingTime;
    }


    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getEndingTime() {
        return endingTime;
    }

    public void setEndingTime(String endingTime) {
        this.endingTime = endingTime;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPassengerMail() {
        return passengerMail;
    }

    public void setPassengerMail(String passengerMail) {
        this.passengerMail = passengerMail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public Ride(){}

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> results = new HashMap<>();
        results.put("Name", getPassengerName());
        results.put("Origin:", getOrigin());
        results.put("Destination:", getDestination());
        results.put("Time", getEndingTime());
        results.put("Phone number", getPhoneNumber());
        results.put("email", getPassengerMail());
        results.put("Credit card", getCreditCard());
        return results;
    }
}
