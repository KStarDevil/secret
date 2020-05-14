package com.ykko.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.ykko.app.data.model.AllPost;
import com.ykko.app.data.model.PopularPost;
import com.ykko.app.ui.admin_home.AdminHomeActivity;
import com.ykko.app.ui.admin_login.AdminLoginActivity;


public class HomeLoginActivity extends Activity {

    private static final String TAG = "FirebaseMessagingService : ";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button btn = findViewById(R.id.visitor_button);
        Button adminBtn = findViewById(R.id.admin_button);


        final Intent Customer_Home_intent = new Intent(this, HomeActivity.class);
        final Intent customerintent = new Intent(this, CustomerLoginActivity.class);
        final Intent adminIntent = new Intent(this, AdminLoginActivity.class);
        final Intent Admin_Home_intent = new Intent(this, AdminHomeActivity.class);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("FILE_NAME", MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();

                String username = sp.getString("key","NON");
                String phone = sp.getString("phone","NON");
                if(!username.equals("NON") && !phone.equals("NON")){
                    startActivity(Customer_Home_intent);

                }else {
                    startActivity(customerintent);
                }

            }
        });

        adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("admin", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                String admin_username = sp.getString("adminusername","NON");
                String admin_password = sp.getString("adminpassword","NON");
                if(!admin_username.equals("NON") && !admin_password.equals("NON")){
                    startActivity(Admin_Home_intent);
                }else {
                    startActivity(adminIntent);
                }

            }
        });


    }
}
