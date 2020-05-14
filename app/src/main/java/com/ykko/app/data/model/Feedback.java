package com.ykko.app.data.model;

public class Feedback {
    public String reviewerName;
    public String comment;
    public float quality;

    public Feedback() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Feedback(String reviewerName,String comment,int quality) {
        this.reviewerName = reviewerName;
        this.comment = comment;
        this.quality = quality;
    }
}
