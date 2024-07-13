package com.example.hw1.SupportingClasses;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class MSPV3 {

    private static MSPV3 instance;
    private SharedPreferences prefs;

    private MSPV3(Context context) {
        prefs = context.getSharedPreferences("TopScores", Context.MODE_PRIVATE);
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new MSPV3(context);
        }
    }

    public static MSPV3 getInstance() {
        return instance;
    }

    public void saveString(String key, String value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String readString(String key, String def) {
        return prefs.getString(key, def);
    }

    public void saveInt(String key, int value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int readInt(String key, int def) {
        return prefs.getInt(key, def);
    }

    public void savePlayers(List<PlayerScore> players) {
        SharedPreferences.Editor editor = prefs.edit();
        for (int i = 0; i < players.size() && i < 10; i++) {
            editor.putString("player_name_" + i, players.get(i).getName());
            editor.putInt("player_score_" + i, players.get(i).getScore());
            editor.putFloat("player_latitude_" + i, (float) players.get(i).getLatitude());
            editor.putFloat("player_longitude_" + i, (float) players.get(i).getLongitude());
        }
        editor.apply();
    }

    public List<PlayerScore> getPlayers() {
        List<PlayerScore> players = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String name = prefs.getString("player_name_" + i, null);
            int score = prefs.getInt("player_score_" + i, -1);
            float latitude = prefs.getFloat("player_latitude_" + i, -1);
            float longitude = prefs.getFloat("player_longitude_" + i, -1);
            if (name != null && score != -1) {
                players.add(new PlayerScore(name, score, latitude, longitude));
            }
        }
        return players;
    }
}
