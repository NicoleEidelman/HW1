package com.example.hw1;

public class obstacle {

    private final int obstacleImage;
    private int obstacleRow;
    private final int col;

    public obstacle(int col){
        this.obstacleImage = R.drawable.ic_evil;
        this.col = col;
        this.obstacleRow = 0;
    }


    public int getObstacleImage() {return obstacleImage;}

    public int obstacleRowLocation() {return obstacleRow;}

    public int obstacleMoveRowLocation() {return obstacleRow++;}

    public int getCol(){return col;}


}
