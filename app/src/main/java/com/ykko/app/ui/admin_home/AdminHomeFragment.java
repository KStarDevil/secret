package com.ykko.app.ui.admin_home;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ykko.app.R;
import com.ykko.app.data.model.Feedback;
import com.ykko.app.data.model.FoodMenu;
import com.ykko.app.data.model.Order;

import java.util.ArrayList;
import java.util.List;

public class AdminHomeFragment extends Fragment {

    private AdminHomeViewModel mViewModel;
    private List<Order> orderPosts = new ArrayList<>();
    private List<String> keys = new ArrayList<>();

    private List<FoodMenu> foodMenuPosts = new ArrayList<>();
    private List<String> foodMenuKeys = new ArrayList<>();

    private List<Feedback> reviewPosts = new ArrayList<>();
    private List<String> reviewKeys = new ArrayList<>();


    private RecyclerView orderPostsView;
    private RecyclerView.Adapter orderPostsViewAdapter;
    private RecyclerView.LayoutManager orderPostsViewLayoutManager;

    private RecyclerView foodMenuPostsView;
    private RecyclerView.Adapter foodMenuPostsViewAdapter;
    private RecyclerView.LayoutManager foodMenuPostsViewLayoutManager;

    private RecyclerView reviewPostsView;
    private RecyclerView.Adapter reviewPostsViewAdapter;
    private RecyclerView.LayoutManager reviewPostsViewLayoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel =
                ViewModelProviders.of(this).get(AdminHomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_admin_home, container, false);

        TextView reservationsViewAllTextView = root.findViewById(R.id.admin_reservations_view_all);
        TextView foodMenuViewAllTextView = root.findViewById(R.id.admin_food_menu_view_all);
        TextView reviewsViewAllTextView = root.findViewById(R.id.admin_reviews_view_all);

        reservationsViewAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(),R.id.nav_admin_host_fragment).navigate(R.id.nav_admin_reservation);
            }
        });

        foodMenuViewAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(),R.id.nav_admin_host_fragment).navigate(R.id.nav_admin_food_menu);
            }
        });

        reviewsViewAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(),R.id.nav_admin_host_fragment).navigate(R.id.nav_admin_review);
            }
        });

        orderPostsView = root.findViewById(R.id.order_posts_view);
        orderPostsView.setHasFixedSize(true);
        orderPostsViewLayoutManager = new LinearLayoutManager(getActivity());
        orderPostsView.setLayoutManager(orderPostsViewLayoutManager);

        reviewPostsView = root.findViewById(R.id.admin_review_posts_view);
        reviewPostsView.setHasFixedSize(true);
        reviewPostsViewLayoutManager = new LinearLayoutManager(getActivity());
        reviewPostsView.setLayoutManager(reviewPostsViewLayoutManager);

        foodMenuPostsView = root.findViewById(R.id.admin_foodmenu_posts_view);
        foodMenuPostsView.setHasFixedSize(true);
        foodMenuPostsViewLayoutManager = new LinearLayoutManager(getActivity());
        foodMenuPostsView.setLayoutManager(foodMenuPostsViewLayoutManager);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference orderPostsRef = database.getReference("orderPosts").child("posts");
        DatabaseReference reviewPostsRef = database.getReference("feedbackPosts").child("posts");
        DatabaseReference foodMenuPostsRef = database.getReference("menuPosts").child("posts");

        ValueEventListener orderPostListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                keys.clear();
                orderPosts.clear();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    Order post = keyNode.getValue(Order.class);
                    orderPosts.add(post);
                }

                orderPostsViewAdapter = new AdminReservationAdapter(orderPosts,keys,getContext(),true);
                orderPostsView.setAdapter(orderPostsViewAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("", "loadPost:onCancelled", databaseError.toException());
            }
        };

        ValueEventListener reviewPostListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                reviewKeys.clear();
                reviewPosts.clear();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    reviewKeys.add(keyNode.getKey());
                    Feedback post = keyNode.getValue(Feedback.class);
                    reviewPosts.add(post);
                }

                reviewPostsViewAdapter = new AdminReviewAdapter(reviewPosts,reviewKeys,getContext());
                reviewPostsView.setAdapter(reviewPostsViewAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("", "loadPost:onCancelled", databaseError.toException());
            }
        };

        ValueEventListener menuPostListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                foodMenuKeys.clear();
                foodMenuPosts.clear();

                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    foodMenuKeys.add(keyNode.getKey());
                    FoodMenu post = keyNode.getValue(FoodMenu.class);
                    foodMenuPosts.add(post);
                }

                foodMenuPostsViewAdapter = new AdminMenuAdapter(foodMenuPosts,foodMenuKeys,getContext());
                foodMenuPostsView.setAdapter(foodMenuPostsViewAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("", "loadPost:onCancelled", databaseError.toException());
            }
        };

        orderPostsRef.addValueEventListener(orderPostListener);
        foodMenuPostsRef.addValueEventListener(menuPostListener);
        reviewPostsRef.addValueEventListener(reviewPostListener);
        return root;
    }



}
