package com.ykko.app.ui.reservation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.ykko.app.R;
import com.ykko.app.data.model.Reservation;

import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.MyViewHolder> {
    private List<Reservation> posts;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView shopNameTextView;
        public TextView shopHoursTextView;
        public TextView shopPhNoTextView;
        public Button reserveBtn;

        public MyViewHolder(View itemView) {
            super(itemView);
            shopNameTextView = itemView.findViewById(R.id.shop_name);
            shopHoursTextView = itemView.findViewById(R.id.shop_hours);
            shopPhNoTextView = itemView.findViewById(R.id.shop_ph);

            reserveBtn = itemView.findViewById(R.id.reserve_btn);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ReservationAdapter(List<Reservation> reservationPosts) {
        posts = reservationPosts;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ReservationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        // create a new view\
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View allPostView = inflater.inflate(R.layout.reservation_posts_row,parent,false);

        MyViewHolder viewHolder  = new MyViewHolder(allPostView);
        viewHolder.reserveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.nav_table_reservation);
            }
        });
        return viewHolder ;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TextView shopNameTextView = holder.shopNameTextView;
        TextView shopHoursTextView = holder.shopHoursTextView;
        TextView shopPhNoTextView = holder.shopPhNoTextView;

        shopNameTextView.setText(posts.get(position).branchName);
        shopHoursTextView.setText(posts.get(position).hours);
        shopPhNoTextView.setText(posts.get(position).phNo);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return posts.size();
    }
}
