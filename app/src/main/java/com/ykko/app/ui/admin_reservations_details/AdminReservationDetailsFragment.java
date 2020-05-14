package com.ykko.app.ui.admin_reservations_details;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ykko.app.HomeActivity;
import com.ykko.app.R;
import com.ykko.app.data.model.Order;
import com.ykko.app.ui.FirebaseDatabaseHelper;
import com.ykko.app.ui.admin_home.AdminHomeActivity;

import java.util.ArrayList;
import java.util.List;

public class AdminReservationDetailsFragment extends Fragment {


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_admin_reservation_details, container, false);

        final Order order = getArguments().getParcelable("orderDetailKey");
        final String key = getArguments().getString("orderDetailKeyKey");

        TextView confirmNameTextView = root.findViewById(R.id.admin_confirm_name);
        TextView confirmPhNoTextView = root.findViewById(R.id.admin_confirm_phNo);
        TextView confirmTownshipTextView = root.findViewById(R.id.admin_confirm_township);
        TextView confirmBranchTextView = root.findViewById(R.id.admin_confirm_branch);
        TextView confirmDateTextView = root.findViewById(R.id.admin_confirm_date);
        TextView confirmNoOfPersonsTextView = root.findViewById(R.id.admin_confirm_noOfPerson);
        TextView confirmFoodOneTextView = root.findViewById(R.id.admin_confirm_food1);
        //TextView confirmFoodTwoTextView = root.findViewById(R.id.admin_confirm_food2);
        TextView confirmDesTextView = root.findViewById(R.id.admin_confirm_des);
        TextView confirmstatusTextView = root.findViewById(R.id.admin_confirm_status);

        confirmstatusTextView.setBackgroundColor(Color.parseColor("#FF0000"));
        confirmstatusTextView.setTextColor(Color.parseColor("#ffffff"));

        int order_status = order.confirm_status;
        if(order_status == 1){
            confirmstatusTextView.setBackgroundColor(Color.parseColor("#7C932A"));
            confirmstatusTextView.setText("CONFIRMED");
        }else {
            confirmstatusTextView.setBackgroundColor(Color.parseColor("#e3c500"));
            confirmstatusTextView.setText("Pending");
        }

        Button deleteBtn = root.findViewById(R.id.admin_delete_res_btn);
        Button confirmbtn = root.findViewById(R.id.admin_confirm_btn);
        confirmNameTextView.setText(order.name);
        confirmPhNoTextView.setText(order.phNo);
        confirmTownshipTextView.setText(order.township);
        confirmBranchTextView.setText(order.branch);
        confirmDateTextView.setText(order.date);
        confirmNoOfPersonsTextView.setText(String.valueOf(order.numberOfPersons));
        confirmFoodOneTextView.setText(order.food1);
        //confirmFoodTwoTextView.setText(order.food2);
        confirmDesTextView.setText(order.description);

        confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order.confirm_status = 1;
                FirebaseDatabaseHelper databaseHelper = new FirebaseDatabaseHelper();
                databaseHelper.deleteData("orderPosts", key, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {

                    }
                });
                databaseHelper.addData("orderPosts", order, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsInserted() {
                        Toast.makeText(getActivity(),"Confirm Order Successful",Toast.LENGTH_SHORT).show();
                        String topic = "customer";
                        String title = "Order CONFIRMED";
//                        SharedPreferences sp = getContext().getSharedPreferences("FILE_NAME", MODE_PRIVATE);
//                        String username = sp.getString("key","NON");
                        String message = "Your Order has been CONFIRMED";

                        ((AdminHomeActivity)getActivity()).Send_Noti_Data(topic,title,message,order.name);
                        assert getFragmentManager() != null;
                        getFragmentManager().popBackStack();
                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {

                    }
                });

            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabaseHelper databaseHelper = new FirebaseDatabaseHelper();
                databaseHelper.deleteData("orderPosts", key, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {
                        Toast.makeText(getActivity(),"Delete Reservation",Toast.LENGTH_SHORT).show();
                        String topic = "customer";
                        String title = "Order DECLINED";
//                        SharedPreferences sp = getContext().getSharedPreferences("FILE_NAME", MODE_PRIVATE);
//                        String username = sp.getString("key","NON");
                        String message = "Your Order has been DECLINED";

                        ((AdminHomeActivity)getActivity()).Send_Noti_Data(topic,title,message,order.name);
                        getFragmentManager().popBackStack();
                    }
                });

            }
        });
        return root;
    }
}
