package com.example.arithmeticgame;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaderboardActivity extends AppCompatActivity {

    private TabHost tabHost;
    private ListView easyLeaderboardListView;
    private ListView mediumLeaderboardListView;
    private ListView hardLeaderboardListView;
    private Button backButton;

    // Mock data for leaderboard
    // In a real app, this would come from a server or local database
    private List<LeaderboardEntry> easyLeaderboard;
    private List<LeaderboardEntry> mediumLeaderboard;
    private List<LeaderboardEntry> hardLeaderboard;

    public static class LeaderboardEntry {
        private String playerName;
        private int score;
        private String date;

        public LeaderboardEntry(String playerName, int score, String date) {
            this.playerName = playerName;
            this.score = score;
            this.date = date;
        }

        public String getPlayerName() {
            return playerName;
        }

        public int getScore() {
            return score;
        }

        public String getDate() {
            return date;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        // Initialize UI components
        tabHost = findViewById(R.id.tabHost);
        easyLeaderboardListView = findViewById(R.id.easyLeaderboardListView);
        mediumLeaderboardListView = findViewById(R.id.mediumLeaderboardListView);
        hardLeaderboardListView = findViewById(R.id.hardLeaderboardListView);
        backButton = findViewById(R.id.backButton);

        // Set up tabs
        tabHost.setup();

        TabHost.TabSpec easyTab = tabHost.newTabSpec("easy");
        easyTab.setIndicator("Easy");
        easyTab.setContent(R.id.easyTab);
        tabHost.addTab(easyTab);

        TabHost.TabSpec mediumTab = tabHost.newTabSpec("medium");
        mediumTab.setIndicator("Medium");
        mediumTab.setContent(R.id.mediumTab);
        tabHost.addTab(mediumTab);

        TabHost.TabSpec hardTab = tabHost.newTabSpec("hard");
        hardTab.setIndicator("Hard");
        hardTab.setContent(R.id.hardTab);
        tabHost.addTab(hardTab);

        // Initialize leaderboard data
        initializeLeaderboardData();

        // Display leaderboards
        displayLeaderboard(easyLeaderboardListView, easyLeaderboard);
        displayLeaderboard(mediumLeaderboardListView, mediumLeaderboard);
        displayLeaderboard(hardLeaderboardListView, hardLeaderboard);

        // Set up back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initializeLeaderboardData() {
        // In a real app, this data would come from a server or local database
        // For now, we'll use mock data

        easyLeaderboard = new ArrayList<>();
        easyLeaderboard.add(new LeaderboardEntry("Player1", 500, "2023-05-15"));
        easyLeaderboard.add(new LeaderboardEntry("Player2", 450, "2023-05-14"));
        easyLeaderboard.add(new LeaderboardEntry("Player3", 400, "2023-05-13"));
        easyLeaderboard.add(new LeaderboardEntry("Player4", 350, "2023-05-12"));
        easyLeaderboard.add(new LeaderboardEntry("Player5", 300, "2023-05-11"));

 

        mediumLeaderboard = new ArrayList<>();
        mediumLeaderboard.add(new LeaderboardEntry("Player1", 800, "2023-05-15"));
        mediumLeaderboard.add(new LeaderboardEntry("Player2", 750, "2023-05-14"));
        mediumLeaderboard.add(new LeaderboardEntry("Player3", 700, "2023-05-13"));
        mediumLeaderboard.add(new LeaderboardEntry("Player4", 650, "2023-05-12"));
        mediumLeaderboard.add(new LeaderboardEntry("Player5", 600, "2023-05-11"));

        hardLeaderboard = new ArrayList<>();
        hardLeaderboard.add(new LeaderboardEntry("Player1", 1200, "2023-05-15"));
        hardLeaderboard.add(new LeaderboardEntry("Player2", 1100, "2023-05-14"));
        hardLeaderboard.add(new LeaderboardEntry("Player3", 1000, "2023-05-13"));
        hardLeaderboard.add(new LeaderboardEntry("Player4", 900, "2023-05-12"));
        hardLeaderboard.add(new LeaderboardEntry("Player5", 800, "2023-05-11"));
    }

    private void displayLeaderboard(ListView listView, List<LeaderboardEntry> leaderboard) {
        // Prepare data for adapter
        List<Map<String, Object>> data = new ArrayList<>();
        int rank = 1;
        for (LeaderboardEntry entry : leaderboard) {
            Map<String, Object> item = new HashMap<>();
            item.put("rank", "#" + rank);
            item.put("playerName", entry.getPlayerName());
            item.put("score", String.valueOf(entry.getScore()));
            item.put("date", entry.getDate());
            data.add(item);
            rank++;
        }

        // Create adapter
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                data,
                R.layout.leaderboard_item,
                new String[]{"rank", "playerName", "score", "date"},
                new int[]{R.id.rankTextView, R.id.playerNameTextView, R.id.scoreTextView, R.id.dateTextView}
        );

        // Set adapter
        listView.setAdapter(adapter);
    }
}
