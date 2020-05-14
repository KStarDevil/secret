package com.ykko.app.ui.admin_home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.ykko.app.R;
import com.ykko.app.data.model.Feedback;
import com.ykko.app.ui.FirebaseDatabaseHelper;

import java.util.List;

public class AdminReviewAdapter extends RecyclerView.Adapter<AdminReviewAdapter.MyViewHolder> {
    private List<Feedback> posts;
    private List<String> keys;
    private Context mContext;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView reviewerNameTextView;
        public TextView commentTextView;
        public RatingBar ratingBar;
        public ImageButton deleteBtn;

        public MyViewHolder(View itemView) {
            super(itemView);
            reviewerNameTextView = itemView.findViewById(R.id.admin_reviewer_name);
            commentTextView = itemView.findViewById(R.id.admin_reviewer_comment);
            ratingBar = itemView.findViewById(R.id.admin_reviewer_rating);
            deleteBtn = itemView.findViewById(R.id.admin_review_del_btn);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Navigation.findNavController(v).navigate(R.id.nav_admin_review);
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdminReviewAdapter(List<Feedback> reviewPosts,List<String> dataKeys,Context context) {
        posts = reviewPosts;
        keys = dataKeys;
        mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdminReviewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        // create a new view\
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View reviewPostView = inflater.inflate(R.layout.admin_review_posts_row,parent,false);

        AdminReviewAdapter.MyViewHolder viewHolder  = new AdminReviewAdapter.MyViewHolder(reviewPostView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        TextView reviewerNameTextView = holder.reviewerNameTextView;
        TextView commentTextView = holder.commentTextView;
        RatingBar ratingBar = holder.ratingBar;
        ImageButton deleteBtn = holder.deleteBtn;

        reviewerNameTextView.setText(posts.get(position).reviewerName);
        commentTextView.setText(posts.get(position).comment);
        ratingBar.setRating(posts.get(position).quality);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Alert");
                builder.setMessage("You are about to delete this record of database. Do you really want to proceed ?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabaseHelper databaseHelper = new FirebaseDatabaseHelper();
                        databaseHelper.deleteData("feedbackPosts", keys.get(position), new FirebaseDatabaseHelper.DataStatus() {
                            @Override
                            public void DataIsInserted() {

                            }

                            @Override
                            public void DataIsUpdated() {

                            }

                            @Override
                            public void DataIsDeleted() {
                                Toast.makeText(mContext, "You've chosen to delete this record", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext, "You've changed your mind to delete this record", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

}
