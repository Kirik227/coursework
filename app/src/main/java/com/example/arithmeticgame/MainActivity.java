package com.example.arithmeticgame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends AppCompatActivity {

    private RadioGroup difficultyGroup;
    private CheckBox additionCheckbox;
    private CheckBox subtractionCheckbox;
    private CheckBox multiplicationCheckbox;
    private CheckBox divisionCheckbox;
    private Button startButton;
    private Button highScoreButton;
    private Button achievementsButton;
    private Button shopButton;
    private Button dailyChallengeButton;
    private Button leaderboardButton;
    private Button settingsButton;
    private TextView pointsTextView;

    private int playerPoints;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load settings
        prefs = getSharedPreferences("MathGamePrefs", MODE_PRIVATE);
        boolean darkModeEnabled = prefs.getBoolean("DARK_MODE_ENABLED", false);

        // Apply dark mode if enabled
        if (darkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        setContentView(R.layout.activity_main);

        // Initialize UI components
        difficultyGroup = findViewById(R.id.difficultyGroup);
        additionCheckbox = findViewById(R.id.additionCheckbox);
        subtractionCheckbox = findViewById(R.id.subtractionCheckbox);
        multiplicationCheckbox = findViewById(R.id.multiplicationCheckbox);
        divisionCheckbox = findViewById(R.id.divisionCheckbox);
        startButton = findViewById(R.id.startButton);
        highScoreButton = findViewById(R.id.highScoreButton);
        achievementsButton = findViewById(R.id.achievementsButton);
        shopButton = findViewById(R.id.shopButton);
        dailyChallengeButton = findViewById(R.id.dailyChallengeButton);
        leaderboardButton = findViewById(R.id.leaderboardButton);
        settingsButton = findViewById(R.id.settingsButton);
        pointsTextView = findViewById(R.id.pointsTextView);

        // Set default selection
        additionCheckbox.setChecked(true);

        // Load player points
        playerPoints = prefs.getInt("PLAYER_POINTS", 0);
        updatePointsDisplay();

        // Check if tutorial should be shown
        boolean tutorialCompleted = prefs.getBoolean("TUTORIAL_COMPLETED", false);
        if (!tutorialCompleted) {
            // Show tutorial
            Intent tutorialIntent = new Intent(MainActivity.this, TutorialActivity.class);
            startActivity(tutorialIntent);
        }

        // Set up button listeners
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });

        highScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HighScoreActivity.class);
                startActivity(intent);
            }
        });

        achievementsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AchievementActivity.class);
                startActivity(intent);
            }
        });

        shopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShopActivity.class);
                startActivity(intent);
            }
        });

        dailyChallengeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DailyChallengeActivity.class);
                startActivity(intent);
            }
        });

        leaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LeaderboardActivity.class);
                startActivity(intent);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh player points when returning to this activity
        playerPoints = prefs.getInt("PLAYER_POINTS", 0);
        updatePointsDisplay();

        // Apply difficulty from settings
        int difficultyLevel = prefs.getInt("DIFFICULTY_LEVEL", 1); // 0=Easy, 1=Medium, 2=Hard
        switch (difficultyLevel) {
            case 0:
                ((RadioButton) findViewById(R.id.easyRadio)).setChecked(true);
                break;
            case 1:
                ((RadioButton) findViewById(R.id.mediumRadio)).setChecked(true);
                break;
            case 2:
                ((RadioButton) findViewById(R.id.hardRadio)).setChecked(true);
                break;
        }
    }

    private void updatePointsDisplay() {
        pointsTextView.setText("Баллы: " + playerPoints);
    }

    private void startGame() {
        // Get selected difficulty
        int selectedDifficultyId = difficultyGroup.getCheckedRadioButtonId();
        RadioButton selectedDifficulty = findViewById(selectedDifficultyId);
        String difficulty = selectedDifficulty.getText().toString();

        // Get selected operations
        boolean addition = additionCheckbox.isChecked();
        boolean subtraction = subtractionCheckbox.isChecked();
        boolean multiplication = multiplicationCheckbox.isChecked();
        boolean division = divisionCheckbox.isChecked();

        // Ensure at least one operation is selected
        if (!addition && !subtraction && !multiplication && !division) {
            additionCheckbox.setChecked(true);
            addition = true;
        }

        // Start game activity with selected options
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        intent.putExtra("DIFFICULTY", difficulty);
        intent.putExtra("ADDITION", addition);
        intent.putExtra("SUBTRACTION", subtraction);
        intent.putExtra("MULTIPLICATION", multiplication);
        intent.putExtra("DIVISION", division);
        intent.putExtra("IS_CHALLENGE", false);
        startActivity(intent);
    }
}