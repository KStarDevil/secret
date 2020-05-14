package com.ykko.app.ui.gallery;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ykko.app.R;
import com.ykko.app.data.Global_Variable;
import com.ykko.app.data.model.Order;
import com.ykko.app.ui.admin_home.AdminReservationAdapter;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;

    private List<Order> orderPosts = new ArrayList<>();
    private List<String> keys = new ArrayList<>();
    private RecyclerView orderPostsView;
    private RecyclerView.Adapter orderPostsViewAdapter;
    private LinearLayout reserve_menu;
    private RecyclerView.LayoutManager orderPostsViewLayoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        galleryViewModel =
//                ViewModelProviders.of(this).get(GalleryViewModel.class);
//        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
//        final TextView textView = root.findViewById(R.id.text_gallery);
//        galleryViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//
        SharedPreferences sp = getContext().getSharedPreferences("FILE_NAME", 0);
        SharedPreferences.Editor edit = sp.edit();
        final String Username = sp.getString("key","NON");
        final String Userphone =sp.getString("phone","NON");
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        orderPostsView = root.findViewById(R.id.myorder_list);
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
                    if(post.name.equals(Username) && post.phNo.equals(Userphone)){
                        orderPosts.add(post);
                    }

                }

                orderPostsViewAdapter = new AdminReservationAdapter(orderPosts,keys,getContext(),false);
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