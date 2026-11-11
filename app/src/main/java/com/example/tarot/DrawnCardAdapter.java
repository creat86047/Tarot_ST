package com.example.tarot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

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
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_drawn_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String posMeaning = (position < positionMeanings.size()) ? positionMeanings.get(position) : ("位置 " + (position + 1));
        holder.tvCardPosition.setText("第 " + (position + 1) + " 張 • 【" + posMeaning + "】");

        if (position < drawnCards.size() && drawnCards.get(position) != null) {
            TarotCard card = drawnCards.get(position);

            holder.tvLockBadge.setText("🔒 已鎖定");
            holder.tvLockBadge.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.tarot_surface_variant));

            holder.tvCardName.setText(card.getName() + " (" + card.getEnglishName() + ")");
            holder.tvCardName.setVisibility(View.VISIBLE);

            if (card.isReversed()) {
                holder.tvOrientation.setText("逆位 (Reversed)");
                holder.tvOrientation.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.tarot_reversed_crimson));
            } else {
                holder.tvOrientation.setText("正位 (Upright)");
                holder.tvOrientation.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.tarot_upright_green));
            }
            holder.tvOrientation.setVisibility(View.VISIBLE);

            holder.tvArcanaSuit.setText(card.getArcana() + " • " + card.getSuit());
            holder.tvArcanaSuit.setVisibility(View.VISIBLE);

            holder.tvKeywords.setText("關鍵字：" + card.getCurrentKeywords());
            holder.tvKeywords.setVisibility(View.VISIBLE);

            holder.tvDescription.setText(card.getDescription());
            holder.tvDescription.setVisibility(View.VISIBLE);

        } else {
            // Pending draw position
            holder.tvLockBadge.setText("⏳ 待抽取");
            holder.tvLockBadge.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.tarot_card_border));

            holder.tvCardName.setText("卡牌尚在牌庫中，等待抽取...");
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
        TextView tvCardPosition;
        TextView tvLockBadge;
        TextView tvCardName;
        TextView tvOrientation;
        TextView tvArcanaSuit;
        TextView tvKeywords;
        TextView tvDescription;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCardPosition = itemView.findViewById(R.id.tv_card_position);
            tvLockBadge = itemView.findViewById(R.id.tv_lock_badge);
            tvCardName = itemView.findViewById(R.id.tv_card_name);
            tvOrientation = itemView.findViewById(R.id.tv_orientation);
            tvArcanaSuit = itemView.findViewById(R.id.tv_arcana_suit);
            tvKeywords = itemView.findViewById(R.id.tv_keywords);
            tvDescription = itemView.findViewById(R.id.tv_description);
        }
    }
}
