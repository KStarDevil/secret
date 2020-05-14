package com.ykko.app.data.model;

public class Reservation {
    public String branchName;
    public String hours;
    public String phNo;

    public Reservation() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Reservation(String branchName,String hours, String phNo) {
        this.branchName = branchName;
        this.hours = hours;
        this.phNo = phNo;
    }
}
