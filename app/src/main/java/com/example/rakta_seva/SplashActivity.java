package com.example.rakta_seva;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rakta_seva.data.DatabaseSeeder;
import com.example.rakta_seva.utils.SessionManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Seed database on first launch
        DatabaseSeeder.seedIfNeeded(this);

        // Animate logo
        ImageView logo = findViewById(R.id.splash_logo);
        ScaleAnimation scaleAnim = new ScaleAnimation(
                0.5f, 1.0f, 0.5f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnim.setDuration(800);
        scaleAnim.setFillAfter(true);

        AlphaAnimation fadeIn = new AlphaAnimation(0f, 1f);
        fadeIn.setDuration(1000);
        fadeIn.setFillAfter(true);

        logo.startAnimation(scaleAnim);

        // Navigate after 2.5 seconds
        new Handler().postDelayed(() -> {
            SessionManager sessionManager = new SessionManager(SplashActivity.this);
            Intent intent;
            if (sessionManager.isLoggedIn()) {
                intent = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, LandingActivity.class);
            }
            startActivity(intent);
            finish();
        }, 2500);
    }
}
