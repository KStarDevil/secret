package com.ykko.app.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ykko.app.R;
import com.ykko.app.data.model.FoodMenu;
import com.ykko.app.data.model.PopularPost;

import java.util.List;

public class HomePopularPostsAdapter extends RecyclerView.Adapter<HomePopularPostsAdapter.MyViewHolder> {
    private List<FoodMenu> posts;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
//        public TextView textView;
//        public TextView foodNameTextView;
        public RatingBar ratingBar;

        public TextView foodStickNameTextView;
        public TextView foodStickTypeTextView;
        public TextView foodStickPriceTextView;
        public ImageView imageView;
        //public Button reserveBtn;

        public MyViewHolder(View itemView) {
            super(itemView);
//            textView = itemView.findViewById(R.id.popular_food_price);
//            foodNameTextView = itemView.findViewById(R.id.popular_food_name);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            foodStickNameTextView = itemView.findViewById(R.id.popular_food_name);
            foodStickPriceTextView = itemView.findViewById(R.id.popular_food_price);
            imageView = itemView.findViewById(R.id.foodImage);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public HomePopularPostsAdapter( List<FoodMenu> foodMenuPosts) {
        posts = foodMenuPosts;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public HomePopularPostsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        // create a new view\
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View allPostView = inflater.inflate(R.layout.visitor_popular_posts_row,parent,false);

        MyViewHolder viewHolder  = new MyViewHolder(allPostView);
        return viewHolder ;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        TextView textView = holder.textView;
//        TextView foodNameTextView = holder.foodNameTextView;
//        RatingBar ratingBar = holder.ratingBar;
//
//        textView.setText(posts.get(position).price);
//        foodNameTextView.setText(posts.get(position).foodName);
//        ratingBar.setNumStars(posts.get(position).quality);

        TextView foodStickTypeTextView = holder.foodStickTypeTextView;
        TextView foodStickNameTextView = holder.foodStickNameTextView;
        TextView foodStickPriceTextView = holder.foodStickPriceTextView;
        ImageView imageView = holder.imageView;

        foodStickNameTextView.setText(posts.get(position).foodStickName);
//        foodStickTypeTextView.setText(posts.get(position).foodStickType);
        foodStickPriceTextView.setText(posts.get(position).price);
        Picasso.get().load(posts.get(position).imageUrl).into(imageView);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return posts.size();
    }
}
