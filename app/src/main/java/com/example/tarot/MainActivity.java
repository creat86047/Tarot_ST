package com.example.tarot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        MaterialButton btnStartReading = findViewById(R.id.btn_start_reading);
        MaterialButton btnBrowseDeck = findViewById(R.id.btn_browse_deck);
        MaterialButton btnAbout = findViewById(R.id.btn_about);

        btnStartReading.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SetupActivity.class);
            startActivity(intent);
        });

        btnBrowseDeck.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DeckBrowseActivity.class);
            startActivity(intent);
        });

        btnAbout.setOnClickListener(v -> showAboutDialog());
    }

    private void showAboutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("塔羅高塔玩家報告 — 使用說明")
                .setMessage("【系統核心功能與設計原則】：\n\n" +
                        "1. 78張完整塔羅牌：包含 22 張大阿爾克那與 56 張小阿爾克那。\n\n" +
                        "2. 隨機正逆位與動態洗牌：每次抽牌前皆進行亂序混合，正逆位機率各為 50%。\n\n" +
                        "3. 卡牌抽畢鎖定機制：每一張牌被抽出後即刻【鎖定】，不可人為更換，保持占卜當下的直覺與專一性。\n\n" +
                        "4. 自訂第1張~最後一張位置意涵：預設多款經典牌陣（單牌、時間線、四要素、高塔突破陣），並支援完全自訂各位置定義。\n\n" +
                        "5. AI 解牌 Prompt 匯出：抽牌完成後自動生成高品質 Prompt，可一鍵複製給 ChatGPT、Claude 或 DeepSeek 進行多層次深度解牌與高塔警訊剖析。\n\n" +
                        "6. 輕量離線，隱私安全：不需預先儲存遊戲或歷史資料庫，退遊戲即清理，專注於當下占卜與文字報告產出。")
                .setPositiveButton("瞭解", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
