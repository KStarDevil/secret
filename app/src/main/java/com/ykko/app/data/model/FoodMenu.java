package com.ykko.app.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class FoodMenu implements Parcelable {
    public String foodStickName;
    public String foodStickType;
    public String price;
    public String available_branch;
    public String imageUrl;

    public FoodMenu() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public FoodMenu(String foodStickName, String foodStickType, String price,String available_branch,String imageUrl) {
        this.foodStickName = foodStickName;
        this.foodStickType = foodStickType;
        this.price = price;
        this.available_branch = available_branch;
        this.imageUrl = imageUrl;
    }

    protected FoodMenu(Parcel in) {
        foodStickName = in.readString();
        foodStickType = in.readString();
        price = in.readString();
        available_branch = in.readString();
        imageUrl = in.readString();
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static final Creator<FoodMenu> CREATOR = new Creator<FoodMenu>() {
        @Override
        public FoodMenu createFromParcel(Parcel in) {
            return new FoodMenu(in);
        }

        @Override
        public FoodMenu[] newArray(int size) {
            return new FoodMenu[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(foodStickName);
        dest.writeString(foodStickType);
        dest.writeString(price);
        dest.writeString(available_branch);
        dest.writeString(imageUrl);
    }
}
