package com.example.hw1;

import com.example.hw1.Interface.GameObject;

public class obstacle implements GameObject {

    private final int obstacleImage;
    private int obstacleRow;
    private final int col;

    public obstacle(int col){
        this.obstacleImage = R.drawable.ic_evil;
        this.col = col;
        this.obstacleRow = 0;
    }

    @Override
    public int getImage() {
        return obstacleImage;
    }

    @Override
    public int getRowLocation() {
        return obstacleRow;
    }

    @Override
    public void moveRowLocation() {
        obstacleRow++;
    }

    @Override
    public int getCol() {
        return col;
    }
}
