package com.ykko.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ykko.app.data.Global_Variable;
import com.ykko.app.data.model.User;
import com.ykko.app.ui.FirebaseDatabaseHelper;

import java.util.ArrayList;


public class CustomerLoginActivity extends Activity {

    private ArrayList<User> Users = new ArrayList<>();
    User newOrder = new User();

    public Button loginbtn;
    public Button signupbtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_login);
        loginbtn = findViewById(R.id.login);
        signupbtn = findViewById(R.id.sign_up);
        final TextView username = findViewById(R.id.customer_username);
        final TextView phone = findViewById(R.id.customer_phone);


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username12 = username.getText().toString();
                String phone12 = phone.getText().toString();
                login_data(username12, phone12, Users);
            }
        });

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username12 = username.getText().toString();
                String phone12 = phone.getText().toString();
                signup_data(username12, phone12, Users);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference getuser = database.getReference("userposts").child("posts");
//

        getuser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Users.clear();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    User post = keyNode.getValue(User.class);
                    if (post != null) {
                        Users.add(post);
                    }
                }
                signupbtn.setEnabled(true);
                loginbtn.setEnabled(true);
                //   Toast.makeText(getApplicationContext(), "Users added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void login_data(final String username1, final String phone1, ArrayList<User> Users1) {
        final Intent intent = new Intent(this, HomeActivity.class);
        boolean bl = false;
        if (username1 == null || phone1 == null || TextUtils.isEmpty(username1) || TextUtils.isEmpty(phone1)) {
            Toast.makeText(getApplicationContext(), "Please Fill Username and Phone", Toast.LENGTH_SHORT).show();
        } else {
            for (final User transactionLine : Users1) {
                if (transactionLine.username.equals(username1) && transactionLine.phone.equals(phone1)) {
                    bl = true;
                }
            }
            if (!bl) {
                Toast.makeText(getApplicationContext(), "Username or Phone wrong", Toast.LENGTH_SHORT).show();
            } else {
                ((Global_Variable) this.getApplication()).setusername(username1);
                ((Global_Variable) this.getApplication()).setphone(phone1);
                SharedPreferences sp = getSharedPreferences("FILE_NAME", MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("key",username1);
                edit.putString("phone",phone1);
                edit.apply();
                FirebaseMessaging.getInstance().subscribeToTopic("customer");
                startActivity(intent);
            }
        }
    }

    public void signup_data(final String username1, final String phone1, ArrayList<User> Users1) {
        final Intent intent = new Intent(this, HomeActivity.class);
        User us = new User();
        us.username = username1;
        us.phone = phone1;
        boolean bl = false;
        final boolean signuped = false;
        if (username1 == null || phone1 == null || TextUtils.isEmpty(username1) || TextUtils.isEmpty(phone1)) {
            Toast.makeText(getApplicationContext(), "Please Fill Username and Phone", Toast.LENGTH_SHORT).show();
        } else {

            for (final User transactionLine : Users1) {
                if (transactionLine.username.equals(username1)) {
                    bl = true;
                }
            }
            if (bl) {
                Toast.makeText(getApplicationContext(), "Username already exist", Toast.LENGTH_SHORT).show();
            } else {
                FirebaseDatabaseHelper databaseHelper = new FirebaseDatabaseHelper();
                databaseHelper.addData("userposts", us, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsInserted() {
                        Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                        ((Global_Variable) getApplicationContext()).setusername(username1);
                        ((Global_Variable) getApplicationContext()).setphone(phone1);
                        // save data into share SharePreference
                        SharedPreferences sp = getSharedPreferences("FILE_NAME", MODE_PRIVATE);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString("key",username1);
                        edit.putString("phone",phone1);
                        edit.apply();
                        FirebaseMessaging.getInstance().subscribeToTopic("customer");

                        // get data from share SharePreference
                        //SharedPreferences sp = getSharedPreferences("FILE_NAME", MODE_PRIVATE);
                       // String result = sp.getString();
                        startActivity(intent);
                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {

                    }
                });


            }
        }
    }
}


