package com.example.arithmeticgame;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;
import java.util.Random;

public class DailyChallenge {

    public static class Challenge {
        private String title;
        private String description;
        private int targetScore;
        private String difficulty;
        private boolean[] operations; // [addition, subtraction, multiplication, division]
        private int reward;
        private boolean completed;

        public Challenge(String title, String description, int targetScore,
                         String difficulty, boolean[] operations, int reward) {
            this.title = title;
            this.description = description;
            this.targetScore = targetScore;
            this.difficulty = difficulty;
            this.operations = operations;
            this.reward = reward;
            this.completed = false;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public int getTargetScore() {
            return targetScore;
        }

        public String getDifficulty() {
            return difficulty;
        }

        public boolean[] getOperations() {
            return operations;
        }

        public int getReward() {
            return reward;
        }

        public boolean isCompleted() {
            return completed;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }
    }

    private Context context;
    private SharedPreferences prefs;
    private Random random;

    public DailyChallenge(Context context) {
        this.context = context;
        this.prefs = context.getSharedPreferences("MathGameChallenges", Context.MODE_PRIVATE);
        this.random = new Random();
    }

    public Challenge getTodayChallenge() {
        // Check if we already have a challenge for today
        String today = getTodayString();
        String savedDate = prefs.getString("LAST_CHALLENGE_DATE", "");

        if (today.equals(savedDate)) {
            // Load existing challenge
            return loadSavedChallenge();
        } else {
            // Generate new challenge
            Challenge challenge = generateRandomChallenge();
            saveChallenge(challenge);
            return challenge;
        }
    }

    private String getTodayString() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR) + "-" +
                (calendar.get(Calendar.MONTH) + 1) + "-" +
                calendar.get(Calendar.DAY_OF_MONTH);
    }

    private Challenge loadSavedChallenge() {
        String title = prefs.getString("CHALLENGE_TITLE", "Daily Challenge");
        String description = prefs.getString("CHALLENGE_DESCRIPTION", "Complete the challenge");
        int targetScore = prefs.getInt("CHALLENGE_TARGET_SCORE", 10);
        String difficulty = prefs.getString("CHALLENGE_DIFFICULTY", "Medium");
        boolean[] operations = new boolean[4];
        operations[0] = prefs.getBoolean("CHALLENGE_ADDITION", true);
        operations[1] = prefs.getBoolean("CHALLENGE_SUBTRACTION", false);
        operations[2] = prefs.getBoolean("CHALLENGE_MULTIPLICATION", false);
        operations[3] = prefs.getBoolean("CHALLENGE_DIVISION", false);
        int reward = prefs.getInt("CHALLENGE_REWARD", 100);
        boolean completed = prefs.getBoolean("CHALLENGE_COMPLETED", false);

        Challenge challenge = new Challenge(title, description, targetScore, difficulty, operations, reward);
        challenge.setCompleted(completed);

        return challenge;
    }

    private void saveChallenge(Challenge challenge) {
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("LAST_CHALLENGE_DATE", getTodayString());
        editor.putString("CHALLENGE_TITLE", challenge.getTitle());
        editor.putString("CHALLENGE_DESCRIPTION", challenge.getDescription());
        editor.putInt("CHALLENGE_TARGET_SCORE", challenge.getTargetScore());
        editor.putString("CHALLENGE_DIFFICULTY", challenge.getDifficulty());
        editor.putBoolean("CHALLENGE_ADDITION", challenge.getOperations()[0]);
        editor.putBoolean("CHALLENGE_SUBTRACTION", challenge.getOperations()[1]);
        editor.putBoolean("CHALLENGE_MULTIPLICATION", challenge.getOperations()[2]);
        editor.putBoolean("CHALLENGE_DIVISION", challenge.getOperations()[3]);
        editor.putInt("CHALLENGE_REWARD", challenge.getReward());
        editor.putBoolean("CHALLENGE_COMPLETED", challenge.isCompleted());

        editor.apply();
    }

    public void completeChallenge() {
        Challenge challenge = loadSavedChallenge();
        if (!challenge.isCompleted()) {
            challenge.setCompleted(true);
            saveChallenge(challenge);

            // Award points
            SharedPreferences gamePrefs = context.getSharedPreferences("MathGamePrefs", Context.MODE_PRIVATE);
            int currentPoints = gamePrefs.getInt("PLAYER_POINTS", 0);
            SharedPreferences.Editor editor = gamePrefs.edit();
            editor.putInt("PLAYER_POINTS", currentPoints + challenge.getReward());
            editor.apply();
        }
    }

    private Challenge generateRandomChallenge() {
        // Generate random challenge parameters
        String[] titles = {
                "Math Master",
                "Number Ninja",
                "Calculation King",
                "Arithmetic Ace",
                "Formula Wizard"
        };

        String[] difficulties = {"Easy", "Medium", "Hard"};
        String difficulty = difficulties[random.nextInt(difficulties.length)];

        int targetScore;
        switch (difficulty) {
            case "Easy":
                targetScore = 5 + random.nextInt(6); // 5-10
                break;
            case "Medium":
                targetScore = 10 + random.nextInt(11); // 10-20
                break;
            case "Hard":
                targetScore = 20 + random.nextInt(21); // 20-40
                break;
            default:
                targetScore = 10;
        }

        // Ensure at least one operation is selected
        boolean[] operations = new boolean[4];
        int opCount = 0;
        while (opCount == 0) {
            for (int i = 0; i < operations.length; i++) {
                operations[i] = random.nextBoolean();
                if (operations[i]) opCount++;
            }
        }

        // Generate description based on operations
        StringBuilder descBuilder = new StringBuilder("Score " + targetScore + " points using ");
        if (operations[0]) descBuilder.append("addition");
        if (operations[1]) {
            if (operations[0]) descBuilder.append(", ");
            descBuilder.append("subtraction");
        }
        if (operations[2]) {
            if (operations[0] || operations[1]) descBuilder.append(", ");
            descBuilder.append("multiplication");
        }
        if (operations[3]) {
            if (operations[0] || operations[1] || operations[2]) descBuilder.append(", ");
            descBuilder.append("division");
        }
        descBuilder.append(" in " + difficulty + " mode");

        // Calculate reward based on difficulty and target score
        int baseReward = 50;
        int difficultyMultiplier;
        switch (difficulty) {
            case "Easy":
                difficultyMultiplier = 1;
                break;
            case "Medium":
                difficultyMultiplier = 2;
                break;
            case "Hard":
                difficultyMultiplier = 3;
                break;
            default:
                difficultyMultiplier = 1;
        }
        int reward = baseReward * difficultyMultiplier + targetScore * 5;

        return new Challenge(
                titles[random.nextInt(titles.length)],
                descBuilder.toString(),
                targetScore,
                difficulty,
                operations,
                reward
        );
    }
}

