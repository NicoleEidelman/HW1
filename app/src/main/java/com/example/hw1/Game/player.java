package com.example.hw1.Game;

import com.example.hw1.R;

public class player {

    private int playerImage;
    private int playerLocation = 1;

    public player(){this.playerImage = R.drawable.ic_player;}

    public int getImage() {return playerImage;}

    public int Location() {return playerLocation;}



    public void moveRight() {playerLocation++;}

    public void moveLeft() {playerLocation--;}
}
