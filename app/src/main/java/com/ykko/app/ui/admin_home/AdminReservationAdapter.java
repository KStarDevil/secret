package com.ykko.app.ui.admin_home;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ykko.app.R;
import com.ykko.app.data.model.Order;

import java.util.List;

public class AdminReservationAdapter extends RecyclerView.Adapter<AdminReservationAdapter.MyViewHolder> {
    private List<Order> posts;
    private List<String> keys;
    private Context mContext;
private boolean clickable1 =true;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView confirmNameTextView;
        public TextView confirmPhNoTextView;
        public TextView confirmTownshipTextView;
        public TextView confirmBranchTextView;
        public TextView confirmDateTextView;
        public TextView confirmNoOfPersonsTextView;
        public TextView confirmFoodOneTextView;
        public TextView confirmFoodTwoTextView;
        public TextView confirmDesTextView;
        public TextView confirmstatusTextView;
        public MyViewHolder(View itemView) {
            super(itemView);
            confirmNameTextView = itemView.findViewById(R.id.admin_confirm_name);
            confirmPhNoTextView = itemView.findViewById(R.id.admin_confirm_phNo);
            confirmTownshipTextView = itemView.findViewById(R.id.admin_confirm_township);
            confirmBranchTextView = itemView.findViewById(R.id.admin_confirm_branch);
            confirmDateTextView = itemView.findViewById(R.id.admin_confirm_date);
            confirmNoOfPersonsTextView = itemView.findViewById(R.id.admin_confirm_noOfPerson);
            confirmFoodOneTextView = itemView.findViewById(R.id.admin_confirm_food1);
            confirmFoodTwoTextView = itemView.findViewById(R.id.admin_confirm_food2);
            confirmDesTextView = itemView.findViewById(R.id.admin_confirm_des);
            confirmstatusTextView = itemView.findViewById(R.id.admin_confirm_status);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(v).navigate(R.id.nav_admin_reservation);
                }
            });

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdminReservationAdapter(List<Order> orderPosts,List<String> dataKeys,Context context,boolean clickable) {
        posts = orderPosts;
        keys = dataKeys;
        mContext = context;
        clickable1 =clickable;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdminReservationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                                    int viewType) {
        // create a new view\
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View orderPostView = inflater.inflate(R.layout.admin_reservation_posts_row,parent,false);

        AdminReservationAdapter.MyViewHolder viewHolder  = new AdminReservationAdapter.MyViewHolder(orderPostView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        TextView confirmNameTextView = holder.confirmNameTextView;
        TextView confirmPhNoTextView = holder.confirmPhNoTextView;
        TextView confirmTownshipTextView = holder.confirmTownshipTextView;
        TextView confirmBranchTextView = holder.confirmBranchTextView;
        TextView confirmDateTextView = holder.confirmDateTextView;
        TextView confirmNoOfPersonsTextView = holder.confirmNoOfPersonsTextView;
        TextView confirmFoodOneTextView = holder.confirmFoodOneTextView;
        TextView confirmFoodTwoTextView = holder.confirmFoodTwoTextView;
        TextView confirmDesTextView = holder.confirmDesTextView;
        TextView confirmstatusTextView = holder.confirmDesTextView;
        View itemView = holder.itemView;

        confirmNameTextView.setText(posts.get(position).name);
        confirmPhNoTextView.setText(posts.get(position).phNo);
        confirmTownshipTextView.setText(posts.get(position).township);
        confirmBranchTextView.setText(posts.get(position).branch);
        confirmDateTextView.setText(posts.get(position).date);
        confirmNoOfPersonsTextView.setText(String.valueOf(posts.get(position).numberOfPersons));
        confirmFoodOneTextView.setText(posts.get(position).food1);
        confirmFoodTwoTextView.setText(posts.get(position).food2);
        confirmDesTextView.setText(posts.get(position).description);
        int status = posts.get(position).confirm_status;
        confirmstatusTextView.setBackgroundColor(Color.parseColor("#FF0000"));
        confirmstatusTextView.setTextColor(Color.parseColor("#ffffff"));

        if(status == 1){
            confirmstatusTextView.setBackgroundColor(Color.parseColor("#7C932A"));
            confirmstatusTextView.setText("CONFIRMED");
        }else {
            confirmstatusTextView.setBackgroundColor(Color.parseColor("#e3c500"));
            confirmstatusTextView.setText("Pending");
        }
if(clickable1){

    itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString("orderDetailKeyKey",keys.get(position));
            bundle.putParcelable("orderDetailKey", posts.get(position));
            Navigation.findNavController(v).navigate(R.id.nav_admin_reservation_detail,bundle);
        }
    });
}else{
    itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    });
}


    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


}
