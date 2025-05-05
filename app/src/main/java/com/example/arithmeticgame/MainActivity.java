package com.example.arithmeticgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private RadioGroup difficultyGroup;
    private CheckBox additionCheckbox;
    private CheckBox subtractionCheckbox;
    private CheckBox multiplicationCheckbox;
    private CheckBox divisionCheckbox;
    private Button startButton;
    private Button highScoreButton;
    private Button achievementsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        // Set default selection
        additionCheckbox.setChecked(true);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });

        // Set up high score button
        highScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HighScoreActivity.class);
                startActivity(intent);
            }
        });

        // Set up achievements button
        achievementsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AchievementActivity.class);
                startActivity(intent);
            }
        });
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
        startActivity(intent);
    }
}