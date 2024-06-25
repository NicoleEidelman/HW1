package com.example.hw1;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GameManager {
    private static final int MAX = 3;
    private static final int MAXLIVES = 3;
    private static final int LIMIT = 4;
    private static final int MIN = 0;
    private player player;
    private List<obstacle> obstacles;

    private int lives = 3;
    private int timerCounter = 1;



public GameManager(){
    player = new player();
    obstacles = new ArrayList<>();

}





    public int playerLocation() {
        return player.playerLocation();
    }

    public int moveRight() {
        if (player.playerLocation() + 1 < MAX) {
            player.playerLocationRight();

        }
        return player.playerLocation();
    }

    public int moveLeft() {
        if (player.playerLocation() - 1 >= MIN) {
            player.playerLocationLeft();
        }
        return player.playerLocation();
    }

    public int getPlayerImage() {return player.getImage();}

    public int getLives() {return lives;}
    public void updateLives(){lives=MAXLIVES;}

    public void addNewObs(){
        Random rand = new Random();
        int col = rand.nextInt(MAX);
            obstacles.add(new obstacle(col));
    }
    
    public boolean nextBlock() {
        moveObsInMat();
        boolean collisionOccurred = checkCollisions();
        timerCounter++;
        if (timerCounter % 2 == 0) { // Add a new obstacle every other tick
            addNewObs();
        }
        return collisionOccurred;
    }

    private boolean checkCollisions() {
        boolean collisionOccurred = false;
        for (obstacle obs : obstacles) {
            if (obs.obstacleRowLocation() == LIMIT && obs.getCol() == player.playerLocation()) {
                lives--;

                collisionOccurred = true;
                if (lives == 0) {

                }
                break;
            }
        }
        return collisionOccurred;
    }

    public void moveObsInMat(){
        Iterator<obstacle> it = obstacles.iterator();
        while(it.hasNext()){
            obstacle obs = it.next();
            if(obs.obstacleRowLocation() + 1 > LIMIT) {
                it.remove();
            }
            else {

                obs.obstacleMoveRowLocation();
            }

        }
    }

    public List<obstacle> getObstacles() {
        return obstacles;
    }






}
