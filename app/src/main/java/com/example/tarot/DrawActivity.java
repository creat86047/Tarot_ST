package com.example.tarot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tarot.data.TarotDeck;
import com.example.tarot.model.TarotCard;
import com.example.tarot.model.TarotReading;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class DrawActivity extends AppCompatActivity {

    private TextView tvDrawQuestion;
    private TextView tvDrawPrompt;
    private TextView tvDeckStatus;
    private MaterialButton btnDrawCard;
    private MaterialButton btnShuffleDeck;
    private MaterialButton btnGoReport;
    private RecyclerView rvDrawnCards;

    private DrawnCardAdapter drawnCardAdapter;

    private String question;
    private String spreadName;
    private List<String> positionMeanings;

    private List<TarotCard> fullShuffledDeck;
    private List<TarotCard> drawnCards;
    private int currentDrawIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        // Get Intent Extras
        question = getIntent().getStringExtra("EXTRA_QUESTION");
        spreadName = getIntent().getStringExtra("EXTRA_SPREAD_NAME");
        positionMeanings = getIntent().getStringArrayListExtra("EXTRA_POSITIONS");

        if (positionMeanings == null || positionMeanings.isEmpty()) {
            positionMeanings = new ArrayList<>();
            positionMeanings.add("當前指引");
        }

        drawnCards = new ArrayList<>();

        // Views Initialization
        tvDrawQuestion = findViewById(R.id.tv_draw_question);
        tvDrawPrompt = findViewById(R.id.tv_draw_prompt);
        tvDeckStatus = findViewById(R.id.tv_deck_status);
        btnDrawCard = findViewById(R.id.btn_draw_card);
        btnShuffleDeck = findViewById(R.id.btn_shuffle_deck);
        btnGoReport = findViewById(R.id.btn_go_report);
        rvDrawnCards = findViewById(R.id.rv_drawn_cards);

        tvDrawQuestion.setText("問題：" + question);

        // RecyclerView Setup
        drawnCardAdapter = new DrawnCardAdapter(positionMeanings, drawnCards);
        rvDrawnCards.setLayoutManager(new LinearLayoutManager(this));
        rvDrawnCards.setAdapter(drawnCardAdapter);

        // Initialize Shuffled 78 Deck
        shuffleAndResetDeck();

        // Button Listeners
        btnDrawCard.setOnClickListener(v -> drawNextCard());

        btnShuffleDeck.setOnClickListener(v -> {
            shuffleAndResetDeck();
            Toast.makeText(this, "已重新打亂 78 張牌庫與重置抽牌狀態！", Toast.LENGTH_SHORT).show();
        });

        btnGoReport.setOnClickListener(v -> {
            if (drawnCards.size() < positionMeanings.size()) {
                Toast.makeText(this, "請先抽完所有位置的卡牌！", Toast.LENGTH_SHORT).show();
                return;
            }

            TarotReading reading = new TarotReading(question, spreadName, positionMeanings);
            reading.setDrawnCards(new ArrayList<>(drawnCards));

            Intent intent = new Intent(DrawActivity.this, ReportActivity.class);
            intent.putExtra("EXTRA_READING", reading);
            startActivity(intent);
        });
    }

    private void shuffleAndResetDeck() {
        fullShuffledDeck = TarotDeck.createShuffledDeck(); // 78張牌亂序 + 隨機正逆位
        drawnCards.clear();
        currentDrawIndex = 0;

        btnDrawCard.setEnabled(true);
        btnGoReport.setEnabled(false);

        updateUIStatus();
        drawnCardAdapter.updateData(positionMeanings, drawnCards);
    }

    private void drawNextCard() {
        if (currentDrawIndex >= positionMeanings.size()) {
            Toast.makeText(this, "所有卡牌已抽完且已鎖定！", Toast.LENGTH_SHORT).show();
            return;
        }

        if (fullShuffledDeck == null || fullShuffledDeck.isEmpty()) {
            fullShuffledDeck = TarotDeck.createShuffledDeck();
        }

        // Draw top card from shuffled deck
        TarotCard card = fullShuffledDeck.remove(0);
        drawnCards.add(card);

        Toast.makeText(this, "第 " + (currentDrawIndex + 1) + " 張牌已抽出並【鎖定】：\n" + card.getDisplayName(), Toast.LENGTH_SHORT).show();

        currentDrawIndex++;

        drawnCardAdapter.updateData(positionMeanings, drawnCards);
        rvDrawnCards.smoothScrollToPosition(currentDrawIndex - 1);

        updateUIStatus();
    }

    private void updateUIStatus() {
        tvDeckStatus.setText("牌庫剩餘：" + fullShuffledDeck.size() + " 張 / 78 張（正逆位已隨機打亂）");

        if (currentDrawIndex < positionMeanings.size()) {
            String currentMeaning = positionMeanings.get(currentDrawIndex);
            tvDrawPrompt.setText("請點擊下方「抽取卡牌」，抽取 第 " + (currentDrawIndex + 1) + " 張牌\n【" + currentMeaning + "】");
            btnDrawCard.setEnabled(true);
            btnGoReport.setEnabled(false);
        } else {
            tvDrawPrompt.setText("🎉 全牌陣 " + positionMeanings.size() + " 張卡牌已全數抽完並即刻鎖定！");
            btnDrawCard.setEnabled(false);
            btnGoReport.setEnabled(true);
        }
    }
}
