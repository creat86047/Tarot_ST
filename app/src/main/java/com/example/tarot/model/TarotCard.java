package com.example.tarot.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TarotCard implements Parcelable {
    private final int id;
    private final String name;
    private final String englishName;
    private final String arcana; // 大阿爾克那 / 小阿爾克那
    private final String suit;   // 主牌, 權杖, 聖盃, 寶劍, 錢幣
    private final String uprightKeywords;
    private final String reversedKeywords;
    private final String description;
    private final String reversedDescription;
    
    private boolean isReversed; // 抽牌時隨機賦予的正/逆位狀態

    private TarotCard(Parcel in) {
        id = in.readInt();
        name = in.readString();
        englishName = in.readString();
        arcana = in.readString();
        suit = in.readString();
        uprightKeywords = in.readString();
        reversedKeywords = in.readString();
        description = in.readString();
        reversedDescription = in.readString();
        isReversed = in.readByte() != 0;
    }

    public TarotCard(int id, String name, String englishName, String arcana, String suit, 
                     String uprightKeywords, String reversedKeywords, String description) {
        this(id, name, englishName, arcana, suit, uprightKeywords, reversedKeywords,
                description, description);
    }

    public TarotCard(int id, String name, String englishName, String arcana, String suit,
                     String uprightKeywords, String reversedKeywords, String description,
                     String reversedDescription) {
        this.id = id;
        this.name = name;
        this.englishName = englishName;
        this.arcana = arcana;
        this.suit = suit;
        this.uprightKeywords = uprightKeywords;
        this.reversedKeywords = reversedKeywords;
        this.description = description;
        this.reversedDescription = reversedDescription;
        this.isReversed = false;
    }

    public TarotCard copy() {
        TarotCard card = new TarotCard(id, name, englishName, arcana, suit,
                uprightKeywords, reversedKeywords, description, reversedDescription);
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

    public String getReversedDescription() {
        return reversedDescription;
    }

    public String getCurrentDescription() {
        return isReversed ? reversedDescription : description;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(englishName);
        dest.writeString(arcana);
        dest.writeString(suit);
        dest.writeString(uprightKeywords);
        dest.writeString(reversedKeywords);
        dest.writeString(description);
        dest.writeString(reversedDescription);
        dest.writeByte((byte) (isReversed ? 1 : 0));
    }

    public static final Creator<TarotCard> CREATOR = new Creator<TarotCard>() {
        @Override
        public TarotCard createFromParcel(Parcel in) {
            return new TarotCard(in);
        }

        @Override
        public TarotCard[] newArray(int size) {
            return new TarotCard[size];
        }
    };
}
