package com.example.racheli.gettaxi2.model.entities;

public class Driver implements java.io.Serializable{

    private static final long serialVersionUID = 1L;
    private String fullName;
    private String id;
    private String phoneNumber;
    private String email;
    private String cCardNumber;
    private String password;

    public Driver(String fullName, String id, String phoneNumber, String email, String cCardNumber, String password) {
        this.fullName = fullName;
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.cCardNumber = cCardNumber;
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getcCardNumber() {
        return cCardNumber;
    }

    public void setcCardNumber(String cCardNumber) {
        this.cCardNumber = cCardNumber;
    }

    public Driver(){}
}
