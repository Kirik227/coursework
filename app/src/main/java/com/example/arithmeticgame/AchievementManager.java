package com.example.arithmeticgame;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.List;

public class AchievementManager {

    public static class Achievement {
        private String id;
        private String title;
        private String description;
        private boolean unlocked;
        private int progress;
        private int targetProgress;
        private int iconResId;

        public Achievement(String id, String title, String description, int targetProgress, int iconResId) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.targetProgress = targetProgress;
            this.iconResId = iconResId;
            this.unlocked = false;
            this.progress = 0;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public boolean isUnlocked() {
            return unlocked;
        }

        public int getProgress() {
            return progress;
        }

        public int getTargetProgress() {
            return targetProgress;
        }

        public int getIconResId() {
            return iconResId;
        }

        public void setProgress(int progress) {
            this.progress = Math.min(progress, targetProgress);
            if (this.progress >= targetProgress) {
                unlocked = true;
            }
        }

        public void incrementProgress() {
            setProgress(progress + 1);
        }

        public float getProgressPercentage() {
            return (float) progress / targetProgress;
        }
    }

    private Context context;
    private List<Achievement> achievements;
    private SharedPreferences prefs;

    public AchievementManager(Context context) {
        this.context = context;
        this.achievements = new ArrayList<>();
        this.prefs = context.getSharedPreferences("MathGameAchievements", Context.MODE_PRIVATE);

        // Initialize achievements
        initializeAchievements();

        // Load saved progress
        loadProgress();
    }

    private void initializeAchievements() {
        // Add achievements
        achievements.add(new Achievement(
                "first_correct",
                "First Steps",
                "Answer your first problem correctly",
                1,
                R.drawable.ic_launcher_foreground // Replace with actual icon
        ));

        achievements.add(new Achievement(
                "ten_correct",
                "Getting Started",
                "Answer 10 problems correctly",
                10,
                R.drawable.ic_launcher_foreground // Replace with actual icon
        ));

        achievements.add(new Achievement(
                "fifty_correct",
                "Math Enthusiast",
                "Answer 50 problems correctly",
                50,
                R.drawable.ic_launcher_foreground // Replace with actual icon
        ));

        achievements.add(new Achievement(
                "hundred_correct",
                "Math Master",
                "Answer 100 problems correctly",
                100,
                R.drawable.ic_launcher_foreground // Replace with actual icon
        ));

        achievements.add(new Achievement(
                "combo_5",
                "Combo Starter",
                "Get a 5x combo",
                1,
                R.drawable.ic_launcher_foreground // Replace with actual icon
        ));

        achievements.add(new Achievement(
                "combo_10",
                "Combo Master",
                "Get a 10x combo",
                1,
                R.drawable.ic_launcher_foreground // Replace with actual icon
        ));

        achievements.add(new Achievement(
                "level_5",
                "Level Up",
                "Reach level 5",
                1,
                R.drawable.ic_launcher_foreground // Replace with actual icon
        ));

        achievements.add(new Achievement(
                "level_10",
                "High Achiever",
                "Reach level 10",
                1,
                R.drawable.ic_launcher_foreground // Replace with actual icon
        ));
    }

    private void loadProgress() {
        for (Achievement achievement : achievements) {
            int progress = prefs.getInt(achievement.getId(), 0);
            achievement.setProgress(progress);
        }
    }

    public void saveProgress() {
        SharedPreferences.Editor editor = prefs.edit();
        for (Achievement achievement : achievements) {
            editor.putInt(achievement.getId(), achievement.getProgress());
        }
        editor.apply();
    }

    public List<Achievement> getAchievements() {
        return achievements;
    }

    public List<Achievement> getUnlockedAchievements() {
        List<Achievement> unlocked = new ArrayList<>();
        for (Achievement achievement : achievements) {
            if (achievement.isUnlocked()) {
                unlocked.add(achievement);
            }
        }
        return unlocked;
    }

    public Achievement getAchievementById(String id) {
        for (Achievement achievement : achievements) {
            if (achievement.getId().equals(id)) {
                return achievement;
            }
        }
        return null;
    }

    public void updateAchievement(String id, int progress) {
        Achievement achievement = getAchievementById(id);
        if (achievement != null) {
            achievement.setProgress(progress);
            saveProgress();
        }
    }

    public void incrementAchievement(String id) {
        Achievement achievement = getAchievementById(id);
        if (achievement != null) {
            achievement.incrementProgress();
            saveProgress();
        }
    }

    // Track specific game events
    public void onCorrectAnswer(int totalCorrect, int comboCount, int currentLevel) {
        // Update total correct answers achievements
        if (totalCorrect == 1) {
            updateAchievement("first_correct", 1);
        }
        if (totalCorrect >= 10) {
            updateAchievement("ten_correct", totalCorrect);
        }
        if (totalCorrect >= 50) {
            updateAchievement("fifty_correct", totalCorrect);
        }
        if (totalCorrect >= 100) {
            updateAchievement("hundred_correct", totalCorrect);
        }

        // Update combo achievements
        if (comboCount >= 5) {
            updateAchievement("combo_5", 1);
        }
        if (comboCount >= 10) {
            updateAchievement("combo_10", 1);
        }

        // Update level achievements
        if (currentLevel >= 5) {
            updateAchievement("level_5", 1);
        }
        if (currentLevel >= 10) {
            updateAchievement("level_10", 1);
        }
    }
}
