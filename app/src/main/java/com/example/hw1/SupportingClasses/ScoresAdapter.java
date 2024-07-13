package com.example.hw1.SupportingClasses;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hw1.Fragment.Fragment_TopScores;
import com.example.hw1.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class ScoresAdapter extends RecyclerView.Adapter<ScoresAdapter.ScoreViewHolder>   {

    private List<PlayerScore> players;
    private Fragment_TopScores.OnPlayerClickListener listener;

    public ScoresAdapter(List<PlayerScore> players, Fragment_TopScores.OnPlayerClickListener listener) {
        this.players = players;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_score, parent, false);
        return new ScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        PlayerScore player = players.get(position);
        holder.game_LBL_name.setText(player.getName());
        holder.game_LBL_score.setText(String.valueOf(player.getScore()));
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPlayerClick(player);
            }
        });
    }

    @Override
    public int getItemCount() {
        return players == null ? 0 : players.size();
    }
    public PlayerScore getItem(int position) {
        return players.get(position);
    }




    public static class ScoreViewHolder extends RecyclerView.ViewHolder {
        private final MaterialTextView game_LBL_name;
        private final MaterialTextView game_LBL_score;

        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            game_LBL_name = itemView.findViewById(R.id.game_LBL_name);
            game_LBL_score = itemView.findViewById(R.id.game_LBL_score);
        }
    }
}
