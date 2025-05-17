package com.example.arithmeticgame;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class  PowerUpManager {

    public enum PowerUpType {
        EXTRA_TIME,
        HINT,
        SKIP_PROBLEM,
        DOUBLE_POINTS
    }

    public static class PowerUp {
        private PowerUpType type;
        private String name;
        private String description;
        private int iconResId;
        private boolean isActive;
        private long activationTime;
        private long duration; // in milliseconds, 0 for instant use

        public PowerUp(PowerUpType type, String name, String description, int iconResId, long duration) {
            this.type = type;
            this.name = name;
            this.description = description;
            this.iconResId = iconResId;
            this.duration = duration;
            this.isActive = false;
        }

        public PowerUpType getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public int getIconResId() {
            return iconResId;
        }

        public boolean isActive() {
            return isActive;
        }

        public void activate() {
            isActive = true;
            activationTime = System.currentTimeMillis();
        }

        public void deactivate() {
            isActive = false;
        }

        public boolean hasExpired() {
            if (duration == 0) {
                return true; // Instant use power-ups expire immediately after use
            }
            return isActive && (System.currentTimeMillis() - activationTime > duration);
        }

        public long getRemainingTime() {
            if (!isActive || duration == 0) {
                return 0;
            }
            long elapsed = System.currentTimeMillis() - activationTime;
            return Math.max(0, duration - elapsed);
        }
    }

    private Context context;
    private List<PowerUp> availablePowerUps;
    private Random random;

    public PowerUpManager(Context context) {
        this.context = context;
        this.availablePowerUps = new ArrayList<>();
        this.random = new Random();

        // Initialize available power-ups
        initializePowerUps();
    }

    private void initializePowerUps() {
        // Extra Time: Adds 10 seconds to the timer
        availablePowerUps.add(new PowerUp(
                PowerUpType.EXTRA_TIME,
                "Дополнительное время",
                "Добавляет 10 секунд к таймеру",
                R.drawable.ic_launcher_foreground, // Replace with actual icon
                0 // Instant use
        ));

        // Hint: Shows the first digit of the answer
        availablePowerUps.add(new PowerUp(
                PowerUpType.HINT,
                "Подсказка",
                "Показать первую цифру ответа",
                R.drawable.ic_launcher_foreground, // Replace with actual icon
                0 // Instant use
        ));

        // Skip Problem: Skips the current problem without penalty
        availablePowerUps.add(new PowerUp(
                PowerUpType.SKIP_PROBLEM,
                "Пропустить",
                "Пропустить текущую проблему без штрафов",
                R.drawable.ic_launcher_foreground, // Replace with actual icon
                0 // Instant use
        ));

        // Double Points: Doubles points for 30 seconds
        availablePowerUps.add(new PowerUp(
                PowerUpType.DOUBLE_POINTS,
                "Двойные баллы",
                "Удваивает очки в течение 30 секунд",
                R.drawable.ic_launcher_foreground, // Replace with actual icon
                30000 // 30 seconds duration
        ));
    }

    public PowerUp getRandomPowerUp() {
        if (availablePowerUps.isEmpty()) {
            return null;
        }

        int index = random.nextInt(availablePowerUps.size());
        PowerUp powerUpTemplate = availablePowerUps.get(index);

        // Create a new instance of the power-up
        return new PowerUp(
                powerUpTemplate.getType(),
                powerUpTemplate.getName(),
                powerUpTemplate.getDescription(),
                powerUpTemplate.getIconResId(),
                powerUpTemplate.duration
        );
    }

    public Drawable getIconForPowerUp(PowerUp powerUp) {
        return ContextCompat.getDrawable(context, powerUp.getIconResId());
    }

    public void updateActivePowerUps(List<PowerUp> activePowerUps) {
        // Check for expired power-ups and deactivate them
        for (PowerUp powerUp : activePowerUps) {
            if (powerUp.isActive() && powerUp.hasExpired()) {
                powerUp.deactivate();
            }
        }
    }
}
