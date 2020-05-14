package com.ykko.app.ui.admin_home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ramotion.circlemenu.CircleMenuView;
import com.ykko.app.R;
import com.ykko.app.data.Global_Variable;
import com.ykko.app.data.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdminHomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;


    EditText edtTitle;
    EditText edtMessage;
    private final String server_key = "AAAAWumHHDE:APA91bEHNpTibtIpKHz_H7qu_nAV0CqzgIi6G7eJmStJdab9da3FwAR9sHHbjciPrmSg0LGJUlkicBBu8iLAqdWyTJ8cMNNf5fH72aHXi6dj--aCYi_cthkY50GnW6YBLY9jAMCsCAv5";
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + server_key;
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        Toolbar toolbar = findViewById(R.id.admin_toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.admin_drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_admin_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        Button logoutbtn =  findViewById(R.id.admin_logout);
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((Global_Variable) getApplicationContext()).setusername("");
                ((Global_Variable) getApplicationContext()).setphone("");
                FirebaseMessaging.getInstance().unsubscribeFromTopic("admin");
                SharedPreferences sp = AdminHomeActivity.this.getSharedPreferences("admin", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.remove("adminusername");
                edit.remove("adminpassword");
                edit.apply();
                finish();
            }
        });
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_admin_home,R.id.nav_admin_reservation,
                R.id.nav_admin_food_menu,R.id.nav_admin_review,
                R.id.nav_admin_add_menu,R.id.nav_admin_edit_menu,R.id.nav_admin_reservation_detail,R.id.nav_admin_food_detail)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_admin_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        CircleMenuView circleMenuView = findViewById(R.id.admin_ramotion_circle_menu);
        circleMenuView.setEventListener(new CircleMenuView.EventListener(){
            @Override
            public void onButtonClickAnimationStart(@NonNull CircleMenuView circleView, int index) {
                toMenu(index);
            }
        });

    }

    public void toMenu(int index){
        switch (index){
            case 0:
                Navigation.findNavController(this,R.id.nav_admin_host_fragment).navigate(R.id.nav_admin_home);
                break;
            case 1:
                Navigation.findNavController(this,R.id.nav_admin_host_fragment).navigate(R.id.nav_admin_reservation);
                break;
            case 2:
                Navigation.findNavController(this,R.id.nav_admin_host_fragment).navigate(R.id.nav_admin_food_menu);
                break;
            case 3:
                Navigation.findNavController(this,R.id.nav_admin_host_fragment).navigate(R.id.nav_admin_review);
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_admin_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    public void Send_Noti_Data(String topic, String Title,String Message,String username){
        TOPIC = "/topics/"+topic; //topic must match with what the receiver subscribed to
        NOTIFICATION_TITLE = Title;
        NOTIFICATION_MESSAGE = Message;

        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        JSONObject data = new JSONObject();
        try {
            notifcationBody.put("title", NOTIFICATION_TITLE);
            notifcationBody.put("text", NOTIFICATION_MESSAGE);
            notifcationBody.put("click_action", "CUSTOMER_HOME");
            data.put("username", username);
            notification.put("to", TOPIC);
            notification.put("notification", notifcationBody);
            notification.put("data", data);
        } catch (JSONException e) {
            Log.e(TAG, "onCreate: " + e.getMessage() );
        }
        sendNotification(notification);
    }
    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
}
