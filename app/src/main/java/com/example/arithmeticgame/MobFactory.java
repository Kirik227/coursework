package com.example.arithmeticgame;

import android.content.Context;
import android.graphics.Color;

import java.util.Random;

public class MobFactory {

    public enum MobType {
        NORMAL,
        FAST,
        TOUGH,
        BOSS
    }

    public static class MobData {
        private MobType type;
        private String name;
        private int color;
        private float healthMultiplier;
        private float speedMultiplier;
        private int pointsValue;
        private int size;

        public MobData(MobType type, String name, int color, float healthMultiplier,
                       float speedMultiplier, int pointsValue, int size) {
            this.type = type;
            this.name = name;
            this.color = color;
            this.healthMultiplier = healthMultiplier;
            this.speedMultiplier = speedMultiplier;
            this.pointsValue = pointsValue;
            this.size = size;
        }

        public MobType getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public int getColor() {
            return color;
        }

        public float getHealthMultiplier() {
            return healthMultiplier;
        }

        public float getSpeedMultiplier() {
            return speedMultiplier;
        }

        public int getPointsValue() {
            return pointsValue;
        }

        public int getSize() {
            return size;
        }
    }

    private Context context;
    private Random random;

    public MobFactory(Context context) {
        this.context = context;
        this.random = new Random();
    }

    public MobData createMob(MobType type, int level) {
        switch (type) {
            case NORMAL:
                return createNormalMob(level);
            case FAST:
                return createFastMob(level);
            case TOUGH:
                return createToughMob(level);
            case BOSS:
                return createBossMob(level);
            default:
                return createNormalMob(level);
        }
    }

    public MobData createRandomMob(int level) {
        // Boss every 5 levels
        if (level % 5 == 0 && level > 0) {
            return createBossMob(level);
        }

        // Otherwise random mob type based on level
        int rand = random.nextInt(100);
        if (level < 3) {
            // Early levels: mostly normal mobs
            return createNormalMob(level);
        } else if (level < 7) {
            // Mid levels: introduce fast mobs
            if (rand < 70) {
                return createNormalMob(level);
            } else {
                return createFastMob(level);
            }
        } else {
            // Later levels: all mob types
            if (rand < 40) {
                return createNormalMob(level);
            } else if (rand < 70) {
                return createFastMob(level);
            } else {
                return createToughMob(level);
            }
        }
    }

    private MobData createNormalMob(int level) {
        return new MobData(
                MobType.NORMAL,
                "Обычный Моб",
                Color.rgb(76, 175, 80), // Green
                1.0f,
                1.0f,
                1,
                100 // Base size
        );
    }

    private MobData createFastMob(int level) {
        return new MobData(
                MobType.FAST,
                "Быстрый Моб",
                Color.rgb(33, 150, 243), // Blue
                0.7f,
                1.5f,
                2,
                80 // Smaller size
        );
    }

    private MobData createToughMob(int level) {
        return new MobData(
                MobType.TOUGH,
                "Сложный Моб",
                Color.rgb(156, 39, 176), // Purple
                1.5f,
                0.8f,
                3,
                120 // Larger size
        );
    }

    private MobData createBossMob(int level) {
        return new MobData(
                MobType.BOSS,
                "БОСС",
                Color.rgb(244, 67, 54), // Red
                2.5f,
                0.7f,
                5,
                150 // Much larger size
        );
    }
}

