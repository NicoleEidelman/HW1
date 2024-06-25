package com.example.hw1;

public class player {

    private int playerImage;
    private int playerLocation = 1;

    public player(){this.playerImage = R.drawable.ic_player;}

    public int getImage() {return playerImage;}

    public int playerLocation() {return playerLocation;}



    public void playerLocationRight() {playerLocation++;}

    public void playerLocationLeft() {playerLocation--;}
}
