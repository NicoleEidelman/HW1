package com.example.hw1;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import com.google.android.material.button.MaterialButton;


public class MainActivity extends AppCompatActivity {

    private final int row = 6;
    boolean collisionOccurred;
    private final int col = 3;
    private final int MAXLIVES = 3;
    private final int DELAY = 1000;
    private final Handler handler = new Handler();
    private MediaPlayer mediaPlayer;
    private AppCompatImageView[][] game_matrix;
    private AppCompatImageView[] game_IMG_hearts;
    private MaterialButton game_button_right;
    private MaterialButton game_button_left;
    private GameManager gameManager;
    private MySignal MS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MySignal.init(this);
        MS = MySignal.getInstance();
        findViews();
        gameManager = new GameManager();
        mediaPlayer = MediaPlayer.create(this, R.raw.hit_sound);
        game_matrix[5][1].setImageResource(gameManager.getPlayerImage());
        game_button_right.setOnClickListener(v -> side(true));
        game_button_left.setOnClickListener(v -> side(false));
        nextBlock();
        }



    private void vibrateOnCollision() {MS.vibrate();}

    private void playSoundOnCollision() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    private void toast(String message){MS.toast(message);}


    private void handleCollision() {//Calls vibrateOnCollision() and playSoundOnCollision() to handle the collision effects.
        vibrateOnCollision();
        playSoundOnCollision();
        toast("Oh no ouch");
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext()
                , R.anim.blink_animation);
        game_matrix[5][gameManager.playerLocation()].startAnimation(animation);
    }

    private void movePlayer(int newLoc,int oldLoc) { //Moves the player's image from the old location to the new location in the game matrix.

        game_matrix[5][oldLoc].setVisibility(View.INVISIBLE);
        game_matrix[5][newLoc].setVisibility(View.VISIBLE);
        game_matrix[5][newLoc].setImageResource(gameManager.getPlayerImage());

    }

    private void moveObstacle(obstacle obs) {  //Updates the position of an obstacle in the game matrix, making the previous position invisible and the new position visible with the obstacle image.
        if (obs.obstacleRowLocation() - 1 >= 0) {
            game_matrix[obs.obstacleRowLocation() - 1][obs.getCol()].setVisibility(View.INVISIBLE);
        }
        game_matrix[obs.obstacleRowLocation()][obs.getCol()].setVisibility(View.VISIBLE);
        game_matrix[obs.obstacleRowLocation()][obs.getCol()].setImageResource(obs.getObstacleImage());

    }

    private void side(boolean side) { //Moves the player either to the left or right based on the side parameter and updates the game matrix.

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
    };;
    private void startTimer(){handler.postDelayed(runnable, DELAY);}
    private void stop(){handler.removeCallbacks(runnable);}

    private void tick() {

        if (gameManager.getLives() == 0) {
            toast("endless game");//Checks if the player has any lives left
            gameManager.updateLives();
            updateUI();

        } else {
             collisionOccurred = nextBlock();  // If a collision occurred, calls handleCollision().
            if (collisionOccurred) {
                handleCollision();
            }

            updateUI();//Updates the UI to reflect the new game state.
        }
    }


    //Calls gameManager.nextBlock() to advance the game state and update the UI.
    private boolean nextBlock() {return gameManager.nextBlock();}

    private void updateUI() {  //Updates the visibility and images of the game matrix and hearts based on the current game state.
        for (int i = 0; i < row-1; i++) {
            for (int j = 0; j < col; j++) {//For each cell in the matrix, it sets the visibility to View.INVISIBLE. This essentially clears any previous state by making all cells invisible.
                game_matrix[i][j].setVisibility(View.INVISIBLE);
            }
        }

        for(obstacle obs: gameManager.getObstacles()){       //it retrieves the list of current obstacles from the gameManager using
            moveObstacle(obs);                              //For each obstacle, it calls the moveObstacle(obs) function. This function updates the visibility and image of the cells in the game matrix where the obstacle is located, making the obstacles visible in their new positions.
        }


        for (int i = 0; i < gameManager.getLives(); i++) {
            game_IMG_hearts[i].setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < MAXLIVES-gameManager.getLives(); i++) {
            game_IMG_hearts[i].setVisibility(View.INVISIBLE);
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
        game_matrix[1][0] = findViewById(R.id.game_LAY_10);
        game_matrix[1][1] = findViewById(R.id.game_LAY_11);
        game_matrix[1][2] = findViewById(R.id.game_LAY_12);
        game_matrix[2][0] = findViewById(R.id.game_LAY_20);
        game_matrix[2][1] = findViewById(R.id.game_LAY_21);
        game_matrix[2][2] = findViewById(R.id.game_LAY_22);
        game_matrix[3][0] = findViewById(R.id.game_LAY_30);
        game_matrix[3][1] = findViewById(R.id.game_LAY_31);
        game_matrix[3][2] = findViewById(R.id.game_LAY_32);
        game_matrix[4][0] = findViewById(R.id.game_LAY_40);
        game_matrix[4][1] = findViewById(R.id.game_LAY_41);
        game_matrix[4][2] = findViewById(R.id.game_LAY_42);
        game_matrix[5][0] = findViewById(R.id.game_LAY_50);
        game_matrix[5][1] = findViewById(R.id.game_LAY_51);
        game_matrix[5][2] = findViewById(R.id.game_LAY_52);


        }
    protected void onResume() {
        Log.d("pttt", "onResume");
        super.onResume();
        startTimer();
    }

    protected void onStop() {
        Log.d("pttt", "onStop");
        super.onStop();
        stop();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }



    }
