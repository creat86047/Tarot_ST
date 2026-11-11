package com.example.tarot;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PositionAdapter extends RecyclerView.Adapter<PositionAdapter.ViewHolder> {

    private final List<String> positions = new ArrayList<>();

    public PositionAdapter(List<String> initialPositions) {
        if (initialPositions != null) {
            positions.addAll(initialPositions);
        }
    }

    public void updatePositions(List<String> newPositions) {
        positions.clear();
        if (newPositions != null) {
            positions.addAll(newPositions);
        }
        notifyDataSetChanged();
    }

    public List<String> getPositions() {
        return new ArrayList<>(positions);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_position_input, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvPositionLabel.setText("位置 " + (position + 1));
        
        // Remove existing text watcher to prevent recycled view bug
        if (holder.textWatcher != null) {
            holder.etPositionMeaning.removeTextChangedListener(holder.textWatcher);
        }

        holder.etPositionMeaning.setText(positions.get(position));

        holder.textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int adapterPos = holder.getAdapterPosition();
                if (adapterPos != RecyclerView.NO_POSITION && adapterPos < positions.size()) {
                    positions.set(adapterPos, s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        holder.etPositionMeaning.addTextChangedListener(holder.textWatcher);
    }

    @Override
    public int getItemCount() {
        return positions.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPositionLabel;
        EditText etPositionMeaning;
        TextWatcher textWatcher;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPositionLabel = itemView.findViewById(R.id.tv_position_label);
            etPositionMeaning = itemView.findViewById(R.id.et_position_meaning);
        }
    }
}
