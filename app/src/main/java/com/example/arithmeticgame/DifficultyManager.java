package com.example.arithmeticgame;

public class DifficultyManager {

    private String baseDifficulty; // "Easy", "Medium", or "Hard"
    private int currentLevel;
    private int score;
    private int problemsPerLevel;

    // Difficulty parameters
    private int minNumber;
    private int maxNumber;
    private int timeLimit; // in milliseconds
    private float wrongAnswerPenalty; // percentage of health lost per wrong answer

    public DifficultyManager(String baseDifficulty) {
        this.baseDifficulty = baseDifficulty;
        this.currentLevel = 1;
        this.score = 0;
        this.problemsPerLevel = 5; // Level up every 5 problems

        // Initialize difficulty parameters based on base difficulty
        initializeDifficultyParameters();
    }

    private void initializeDifficultyParameters() {
        switch (baseDifficulty) {
            case "Easy":
                minNumber = 1;
                maxNumber = 10;
                timeLimit = 15000; // 15 seconds
                wrongAnswerPenalty = 0.25f;
                break;
            case "Medium":
                minNumber = 1;
                maxNumber = 20;
                timeLimit = 10000; // 10 seconds
                wrongAnswerPenalty = 0.33f;
                break;
            case "Hard":
                minNumber = 1;
                maxNumber = 50;
                timeLimit = 5000; // 5 seconds
                wrongAnswerPenalty = 0.5f;
                break;
            default:
                // Default to Easy
                minNumber = 1;
                maxNumber = 10;
                timeLimit = 15000;
                wrongAnswerPenalty = 0.25f;
        }
    }

    public void incrementScore() {
        score++;

        // Check if we should level up
        if (score % problemsPerLevel == 0) {
            levelUp();
        }
    }

    private void levelUp() {
        currentLevel++;

        // Increase difficulty parameters
        maxNumber += 5;
        timeLimit = Math.max(2000, timeLimit - 500); // Decrease time limit, but not below 2 seconds
        wrongAnswerPenalty = Math.min(1.0f, wrongAnswerPenalty + 0.05f); // Increase penalty, but not above 100%
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getMinNumber() {
        return minNumber;
    }

    public int getMaxNumber() {
        return maxNumber;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public float getWrongAnswerPenalty() {
        return wrongAnswerPenalty;
    }

    public String getLevelText() {
        return "Level " + currentLevel;
    }

    public boolean isTimeBonus() {
        // Every 3 levels, give a time bonus
        return currentLevel % 3 == 0;
    }

    public int getTimeBonusAmount() {
        return 5000; // 5 seconds bonus
    }
}