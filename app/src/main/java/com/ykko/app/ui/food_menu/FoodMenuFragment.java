package com.ykko.app.ui.food_menu;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ykko.app.R;
import com.ykko.app.data.model.FoodMenu;

import java.util.ArrayList;
import java.util.List;

public class FoodMenuFragment extends Fragment {

    private List<FoodMenu> foodMenuPosts = new ArrayList<>();
    private List<String> keys = new ArrayList<>();
    private FoodMenuViewModel foodMenuViewModel;

    private RecyclerView foodmenuPostsView;
    private RecyclerView.Adapter foodmenuPostsViewAdapter;
    private RecyclerView.LayoutManager foodmenuPostsLayoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        foodMenuViewModel =
                ViewModelProviders.of(this).get(FoodMenuViewModel.class);
        View root = inflater.inflate(R.layout.fragment_food_menu, container, false);
        foodmenuPostsView = root.findViewById(R.id.foodmenu_posts_view);
        foodmenuPostsView.setHasFixedSize(true);
        foodmenuPostsLayoutManager = new LinearLayoutManager(getActivity());
        foodmenuPostsView.setLayoutManager(foodmenuPostsLayoutManager);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference menuPostsRef = database.getReference("menuPosts").child("posts");

        ValueEventListener menuPostListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                keys.clear();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    FoodMenu post = keyNode.getValue(FoodMenu.class);
                    foodMenuPosts.add(post);
                }

                foodmenuPostsViewAdapter = new FoodMenuAdapter(foodMenuPosts);
                foodmenuPostsView.setAdapter(foodmenuPostsViewAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("", "loadPost:onCancelled", databaseError.toException());
            }
        };
        menuPostsRef.addValueEventListener(menuPostListener);

        return root;
    }

}
