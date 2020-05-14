package com.ykko.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ramotion.circlemenu.CircleMenuView;
import com.ykko.app.data.Global_Variable;
import com.ykko.app.data.MySingleton;
import com.ykko.app.ui.gallery.GalleryFragment;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences sp = getSharedPreferences("FILE_NAME", MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        String T = sp.getString("key", "NON");
        String T1 = ((Global_Variable) this.getApplication()).getphone();
        //Toast.makeText(getApplicationContext(), "Username = "+ T +"Phone : "+T1, Toast.LENGTH_SHORT).show();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        Button logoutbtn = findViewById(R.id.logout);
        TextView Welcome_text = findViewById(R.id.Welcome_Text);
        Welcome_text.setText("Welcome Back " + T);
        // Passing each menu ID as a set of Ids because each
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((Global_Variable) getApplicationContext()).setusername("");
                ((Global_Variable) getApplicationContext()).setphone("");
                SharedPreferences sp = getSharedPreferences("FILE_NAME", MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.remove("key");
                edit.remove("phone");
                edit.apply();
                FirebaseMessaging.getInstance().unsubscribeFromTopic("customer");
                finish();
            }
        });
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_reservation, R.id.nav_branches, R.id.nav_food_menu, R.id.nav_feedback,
                R.id.nav_table_reservation, R.id.nav_confirm_order, R.id.nav_reserved)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        CircleMenuView circleMenuView = findViewById(R.id.ramotion_circle_menu);
        circleMenuView.setEventListener(new CircleMenuView.EventListener() {
            @Override
            public void onButtonClickAnimationStart(@NonNull CircleMenuView circleView, int index) {
                toMenu(index);
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        //FragmentTransaction ft = fragmentManager.beginTransaction();

        Intent intent = this.getIntent();

//        if (intent != null) {
//
//            String menuFragment = getIntent().getStringExtra("GALLERY");
//
//            if (menuFragment != null) {
//                if (menuFragment.equals("GALLERY")) {
//                    GalleryFragment f1 = new GalleryFragment();
//                    //fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, f1).commit();
////                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
////                    fragmentTransaction.replace(R.id.nav_host_fragment, f1,"My Order");
////                    fragmentTransaction.commit();
//                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                    transaction.replace(R.id.nav_host_fragment, f1);
//                    transaction.commit();
//                    navigationView.getMenu().getItem(1).setChecked(true);
//                }
//            }
//        }
    }

    public void toMenu(int index) {
        switch (index) {
            case 0:
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_home);
                break;
            case 1:
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_reservation);
                break;
            case 2:
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_branches);
                break;
            case 3:
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_food_menu);
                break;
            case 4:
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_feedback);
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    public void Send_Noti_Data(String topic, String Title, String Message) {
        TOPIC = "/topics/" + topic; //topic must match with what the receiver subscribed to
        NOTIFICATION_TITLE = Title;
        NOTIFICATION_MESSAGE = Message;

        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        JSONObject data = new JSONObject();
        try {
            notifcationBody.put("title", NOTIFICATION_TITLE);
            notifcationBody.put("text", NOTIFICATION_MESSAGE);
            notifcationBody.put("click_action", "ADMIN_HOME");
            data.put("username", "Admin");
            notification.put("to", TOPIC);
            notification.put("notification", notifcationBody);
            notification.put("data", data);
        } catch (JSONException e) {
            Log.e(TAG, "onCreate: " + e.getMessage());
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
                }) {
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
