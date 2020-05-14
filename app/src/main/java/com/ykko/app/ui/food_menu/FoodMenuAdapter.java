package com.ykko.app.ui.food_menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ykko.app.R;
import com.ykko.app.data.model.FoodMenu;

import java.util.List;

public class FoodMenuAdapter extends RecyclerView.Adapter<FoodMenuAdapter.MyViewHolder> {
    private List<FoodMenu> posts;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView foodStickNameTextView;
        public TextView foodStickTypeTextView;
        public TextView foodStickPriceTextView;
        public ImageView imageView;
        public Button reserveBtn;

        public MyViewHolder(View itemView) {
            super(itemView);
            foodStickNameTextView = itemView.findViewById(R.id.food_stick_name);
            foodStickTypeTextView = itemView.findViewById(R.id.food_stick_type);
            foodStickPriceTextView = itemView.findViewById(R.id.food_stick_price);
            imageView = itemView.findViewById(R.id.visitor_image_view);
            reserveBtn = itemView.findViewById(R.id.reserve_btn);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public FoodMenuAdapter(List<FoodMenu> foodMenuPosts) {
        posts = foodMenuPosts;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FoodMenuAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                                          int viewType) {
        // create a new view\
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View allPostView = inflater.inflate(R.layout.foodmenu_posts_row,parent,false);

        FoodMenuAdapter.MyViewHolder viewHolder  = new FoodMenuAdapter.MyViewHolder(allPostView);
        viewHolder.reserveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.nav_table_reservation);
            }
        });
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(FoodMenuAdapter.MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TextView foodStickTypeTextView = holder.foodStickTypeTextView;
        TextView foodStickNameTextView = holder.foodStickNameTextView;
        TextView foodStickPriceTextView = holder.foodStickPriceTextView;
        ImageView imageView = holder.imageView;

        foodStickNameTextView.setText(posts.get(position).foodStickName);
        foodStickTypeTextView.setText(posts.get(position).foodStickType);
        foodStickPriceTextView.setText(posts.get(position).price);
        Picasso.get().load(posts.get(position).imageUrl).into(imageView);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return posts.size();
    }
}
