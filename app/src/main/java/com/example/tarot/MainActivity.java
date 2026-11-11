package com.example.tarot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends BaseActivity {

    private static final String PREFS_NAME = "TarotPrefs";
    private static final String KEY_FIRST_LAUNCH = "is_first_launch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupEdgeToEdgePadding(findViewById(R.id.main));
        setupTopHeaderBar();

        MaterialButton btnStartReading = findViewById(R.id.btn_start_reading);
        MaterialButton btnBrowseDeck = findViewById(R.id.btn_browse_deck);

        btnStartReading.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SetupActivity.class);
            startActivity(intent);
        });

        btnBrowseDeck.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DeckBrowseActivity.class);
            startActivity(intent);
        });

        // 檢查是否為首次使用，如果是則自動彈窗顯示系統特色與說明
        checkFirstLaunch();
    }

    private void checkFirstLaunch() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isFirstLaunch = prefs.getBoolean(KEY_FIRST_LAUNCH, true);
        if (isFirstLaunch) {
            showAboutDialog();
            prefs.edit().putBoolean(KEY_FIRST_LAUNCH, false).apply();
        }
    }
}
