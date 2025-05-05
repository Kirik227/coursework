package com.example.arithmeticgame;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.media.MediaPlayer;
import android.content.Intent;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class GameActivity extends AppCompatActivity {

    private TextView problemTextView;
    private TextView timerTextView;
    private TextView scoreTextView;
    private TextView levelTextView;
    private TextView comboTextView;
    private EditText answerEditText;
    private Button checkButton;
    private LinearLayout powerUpContainer;

    private String difficulty;
    private boolean addition;
    private boolean subtraction;
    private boolean multiplication;
    private boolean division;

    private int score = 0;
    private int currentAnswer;
    private CountDownTimer timer;
    private long timeRemaining;
    private Random random = new Random();

    private MobView mobView;
    private MediaPlayer correctSound;
    private MediaPlayer wrongSound;
    private MediaPlayer gameOverSound;
    private MediaPlayer levelUpSound;
    private MediaPlayer powerUpSound;

    // New game systems
    private DifficultyManager difficultyManager;
    private ComboSystem comboSystem;
    private PowerUpManager powerUpManager;
    private AchievementManager achievementManager;
    private List<PowerUpManager.PowerUp> activePowerUps;

    // Power-up UI elements
    private List<ImageView> powerUpIcons;

    // Animation
    private Animation shakeAnimation;
    private Animation fadeInAnimation;
    private Animation bounceAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Initialize UI components
        problemTextView = findViewById(R.id.problemTextView);
        timerTextView = findViewById(R.id.timerTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        levelTextView = findViewById(R.id.levelTextView);
        comboTextView = findViewById(R.id.comboTextView);
        answerEditText = findViewById(R.id.answerEditText);
        checkButton = findViewById(R.id.checkButton);
        powerUpContainer = findViewById(R.id.powerUpContainer);
        mobView = findViewById(R.id.mobView);

        // Get game settings from intent
        difficulty = getIntent().getStringExtra("DIFFICULTY");
        addition = getIntent().getBooleanExtra("ADDITION", true);
        subtraction = getIntent().getBooleanExtra("SUBTRACTION", false);
        multiplication = getIntent().getBooleanExtra("MULTIPLICATION", false);
        division = getIntent().getBooleanExtra("DIVISION", false);

        // Initialize game systems
        difficultyManager = new DifficultyManager(difficulty);
        comboSystem = new ComboSystem();
        powerUpManager = new PowerUpManager(this);
        achievementManager = new AchievementManager(this);
        activePowerUps = new ArrayList<>();
        powerUpIcons = new ArrayList<>();

        // Load animations
        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake);
        fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce);

        // Initialize sounds - with error handling
        try {
            correctSound = MediaPlayer.create(this, R.raw.correct_sound);
            wrongSound = MediaPlayer.create(this, R.raw.wrong_sound);
            gameOverSound = MediaPlayer.create(this, R.raw.game_over_sound);
            levelUpSound = MediaPlayer.create(this, R.raw.level_up_sound);
            powerUpSound = MediaPlayer.create(this, R.raw.power_up_sound);
        } catch (Exception e) {
            // Handle missing sound resources
            Toast.makeText(this, "Sound resources not found", Toast.LENGTH_SHORT).show();
        }

        // Set up check button
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });

        // Update UI
        updateScore();
        updateLevel();
        updateCombo();

        // Start the game
        generateNewProblem();
    }

    private void generateNewProblem() {
        // Cancel existing timer
        if (timer != null) {
            timer.cancel();
        }

        // Generate a new math problem based on selected operations
        List<String> operations = new ArrayList<>();
        if (addition) operations.add("+");
        if (subtraction) operations.add("-");
        if (multiplication) operations.add("×");
        if (division) operations.add("÷");

        String operation = operations.get(random.nextInt(operations.size()));

        // Get difficulty parameters
        int minNumber = difficultyManager.getMinNumber();
        int maxNumber = difficultyManager.getMaxNumber();

        int num1 = random.nextInt(maxNumber - minNumber + 1) + minNumber;
        int num2 = random.nextInt(maxNumber - minNumber + 1) + minNumber;

        // For division, ensure clean division
        if (operation.equals("÷")) {
            // Make sure num2 is not zero
            if (num2 == 0) num2 = 1;

            // Make num1 a multiple of num2 for clean division
            num1 = num2 * (random.nextInt(10) + 1);
        }

        // For subtraction, ensure positive result
        if (operation.equals("-") && num2 > num1) {
            int temp = num1;
            num1 = num2;
            num2 = temp;
        }

        // Calculate the answer
        switch (operation) {
            case "+":
                currentAnswer = num1 + num2;
                break;
            case "-":
                currentAnswer = num1 - num2;
                break;
            case "×":
                currentAnswer = num1 * num2;
                break;
            case "÷":
                currentAnswer = num1 / num2;
                break;
        }

        // Display the problem
        String problem = num1 + " " + operation + " " + num2;
        problemTextView.setText(problem);
        mobView.setProblem(problem);
        mobView.setHealth(1.0f); // Reset mob health
        answerEditText.setText("");

        // Check for power-up chance (10% chance)
        if (random.nextInt(10) == 0) {
            PowerUpManager.PowerUp powerUp = powerUpManager.getRandomPowerUp();
            if (powerUp != null) {
                activePowerUps.add(powerUp);
                addPowerUpIcon(powerUp);
                if (powerUpSound != null) {
                    powerUpSound.start();
                }
                Toast.makeText(this, "Power-up acquired: " + powerUp.getName(), Toast.LENGTH_SHORT).show();
            }
        }

        // Start the timer
        startTimer();
    }

    private void startTimer() {
        // Get time limit from difficulty manager
        int timeLimit = difficultyManager.getTimeLimit();

        // Check for active time-related power-ups
        for (PowerUpManager.PowerUp powerUp : activePowerUps) {
            if (powerUp.isActive() && powerUp.getType() == PowerUpManager.PowerUpType.EXTRA_TIME) {
                timeLimit += 10000; // Add 10 seconds
            }
        }

        timer = new CountDownTimer(timeLimit, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemaining = millisUntilFinished;
                timerTextView.setText("Time: " + millisUntilFinished / 1000 + "." + (millisUntilFinished % 1000) / 100);

                // Update power-ups
                powerUpManager.updateActivePowerUps(activePowerUps);
                updatePowerUpIcons();
            }

            @Override
            public void onFinish() {
                gameOver();
            }
        }.start();
    }

    private void checkAnswer() {
        String userAnswerStr = answerEditText.getText().toString().trim();

        if (userAnswerStr.isEmpty()) {
            Toast.makeText(this, "Please enter an answer", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int userAnswer = Integer.parseInt(userAnswerStr);

            if (userAnswer == currentAnswer) {
                // Correct answer
                if (correctSound != null) {
                    correctSound.start();
                }

                // Increment combo
                comboSystem.incrementCombo();

                // Calculate points with combo multiplier
                int pointsEarned = comboSystem.getComboMultiplier();

                // Check for double points power-up
                for (PowerUpManager.PowerUp powerUp : activePowerUps) {
                    if (powerUp.isActive() && powerUp.getType() == PowerUpManager.PowerUpType.DOUBLE_POINTS) {
                        pointsEarned *= 2;
                    }
                }

                score += pointsEarned;
                difficultyManager.incrementScore();

                // Update UI
                updateScore();
                updateLevel();
                updateCombo();

                // Update achievements
                achievementManager.onCorrectAnswer(score, comboSystem.getComboCount(), difficultyManager.getCurrentLevel());

                // Show hit animation
                mobView.showHitAnimation();

                // Show points earned animation
                showPointsEarnedAnimation(pointsEarned);

                // Short delay before next problem
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        generateNewProblem();
                    }
                }, 500);
            } else {
                // Wrong answer
                if (wrongSound != null) {
                    wrongSound.start();
                }

                // Reset combo
                comboSystem.resetCombo();
                updateCombo();

                // Reduce mob health
                float penalty = difficultyManager.getWrongAnswerPenalty();
                mobView.setHealth(mobView.getHealth() - penalty);

                // Shake the answer field
                answerEditText.startAnimation(shakeAnimation);

                Toast.makeText(this, "Wrong! Try again.", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateScore() {
        scoreTextView.setText("Score: " + score);
    }

    private void updateLevel() {
        levelTextView.setText(difficultyManager.getLevelText());
    }

    private void updateCombo() {
        if (comboSystem.getComboCount() >= 3) {
            comboTextView.setVisibility(View.VISIBLE);
            comboTextView.setText(comboSystem.getComboText() + " x" + comboSystem.getComboMultiplier());
            comboTextView.startAnimation(bounceAnimation);
        } else {
            comboTextView.setVisibility(View.INVISIBLE);
        }
    }

    private void addPowerUpIcon(PowerUpManager.PowerUp powerUp) {
        ImageView iconView = new ImageView(this);
        iconView.setImageResource(powerUp.getIconResId());
        iconView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        iconView.setPadding(8, 8, 8, 8);

        // Set click listener for power-up
        iconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activatePowerUp(powerUp);
                powerUpContainer.removeView(iconView);
                powerUpIcons.remove(iconView);
            }
        });

        powerUpContainer.addView(iconView);
        powerUpIcons.add(iconView);

        // Show animation
        iconView.startAnimation(fadeInAnimation);
    }

    private void updatePowerUpIcons() {
        // Remove expired power-ups
        for (int i = activePowerUps.size() - 1; i >= 0; i--) {
            PowerUpManager.PowerUp powerUp = activePowerUps.get(i);
            if (!powerUp.isActive() && powerUp.hasExpired()) {
                activePowerUps.remove(i);
                if (i < powerUpIcons.size()) {
                    powerUpContainer.removeView(powerUpIcons.get(i));
                    powerUpIcons.remove(i);
                }
            }
        }
    }

    private void activatePowerUp(PowerUpManager.PowerUp powerUp) {
        powerUp.activate();

        if (powerUpSound != null) {
            powerUpSound.start();
        }

        switch (powerUp.getType()) {
            case EXTRA_TIME:
                // Add 10 seconds to the timer
                if (timer != null) {
                    timer.cancel();
                }
                startTimer();
                Toast.makeText(this, "Extra time added!", Toast.LENGTH_SHORT).show();
                break;

            case HINT:
                // Show the first digit of the answer
                String hint = String.valueOf(currentAnswer).charAt(0) + "...";
                Toast.makeText(this, "Hint: " + hint, Toast.LENGTH_LONG).show();
                break;

            case SKIP_PROBLEM:
                // Skip to the next problem
                Toast.makeText(this, "Problem skipped!", Toast.LENGTH_SHORT).show();
                generateNewProblem();
                break;

            case DOUBLE_POINTS:
                // Double points is handled in checkAnswer()
                Toast.makeText(this, "Double points activated for 30 seconds!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void showPointsEarnedAnimation(int points) {
        // Create a temporary TextView to show points earned
        TextView pointsTextView = new TextView(this);
        pointsTextView.setText("+" + points);
        pointsTextView.setTextSize(24);
        pointsTextView.setTextColor(getResources().getColor(android.R.color.holo_green_light));

        // Add to layout
        LinearLayout gameLayout = findViewById(R.id.gameLayout);
        gameLayout.addView(pointsTextView);

        // Position near the mob
        pointsTextView.setX(mobView.getX() + mobView.getWidth() / 2);
        pointsTextView.setY(mobView.getY() + mobView.getHeight() / 4);

        // Create animation
        Animation pointsAnim = AnimationUtils.loadAnimation(this, R.anim.points_earned);
        pointsAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                gameLayout.removeView(pointsTextView);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        // Start animation
        pointsTextView.startAnimation(pointsAnim);
    }

    private void gameOver() {
        if (timer != null) {
            timer.cancel();
        }

        if (gameOverSound != null) {
            gameOverSound.start();
        }

        // Start GameOverActivity
        Intent intent = new Intent(GameActivity.this, GameOverActivity.class);
        intent.putExtra("FINAL_SCORE", score);
        intent.putExtra("DIFFICULTY", difficulty);
        intent.putExtra("ADDITION", addition);
        intent.putExtra("SUBTRACTION", subtraction);
        intent.putExtra("MULTIPLICATION", multiplication);
        intent.putExtra("DIVISION", division);
        intent.putExtra("MAX_COMBO", comboSystem.getMaxCombo());
        intent.putExtra("MAX_LEVEL", difficultyManager.getCurrentLevel());
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }

        // Release MediaPlayer resources
        if (correctSound != null) {
            correctSound.release();
            correctSound = null;
        }
        if (wrongSound != null) {
            wrongSound.release();
            wrongSound = null;
        }
        if (gameOverSound != null) {
            gameOverSound.release();
            gameOverSound = null;
        }
        if (levelUpSound != null) {
            levelUpSound.release();
            levelUpSound = null;
        }
        if (powerUpSound != null) {
            powerUpSound.release();
            powerUpSound = null;
        }
    }
}

