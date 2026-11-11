package com.example.tarot;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tarot.data.TarotDeck;
import com.example.tarot.model.TarotCard;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class DeckBrowseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_browse);

        RecyclerView rvDeckCards = findViewById(R.id.rv_deck_cards);
        MaterialButton btnBackHome = findViewById(R.id.btn_back_home);

        List<TarotCard> allCards = TarotDeck.getAllCards();

        // Create position meanings for library display
        List<String> libraryTitles = new java.util.ArrayList<>(allCards.size());
        for (int i = 0; i < allCards.size(); i++) {
            TarotCard card = allCards.get(i);
            libraryTitles.add(card.getArcana() + " • " + card.getSuit());
        }

        DrawnCardAdapter adapter = new DrawnCardAdapter(libraryTitles, allCards);
        rvDeckCards.setLayoutManager(new LinearLayoutManager(this));
        rvDeckCards.setAdapter(adapter);

        btnBackHome.setOnClickListener(v -> finish());
    }
}
