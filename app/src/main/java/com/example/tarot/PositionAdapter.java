package com.example.tarot;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tarot.databinding.ItemPositionInputBinding;

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
        ItemPositionInputBinding binding = ItemPositionInputBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.tvPositionLabel.setText(
                holder.binding.getRoot().getContext().getString(R.string.position_label_format, position + 1));
        
        // Remove existing text watcher to prevent recycled view bug
        if (holder.textWatcher != null) {
            holder.binding.etPositionMeaning.removeTextChangedListener(holder.textWatcher);
        }

        holder.binding.etPositionMeaning.setText(positions.get(position));

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

        holder.binding.etPositionMeaning.addTextChangedListener(holder.textWatcher);
    }

    @Override
    public int getItemCount() {
        return positions.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemPositionInputBinding binding;
        TextWatcher textWatcher;

        ViewHolder(@NonNull ItemPositionInputBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
