package com.example.hw1;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class main_screen extends AppCompatActivity {
    private ExtendedFloatingActionButton main_BTN_button;
    private ExtendedFloatingActionButton main_BTN_sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        findViews();
        initViews();
    }

    private void initViews() {
        main_BTN_button.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        });

        main_BTN_sensor.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            String text = "true";
            Bundle bundle = new Bundle();
            bundle.putString("KEY_MESSAGE", text);
            i.putExtras(bundle);
            startActivity(i);
        });
    }

    private void findViews() {
        main_BTN_button = findViewById(R.id.main_BTN_button);
        main_BTN_sensor = findViewById(R.id.main_BTN_sensor);
    }
}