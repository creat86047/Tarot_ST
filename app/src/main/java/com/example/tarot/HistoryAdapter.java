package com.example.tarot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tarot.model.TarotCard;
import com.example.tarot.model.TarotReading;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    public interface OnHistoryClickListener {
        void onItemClick(TarotReading reading);
        void onDeleteClick(TarotReading reading);
    }

    private final List<TarotReading> readings = new ArrayList<>();
    private final OnHistoryClickListener listener;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());

    public HistoryAdapter(List<TarotReading> readings, OnHistoryClickListener listener) {
        if (readings != null) {
            this.readings.addAll(readings);
        }
        this.listener = listener;
    }

    public void updateData(List<TarotReading> newReadings) {
        readings.clear();
        if (newReadings != null) {
            readings.addAll(newReadings);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TarotReading reading = readings.get(position);

        String dateStr = dateFormat.format(new Date(reading.getTimestamp()));
        holder.tvHistoryDate.setText(dateStr);

        holder.tvHistoryQuestion.setText("問題：" + reading.getQuestion());
        holder.tvHistorySpread.setText("牌陣：" + reading.getSpreadName());

        StringBuilder sb = new StringBuilder("抽牌摘要：");
        List<TarotCard> cards = reading.getDrawnCards();
        if (cards != null && !cards.isEmpty()) {
            for (int i = 0; i < cards.size(); i++) {
                TarotCard c = cards.get(i);
                sb.append((i + 1)).append(". ").append(c.getName())
                        .append("(").append(c.isReversed() ? "逆" : "正").append(")");
                if (i < cards.size() - 1) {
                    sb.append(" | ");
                }
            }
        } else {
            sb.append("無卡牌資料");
        }
        holder.tvHistoryCardsSummary.setText(sb.toString());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(reading);
            }
        });

        holder.btnDeleteHistory.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(reading);
            }
        });
    }

    @Override
    public int getItemCount() {
        return readings.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHistoryDate;
        ImageButton btnDeleteHistory;
        TextView tvHistoryQuestion;
        TextView tvHistorySpread;
        TextView tvHistoryCardsSummary;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHistoryDate = itemView.findViewById(R.id.tv_history_date);
            btnDeleteHistory = itemView.findViewById(R.id.btn_delete_history);
            tvHistoryQuestion = itemView.findViewById(R.id.tv_history_question);
            tvHistorySpread = itemView.findViewById(R.id.tv_history_spread);
            tvHistoryCardsSummary = itemView.findViewById(R.id.tv_history_cards_summary);
        }
    }
}
