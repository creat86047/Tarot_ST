package com.example.tarot;

import com.example.tarot.model.TarotCard;
import com.example.tarot.model.TarotReading;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TarotReadingTest {

    @Test
    public void generateAIPrompt_includesQuestionPositionCardAndOrientation() {
        TarotReading reading = new TarotReading("近期工作方向？", "單牌指引",
                Collections.singletonList("核心議題"));
        TarotCard card = new TarotCard(16, "高塔", "The Tower", "大阿爾克那", "主牌",
                "突破", "抗拒變革", "舊結構崩解後迎來重建。", "先承認抗拒，再安排安全的改變步驟。");
        card.setReversed(true);
        reading.addDrawnCard(card);

        String prompt = reading.generateAIPrompt();
        assertTrue(prompt.contains("近期工作方向？"));
        assertTrue(prompt.contains("核心議題"));
        assertTrue(prompt.contains("高塔 (The Tower)"));
        assertTrue(prompt.contains("抗拒變革"));
        assertTrue(prompt.contains("逆位"));
        assertTrue(prompt.contains("先承認抗拒"));
    }

    @Test
    public void generateAIPrompt_handlesMissingQuestionAndPositions() {
        TarotReading reading = new TarotReading(null, null, null);
        String prompt = reading.generateAIPrompt();

        assertTrue(prompt.contains("未指定問題"));
        assertTrue(prompt.contains("未指定"));
        assertFalse(prompt.contains("null"));
    }
}
