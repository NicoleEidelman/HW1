package com.example.hw1.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hw1.SupportingClasses.MSPV3;
import com.example.hw1.SupportingClasses.PlayerScore;
import com.example.hw1.R;
import com.example.hw1.SupportingClasses.ScoresAdapter;

import java.util.List;

public class Fragment_TopScores extends Fragment {

    private RecyclerView recyclerView;
    private ScoresAdapter scoresAdapter;
    private MSPV3 msp;
    private OnPlayerClickListener listener;

    public void setOnPlayerClickListener(OnPlayerClickListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_scores, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        msp = MSPV3.getInstance();

        List<PlayerScore> topPlayers = msp.getPlayers();

        scoresAdapter = new ScoresAdapter(topPlayers, listener);

        recyclerView.setAdapter(scoresAdapter);

        return view;
    }

    public interface OnPlayerClickListener {
        void onPlayerClick(PlayerScore playerScore);
    }

}
