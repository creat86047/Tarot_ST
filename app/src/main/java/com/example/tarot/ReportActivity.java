package com.example.tarot;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tarot.model.TarotCard;
import com.example.tarot.model.TarotReading;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class ReportActivity extends AppCompatActivity {

    private TextView tvReportQuestion;
    private TextView tvReportStats;
    private RecyclerView rvReportCards;
    private EditText etAiPrompt;

    private TarotReading reading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        reading = (TarotReading) getIntent().getSerializableExtra("EXTRA_READING");

        tvReportQuestion = findViewById(R.id.tv_report_question);
        tvReportStats = findViewById(R.id.tv_report_stats);
        rvReportCards = findViewById(R.id.rv_report_cards);
        etAiPrompt = findViewById(R.id.et_ai_prompt);

        MaterialButton btnCopyPrompt = findViewById(R.id.btn_copy_prompt);
        MaterialButton btnShare = findViewById(R.id.btn_share);
        MaterialButton btnNewReading = findViewById(R.id.btn_new_reading);

        if (reading != null) {
            tvReportQuestion.setText("占卜問題：" + reading.getQuestion());

            // Compute statistics
            List<TarotCard> cards = reading.getDrawnCards();
            int upright = 0, reversed = 0, major = 0, minor = 0;
            if (cards != null) {
                for (TarotCard c : cards) {
                    if (c.isReversed()) reversed++; else upright++;
                    if ("大阿爾克那".equals(c.getArcana())) major++; else minor++;
                }
            }

            String stats = "牌陣：" + reading.getSpreadName() +
                    " | 統計：正位 " + upright + " 張 / 逆位 " + reversed + " 張" +
                    " | 大牌 " + major + " 張 / 小牌 " + minor + " 張";
            tvReportStats.setText(stats);

            // RecyclerView Setup
            DrawnCardAdapter adapter = new DrawnCardAdapter(reading.getPositionMeanings(), cards);
            rvReportCards.setLayoutManager(new LinearLayoutManager(this));
            rvReportCards.setAdapter(adapter);

            // Generate AI Prompt text
            String promptText = reading.generateAIPrompt();
            etAiPrompt.setText(promptText);
        }

        // Button Listeners
        btnCopyPrompt.setOnClickListener(v -> copyPromptToClipboard());

        btnShare.setOnClickListener(v -> shareReportText());

        btnNewReading.setOnClickListener(v -> {
            Intent intent = new Intent(ReportActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void copyPromptToClipboard() {
        String prompt = etAiPrompt.getText().toString();
        if (prompt.isEmpty()) {
            Toast.makeText(this, "無 Prompt 內容可複製", Toast.LENGTH_SHORT).show();
            return;
        }

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Tarot AI Prompt", prompt);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "已成功複製 AI 解牌 Prompt！可直接貼給 ChatGPT / Claude 解牌", Toast.LENGTH_LONG).show();
        }
    }

    private void shareReportText() {
        String prompt = etAiPrompt.getText().toString();
        if (prompt.isEmpty()) {
            Toast.makeText(this, "無報告內容可分享", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "塔羅高塔玩家報告 - " + (reading != null ? reading.getQuestion() : "占卜結果"));
        shareIntent.putExtra(Intent.EXTRA_TEXT, prompt);
        startActivity(Intent.createChooser(shareIntent, "分享塔羅玩家報告"));
    }
}
