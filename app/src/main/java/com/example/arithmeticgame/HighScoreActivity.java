package com.example.arithmeticgame;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HighScoreActivity extends AppCompatActivity {

    private TextView easyScoreTextView;
    private TextView mediumScoreTextView;
    private TextView hardScoreTextView;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        // Initialize UI components
        easyScoreTextView = findViewById(R.id.easyScoreTextView);
        mediumScoreTextView = findViewById(R.id.mediumScoreTextView);
        hardScoreTextView = findViewById(R.id.hardScoreTextView);
        backButton = findViewById(R.id.backButton);

        // Load high scores
        loadHighScores();

        // Set up back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadHighScores() {
        // Get shared preferences
        SharedPreferences prefs = getSharedPreferences("Настройки математической игры", MODE_PRIVATE);

        // Get high scores for each difficulty
        int easyHighScore = prefs.getInt("HIGH_SCORE_EASY", 0);
        int mediumHighScore = prefs.getInt("HIGH_SCORE_MEDIUM", 0);
        int hardHighScore = prefs.getInt("HIGH_SCORE_HARD", 0);

        // Display high scores
        easyScoreTextView.setText("Легкий: " + easyHighScore);
        mediumScoreTextView.setText("Средний: " + mediumHighScore);
        hardScoreTextView.setText("Сложный: " + hardHighScore);
    }
}

