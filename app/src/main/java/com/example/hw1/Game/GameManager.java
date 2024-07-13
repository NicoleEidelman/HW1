package com.example.hw1.Game;

import com.example.hw1.Interface.GameObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GameManager {
    private static final int COL = 5;
    private static final int MAXLIVES = 3;
    private static final int ROW = 7;
    private static final int MIN = 0;
    private final com.example.hw1.Game.player player;
    private final List<GameObject> gameObjects;
    private int lives = 3;
    private int timerCounter = 1;
    private GameObject collidedObject;
    private int score=0;

    public GameManager() {
        player = new player();
        gameObjects = new ArrayList<>();
    }

    public int playerLocation() {
        return player.Location();
    }

    public int moveRight() {
        if (player.Location() + 1 < COL) {
            player.moveRight();
        }
        return player.Location();
    }

    public int moveLeft() {
        if (player.Location() - 1 >= MIN) {
            player.moveLeft();
        }
        return player.Location();
    }

    public int getPlayerImage() {
        return player.getImage();
    }

    public int getLives() {
        return lives;
    }
    public int getMaxlives(){return MAXLIVES;}
    public void updateLives() {
        lives = MAXLIVES;
    }

    public void addNewGameObject(GameObject gameObject) {
        gameObjects.add(gameObject);
    }

    public boolean nextBlock() {
        moveGameObjectsInMat();
        boolean collisionOccurred = checkCollisions();
        timerCounter++;
        if (timerCounter % 2 == 0) { // Add a new obstacle every other tick
            addNewGameObject(new obstacle(new Random().nextInt(COL)));
        }
        if (timerCounter % 4 == 0 && lives < MAXLIVES) { // Add a new coin every 4th tick if lives < MAXLIVES
            addNewGameObject(new Coin(new Random().nextInt(COL)));
        }
        return collisionOccurred;
    }

    private boolean checkCollisions() {
        boolean collisionOccurred = false;
        collidedObject = null;
        Iterator<GameObject> it = gameObjects.iterator();
        while (it.hasNext()) {
            GameObject gameObject = it.next();
            if (gameObject.getRowLocation() == ROW && gameObject.getCol() == player.Location()) {
                if (gameObject instanceof obstacle) {
                    lives--;
                    collisionOccurred = true;
                    collidedObject = gameObject;
                    it.remove();
                } else if (gameObject instanceof Coin) {
                    if (lives < MAXLIVES) {
                        lives++;
                    }
                    collisionOccurred = true;
                    collidedObject = gameObject;
                    it.remove();
                }
            }
        }
        return collisionOccurred;
    }

    public void moveGameObjectsInMat() {
        Iterator<GameObject> it = gameObjects.iterator();
        while (it.hasNext()) {
            GameObject gameObject = it.next();
            if (gameObject.getRowLocation() + 1 > ROW) {
                it.remove();
            } else {
                gameObject.moveRowLocation();
            }
        }
    }

    public void clearCoins() {
        gameObjects.removeIf(gameObject -> gameObject instanceof Coin);
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public GameObject getCollidedObject() {
        return collidedObject;
    }

    public int getScore() {
        return score++;
    }

}
