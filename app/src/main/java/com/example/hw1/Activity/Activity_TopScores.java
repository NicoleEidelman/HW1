package com.example.hw1.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hw1.Fragment.Fragment_Bottom;
import com.example.hw1.Fragment.Fragment_TopScores;
import com.example.hw1.R;
import com.example.hw1.SupportingClasses.MSPV3;
import com.example.hw1.SupportingClasses.PlayerScore;

import java.util.List;

public class Activity_TopScores extends AppCompatActivity implements Fragment_TopScores.OnPlayerClickListener  {

    private Fragment_TopScores fragmentTopScores;
    private Fragment_Bottom fragmentBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_score);

        fragmentTopScores = new Fragment_TopScores();
        fragmentBottom = new Fragment_Bottom();
        fragmentTopScores.setOnPlayerClickListener(this);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_LAY_top, fragmentTopScores)
                .add(R.id.main_LAY_bottom, fragmentBottom)
                .commit();


    }
    @Override
    public void onPlayerClick(PlayerScore playerScore) {



        fragmentBottom.zoomToPlayer(playerScore);
    }

}
