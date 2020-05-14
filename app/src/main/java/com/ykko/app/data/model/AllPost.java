package com.ykko.app.data.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class AllPost {

    public String foodName;
    public String foodType;
    public String foodSize;
    public String price;

    public AllPost() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public AllPost(String foodName,String foodType,String foodSize, String price) {
        this.foodName = foodName;
        this.foodType = foodType;
        this.foodSize = foodSize;
        this.price = price;
    }

}

