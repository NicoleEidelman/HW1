package com.example.hw1.Activity;

import static com.example.hw1.Activity.MainActivity.LOCATION_PERMISSION_REQUEST_CODE;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import androidx.annotation.NonNull;
import com.example.hw1.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class main_screen extends AppCompatActivity {
    private ExtendedFloatingActionButton main_BTN_button;
    private ExtendedFloatingActionButton main_BTN_sensor;
    private ExtendedFloatingActionButton main_BTN_top_score;
    private String delay;
    private String playerName;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private double lat;
    private double lon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Intent intent = getIntent();
        playerName = intent.getStringExtra("PLAYER_NAME");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getLastKnownLocation();
        }
        findViews();
        initViews();
    }


    private void initViews() {
        Bundle bundle = new Bundle();
        main_BTN_button.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.putExtra("PLAYER_NAME", playerName);
            bundle.putDouble("LAT", lat);
            bundle.putDouble("LON", lon);
            i.putExtras(bundle);
            startActivity(i);
        });

        main_BTN_sensor.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.putExtra("PLAYER_NAME", playerName);
            i.putExtra("KEY_MESSAGE", "sensor");
            bundle.putDouble("LAT", lat);
            bundle.putDouble("LON", lon);
            i.putExtras(bundle);
            startActivity(i);
        });
        main_BTN_top_score.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), Activity_TopScores.class);
            startActivity(i);
        });
    }

    private void findViews() {
        main_BTN_button = findViewById(R.id.main_BTN_button);
        main_BTN_sensor = findViewById(R.id.main_BTN_sensor);
        main_BTN_top_score = findViewById(R.id.main_BTN_top_score);
    }

    private void getLastKnownLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
        } else {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            lat = location.getLatitude();
                            lon = location.getLongitude();
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastKnownLocation();
            } else {
                finish();
            }
        }
    }
}
