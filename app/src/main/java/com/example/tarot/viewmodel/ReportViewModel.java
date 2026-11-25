package com.example.tarot.viewmodel;

import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.tarot.model.TarotCard;
import com.example.tarot.model.TarotReading;

import java.util.Collections;
import java.util.List;

/** Builds report data independently from the report screen's rendering and share actions. */
public class ReportViewModel extends ViewModel {

    public static final String EXTRA_READING = "EXTRA_READING";
    private static final String STATE_READING = "report.reading";

    private final SavedStateHandle state;

    public ReportViewModel(SavedStateHandle state) {
        this.state = state;
    }

    /** Preserve an existing restored report; otherwise seed the ViewModel from the Activity extra. */
    public void initialize(TarotReading intentReading) {
        if (state.get(STATE_READING) == null && intentReading != null) {
            state.set(STATE_READING, intentReading);
        }
    }

    public TarotReading getReading() {
        return state.get(STATE_READING);
    }

    public String generatePrompt() {
        TarotReading reading = getReading();
        return reading == null ? null : reading.generateAIPrompt();
    }

    public Summary getSummary() {
        TarotReading reading = getReading();
        if (reading == null) {
            return new Summary(0, 0, 0, 0);
        }

        List<TarotCard> cards = reading.getDrawnCards() == null
                ? Collections.emptyList() : reading.getDrawnCards();
        int upright = 0;
        int reversed = 0;
        int major = 0;
        int minor = 0;
        for (TarotCard card : cards) {
            if (card == null) {
                continue;
            }
            if (card.isReversed()) reversed++; else upright++;
            if ("大阿爾克那".equals(card.getArcana())) major++; else minor++;
        }
        return new Summary(upright, reversed, major, minor);
    }

    public static final class Summary {
        private final int uprightCount;
        private final int reversedCount;
        private final int majorCount;
        private final int minorCount;

        Summary(int uprightCount, int reversedCount, int majorCount, int minorCount) {
            this.uprightCount = uprightCount;
            this.reversedCount = reversedCount;
            this.majorCount = majorCount;
            this.minorCount = minorCount;
        }

        public int getUprightCount() {
            return uprightCount;
        }

        public int getReversedCount() {
            return reversedCount;
        }

        public int getMajorCount() {
            return majorCount;
        }

        public int getMinorCount() {
            return minorCount;
        }
    }
}
