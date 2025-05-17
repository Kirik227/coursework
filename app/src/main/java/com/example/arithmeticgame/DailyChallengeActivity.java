package com.example.arithmeticgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DailyChallengeActivity extends AppCompatActivity {

    private TextView challengeTitleTextView;
    private TextView challengeDescriptionTextView;
    private TextView challengeRewardTextView;
    private TextView challengeStatusTextView;
    private Button startChallengeButton;
    private Button backButton;

    private DailyChallenge dailyChallenge;
    private DailyChallenge.Challenge todayChallenge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_challenge);

        // Initialize UI components
        challengeTitleTextView = findViewById(R.id.challengeTitleTextView);
        challengeDescriptionTextView = findViewById(R.id.challengeDescriptionTextView);
        challengeRewardTextView = findViewById(R.id.challengeRewardTextView);
        challengeStatusTextView = findViewById(R.id.challengeStatusTextView);
        startChallengeButton = findViewById(R.id.startChallengeButton);
        backButton = findViewById(R.id.backButton);

        // Initialize daily challenge
        dailyChallenge = new DailyChallenge(this);
        todayChallenge = dailyChallenge.getTodayChallenge();

        // Update UI with challenge info
        updateChallengeUI();

        // Set up start challenge button
        startChallengeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChallenge();
            }
        });

        // Set up back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh challenge data when returning to this activity
        todayChallenge = dailyChallenge.getTodayChallenge();
        updateChallengeUI();
    }

    private void updateChallengeUI() {
        challengeTitleTextView.setText(todayChallenge.getTitle());
        challengeDescriptionTextView.setText(todayChallenge.getDescription());
        challengeRewardTextView.setText("Reward: " + todayChallenge.getReward() + " points");

        if (todayChallenge.isCompleted()) {
            challengeStatusTextView.setText("Status: Completed");
            challengeStatusTextView.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            startChallengeButton.setEnabled(false);
            startChallengeButton.setText("Challenge Completed");
        } else {
            challengeStatusTextView.setText("Status: Not Completed");
            challengeStatusTextView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            startChallengeButton.setEnabled(true);
            startChallengeButton.setText("Start Challenge");
        }
    }

    private void startChallenge() {
        // Start game activity with challenge settings
        Intent intent = new Intent(DailyChallengeActivity.this, GameActivity.class);
        intent.putExtra("DIFFICULTY", todayChallenge.getDifficulty());
        intent.putExtra("ADDITION", todayChallenge.getOperations()[0]);
        intent.putExtra("SUBTRACTION", todayChallenge.getOperations()[1]);
        intent.putExtra("MULTIPLICATION", todayChallenge.getOperations()[2]);
        intent.putExtra("DIVISION", todayChallenge.getOperations()[3]);
        intent.putExtra("IS_CHALLENGE", true);
        intent.putExtra("CHALLENGE_TARGET", todayChallenge.getTargetScore());
        startActivity(intent);
    }
}
