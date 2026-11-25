package com.example.tarot.viewmodel;

import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.tarot.data.TarotDeck;
import com.example.tarot.model.TarotCard;
import com.example.tarot.model.TarotReading;

import java.util.ArrayList;
import java.util.List;

/** Owns the in-progress reading and persists its small UI state through process recreation. */
public class ReadingSessionViewModel extends ViewModel {

    public static final String EXTRA_QUESTION = "EXTRA_QUESTION";
    public static final String EXTRA_SPREAD_NAME = "EXTRA_SPREAD_NAME";
    public static final String EXTRA_POSITIONS = "EXTRA_POSITIONS";

    private static final String STATE_INITIALIZED = "reading.initialized";
    private static final String STATE_QUESTION = "reading.question";
    private static final String STATE_SPREAD_NAME = "reading.spreadName";
    private static final String STATE_POSITIONS = "reading.positions";
    private static final String STATE_REMAINING_CARDS = "reading.remainingCards";
    private static final String STATE_DRAWN_CARDS = "reading.drawnCards";

    private final SavedStateHandle state;

    public ReadingSessionViewModel(SavedStateHandle state) {
        this.state = state;
    }

    /** Initialize once from the Activity's launch extras; restored state always takes precedence. */
    public void initialize(String question, String spreadName, List<String> positionMeanings) {
        if (Boolean.TRUE.equals(state.get(STATE_INITIALIZED))
                && getSavedPositions() != null
                && getSavedCards(STATE_REMAINING_CARDS) != null
                && getSavedCards(STATE_DRAWN_CARDS) != null) {
            return;
        }

        ArrayList<String> safePositions = new ArrayList<>();
        if (positionMeanings != null) {
            safePositions.addAll(positionMeanings.subList(0,
                    Math.min(positionMeanings.size(), TarotDeck.CARD_COUNT)));
        }
        if (safePositions.isEmpty()) {
            safePositions.add("當前指引");
        }

        state.set(STATE_QUESTION, question);
        state.set(STATE_SPREAD_NAME, spreadName);
        state.set(STATE_POSITIONS, safePositions);
        state.set(STATE_INITIALIZED, true);
        shuffle();
    }

    public String getQuestion() {
        return state.get(STATE_QUESTION);
    }

    public String getSpreadName() {
        return state.get(STATE_SPREAD_NAME);
    }

    public ArrayList<String> getPositionMeanings() {
        ArrayList<String> positions = getSavedPositions();
        return positions == null ? new ArrayList<>() : new ArrayList<>(positions);
    }

    public ArrayList<TarotCard> getDrawnCards() {
        ArrayList<TarotCard> cards = getSavedCards(STATE_DRAWN_CARDS);
        return cards == null ? new ArrayList<>() : new ArrayList<>(cards);
    }

    public int getCurrentDrawIndex() {
        return getDrawnCards().size();
    }

    public int getRemainingCardCount() {
        ArrayList<TarotCard> cards = getSavedCards(STATE_REMAINING_CARDS);
        return cards == null ? 0 : cards.size();
    }

    public boolean canOpenReport() {
        ArrayList<String> positions = getSavedPositions();
        return positions != null && !positions.isEmpty()
                && getCurrentDrawIndex() == positions.size();
    }

    /** Draws and locks the next card, or returns null when the spread/deck is exhausted. */
    public TarotCard drawNextCard() {
        ArrayList<String> positions = getSavedPositions();
        ArrayList<TarotCard> remainingCards = getSavedCards(STATE_REMAINING_CARDS);
        ArrayList<TarotCard> drawnCards = getSavedCards(STATE_DRAWN_CARDS);
        if (positions == null || remainingCards == null || drawnCards == null
                || drawnCards.size() >= positions.size() || remainingCards.isEmpty()) {
            return null;
        }

        TarotCard card = remainingCards.remove(0);
        drawnCards.add(card);
        state.set(STATE_REMAINING_CARDS, remainingCards);
        state.set(STATE_DRAWN_CARDS, drawnCards);
        return card;
    }

    public void shuffle() {
        state.set(STATE_REMAINING_CARDS, new ArrayList<>(TarotDeck.createShuffledDeck()));
        state.set(STATE_DRAWN_CARDS, new ArrayList<TarotCard>());
    }

    public TarotReading createReading() {
        TarotReading reading = new TarotReading(getQuestion(), getSpreadName(), getPositionMeanings());
        reading.setDrawnCards(getDrawnCards());
        return reading;
    }

    private ArrayList<String> getSavedPositions() {
        return state.get(STATE_POSITIONS);
    }

    private ArrayList<TarotCard> getSavedCards(String key) {
        return state.get(key);
    }
}
