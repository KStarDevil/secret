package com.ykko.app.ui.admin_review;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.ykko.app.data.model.Feedback;
import com.ykko.app.ui.admin_home.AdminReviewAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdminReviewFragment extends Fragment {

    private AdminReviewViewModel mViewModel;
    private List<Feedback> reviewPosts = new ArrayList<>();
    private List<String> keys = new ArrayList<>();

    private RecyclerView reviewPostsView;
    private RecyclerView.Adapter reviewPostsViewAdapter;
    private RecyclerView.LayoutManager reviewPostsViewLayoutManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(this).get(AdminReviewViewModel.class);
        View root = inflater.inflate(R.layout.fragment_admin_review, container, false);

        reviewPostsView = root.findViewById(R.id.admin_review_posts_view);
        reviewPostsView.setHasFixedSize(true);
        reviewPostsViewLayoutManager = new LinearLayoutManager(getActivity());
        reviewPostsView.setLayoutManager(reviewPostsViewLayoutManager);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reviewPostsRef = database.getReference("feedbackPosts").child("posts");
        ValueEventListener reviewPostListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                keys.clear();
                reviewPosts.clear();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    Feedback post = keyNode.getValue(Feedback.class);
                    reviewPosts.add(post);
                }

                reviewPostsViewAdapter = new AdminReviewAdapter(reviewPosts,keys,getContext());
                reviewPostsView.setAdapter(reviewPostsViewAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("", "loadPost:onCancelled", databaseError.toException());
            }
        };
        reviewPostsRef.addValueEventListener(reviewPostListener);

        return root;
    }


}
