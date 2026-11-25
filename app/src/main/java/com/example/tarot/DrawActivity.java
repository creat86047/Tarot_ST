package com.example.tarot;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tarot.databinding.ActivityDrawBinding;
import com.example.tarot.model.TarotCard;
import com.example.tarot.viewmodel.ReadingSessionViewModel;
import com.example.tarot.viewmodel.ReportViewModel;

import java.util.ArrayList;

public class DrawActivity extends AppCompatActivity {

    private ActivityDrawBinding binding;
    private ReadingSessionViewModel viewModel;
    private DrawnCardAdapter drawnCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDrawBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(ReadingSessionViewModel.class);
        viewModel.initialize(
                getIntent().getStringExtra(ReadingSessionViewModel.EXTRA_QUESTION),
                getIntent().getStringExtra(ReadingSessionViewModel.EXTRA_SPREAD_NAME),
                getIntent().getStringArrayListExtra(ReadingSessionViewModel.EXTRA_POSITIONS));

        binding.tvDrawQuestion.setText(getString(R.string.draw_question_format,
                viewModel.getQuestion() == null ? "" : viewModel.getQuestion()));
        drawnCardAdapter = new DrawnCardAdapter(
                viewModel.getPositionMeanings(), viewModel.getDrawnCards());
        binding.rvDrawnCards.setLayoutManager(new LinearLayoutManager(this));
        binding.rvDrawnCards.setAdapter(drawnCardAdapter);

        binding.btnDrawCard.setOnClickListener(v -> drawNextCard());
        binding.btnShuffleDeck.setOnClickListener(v -> {
            viewModel.shuffle();
            renderSession();
            Toast.makeText(this, R.string.toast_deck_reshuffled, Toast.LENGTH_SHORT).show();
        });
        binding.btnGoReport.setOnClickListener(v -> openReport());

        renderSession();
    }

    private void drawNextCard() {
        int currentIndex = viewModel.getCurrentDrawIndex();
        if (currentIndex >= viewModel.getPositionMeanings().size()) {
            Toast.makeText(this, R.string.toast_all_cards_drawn, Toast.LENGTH_SHORT).show();
            return;
        }

        TarotCard card = viewModel.drawNextCard();
        if (card == null) {
            Toast.makeText(this, R.string.toast_deck_empty, Toast.LENGTH_SHORT).show();
            renderSession();
            return;
        }

        int nextIndex = viewModel.getCurrentDrawIndex();
        Toast.makeText(this, getString(R.string.toast_card_drawn_format,
                nextIndex, card.getDisplayName()), Toast.LENGTH_SHORT).show();
        renderSession();
        binding.rvDrawnCards.smoothScrollToPosition(nextIndex - 1);
    }

    private void renderSession() {
        drawnCardAdapter.updateData(viewModel.getPositionMeanings(), viewModel.getDrawnCards());
        binding.tvDeckStatus.setText(getString(R.string.deck_remaining_format,
                viewModel.getRemainingCardCount()));

        ArrayList<String> positionMeanings = viewModel.getPositionMeanings();
        int currentIndex = viewModel.getCurrentDrawIndex();
        if (currentIndex < positionMeanings.size() && viewModel.getRemainingCardCount() == 0) {
            binding.tvDrawPrompt.setText(R.string.draw_deck_empty);
            binding.btnDrawCard.setEnabled(false);
            binding.btnGoReport.setEnabled(false);
        } else if (currentIndex < positionMeanings.size()) {
            binding.tvDrawPrompt.setText(getString(R.string.draw_prompt_format,
                    currentIndex + 1, positionMeanings.get(currentIndex)));
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
        if (!viewModel.canOpenReport()) {
            Toast.makeText(this, R.string.toast_need_all_cards, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, ReportActivity.class);
        intent.putExtra(ReportViewModel.EXTRA_READING, viewModel.createReading());
        startActivity(intent);
    }
}
