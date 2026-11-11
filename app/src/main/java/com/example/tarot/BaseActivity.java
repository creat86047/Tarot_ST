package com.example.tarot;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * 統一全系統所有頁面的 BaseActivity，配置 Edge-To-Edge 系統島/瀏海避讓與恆定頂部導覽列
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
    }

    /**
     * 為所有頁面的根佈局配置頂部 Safe Area 邊界，徹底解決系統島與瀏海遮擋文字標題的問題
     */
    protected void setupEdgeToEdgePadding(View rootView) {
        if (rootView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                int extraTopPadding = (int) (12 * getResources().getDisplayMetrics().density); // 額外 Safe Area
                v.setPadding(
                        systemBars.left,
                        systemBars.top + extraTopPadding,
                        systemBars.right,
                        systemBars.bottom
                );
                return insets;
            });
        }
    }

    /**
     * 初始化全系統恆定的頂部導覽列 (包含：左上角返回鍵, 右上角金色 ? 使用說明鍵, 右上角金色記事本鍵)
     */
    protected void setupTopHeaderBar() {
        ImageButton btnBack = findViewById(R.id.btn_header_back);
        ImageButton btnHelp = findViewById(R.id.btn_header_help);
        ImageButton btnHistory = findViewById(R.id.btn_header_history);

        if (btnBack != null) {
            if (this instanceof MainActivity) {
                // 首頁時返回鍵隱藏，保持介面簡潔
                btnBack.setVisibility(View.INVISIBLE);
            } else {
                btnBack.setVisibility(View.VISIBLE);
                btnBack.setOnClickListener(v -> finish());
            }
        }

        if (btnHelp != null) {
            btnHelp.setOnClickListener(v -> showAboutDialog());
        }

        if (btnHistory != null) {
            if (this instanceof HistoryActivity) {
                btnHistory.setVisibility(View.GONE);
            } else {
                btnHistory.setVisibility(View.VISIBLE);
                btnHistory.setOnClickListener(v -> {
                    Intent intent = new Intent(this, HistoryActivity.class);
                    startActivity(intent);
                });
            }
        }
    }

    public void showAboutDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this, R.style.Theme_Tarot_Dialog)
                .setTitle("✨ 塔羅高塔 — 系統特色說明")
                .setMessage("【系統核心功能與設計原則】：\n\n" +
                        "1. 78張完整塔羅牌：包含 22 張大阿爾克那與 56 張小阿爾克那。\n\n" +
                        "2. 隨機正逆位與動態洗牌：每次抽牌前皆進行亂序混合，正逆位機率各為 50%。\n\n" +
                        "3. 卡牌抽畢鎖定機制：每一張牌被抽出後即刻【鎖定】，不可人為更換，保持占卜當下的直覺與專一性。\n\n" +
                        "4. 自訂第1張~最後一張位置意涵：預設多款經典牌陣（單牌、時間線、四要素、高塔突破陣），並支援完全自訂各位置定義。\n\n" +
                        "5. AI 解牌 Prompt 匯出：抽牌完成後自動生成高品質 Prompt，可一鍵複製給 ChatGPT、Claude 或 DeepSeek 進行多層次深度解牌與高塔警訊剖析。\n\n" +
                        "6. 輕量離線，隱私安全：不需預先儲存遊戲或歷史資料庫，退遊戲即清理，專注於當下占卜與文字報告產出。")
                .setPositiveButton("我知道了", (d, which) -> d.dismiss())
                .create();

        dialog.show();

        // 確保彈窗背景為深紫色，文字為白色與金黃色，與整體神秘暗色系 UI 保持統一
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(
                    ContextCompat.getColor(this, R.color.tarot_surface)));
        }

        TextView messageView = dialog.findViewById(android.R.id.message);
        if (messageView != null) {
            messageView.setTextColor(ContextCompat.getColor(this, R.color.tarot_text_primary));
            messageView.setTextSize(14f);
            messageView.setLineSpacing(6f, 1.15f);
        }

        TextView titleView = dialog.findViewById(android.R.id.title);
        if (titleView != null) {
            titleView.setTextColor(ContextCompat.getColor(this, R.color.tarot_gold_accent));
            titleView.setTextSize(18f);
        }

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        if (positiveButton != null) {
            positiveButton.setTextColor(ContextCompat.getColor(this, R.color.tarot_gold_accent));
            positiveButton.setTextSize(15f);
        }
    }
}
