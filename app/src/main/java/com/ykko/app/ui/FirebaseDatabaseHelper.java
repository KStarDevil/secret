package com.ykko.app.ui;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FirebaseDatabaseHelper {

    private FirebaseDatabase mDatabase;

    public interface DataStatus{
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public FirebaseDatabaseHelper(){
        mDatabase = FirebaseDatabase.getInstance();
    }

    public void addData(String path,Object object,final DataStatus dataStatus){
        DatabaseReference mRef = mDatabase.getReference(path).child("posts");
        String key = mRef.push().getKey();
        mRef.child(key).setValue(object).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsInserted();
            }
        });
    }



    public void deleteData(String path, String key ,final DataStatus dataStatus){
        DatabaseReference mRef = mDatabase.getReference(path).child("posts");
        mRef.child(key).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsDeleted();
            }
        });
    }
}
