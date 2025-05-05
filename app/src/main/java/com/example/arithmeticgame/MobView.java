package com.example.arithmeticgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class MobView extends View {

    private Paint bodyPaint;
    private Paint textPaint;
    private Paint healthPaint;
    private Paint healthBgPaint;

    private String problem = "";
    private float health = 1.0f; // 1.0 = full health, 0.0 = dead
    private boolean isHit = false;
    private long hitAnimationStartTime = 0;
    private static final long HIT_ANIMATION_DURATION = 300; // milliseconds

    public MobView(Context context) {
        super(context);
        init();
    }

    public MobView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MobView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        bodyPaint = new Paint();
        bodyPaint.setColor(Color.rgb(76, 175, 80)); // Green
        bodyPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(60);
        textPaint.setTextAlign(Paint.Align.CENTER);

        healthPaint = new Paint();
        healthPaint.setColor(Color.RED);
        healthPaint.setStyle(Paint.Style.FILL);

        healthBgPaint = new Paint();
        healthBgPaint.setColor(Color.LTGRAY);
        healthBgPaint.setStyle(Paint.Style.FILL);
    }

    public void setProblem(String problem) {
        this.problem = problem;
        invalidate();
    }

    public void setHealth(float health) {
        this.health = Math.max(0, Math.min(1, health));
        invalidate();
    }

    public void showHitAnimation() {
        isHit = true;
        hitAnimationStartTime = System.currentTimeMillis();
        invalidate();
    }

    // Add this method to the MobView class
    public float getHealth() {
        return health;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        // Calculate mob body size and position
        int mobSize = Math.min(width, height) / 2;
        int centerX = width / 2;
        int centerY = height / 2;

        // Draw health bar
        int healthBarWidth = width - 40;
        int healthBarHeight = 20;
        int healthBarX = (width - healthBarWidth) / 2;
        int healthBarY = centerY - mobSize - 40;

        RectF healthBarBg = new RectF(healthBarX, healthBarY, healthBarX + healthBarWidth, healthBarY + healthBarHeight);
        canvas.drawRect(healthBarBg, healthBgPaint);

        RectF healthBar = new RectF(healthBarX, healthBarY, healthBarX + (healthBarWidth * health), healthBarY + healthBarHeight);
        canvas.drawRect(healthBar, healthPaint);

        // Handle hit animation
        if (isHit) {
            long elapsedTime = System.currentTimeMillis() - hitAnimationStartTime;
            if (elapsedTime > HIT_ANIMATION_DURATION) {
                isHit = false;
            } else {
                // Flash red during hit animation
                bodyPaint.setColor(Color.RED);
            }
        } else {
            // Normal color
            bodyPaint.setColor(Color.rgb(76, 175, 80));
        }

        // Draw mob body (circle)
        canvas.drawCircle(centerX, centerY, mobSize, bodyPaint);

        // Draw problem text
        canvas.drawText(problem, centerX, centerY + 20, textPaint);
    }
}


