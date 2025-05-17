package com.example.arithmeticgame;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class TutorialActivity extends AppCompatActivity {

    private ViewPager2 tutorialViewPager;
    private Button skipButton;
    private Button nextButton;
    private Button prevButton;
    private TextView pageIndicatorTextView;

    private List<TutorialPage> tutorialPages;
    private TutorialPagerAdapter tutorialAdapter;
    private int currentPage = 0;

    public static class TutorialPage {
        private int imageResId;
        private String title;
        private String description;

        public TutorialPage(int imageResId, String title, String description) {
            this.imageResId = imageResId;
            this.title = title;
            this.description = description;
        }

        public int getImageResId() {
            return imageResId;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        // Initialize UI components
        tutorialViewPager = findViewById(R.id.tutorialViewPager);
        skipButton = findViewById(R.id.skipButton);
        nextButton = findViewById(R.id.nextButton);
        prevButton = findViewById(R.id.prevButton);
        pageIndicatorTextView = findViewById(R.id.pageIndicatorTextView);

        // Initialize tutorial pages
        initializeTutorialPages();

        // Set up adapter
        tutorialAdapter = new TutorialPagerAdapter(this, tutorialPages);
        tutorialViewPager.setAdapter(tutorialAdapter);

        // Update UI for initial page
        updatePageIndicator();
        prevButton.setVisibility(View.INVISIBLE); // Hide prev button on first page

        // Set up button listeners
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishTutorial();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage < tutorialPages.size() - 1) {
                    currentPage++;
                    tutorialViewPager.setCurrentItem(currentPage);
                    updatePageIndicator();
                    updateButtonVisibility();
                } else {
                    finishTutorial();
                }
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage > 0) {
                    currentPage--;
                    tutorialViewPager.setCurrentItem(currentPage);
                    updatePageIndicator();
                    updateButtonVisibility();
                }
            }
        });

        // Set up page change listener
        tutorialViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                updatePageIndicator();
                updateButtonVisibility();
            }
        });
    }

    private void initializeTutorialPages() {
        tutorialPages = new ArrayList<>();

        // Add tutorial pages
        tutorialPages.add(new TutorialPage(
                R.drawable.ic_launcher_foreground, // Replace with actual tutorial image
                "Добро пожаловать в математический игровой тренажер!",
                "Тренируйте свои математические навыки с помощью веселых и сложных задач."
        ));

        tutorialPages.add(new TutorialPage(
                R.drawable.ic_launcher_foreground, // Replace with actual tutorial image
                "Решайте математические задачи",
                "Правильно решайте математические задачи, чтобы победить монстров и заработать очки."
        ));

        tutorialPages.add(new TutorialPage(
                R.drawable.ic_launcher_foreground, // Replace with actual tutorial image
                "Создавайте комбинации",
                "Правильно отвечайте на вопросы подряд, чтобы создавать комбинации и зарабатывать больше очков."
        ));

        tutorialPages.add(new TutorialPage(
                R.drawable.ic_launcher_foreground, // Replace with actual tutorial image
                "Используйте бонусы",
                "\n" +
                        "Собирайте и используйте бонусы, которые помогут вам решить сложные задачи."
        ));

        tutorialPages.add(new TutorialPage(
                R.drawable.ic_launcher_foreground, // Replace with actual tutorial image
                "Ежедневные задачи",
                "Выполняйте ежедневные задания, чтобы заработать дополнительные очки и награды."
        ));

        tutorialPages.add(new TutorialPage(
                R.drawable.ic_launcher_foreground, // Replace with actual tutorial image
                "Готовы к игре?",
                "Давайте начнем тренировать ваши математические навыки!"
        ));
    }

    private void updatePageIndicator() {
        pageIndicatorTextView.setText((currentPage + 1) + "/" + tutorialPages.size());
    }

    private void updateButtonVisibility() {
        if (currentPage == 0) {
            prevButton.setVisibility(View.INVISIBLE);
        } else {
            prevButton.setVisibility(View.VISIBLE);
        }

        if (currentPage == tutorialPages.size() - 1) {
            nextButton.setText("Заканчить");
        } else {
            nextButton.setText("Следующий");
        }
    }

    private void finishTutorial() {
        // Mark tutorial as completed
        SharedPreferences prefs = getSharedPreferences("Настройки математической игры", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("TUTORIAL_COMPLETED", true);
        editor.apply();

        // Close tutorial
        finish();
    }
}
