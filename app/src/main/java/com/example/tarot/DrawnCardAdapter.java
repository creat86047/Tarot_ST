package com.example.tarot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tarot.databinding.ItemDrawnCardBinding;
import com.example.tarot.model.TarotCard;

import java.util.ArrayList;
import java.util.List;

public class DrawnCardAdapter extends RecyclerView.Adapter<DrawnCardAdapter.ViewHolder> {

    private final List<String> positionMeanings = new ArrayList<>();
    private final List<TarotCard> drawnCards = new ArrayList<>();

    public DrawnCardAdapter(List<String> positionMeanings, List<TarotCard> drawnCards) {
        if (positionMeanings != null) {
            this.positionMeanings.addAll(positionMeanings);
        }
        if (drawnCards != null) {
            this.drawnCards.addAll(drawnCards);
        }
    }

    public void updateData(List<String> positionMeanings, List<TarotCard> drawnCards) {
        this.positionMeanings.clear();
        if (positionMeanings != null) {
            this.positionMeanings.addAll(positionMeanings);
        }
        this.drawnCards.clear();
        if (drawnCards != null) {
            this.drawnCards.addAll(drawnCards);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDrawnCardBinding binding = ItemDrawnCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String posMeaning = (position < positionMeanings.size()) ? positionMeanings.get(position)
                : holder.itemView.getContext().getString(R.string.position_label_format, position + 1);
        holder.tvCardPosition.setText(holder.itemView.getContext().getString(
                R.string.draw_card_position_format, position + 1, posMeaning));

        if (position < drawnCards.size() && drawnCards.get(position) != null) {
            TarotCard card = drawnCards.get(position);

            holder.tvLockBadge.setText(R.string.card_locked);
            holder.tvLockBadge.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.tarot_surface_variant));

            holder.tvCardName.setText(card.getName() + " (" + card.getEnglishName() + ")");
            holder.tvCardName.setVisibility(View.VISIBLE);

            if (card.isReversed()) {
                holder.tvOrientation.setText(R.string.orientation_reversed);
                holder.tvOrientation.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.tarot_reversed_crimson));
            } else {
                holder.tvOrientation.setText(R.string.orientation_upright);
                holder.tvOrientation.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.tarot_upright_green));
            }
            holder.tvOrientation.setVisibility(View.VISIBLE);

            holder.tvArcanaSuit.setText(card.getArcana() + " • " + card.getSuit());
            holder.tvArcanaSuit.setVisibility(View.VISIBLE);

            holder.tvKeywords.setText(holder.itemView.getContext().getString(
                    R.string.card_keywords_format, card.getCurrentKeywords()));
            holder.tvKeywords.setVisibility(View.VISIBLE);

            holder.tvDescription.setText(card.getCurrentDescription());
            holder.tvDescription.setVisibility(View.VISIBLE);

        } else {
            // Pending draw position
            holder.tvLockBadge.setText(R.string.card_pending);
            holder.tvLockBadge.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.tarot_card_border));

            holder.tvCardName.setText(R.string.card_pending_name);
            holder.tvOrientation.setVisibility(View.GONE);
            holder.tvArcanaSuit.setVisibility(View.GONE);
            holder.tvKeywords.setVisibility(View.GONE);
            holder.tvDescription.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return positionMeanings.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemDrawnCardBinding binding;
        TextView tvCardPosition;
        TextView tvLockBadge;
        TextView tvCardName;
        TextView tvOrientation;
        TextView tvArcanaSuit;
        TextView tvKeywords;
        TextView tvDescription;

        ViewHolder(@NonNull ItemDrawnCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            tvCardPosition = binding.tvCardPosition;
            tvLockBadge = binding.tvLockBadge;
            tvCardName = binding.tvCardName;
            tvOrientation = binding.tvOrientation;
            tvArcanaSuit = binding.tvArcanaSuit;
            tvKeywords = binding.tvKeywords;
            tvDescription = binding.tvDescription;
        }
    }
}
