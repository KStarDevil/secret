package com.ykko.app.ui.reservation;

import androidx.lifecycle.Observer;
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
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ykko.app.R;
import com.ykko.app.data.model.Reservation;
import com.ykko.app.ui.reservation.ReservationAdapter;

import java.util.ArrayList;
import java.util.List;

public class ReservationFragment extends Fragment {

    private List<Reservation> reservationPosts = new ArrayList<>();
    private List<String> keys = new ArrayList<>();
    private ReservationViewModel reservationViewModel;

    private RecyclerView reservationPostsView;
    private RecyclerView.Adapter reservationPostsViewAdapter;
    private RecyclerView.LayoutManager reservationPostsLayoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        reservationViewModel =
                ViewModelProviders.of(this).get(ReservationViewModel.class);
        View root = inflater.inflate(R.layout.fragment_reservation, container, false);

        reservationPostsView = root.findViewById(R.id.reservation_posts_view);
        reservationPostsView.setHasFixedSize(true);
        reservationPostsLayoutManager = new LinearLayoutManager(getActivity());
        reservationPostsView.setLayoutManager(reservationPostsLayoutManager);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reservationPostsRef = database.getReference("reservationPosts").child("posts");

        ValueEventListener menuPostListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                keys.clear();
                reservationPosts.clear();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    Reservation post = keyNode.getValue(Reservation.class);
                    reservationPosts.add(post);
                }

                reservationPostsViewAdapter = new ReservationAdapter(reservationPosts);
                reservationPostsView.setAdapter(reservationPostsViewAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("", "loadPost:onCancelled", databaseError.toException());
            }
        };
        reservationPostsRef.addValueEventListener(menuPostListener);


        return root;
    }


}
