package com.ykko.app.data.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class PopularPost {

    public String foodName;
    public String price;
    public int quality;

    public PopularPost() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public PopularPost(String foodName,int quality, String price) {
        this.foodName = foodName;
        this.quality = quality;
        this.price = price;
    }

}
