package com.example.racheli.gettaxi2.model.entities;

//Ride class.
public class Ride implements java.io.Serializable{
    enum  Status{
        AVAILABLE, ON, DONE
    }

    private static final long serialVersionUID = 1L;
    //private Long ID;
    private String driverName;
    private String status ;
    private String origin;
    private String destination;
    private String startingTime ;
    private String endingTime ;
    private String passengerName;
    private String passengerMail;
    private String phoneNumber;
    private String creditCard;

    public Ride(){}

    public Ride(String origin, String destination, String startingTime, String endingTime,
                String passengerName, String passengerMail, String phoneNumber, String creditCard) {
        this.origin = origin;
        this.destination = destination;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
        this.passengerName = passengerName;
        this.passengerMail = passengerMail;
        this.phoneNumber = phoneNumber;
        this.creditCard = creditCard;
    }

    public Ride(String origin, String destination, String startingTime,
                String passengerName, String passengerMail, String phoneNumber, String creditCard) {

        this.origin = origin;
        this.destination = destination;
        this.startingTime = startingTime;
        this.passengerName = passengerName;
        this.passengerMail = passengerMail;
        this.phoneNumber = phoneNumber;
        this.creditCard = creditCard;
    }

    public Ride(String driverName, String status, String origin, String destination, String startingTime,
                String endingTime, String passengerName, String passengerMail, String phoneNumber, String creditCard) {
        this.driverName = driverName;
        this.status = status;
        this.origin = origin;
        this.destination = destination;
        this.startingTime = startingTime;
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

}
