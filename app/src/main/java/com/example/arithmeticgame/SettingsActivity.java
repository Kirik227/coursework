package com.example.arithmeticgame;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SettingsActivity extends AppCompatActivity {

    private Switch soundSwitch;
    private Switch musicSwitch;
    private Switch vibrationSwitch;
    private Switch darkModeSwitch;
    private SeekBar difficultySeekBar;
    private TextView difficultyValueTextView;
    private Button resetTutorialButton;
    private Button resetProgressButton;
    private Button backButton;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize UI components
        soundSwitch = findViewById(R.id.soundSwitch);
        musicSwitch = findViewById(R.id.musicSwitch);
        vibrationSwitch = findViewById(R.id.vibrationSwitch);
        darkModeSwitch = findViewById(R.id.darkModeSwitch);
        difficultySeekBar = findViewById(R.id.difficultySeekBar);
        difficultyValueTextView = findViewById(R.id.difficultyValueTextView);
        resetTutorialButton = findViewById(R.id.resetTutorialButton);
        resetProgressButton = findViewById(R.id.resetProgressButton);
        backButton = findViewById(R.id.backButton);

        // Initialize shared preferences
        prefs = getSharedPreferences("Настройки математической игры", MODE_PRIVATE);

        // Load settings
        loadSettings();

        // Set up listeners
        soundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveSetting("SOUND_ENABLED", isChecked);
            }
        });

        musicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveSetting("MUSIC_ENABLED", isChecked);
            }
        });

        vibrationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveSetting("VIBRATION_ENABLED", isChecked);
            }
        });

        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveSetting("DARK_MODE_ENABLED", isChecked);
                updateDarkMode(isChecked);
            }
        });

        difficultySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateDifficultyText(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Not needed
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                saveSetting("DIFFICULTY_LEVEL", seekBar.getProgress());
            }
        });

        resetTutorialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTutorial();
            }
        });

        resetProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetProgress();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadSettings() {
        boolean soundEnabled = prefs.getBoolean("SOUND_ENABLED", true);
        boolean musicEnabled = prefs.getBoolean("MUSIC_ENABLED", true);
        boolean vibrationEnabled = prefs.getBoolean("VIBRATION_ENABLED", true);
        boolean darkModeEnabled = prefs.getBoolean("DARK_MODE_ENABLED", false);
        int difficultyLevel = prefs.getInt("DIFFICULTY_LEVEL", 1); // 0=Easy, 1=Medium, 2=Hard

        soundSwitch.setChecked(soundEnabled);
        musicSwitch.setChecked(musicEnabled);
        vibrationSwitch.setChecked(vibrationEnabled);
        darkModeSwitch.setChecked(darkModeEnabled);
        difficultySeekBar.setProgress(difficultyLevel);
        updateDifficultyText(difficultyLevel);
    }

    private void saveSetting(String key, boolean value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private void saveSetting(String key, int value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    private void updateDifficultyText(int progress) {
        String difficultyText;
        switch (progress) {
            case 0:
                difficultyText = "Легкий";
                break;
            case 1:
                difficultyText = "Средний";
                break;
            case 2:
                difficultyText = "Сложный";
                break;
            default:
                difficultyText = "Средний";
        }
        difficultyValueTextView.setText(difficultyText);
    }

    private void updateDarkMode(boolean enabled) {
        if (enabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void resetTutorial() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("TUTORIAL_COMPLETED", false);
        editor.apply();
        Toast.makeText(this, "Сброс руководства. Он появится при следующем запуске приложения.", Toast.LENGTH_SHORT).show();
    }

    private void resetProgress() {
        // Show confirmation dialog
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Сброс хода выполнения");
        builder.setMessage("Вы уверены, что хотите сбросить весь прогресс? При этом будут удалены все рекорды, достижения и приобретенные предметы.");
        builder.setPositiveButton("Reset", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialog, int which) {
                // Reset all progress
                SharedPreferences.Editor editor = prefs.edit();

                // Reset high scores
                editor.remove("HIGH_SCORE_EASY");
                editor.remove("HIGH_SCORE_MEDIUM");
                editor.remove("HIGH_SCORE_HARD");

                // Reset player points
                editor.putInt("PLAYER_POINTS", 0);

                // Reset purchased items
                editor.remove("SHOP_ITEM_extra_time");
                editor.remove("SHOP_ITEM_hint");
                editor.remove("SHOP_ITEM_skip");
                editor.remove("SHOP_ITEM_double_points");

                editor.apply();

                // Reset achievements
                AchievementManager achievementManager = new AchievementManager(SettingsActivity.this);
                for (AchievementManager.Achievement achievement : achievementManager.getAchievements()) {
                    achievementManager.updateAchievement(achievement.getId(), 0);
                }

                Toast.makeText(SettingsActivity.this, "Весь прогресс был сброшен.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Отменить", null);
        builder.show();
    }
}

