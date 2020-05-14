package com.ykko.app.data;


import android.app.Application;

public class Global_Variable extends Application {

    private String username;
    private String phone;

    public String getusername() {
        return username;
    }

    public void setusername(String username) {
        this.username = username;
    }

    public String getphone() {
        return phone;
    }

    public void setphone(String phone) {
        this.phone = phone;
    }
}