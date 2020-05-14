package com.ykko.app.ui.confirm_order;

import androidx.lifecycle.ViewModelProviders;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.ykko.app.HomeActivity;
import com.ykko.app.MainActivity;
import com.ykko.app.R;
import com.ykko.app.data.model.Order;
import com.ykko.app.ui.FirebaseDatabaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ConfirmOrderFragment extends Fragment {

    private ConfirmOrderViewModel mViewModel;
    Order newOrder = new Order();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel =
                ViewModelProviders.of(this).get(ConfirmOrderViewModel.class);
        View root = inflater.inflate(R.layout.fragment_confirm_order, container, false);
        Button cancelbtn = root.findViewById(R.id.cancel_confirm_btn);
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        Button confirmTableBtn = root.findViewById(R.id.confirm_table_btn);
        newOrder.confirm_status = 0;
        confirmTableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                FirebaseDatabaseHelper databaseHelper = new FirebaseDatabaseHelper();
                databaseHelper.addData("orderPosts", newOrder, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsInserted() {
                        Toast.makeText(getActivity(),"Confirm Order Successful",Toast.LENGTH_SHORT).show();
                        String topic = "admin";
                        String title = "Customer Order";
//                        SharedPreferences sp = getContext().getSharedPreferences("FILE_NAME", MODE_PRIVATE);
//                        String username = sp.getString("key","NON");
                        String message = "New order from Customers. Please check";
                        ((HomeActivity)getActivity()).Send_Noti_Data(topic,title,message);
                        Navigation.findNavController(getView()).navigate(R.id.nav_reserved);
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

        TextView confirmName = root.findViewById(R.id.confirm_name);
        TextView confirmPhNo = root.findViewById(R.id.confirm_phNo);
        TextView confirmDate = root.findViewById(R.id.confirm_date);
        TextView confirmBranch = root.findViewById(R.id.confirm_branch);
        TextView confirmTownship = root.findViewById(R.id.confirm_township);
        TextView confirmNoOfPersons = root.findViewById(R.id.confirm_noOfPerson);
        TextView confirmFoodOne = root.findViewById(R.id.confirm_food1);
        TextView confirmFoodTwo= root.findViewById(R.id.confirm_food2);
        TextView confirmDes = root.findViewById(R.id.confirm_des);

        newOrder = getArguments().getParcelable("newOrderKey");
        confirmName.setText(newOrder.name);
        confirmPhNo.setText(newOrder.phNo);
        confirmDate.setText(newOrder.date);
        confirmBranch.setText(newOrder.branch);
        confirmTownship.setText(newOrder.township);
        confirmNoOfPersons.setText(Integer.toString(newOrder.numberOfPersons));
        confirmFoodOne.setText(newOrder.food1);
        confirmFoodTwo.setText(newOrder.food2);
        confirmDes.setText(newOrder.description);
        return root;
    }


}
