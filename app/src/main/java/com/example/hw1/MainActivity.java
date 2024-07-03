package com.example.hw1;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import android.content.Context;

import com.example.hw1.Interface.GameObject;
import com.example.hw1.Interface.MoveCallback;
import com.example.hw1.Utilities.MoveDetector;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity implements MoveCallback {

    private final int row = 9;
    boolean collisionOccurred;
    private final int col = 5;
    private final int DELAY = 1000;
    private final Handler handler = new Handler();
    private MediaPlayer mediaPlayer;
    private MediaPlayer coinMediaPlayer;
    private AppCompatImageView[][] game_matrix;
    private AppCompatImageView[] game_IMG_hearts;
    private MaterialButton game_button_right;
    private MaterialButton game_button_left;
    private GameManager gameManager;
    private MySignal MS;
    private boolean isTrue = false;
    private MoveDetector moveDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MySignal.init(this);
        MS = MySignal.getInstance();
        findViews();
        initViews();
        gameManager = new GameManager();
        mediaPlayer = MediaPlayer.create(this, R.raw.hit_sound);
        coinMediaPlayer = MediaPlayer.create(this, R.raw.good_sound);
        game_matrix[8][1].setImageResource(gameManager.getPlayerImage());
        Intent prev = getIntent();
        String sensor = prev.getExtras().getString("KEY_MESSAGE");
        if (sensor != null) {
            isTrue = true;
        }

        if (!isTrue) {
            game_button_right.setOnClickListener(v -> side(true));
            game_button_left.setOnClickListener(v -> side(false));
            game_button_right.setVisibility(View.VISIBLE);
            game_button_left.setVisibility(View.VISIBLE);
        } else {
            game_button_right.setVisibility(View.INVISIBLE);
            game_button_left.setVisibility(View.INVISIBLE);
            moveDetector = new MoveDetector(this, this);
        }


        nextBlock();
    }

    private void initViews() {

        Intent prev = getIntent();
        String sensor =prev.getExtras().getString("KEY_MESSAGE");
        if(sensor!=null){
            isTrue = true;
        }

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
            toast("endless game");
            gameManager.updateLives();
        } else {
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




 
    private void findViews() {

        game_button_right = findViewById(R.id.game_button_right);
        game_button_left = findViewById(R.id.game_button_left);

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
