package com.ykko.app.ui.admin_reservations;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.ykko.app.R;
import com.ykko.app.data.model.Order;

import java.util.List;

public class AdminReservationListAdapter extends RecyclerView.Adapter<AdminReservationListAdapter.MyViewHolder> {
    private List<Order> posts;
    private List<String> keys;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView confirmNoTextView;
        public TextView confirmNameTextView;
        public TextView confirmPhNoTextView;
       // public ImageButton editbtn;

        public MyViewHolder(View itemView) {
            super(itemView);
            confirmNoTextView = itemView.findViewById(R.id.admin_res_list_no);
            confirmNameTextView = itemView.findViewById(R.id.admin_res_list_name);
            confirmPhNoTextView = itemView.findViewById(R.id.admin_res_list_phNo);
            //editbtn = itemView.findViewById(R.id.admin_res_list_del_btn);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(v).navigate(R.id.nav_admin_reservation);
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdminReservationListAdapter(List<Order> orderPosts,List<String> dataKeys) {
        keys = dataKeys;
        posts = orderPosts;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdminReservationListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                       int viewType) {
        // create a new view\
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View orderPostView = inflater.inflate(R.layout.admin_reservation_list_posts_view, parent, false);

        AdminReservationListAdapter.MyViewHolder viewHolder = new AdminReservationListAdapter.MyViewHolder(orderPostView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdminReservationListAdapter.MyViewHolder holder, final int position) {
        TextView confirmNoTextView = holder.confirmNoTextView;
        TextView confirmNameTextView = holder.confirmNameTextView;
        TextView confirmPhNoTextView = holder.confirmPhNoTextView;
        //ImageButton editbtn = holder.editbtn;
        View itemView = holder.itemView;
        confirmNoTextView.setText(String.valueOf(position));
        confirmNameTextView.setText(posts.get(position).name);
        confirmPhNoTextView.setText(posts.get(position).phNo);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("orderDetailKeyKey",keys.get(position));
                bundle.putParcelable("orderDetailKey", posts.get(position));
                Navigation.findNavController(v).navigate(R.id.nav_admin_reservation_detail,bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


}
