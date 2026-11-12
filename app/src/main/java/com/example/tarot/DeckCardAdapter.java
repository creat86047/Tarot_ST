package com.example.tarot;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tarot.databinding.ItemDeckCardBinding;
import com.example.tarot.model.TarotCard;

import java.util.ArrayList;
import java.util.List;

/** Dedicated card-library presentation; it does not treat cards as drawn positions. */
public class DeckCardAdapter extends RecyclerView.Adapter<DeckCardAdapter.ViewHolder> {

    private final List<TarotCard> cards;

    public DeckCardAdapter(List<TarotCard> cards) {
        this.cards = cards == null ? new ArrayList<>() : new ArrayList<>(cards);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDeckCardBinding binding = ItemDeckCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TarotCard card = cards.get(position);
        holder.binding.tvDeckCardName.setText(card.getName() + " (" + card.getEnglishName() + ")");
        holder.binding.tvDeckCardCategory.setText(holder.binding.getRoot().getContext().getString(
                R.string.deck_card_category_format, card.getArcana(), card.getSuit()));
        holder.binding.tvDeckCardKeywords.setText(holder.binding.getRoot().getContext().getString(
                R.string.deck_card_keywords_format, card.getUprightKeywords(), card.getReversedKeywords()));
        holder.binding.tvDeckCardUprightDescription.setText(holder.binding.getRoot().getContext().getString(
                R.string.deck_card_upright_description_format, card.getDescription()));
        holder.binding.tvDeckCardReversedDescription.setText(holder.binding.getRoot().getContext().getString(
                R.string.deck_card_reversed_description_format, card.getReversedDescription()));
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemDeckCardBinding binding;

        ViewHolder(ItemDeckCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
