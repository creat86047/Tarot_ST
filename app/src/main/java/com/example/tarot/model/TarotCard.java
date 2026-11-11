package com.example.tarot.model;

import java.io.Serializable;

public class TarotCard implements Serializable {
    private final int id;
    private final String name;
    private final String englishName;
    private final String arcana; // 大阿爾克那 / 小阿爾克那
    private final String suit;   // 主牌, 權杖, 聖盃, 寶劍, 錢幣
    private final String uprightKeywords;
    private final String reversedKeywords;
    private final String description;
    
    private boolean isReversed; // 抽牌時隨機賦予的正/逆位狀態

    public TarotCard(int id, String name, String englishName, String arcana, String suit, 
                     String uprightKeywords, String reversedKeywords, String description) {
        this.id = id;
        this.name = name;
        this.englishName = englishName;
        this.arcana = arcana;
        this.suit = suit;
        this.uprightKeywords = uprightKeywords;
        this.reversedKeywords = reversedKeywords;
        this.description = description;
        this.isReversed = false;
    }

    public TarotCard copy() {
        TarotCard card = new TarotCard(id, name, englishName, arcana, suit, 
                uprightKeywords, reversedKeywords, description);
        card.setReversed(this.isReversed);
        return card;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEnglishName() {
        return englishName;
    }

    public String getArcana() {
        return arcana;
    }

    public String getSuit() {
        return suit;
    }

    public String getUprightKeywords() {
        return uprightKeywords;
    }

    public String getReversedKeywords() {
        return reversedKeywords;
    }

    public String getDescription() {
        return description;
    }

    public boolean isReversed() {
        return isReversed;
    }

    public void setReversed(boolean reversed) {
        isReversed = reversed;
    }

    public String getOrientationText() {
        return isReversed ? "逆位 (Reversed)" : "正位 (Upright)";
    }

    public String getCurrentKeywords() {
        return isReversed ? reversedKeywords : uprightKeywords;
    }

    public String getDisplayName() {
        return name + " (" + (isReversed ? "逆位" : "正位") + ")";
    }
}
