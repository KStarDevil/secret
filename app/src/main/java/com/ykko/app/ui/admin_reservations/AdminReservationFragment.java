package com.ykko.app.ui.admin_reservations;

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
import com.ykko.app.data.model.Order;
import com.ykko.app.ui.admin_home.AdminReservationAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdminReservationFragment extends Fragment {

    private AdminReservationViewModel mViewModel;
    private List<Order> orderPosts = new ArrayList<>();
    private List<String> keys = new ArrayList<>();

    private RecyclerView orderPostsView;
    private RecyclerView.Adapter orderPostsViewAdapter;
    private RecyclerView.LayoutManager orderPostsViewLayoutManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(this).get(AdminReservationViewModel.class);
        View root = inflater.inflate(R.layout.fragment_admin_reservation, container, false);

        orderPostsView = root.findViewById(R.id.admin_reservation_list_view);
        orderPostsView.setHasFixedSize(true);
        orderPostsViewLayoutManager = new LinearLayoutManager(getActivity());
        orderPostsView.setLayoutManager(orderPostsViewLayoutManager);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference orderPostsRef = database.getReference("orderPosts").child("posts");
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

                orderPostsViewAdapter = new AdminReservationListAdapter(orderPosts,keys);
                orderPostsView.setAdapter(orderPostsViewAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("", "loadPost:onCancelled", databaseError.toException());
            }
        };

        orderPostsRef.addValueEventListener(orderPostListener);
        return root;
    }

}
