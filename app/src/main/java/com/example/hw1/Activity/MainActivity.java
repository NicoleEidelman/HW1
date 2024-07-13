package com.example.hw1.Activity;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.hw1.Game.Coin;
import com.example.hw1.Game.GameManager;
import com.example.hw1.Interface.GameObject;
import com.example.hw1.Interface.MoveCallback;
import com.example.hw1.SupportingClasses.MSPV3;
import com.example.hw1.SupportingClasses.PlayerScore;
import com.example.hw1.R;
import com.example.hw1.Utilities.MoveDetector;
import com.example.hw1.Utilities.MySignal;
import com.example.hw1.Game.obstacle;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MoveCallback {
    static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private final int row = 9;
    boolean collisionOccurred;
    private final int col = 5;
    private int DELAY = 1000;
    private final Handler handler = new Handler();
    private MediaPlayer mediaPlayer;
    private MediaPlayer coinMediaPlayer;
    private AppCompatImageView[][] game_matrix;
    private AppCompatImageView[] game_IMG_hearts;
    private MaterialButton game_button_right;
    private MaterialButton game_button_left;
    private MaterialTextView trivia_LBL_score;
    private GameManager gameManager;
    private MySignal MS;
    private boolean isTrue = false;
    private String playerName;
    private MoveDetector moveDetector;
    double defaultValue= 0;
    private double latitude = 0;
    private double longitude = 0;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MySignal.init(this);
        MS = MySignal.getInstance();
        findViews();
        gameManager = new GameManager();
        mediaPlayer = MediaPlayer.create(this, R.raw.hit_sound);
        coinMediaPlayer = MediaPlayer.create(this, R.raw.good_sound);
        game_matrix[8][1].setImageResource(gameManager.getPlayerImage());
        game_button_right.setVisibility(View.VISIBLE);
        game_button_left.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        String sensor = intent.getStringExtra("KEY_MESSAGE");
        latitude = intent.getDoubleExtra("LAT",defaultValue);
        longitude = intent.getDoubleExtra("LON",defaultValue);
        playerName = intent.getStringExtra("PLAYER_NAME");

        if (sensor != null && sensor.equals("sensor")) {
            isTrue = true;
        }


        if (!isTrue) {
            game_button_right.setOnClickListener(v -> side(true));
            game_button_left.setOnClickListener(v -> side(false));
        } else {
            game_button_right.setVisibility(View.INVISIBLE);
            game_button_left.setVisibility(View.INVISIBLE);
            moveDetector = new MoveDetector(this, this);
        }



        nextBlock();
    }




    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
        if (isTrue && moveDetector != null) {
            moveDetector.startListening();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        stop();
        savePlayerScore(gameManager.getScore());
        if (moveDetector != null) {
            moveDetector.stopListening();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (coinMediaPlayer != null) {
            coinMediaPlayer.release();
            coinMediaPlayer = null;
        }
    }


    private void vibrateOnCollision() {
        MS.vibrate();
    }

    private void playSoundOnCollision() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }
    private void playSoundOnCoin() {
        if (coinMediaPlayer != null) {
            coinMediaPlayer.start();
        }
    }

    private void toast(String message) {
        MS.toast(message);
    }

    private void handleCollision(GameObject gameObject) {
        if (gameObject instanceof obstacle) {
            vibrateOnCollision();
            playSoundOnCollision();
            toast("Oh no ouch");
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink_animation);
            game_matrix[8][gameManager.playerLocation()].startAnimation(animation);
        } else if (gameObject instanceof Coin) {
            playSoundOnCoin();
            toast("awesome!");
        }
    }




    private void movePlayer(int newLoc, int oldLoc) {
        game_matrix[8][oldLoc].setVisibility(View.INVISIBLE);
        game_matrix[8][newLoc].setVisibility(View.VISIBLE);
        game_matrix[8][newLoc].setImageResource(gameManager.getPlayerImage());
    }

    private void moveGameObject(GameObject gameObject) {
        if (gameObject.getRowLocation() - 1 >= 0) {
            game_matrix[gameObject.getRowLocation() - 1][gameObject.getCol()].setVisibility(View.INVISIBLE);
        }
        game_matrix[gameObject.getRowLocation()][gameObject.getCol()].setVisibility(View.VISIBLE);
        game_matrix[gameObject.getRowLocation()][gameObject.getCol()].setImageResource(gameObject.getImage());
    }

    public void moveX(boolean moveRight) {
        runOnUiThread(() -> side(moveRight));
    }
    @SuppressLint("RestrictedApi")


    public void adjustSpeed(boolean increaseSpeed) {
        runOnUiThread(() -> {
            if (increaseSpeed) {
                if (DELAY > 500) {
                    DELAY -= 100;
                }
            } else {
                if (DELAY < 1500) {
                    DELAY += 100;
                }
            }
        });
    }


    private void side(boolean side) {
        int newLocation;
        int oldLoc = gameManager.playerLocation();
        if (side) {
            newLocation = gameManager.moveRight();
        } else {
            newLocation = gameManager.moveLeft();
        }
        movePlayer(newLocation, oldLoc);
    }

    private final Runnable runnable = new Runnable() {
        public void run() {
            handler.postDelayed(runnable, DELAY);
            tick();
        }
    };

    private void startTimer() {
        handler.postDelayed(runnable, DELAY);
    }

    private void stop() {
        handler.removeCallbacks(runnable);

    }

    private void tick() {
        if (gameManager.getLives() == 0) {
            onStop();
            notifyGameOver();
        } else {
            trivia_LBL_score.setText("" + gameManager.getScore());
            collisionOccurred = nextBlock();
            if (collisionOccurred) {
                handleCollision(gameManager.getCollidedObject());
            }
        }
        updateUI();
    }

    private boolean nextBlock() {
       return gameManager.nextBlock();
    }

    private void updateUI() {
        if (gameManager.getLives() == gameManager.getMaxlives()) {
            gameManager.clearCoins();
        }

        for (int i = 0; i < row - 1; i++) {
            for (int j = 0; j < col; j++) {
                game_matrix[i][j].setVisibility(View.INVISIBLE);
            }
        }

        for (GameObject gameObject : gameManager.getGameObjects()) {
            moveGameObject(gameObject);
        }

        for (int i = 0; i < gameManager.getLives(); i++) {
            game_IMG_hearts[i].setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < gameManager.getMaxlives() - gameManager.getLives(); i++) {
            game_IMG_hearts[gameManager.getMaxlives() - i - 1].setVisibility(View.INVISIBLE);
        }
    }

    private void savePlayerScore(int score) {
        MSPV3 msp = MSPV3.getInstance();
        List<PlayerScore> playerScoreList = msp.getPlayers();

        // Check if the player already exists in the list
        boolean playerExists = false;
        for (PlayerScore playerScore : playerScoreList) {
            if (playerScore.getName().equals(playerName)) {
                // Update the player's score if it's higher
                if (score > playerScore.getScore()) {
                    playerScore.setScore(score);

                }
                playerExists = true;
                break;
            }
        }

        // If the player does not exist, add them to the list
        if (!playerExists) {
            playerScoreList.add(new PlayerScore(playerName, score, latitude, longitude));
        }

        // Sort the list by score in descending order
        Collections.sort(playerScoreList, (p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()));

        // Ensure only the top 10 scores are kept
        if (playerScoreList.size() > 10) {
            playerScoreList = playerScoreList.subList(0, 10);
        }

        // Save the updated list
        msp.savePlayers(playerScoreList);
    }


    private void notifyGameOver() {
        new AlertDialog.Builder(this)
                .setTitle("Game Over")
                .setMessage("Not bad,\n" +
                        "Maybe next time you'll beat them all!")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, main_screen.class);
                        startActivity(intent);
                        finish(); // Ensure the current activity is closed
                    }
                })
                .show();
    }


    private void findViews() {

        game_button_right = findViewById(R.id.game_button_right);
        game_button_left = findViewById(R.id.game_button_left);
        trivia_LBL_score = findViewById(R.id.trivia_LBL_score);

        game_IMG_hearts = new AppCompatImageView[] {
                findViewById(R.id.game_IMG_heart1),
                findViewById(R.id.game_IMG_heart2),
                findViewById(R.id.game_IMG_heart3),

        };

        game_matrix = new AppCompatImageView[row][col] ;
        game_matrix[0][0] = findViewById(R.id.game_LAY_00);
        game_matrix[0][1] = findViewById(R.id.game_LAY_01);
        game_matrix[0][2] = findViewById(R.id.game_LAY_02);
        game_matrix[0][3] = findViewById(R.id.game_LAY_03);
        game_matrix[0][4] = findViewById(R.id.game_LAY_04);
        game_matrix[1][0] = findViewById(R.id.game_LAY_10);
        game_matrix[1][1] = findViewById(R.id.game_LAY_11);
        game_matrix[1][2] = findViewById(R.id.game_LAY_12);
        game_matrix[1][3] = findViewById(R.id.game_LAY_13);
        game_matrix[1][4] = findViewById(R.id.game_LAY_14);
        game_matrix[2][0] = findViewById(R.id.game_LAY_20);
        game_matrix[2][1] = findViewById(R.id.game_LAY_21);
        game_matrix[2][2] = findViewById(R.id.game_LAY_22);
        game_matrix[2][3] = findViewById(R.id.game_LAY_23);
        game_matrix[2][4] = findViewById(R.id.game_LAY_24);
        game_matrix[3][0] = findViewById(R.id.game_LAY_30);
        game_matrix[3][1] = findViewById(R.id.game_LAY_31);
        game_matrix[3][2] = findViewById(R.id.game_LAY_32);
        game_matrix[3][3] = findViewById(R.id.game_LAY_33);
        game_matrix[3][4] = findViewById(R.id.game_LAY_34);
        game_matrix[4][0] = findViewById(R.id.game_LAY_40);
        game_matrix[4][1] = findViewById(R.id.game_LAY_41);
        game_matrix[4][2] = findViewById(R.id.game_LAY_42);
        game_matrix[4][3] = findViewById(R.id.game_LAY_43);
        game_matrix[4][4] = findViewById(R.id.game_LAY_44);
        game_matrix[5][0] = findViewById(R.id.game_LAY_50);
        game_matrix[5][1] = findViewById(R.id.game_LAY_51);
        game_matrix[5][2] = findViewById(R.id.game_LAY_52);
        game_matrix[5][3] = findViewById(R.id.game_LAY_53);
        game_matrix[5][4] = findViewById(R.id.game_LAY_54);
        game_matrix[6][0] = findViewById(R.id.game_LAY_60);
        game_matrix[6][1] = findViewById(R.id.game_LAY_61);
        game_matrix[6][2] = findViewById(R.id.game_LAY_62);
        game_matrix[6][3] = findViewById(R.id.game_LAY_63);
        game_matrix[6][4] = findViewById(R.id.game_LAY_64);
        game_matrix[7][0] = findViewById(R.id.game_LAY_70);
        game_matrix[7][1] = findViewById(R.id.game_LAY_71);
        game_matrix[7][2] = findViewById(R.id.game_LAY_72);
        game_matrix[7][3] = findViewById(R.id.game_LAY_73);
        game_matrix[7][4] = findViewById(R.id.game_LAY_74);
        game_matrix[8][0] = findViewById(R.id.game_LAY_80);
        game_matrix[8][1] = findViewById(R.id.game_LAY_81);
        game_matrix[8][2] = findViewById(R.id.game_LAY_82);
        game_matrix[8][3] = findViewById(R.id.game_LAY_83);
        game_matrix[8][4] = findViewById(R.id.game_LAY_84);

        }




}
