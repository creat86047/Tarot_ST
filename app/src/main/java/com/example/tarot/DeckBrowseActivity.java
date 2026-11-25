package com.example.tarot;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tarot.data.TarotDeck;
import com.example.tarot.databinding.ActivityDeckBrowseBinding;

public class DeckBrowseActivity extends AppCompatActivity {

    private ActivityDeckBrowseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeckBrowseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.rvDeckCards.setLayoutManager(new LinearLayoutManager(this));
        binding.rvDeckCards.setAdapter(new DeckCardAdapter(TarotDeck.getAllCards()));
        binding.btnBackHome.setOnClickListener(v -> finish());
    }
}
