package com.example.tarot;

import android.os.Parcel;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.tarot.model.TarotCard;
import com.example.tarot.model.TarotReading;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class TarotReadingParcelableTest {

    @Test
    public void readingAndCardSurviveParcelRoundTrip() {
        TarotReading original = new TarotReading("工作方向", "單牌指引",
                Collections.singletonList("核心議題"));
        TarotCard card = new TarotCard(16, "高塔", "The Tower", "大阿爾克那", "主牌",
                "突破", "抗拒變革", "舊結構崩解後迎來重建。");
        card.setReversed(true);
        original.addDrawnCard(card);

        Parcel parcel = Parcel.obtain();
        try {
            original.writeToParcel(parcel, 0);
            parcel.setDataPosition(0);
            TarotReading restored = TarotReading.CREATOR.createFromParcel(parcel);

            assertEquals(original.getQuestion(), restored.getQuestion());
            assertEquals(original.getSpreadName(), restored.getSpreadName());
            assertEquals(original.getPositionMeanings(), restored.getPositionMeanings());
            assertEquals(1, restored.getDrawnCards().size());
            assertEquals(card.getId(), restored.getDrawnCards().get(0).getId());
            assertTrue(restored.getDrawnCards().get(0).isReversed());
            assertEquals(card.getReversedDescription(),
                    restored.getDrawnCards().get(0).getReversedDescription());
        } finally {
            parcel.recycle();
        }
    }
}
