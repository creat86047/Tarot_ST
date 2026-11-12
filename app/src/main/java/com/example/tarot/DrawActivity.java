package com.example.tarot;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tarot.data.TarotDeck;
import com.example.tarot.databinding.ActivityDrawBinding;
import com.example.tarot.model.TarotCard;
import com.example.tarot.model.TarotReading;

import java.util.ArrayList;

public class DrawActivity extends AppCompatActivity {

    private static final String STATE_QUESTION = "draw.question";
    private static final String STATE_SPREAD_NAME = "draw.spreadName";
    private static final String STATE_POSITIONS = "draw.positions";
    private static final String STATE_REMAINING_DECK = "draw.remainingDeck";
    private static final String STATE_DRAWN_CARDS = "draw.drawnCards";
    private static final String STATE_DRAW_INDEX = "draw.drawIndex";

    private ActivityDrawBinding binding;
    private DrawnCardAdapter drawnCardAdapter;

    private String question;
    private String spreadName;
    private ArrayList<String> positionMeanings;
    private ArrayList<TarotCard> fullShuffledDeck;
    private ArrayList<TarotCard> drawnCards;
    private int currentDrawIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDrawBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        restoreReadingSetup(savedInstanceState);
        restoreDrawProgress(savedInstanceState);

        binding.tvDrawQuestion.setText(getString(R.string.draw_question_format,
                question == null ? "" : question));
        drawnCardAdapter = new DrawnCardAdapter(positionMeanings, drawnCards);
        binding.rvDrawnCards.setLayoutManager(new LinearLayoutManager(this));
        binding.rvDrawnCards.setAdapter(drawnCardAdapter);

        binding.btnDrawCard.setOnClickListener(v -> drawNextCard());
        binding.btnShuffleDeck.setOnClickListener(v -> {
            shuffleAndResetDeck();
            Toast.makeText(this, R.string.toast_deck_reshuffled, Toast.LENGTH_SHORT).show();
        });
        binding.btnGoReport.setOnClickListener(v -> openReport());

        updateUIStatus();
    }

    private void restoreReadingSetup(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            question = savedInstanceState.getString(STATE_QUESTION);
            spreadName = savedInstanceState.getString(STATE_SPREAD_NAME);
            positionMeanings = savedInstanceState.getStringArrayList(STATE_POSITIONS);
        } else {
            question = getIntent().getStringExtra("EXTRA_QUESTION");
            spreadName = getIntent().getStringExtra("EXTRA_SPREAD_NAME");
            positionMeanings = getIntent().getStringArrayListExtra("EXTRA_POSITIONS");
        }

        if (positionMeanings == null || positionMeanings.isEmpty()) {
            positionMeanings = new ArrayList<>();
            positionMeanings.add(getString(R.string.default_position_meaning));
        }
    }

    private void restoreDrawProgress(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            shuffleAndResetDeck();
            return;
        }

        fullShuffledDeck = getParcelableCardList(savedInstanceState, STATE_REMAINING_DECK);
        drawnCards = getParcelableCardList(savedInstanceState, STATE_DRAWN_CARDS);
        if (fullShuffledDeck == null || drawnCards == null) {
            shuffleAndResetDeck();
            return;
        }

        currentDrawIndex = Math.min(savedInstanceState.getInt(STATE_DRAW_INDEX, drawnCards.size()),
                positionMeanings.size());
        currentDrawIndex = Math.min(currentDrawIndex, drawnCards.size());
    }

    @SuppressWarnings("deprecation")
    private ArrayList<TarotCard> getParcelableCardList(Bundle state, String key) {
        return state.getParcelableArrayList(key);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_QUESTION, question);
        outState.putString(STATE_SPREAD_NAME, spreadName);
        outState.putStringArrayList(STATE_POSITIONS, positionMeanings);
        outState.putParcelableArrayList(STATE_REMAINING_DECK, fullShuffledDeck);
        outState.putParcelableArrayList(STATE_DRAWN_CARDS, drawnCards);
        outState.putInt(STATE_DRAW_INDEX, currentDrawIndex);
    }

    private void shuffleAndResetDeck() {
        fullShuffledDeck = new ArrayList<>(TarotDeck.createShuffledDeck());
        drawnCards = new ArrayList<>();
        currentDrawIndex = 0;
        updateCardList();
        updateUIStatus();
    }

    private void drawNextCard() {
        if (currentDrawIndex >= positionMeanings.size()) {
            Toast.makeText(this, R.string.toast_all_cards_drawn, Toast.LENGTH_SHORT).show();
            return;
        }

        if (fullShuffledDeck.isEmpty()) {
            Toast.makeText(this, R.string.toast_deck_empty, Toast.LENGTH_SHORT).show();
            updateUIStatus();
            return;
        }

        TarotCard card = fullShuffledDeck.remove(0);
        drawnCards.add(card);
        currentDrawIndex++;
        Toast.makeText(this, getString(R.string.toast_card_drawn_format,
                currentDrawIndex, card.getDisplayName()), Toast.LENGTH_SHORT).show();

        updateCardList();
        binding.rvDrawnCards.smoothScrollToPosition(currentDrawIndex - 1);
        updateUIStatus();
    }

    private void updateCardList() {
        if (drawnCardAdapter != null) {
            drawnCardAdapter.updateData(positionMeanings, drawnCards);
        }
    }

    private void updateUIStatus() {
        if (fullShuffledDeck == null) {
            return;
        }

        binding.tvDeckStatus.setText(getString(R.string.deck_remaining_format,
                fullShuffledDeck.size()));
        if (currentDrawIndex < positionMeanings.size() && fullShuffledDeck.isEmpty()) {
            binding.tvDrawPrompt.setText(R.string.draw_deck_empty);
            binding.btnDrawCard.setEnabled(false);
            binding.btnGoReport.setEnabled(false);
        } else if (currentDrawIndex < positionMeanings.size()) {
            binding.tvDrawPrompt.setText(getString(R.string.draw_prompt_format,
                    currentDrawIndex + 1, positionMeanings.get(currentDrawIndex)));
            binding.btnDrawCard.setEnabled(true);
            binding.btnGoReport.setEnabled(false);
        } else {
            binding.tvDrawPrompt.setText(getString(R.string.draw_complete_format,
                    positionMeanings.size()));
            binding.btnDrawCard.setEnabled(false);
            binding.btnGoReport.setEnabled(true);
        }
    }

    private void openReport() {
        if (currentDrawIndex < positionMeanings.size()) {
            Toast.makeText(this, R.string.toast_need_all_cards, Toast.LENGTH_SHORT).show();
            return;
        }

        TarotReading reading = new TarotReading(question, spreadName, positionMeanings);
        reading.setDrawnCards(new ArrayList<>(drawnCards));
        Intent intent = new Intent(this, ReportActivity.class);
        intent.putExtra(ReportActivity.EXTRA_READING, reading);
        startActivity(intent);
    }
}
