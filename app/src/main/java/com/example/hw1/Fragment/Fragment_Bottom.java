package com.example.hw1.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hw1.R;
import com.example.hw1.SupportingClasses.PlayerScore;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;




public class Fragment_Bottom extends Fragment implements OnMapReadyCallback {
    private GoogleMap googleMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bottom, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        return view;

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));



    }


    public void zoomToPlayer(PlayerScore playerScore) {
        if (googleMap != null) {
            LatLng playerLocation = new LatLng(playerScore.getLatitude(), playerScore.getLongitude());



            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(playerLocation, 15));
            googleMap.addMarker(new MarkerOptions()
                    .position(playerLocation)
                    .title(playerScore.getName()));
        } else {
            //
        }
    }
}



