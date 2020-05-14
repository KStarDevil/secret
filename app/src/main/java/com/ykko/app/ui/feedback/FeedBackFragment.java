package com.ykko.app.ui.feedback;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ykko.app.R;
import com.ykko.app.data.model.Feedback;
import com.ykko.app.ui.FirebaseDatabaseHelper;

public class FeedBackFragment extends Fragment {

    private FeedBackViewModel feedBackViewModel;
    private Feedback feedback = new Feedback();
    long feedbackPostID = 1;
    FirebaseDatabase database;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        feedBackViewModel =
                ViewModelProviders.of(this).get(FeedBackViewModel.class);
        View root = inflater.inflate(R.layout.fragment_feedback, container, false);
        database = FirebaseDatabase.getInstance();
        DatabaseReference feedbackPostsRef = database.getReference("feedbackPosts").child("posts");
        ValueEventListener feedbackPostListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                if(dataSnapshot.exists()){
                    feedbackPostID = dataSnapshot.getChildrenCount() +  1;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("", "loadPost:onCancelled", databaseError.toException());
            }
        };
        feedbackPostsRef.addValueEventListener(feedbackPostListener);

        final TextInputEditText reviewerNameEditText = root.findViewById(R.id.review_name);
        final TextInputEditText reviewerCmtEditText = root.findViewById(R.id.review_comment);
        final RatingBar ratingBar = root.findViewById(R.id.review_star);

        Button reviewBtn = root.findViewById(R.id.review_button);
        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedback.reviewerName = reviewerNameEditText.getText().toString();
                feedback.comment = reviewerCmtEditText.getText().toString();
                feedback.quality = ratingBar.getRating();
                FirebaseDatabaseHelper databaseHelper = new FirebaseDatabaseHelper();
                databaseHelper.addData("feedbackPosts", feedback, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsInserted() {
                        Toast.makeText(getContext(),"SEND SUCCESSFUL",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {

                    }
                });
            }
        });
        return root;
    }



}
