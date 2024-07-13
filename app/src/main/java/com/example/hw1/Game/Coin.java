package com.example.hw1.Game;

import com.example.hw1.Interface.GameObject;
import com.example.hw1.R;

public class Coin implements GameObject {

    private final int coinImage;
    private int coinRow;
    private final int coinCol;

    public Coin(int col){
        this.coinImage = R.drawable.ic_good;
        this.coinCol = col;
        this.coinRow = 0;
    }

    @Override
    public int getImage() {
        return coinImage;
    }

    @Override
    public int getRowLocation() {
        return coinRow;
    }

    @Override
    public void moveRowLocation() {
        coinRow++;
    }

    @Override
    public int getCol() {
        return coinCol;
    }
}
