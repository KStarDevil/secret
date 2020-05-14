package com.ykko.app.ui.branches;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ykko.app.R;
import com.ykko.app.ui.branches.BranchesViewModel;

public class BranchesFragment extends Fragment {

    private BranchesViewModel branchesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        branchesViewModel =
                ViewModelProviders.of(this).get(BranchesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_branches, container, false);

        return root;
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng ykko = new LatLng(16.8774834,96.1003678);
            LatLng ykko2 = new LatLng(16.8510287,96.1479804);

            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.addMarker(new MarkerOptions().position(ykko).title("YKKO (Yan Kin)"));
            googleMap.addMarker(new MarkerOptions().position(ykko2).title("YKKO (Nwe Ni)"));

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(ykko));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(ykko2));
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.branches_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}
