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

    private final List<String> defaultHints = new ArrayList<>();
    private final List<String> userInputs = new ArrayList<>();

    public PositionAdapter(List<String> initialHints) {
        updatePositions(initialHints);
    }

    public void updatePositions(List<String> newHints) {
        defaultHints.clear();
        userInputs.clear();
        if (newHints != null) {
            defaultHints.addAll(newHints);
            for (int i = 0; i < newHints.size(); i++) {
                userInputs.add(""); // 預設使用者未輸入，顯示虛體提示字 (Hint)
            }
        }
        notifyDataSetChanged();
    }

    public List<String> getPositions() {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < defaultHints.size(); i++) {
            String input = (i < userInputs.size()) ? userInputs.get(i).trim() : "";
            if (!input.isEmpty()) {
                result.add(input);
            } else {
                result.add(defaultHints.get(i)); // 若未自行輸入，自動採用預設提示詞
            }
        }
        return result;
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

        if (holder.textWatcher != null) {
            holder.etPositionMeaning.removeTextChangedListener(holder.textWatcher);
        }

        String hint = (position < defaultHints.size()) ? defaultHints.get(position) : "請輸入此位置代表的意涵";
        String currentInput = (position < userInputs.size()) ? userInputs.get(position) : "";

        // 設定虛體提示 (Hint)，使用者點擊輸入時自動消失，不需手動刪除預填文字
        holder.etPositionMeaning.setHint("例：" + hint);
        holder.etPositionMeaning.setText(currentInput);

        holder.textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int adapterPos = holder.getBindingAdapterPosition();
                if (adapterPos != RecyclerView.NO_POSITION && adapterPos < userInputs.size()) {
                    userInputs.set(adapterPos, s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        holder.etPositionMeaning.addTextChangedListener(holder.textWatcher);
    }

    @Override
    public int getItemCount() {
        return defaultHints.size();
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
