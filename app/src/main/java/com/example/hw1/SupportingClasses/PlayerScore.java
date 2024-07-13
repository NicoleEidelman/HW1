package com.example.hw1.SupportingClasses;

public class PlayerScore {
    private String name;
    private int score;
    private double latitude;
    private double longitude;




    public PlayerScore setName(String name) {
        this.name = name;
        return this;

    }
   public PlayerScore(String name, int score, double latitude, double longitude) {
        this.name = name;
        this.score = score;
       this.latitude = latitude;
       this.longitude = longitude;

    }

    public PlayerScore setScore(int score) {
        this.score = score;
        return this;
    }
    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }


    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
