package com.example.tarot;

import com.example.tarot.data.TarotDeck;
import com.example.tarot.model.TarotCard;

import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TarotDeckTest {

    @Test
    public void createShuffledDeck_returnsAllCardsAsIndependentCopies() {
        List<TarotCard> originalCards = TarotDeck.getAllCards();
        List<TarotCard> shuffledCards = TarotDeck.createShuffledDeck();
        Set<Integer> cardIds = new HashSet<>();

        assertEquals(78, originalCards.size());
        assertEquals(78, shuffledCards.size());
        for (TarotCard card : shuffledCards) {
            assertNotNull(card);
            cardIds.add(card.getId());
            TarotCard original = originalCards.get(card.getId());
            assertNotSame(original, card);
            assertFalse("cached definition cards must not be changed", original.isReversed());
            assertNotNull(card.getCurrentKeywords());
        }

        assertEquals("the shuffled deck contains every card exactly once", 78, cardIds.size());
    }

    @Test
    public void minorArcanaHaveCardSpecificMeanings() {
        List<TarotCard> cards = TarotDeck.getAllCards();
        Set<String> descriptions = new HashSet<>();
        Set<String> reversedDescriptions = new HashSet<>();
        for (int i = 22; i < cards.size(); i++) {
            TarotCard card = cards.get(i);
            assertFalse(card.getUprightKeywords().isEmpty());
            assertFalse(card.getReversedKeywords().isEmpty());
            assertFalse(card.getDescription().contains("能量顯化與指引"));
            assertFalse(card.getReversedDescription().isEmpty());
            descriptions.add(card.getDescription());
            reversedDescriptions.add(card.getReversedDescription());
        }

        assertEquals("each minor-arcana card has a distinct description", 56, descriptions.size());
        assertEquals("each minor-arcana card has a distinct reversed description", 56,
                reversedDescriptions.size());
        assertTrue(cards.get(22).getDescription().contains("新火花"));
        assertTrue(cards.get(50).getDescription().contains("事實"));
        assertTrue(cards.get(64).getDescription().contains("工作"));
    }
}
