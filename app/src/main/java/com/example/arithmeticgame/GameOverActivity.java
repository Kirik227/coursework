package com.example.arithmeticgame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameOverActivity extends AppCompatActivity {

    private TextView finalScoreTextView;
    private TextView highScoreTextView;
    private Button playAgainButton;
    private Button mainMenuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        // Initialize UI components
        finalScoreTextView = findViewById(R.id.finalScoreTextView);
        highScoreTextView = findViewById(R.id.highScoreTextView);
        playAgainButton = findViewById(R.id.playAgainButton);
        mainMenuButton = findViewById(R.id.mainMenuButton);

        // Get final score from intent
        int finalScore = getIntent().getIntExtra("FINAL_SCORE", 0);
        String difficulty = getIntent().getStringExtra("DIFFICULTY");

        // Update high score if needed
        updateHighScore(finalScore, difficulty);

        // Display final score
        finalScoreTextView.setText("Финальные очки: " + finalScore);

        // Set up play again button
        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get game settings from intent and start a new game
                Intent intent = new Intent(GameOverActivity.this, GameActivity.class);
                intent.putExtra("DIFFICULTY", getIntent().getStringExtra("DIFFICULTY"));
                intent.putExtra("ADDITION", getIntent().getBooleanExtra("ADDITION", true));
                intent.putExtra("SUBTRACTION", getIntent().getBooleanExtra("SUBTRACTION", false));
                intent.putExtra("MULTIPLICATION", getIntent().getBooleanExtra("MULTIPLICATION", false));
                intent.putExtra("DIVISION", getIntent().getBooleanExtra("DIVISION", false));
                startActivity(intent);
                finish();
            }
        });

        // Set up main menu button
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameOverActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void updateHighScore(int score, String difficulty) {
        // Get shared preferences
        SharedPreferences prefs = getSharedPreferences("MathGamePrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Get current high score for this difficulty
        String highScoreKey = "HIGH_SCORE_" + difficulty.toUpperCase();
        int currentHighScore = prefs.getInt(highScoreKey, 0);

        // Update high score if needed
        if (score > currentHighScore) {
            editor.putInt(highScoreKey, score);
            editor.apply();
            highScoreTextView.setText("New High Score: " + score + "!");
        } else {
            highScoreTextView.setText("High Score: " + currentHighScore);
        }
    }
}


