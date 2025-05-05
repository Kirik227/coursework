package com.example.arithmeticgame;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AchievementActivity extends AppCompatActivity {

    private ListView achievementListView;
    private Button backButton;
    private TextView achievementCountTextView;
    private AchievementManager achievementManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        // Initialize UI components
        achievementListView = findViewById(R.id.achievementListView);
        backButton = findViewById(R.id.backButton);
        achievementCountTextView = findViewById(R.id.achievementCountTextView);

        // Initialize achievement manager
        achievementManager = new AchievementManager(this);

        // Set up back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Display achievements
        displayAchievements();
    }

    private void displayAchievements() {
        List<AchievementManager.Achievement> achievements = achievementManager.getAchievements();
        List<AchievementManager.Achievement> unlockedAchievements = achievementManager.getUnlockedAchievements();

        // Update achievement count
        achievementCountTextView.setText(unlockedAchievements.size() + "/" + achievements.size() + " Achievements Unlocked");

        // Prepare data for adapter
        List<Map<String, Object>> data = new ArrayList<>();
        for (AchievementManager.Achievement achievement : achievements) {
            Map<String, Object> item = new HashMap<>();
            item.put("title", achievement.getTitle());
            item.put("description", achievement.getDescription());
            item.put("progress", achievement.getProgress() + "/" + achievement.getTargetProgress());
            item.put("icon", achievement.getIconResId());
            item.put("unlocked", achievement.isUnlocked());
            data.add(item);
        }

        // Create adapter
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                data,
                R.layout.achievement_item,
                new String[]{"title", "description", "progress", "icon", "unlocked"},
                new int[]{R.id.achievementTitle, R.id.achievementDescription, R.id.achievementProgress, R.id.achievementIcon, R.id.achievementStatus}
        );

        // Set adapter
        achievementListView.setAdapter(adapter);
    }
}
