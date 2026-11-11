package com.example.tarot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tarot.data.HistoryHelper;
import com.example.tarot.model.TarotReading;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class HistoryActivity extends BaseActivity {

    private RecyclerView rvHistory;
    private TextView tvEmptyHistory;
    private HistoryHelper historyHelper;
    private List<TarotReading> readingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        setupEdgeToEdgePadding(findViewById(R.id.main));
        setupTopHeaderBar();

        rvHistory = findViewById(R.id.rv_history);
        tvEmptyHistory = findViewById(R.id.tv_empty_history);
        MaterialButton btnClearAll = findViewById(R.id.btn_clear_all_history);
        MaterialButton btnBackHome = findViewById(R.id.btn_back_home_history);

        historyHelper = new HistoryHelper(this);

        rvHistory.setLayoutManager(new LinearLayoutManager(this));

        loadHistory();

        btnClearAll.setOnClickListener(v -> {
            if (readingList == null || readingList.isEmpty()) {
                Toast.makeText(this, "目前沒有紀錄可清空", Toast.LENGTH_SHORT).show();
                return;
            }

            new AlertDialog.Builder(this, R.style.Theme_Tarot_Dialog)
                    .setTitle("確認清空歷史紀錄")
                    .setMessage("確定要刪除所有過去的占卜與抽卡紀錄嗎？刪除後無法恢復。")
                    .setPositiveButton("確定清空", (dialog, which) -> {
                        historyHelper.clearAllHistory();
                        loadHistory();
                        Toast.makeText(this, "已清空所有歷史紀錄", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("取消", null)
                    .show();
        });

        btnBackHome.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadHistory();
    }

    private void loadHistory() {
        readingList = historyHelper.getAllReadings();
        if (readingList.isEmpty()) {
            rvHistory.setVisibility(View.GONE);
            tvEmptyHistory.setVisibility(View.VISIBLE);
        } else {
            rvHistory.setVisibility(View.VISIBLE);
            tvEmptyHistory.setVisibility(View.GONE);
        }

        HistoryAdapter adapter = new HistoryAdapter(readingList, new HistoryAdapter.OnHistoryClickListener() {
            @Override
            public void onItemClick(TarotReading reading) {
                Intent intent = new Intent(HistoryActivity.this, ReportActivity.class);
                intent.putExtra("EXTRA_READING", reading);
                intent.putExtra("EXTRA_FROM_HISTORY", true);
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(TarotReading reading) {
                historyHelper.deleteReading(reading.getTimestamp());
                loadHistory();
                Toast.makeText(HistoryActivity.this, "已刪除該筆占卜紀錄", Toast.LENGTH_SHORT).show();
            }
        });

        rvHistory.setAdapter(adapter);
    }
}
