package com.example.hw1.Activity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hw1.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class Activity_EnterName extends AppCompatActivity {
    private EditText enterNameEditText;
    private ExtendedFloatingActionButton startGameButton;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_name);
        findView();
        initView();
    }

    private void initView() {
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String playerName = enterNameEditText.getText().toString();
                if (!playerName.isEmpty()) {
                    Intent intent = new Intent(Activity_EnterName.this, main_screen.class);
                    intent.putExtra("PLAYER_NAME", playerName);
                    startActivity(intent);
                }
            }
        });
    }

    private void findView() {
        enterNameEditText = findViewById(R.id.enter_name_edit_text);
        startGameButton = findViewById(R.id.main_BTN_getNamebutton);
    }
}
