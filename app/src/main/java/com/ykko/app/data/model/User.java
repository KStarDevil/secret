package com.ykko.app.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    public String username;
    public String phone;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username,String phone) {
        this.username = username;
        this.phone = phone;

    }

    protected User(Parcel in) {
        username = in.readString();
        phone = in.readString();

    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(phone);

    }
}
