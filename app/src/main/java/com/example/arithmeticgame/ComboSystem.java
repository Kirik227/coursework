package com.example.arithmeticgame;

public class ComboSystem {

    private int comboCount;
    private int maxCombo;
    private long lastCorrectAnswerTime;
    private static final long COMBO_TIMEOUT = 5000; // 5 seconds to maintain combo

    public ComboSystem() {
        comboCount = 0;
        maxCombo = 0;
        lastCorrectAnswerTime = 0;
    }

    public void incrementCombo() {
        long currentTime = System.currentTimeMillis();

        // Check if combo has timed out
        if (currentTime - lastCorrectAnswerTime > COMBO_TIMEOUT && comboCount > 0) {
            resetCombo();
        }

        comboCount++;
        if (comboCount > maxCombo) {
            maxCombo = comboCount;
        }

        lastCorrectAnswerTime = currentTime;
    }

    public void resetCombo() {
        comboCount = 0;
    }

    public int getComboCount() {
        return comboCount;
    }

    public int getMaxCombo() {
        return maxCombo;
    }

    public boolean isComboActive() {
        return comboCount > 0 && (System.currentTimeMillis() - lastCorrectAnswerTime <= COMBO_TIMEOUT);
    }

    public int getComboMultiplier() {
        if (comboCount < 3) {
            return 1;
        } else if (comboCount < 5) {
            return 2;
        } else if (comboCount < 10) {
            return 3;
        } else {
            return 5;
        }
    }

    public String getComboText() {
        if (comboCount < 3) {
            return "";
        } else if (comboCount < 5) {
            return "Good!";
        } else if (comboCount < 10) {
            return "Great!";
        } else if (comboCount < 15) {
            return "Excellent!";
        } else if (comboCount < 20) {
            return "Amazing!";
        } else {
            return "Incredible!";
        }
    }
}