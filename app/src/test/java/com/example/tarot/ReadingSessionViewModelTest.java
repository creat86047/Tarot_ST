package com.example.tarot;

import androidx.lifecycle.SavedStateHandle;

import com.example.tarot.model.TarotCard;
import com.example.tarot.viewmodel.ReadingSessionViewModel;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ReadingSessionViewModelTest {

    @Test
    public void drawProgressIsRetainedAndDoesNotRedrawCards() {
        SavedStateHandle handle = new SavedStateHandle();
        ReadingSessionViewModel first = new ReadingSessionViewModel(handle);
        first.initialize("工作方向", "三牌時間線", Arrays.asList("過去", "現在", "未來"));

        TarotCard firstCard = first.drawNextCard();
        assertNotNull(firstCard);
        assertEquals(1, first.getCurrentDrawIndex());
        assertEquals(77, first.getRemainingCardCount());

        ReadingSessionViewModel restored = new ReadingSessionViewModel(handle);
        restored.initialize("不要覆蓋已保存狀態", "其他牌陣", Arrays.asList("新位置"));

        assertEquals("工作方向", restored.getQuestion());
        assertEquals(Arrays.asList("過去", "現在", "未來"), restored.getPositionMeanings());
        assertEquals(1, restored.getCurrentDrawIndex());
        assertEquals(firstCard.getId(), restored.getDrawnCards().get(0).getId());

        Set<Integer> cardIds = new HashSet<>();
        for (TarotCard card : restored.getDrawnCards()) {
            cardIds.add(card.getId());
        }
        while (!restored.canOpenReport()) {
            TarotCard nextCard = restored.drawNextCard();
            assertNotNull(nextCard);
            cardIds.add(nextCard.getId());
        }

        assertTrue(restored.canOpenReport());
        assertEquals(3, cardIds.size());
        assertNull(restored.drawNextCard());
        assertEquals(75, restored.getRemainingCardCount());
    }

    @Test
    public void shuffleClearsDrawnCardsAndRestoresFullDeck() {
        ReadingSessionViewModel viewModel = new ReadingSessionViewModel(new SavedStateHandle());
        viewModel.initialize("問題", "單牌", Arrays.asList("指引"));
        viewModel.drawNextCard();

        viewModel.shuffle();

        assertTrue(viewModel.getDrawnCards().isEmpty());
        assertEquals(0, viewModel.getCurrentDrawIndex());
        assertEquals(78, viewModel.getRemainingCardCount());
    }
}
