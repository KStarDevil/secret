package com.ykko.app.ui.admin_home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ykko.app.R;
import com.ykko.app.data.model.FoodMenu;
import com.ykko.app.ui.FirebaseDatabaseHelper;

import java.util.List;

public class AdminMenuAdapter extends RecyclerView.Adapter<AdminMenuAdapter.MyViewHolder> {
    private List<FoodMenu> posts;
    private List<String> keys;
    private Context mContext;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView foodStickNameTextView;
        public TextView foodStickTypeTextView;
        public TextView priceTextView;
        public TextView branchTextView;
        public ImageView imageView;
        public ImageButton editBtn;
        public ImageButton deleteBtn;

        public MyViewHolder(View itemView) {
            super(itemView);
            foodStickNameTextView = itemView.findViewById(R.id.admin_food_stick_name);
            foodStickTypeTextView = itemView.findViewById(R.id.admin_food_stick_type);
            priceTextView = itemView.findViewById(R.id.admin_food_stick_price);
            branchTextView = itemView.findViewById(R.id.admin_available_branch);
            imageView = itemView.findViewById(R.id.menu_image);
            editBtn = itemView.findViewById(R.id.admin_menu_edit_btn);
            deleteBtn = itemView.findViewById(R.id.admin_menu_del_btn);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdminMenuAdapter(List<FoodMenu> foodMenuPosts, List<String> dataKeys, Context context) {
        posts = foodMenuPosts;
        keys = dataKeys;
        mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdminMenuAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        // create a new view\
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View menuPostView = inflater.inflate(R.layout.admin_foodmenu_posts_row,parent,false);

        AdminMenuAdapter.MyViewHolder viewHolder  = new AdminMenuAdapter.MyViewHolder(menuPostView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        TextView foodStickNameTextView = holder.foodStickNameTextView;
        TextView foodStickTypeTextView = holder.foodStickTypeTextView;
        TextView priceTextView = holder.priceTextView;
        TextView branchTextView = holder.branchTextView;
        ImageView imageView = holder.imageView;
        ImageButton editBtn = holder.editBtn;
        ImageButton deleteBtn = holder.deleteBtn;
        View itemView = holder.itemView;

        foodStickNameTextView.setText(posts.get(position).foodStickName);
        foodStickTypeTextView.setText(posts.get(position).foodStickType);
        priceTextView.setText(posts.get(position).price);
        branchTextView.setText(posts.get(position).available_branch);
        Picasso.get().load(posts.get(position).imageUrl).into(imageView);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("menuDetailKeyKey",keys.get(position));
                bundle.putParcelable("menuDetailKey", posts.get(position));
                Navigation.findNavController(v).navigate(R.id.nav_admin_food_detail,bundle);
            }
        });
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
                        databaseHelper.deleteData("menuPosts", String.valueOf(keys.get(position)), new FirebaseDatabaseHelper.DataStatus() {
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

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("menuDetailKeyKey",keys.get(position));
                bundle.putParcelable("menuDetailKey", posts.get(position));
                Navigation.findNavController(v).navigate(R.id.nav_admin_food_detail,bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

}
