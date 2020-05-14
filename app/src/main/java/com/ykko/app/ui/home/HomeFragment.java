package com.ykko.app.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ykko.app.R;
import com.ykko.app.data.model.AllPost;
import com.ykko.app.data.model.FoodMenu;
import com.ykko.app.data.model.PopularPost;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private List<PopularPost> popularPosts = new ArrayList<>();
    private List<AllPost> allPosts = new ArrayList<>();
    private List<String> keys = new ArrayList<>();

    private HomeViewModel homeViewModel;
    private List<FoodMenu> foodMenuPosts = new ArrayList<>();
    private List<FoodMenu> PopularFood = new ArrayList<>();
    private List<FoodMenu> allFood = new ArrayList<>();
    private RecyclerView popularPostsView;
    private RecyclerView.Adapter popularPostsViewAdapter;
    private RecyclerView.LayoutManager popularPostsLayoutManager;

    private RecyclerView allPostsView;
    private RecyclerView.Adapter allPostsViewAdapter;
    private RecyclerView.LayoutManager allPostsViewLayoutManager;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);

        View root = inflater.inflate(R.layout.fragment_visitor_home, container, false);

        popularPostsView = root.findViewById(R.id.popular_posts_view);
        popularPostsView.setHasFixedSize(true);
        popularPostsLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
        popularPostsView.setLayoutManager(popularPostsLayoutManager);

        allPostsView = root.findViewById(R.id.all_posts_view);
        allPostsView.setHasFixedSize(true);
        allPostsViewLayoutManager = new LinearLayoutManager(getActivity());
        allPostsView.setLayoutManager(allPostsViewLayoutManager);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference popularPostsRef = database.getReference("menuPosts").child("posts");
        DatabaseReference allPostsRef = database.getReference("menuPosts").child("posts");

        ValueEventListener popularPostListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PopularFood.clear();
                // Get Post object and use the values to update the UI
                keys.clear();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    FoodMenu post = keyNode.getValue(FoodMenu.class);
                    if(post != null) {
                        if (post.price != null) {
                            int price = Integer.parseInt(post.price);
                            if (price < 5000) {
                                PopularFood.add(post);
                            }
                        }
                    }
                }
                popularPostsViewAdapter = new HomePopularPostsAdapter(PopularFood);
                popularPostsView.setAdapter(popularPostsViewAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("", "loadPost:onCancelled", databaseError.toException());
            }
        };
        ValueEventListener allPostListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allFood.clear();
                // Get Post object and use the values to update the UI
                keys.clear();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    keys.add(keyNode.getKey());
//                    AllPost post = keyNode.getValue(AllPost.class);
//                    allPosts.add(post);
                    FoodMenu post = keyNode.getValue(FoodMenu.class);
                    allFood.add(post);
                }

                allPostsViewAdapter = new HomeAllPostsAdapter(allFood);
                allPostsView.setAdapter(allPostsViewAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("", "loadPost:onCancelled", databaseError.toException());
            }
        };
        popularPostsRef.addValueEventListener(popularPostListener);
        allPostsRef.addValueEventListener(allPostListener);
        //        final TextView textView = root.findViewById(R.id.text_home);
        //        homeViewModel.getText().observe(this, new Observer<String>() {
        //            @Override
        //            public void onChanged(@Nullable String s) {
        //                textView.setText(s);
        //            }
        //        });
        return root;
    }
}