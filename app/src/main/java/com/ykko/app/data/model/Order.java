package com.ykko.app.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Order implements Parcelable {
    public String branch;
    public String date;
    public String description;
    public String food1;
    public String food2;
    public String name;
    public int numberOfPersons;
    public String phNo;
    public String township;
    public int confirm_status;

    public Order() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Order(String branch,String date, String description,String food1,String food2,String name,int numberOfPersons,String phNo,String township, int confirm_status) {
        this.branch = branch;
        this.date = date;
        this.description = description;
        this.food1 = food1;
        this.food2 = food2;
        this.name = name;
        this.numberOfPersons = numberOfPersons;
        this.phNo = phNo;
        this.township = township;
        this.confirm_status = confirm_status;
    }

    protected Order(Parcel in) {
        branch = in.readString();
        date = in.readString();
        description = in.readString();
        food1 = in.readString();
        food2 = in.readString();
        name = in.readString();
        numberOfPersons = in.readInt();
        phNo = in.readString();
        township = in.readString();
        confirm_status = in.readInt();
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
        dest.writeString(branch);
        dest.writeString(date);
        dest.writeString(description);
        dest.writeString(food1);
        dest.writeString(food2);
        dest.writeString(name);
        dest.writeInt(numberOfPersons);
        dest.writeString(phNo);
        dest.writeString(township);
        dest.writeInt(confirm_status);
    }
}
