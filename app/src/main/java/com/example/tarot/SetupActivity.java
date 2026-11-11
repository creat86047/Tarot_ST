package com.example.tarot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetupActivity extends AppCompatActivity {

    private EditText etQuestion;
    private Spinner spinnerPresets;
    private LinearLayout layoutCustomCount;
    private TextView tvCardCount;
    private RecyclerView rvPositions;
    private PositionAdapter positionAdapter;

    private int customCount = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        etQuestion = findViewById(R.id.et_question);
        spinnerPresets = findViewById(R.id.spinner_presets);
        layoutCustomCount = findViewById(R.id.layout_custom_count);
        tvCardCount = findViewById(R.id.tv_card_count);
        rvPositions = findViewById(R.id.rv_positions);
        MaterialButton btnMinusCard = findViewById(R.id.btn_minus_card);
        MaterialButton btnPlusCard = findViewById(R.id.btn_plus_card);
        MaterialButton btnConfirmSetup = findViewById(R.id.btn_confirm_setup);

        // RecyclerView Setup
        positionAdapter = new PositionAdapter(getPresetMeanings(1)); // default 3-card
        rvPositions.setLayoutManager(new LinearLayoutManager(this));
        rvPositions.setAdapter(positionAdapter);

        // Spinner Presets Setup
        String[] presets = new String[]{
                getString(R.string.preset_single),
                getString(R.string.preset_timeline),
                getString(R.string.preset_four_elements),
                getString(R.string.preset_tower),
                getString(R.string.preset_custom)
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, presets) {
            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (view instanceof TextView) {
                    ((TextView) view).setTextColor(androidx.core.content.ContextCompat.getColor(SetupActivity.this, R.color.tarot_text_primary));
                }
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, android.view.ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if (view instanceof TextView) {
                    ((TextView) view).setTextColor(androidx.core.content.ContextCompat.getColor(SetupActivity.this, R.color.tarot_text_primary));
                    view.setBackgroundColor(androidx.core.content.ContextCompat.getColor(SetupActivity.this, R.color.tarot_surface_variant));
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerPresets.setAdapter(adapter);
        spinnerPresets.setSelection(1); // Default to timeline (3 cards)

        spinnerPresets.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 4) { // Custom
                    layoutCustomCount.setVisibility(View.VISIBLE);
                    updateCustomPositions();
                } else {
                    layoutCustomCount.setVisibility(View.GONE);
                    positionAdapter.updatePositions(getPresetMeanings(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Custom Count Listeners
        btnMinusCard.setOnClickListener(v -> {
            if (customCount > 1) {
                customCount--;
                tvCardCount.setText(String.valueOf(customCount));
                updateCustomPositions();
            }
        });

        btnPlusCard.setOnClickListener(v -> {
            if (customCount < 10) {
                customCount++;
                tvCardCount.setText(String.valueOf(customCount));
                updateCustomPositions();
            }
        });

        // Start Draw Button Listener
        btnConfirmSetup.setOnClickListener(v -> {
            String question = etQuestion.getText().toString().trim();
            if (question.isEmpty()) {
                question = getString(R.string.hint_question);
            }

            List<String> positionMeanings = positionAdapter.getPositions();
            if (positionMeanings.isEmpty()) {
                Toast.makeText(this, "請至少設定一個抽牌位置！", Toast.LENGTH_SHORT).show();
                return;
            }

            String spreadName = presets[spinnerPresets.getSelectedItemPosition()];

            Intent intent = new Intent(SetupActivity.this, DrawActivity.class);
            intent.putExtra("EXTRA_QUESTION", question);
            intent.putExtra("EXTRA_SPREAD_NAME", spreadName);
            intent.putStringArrayListExtra("EXTRA_POSITIONS", new ArrayList<>(positionMeanings));
            startActivity(intent);
        });
    }

    private List<String> getPresetMeanings(int presetIndex) {
        switch (presetIndex) {
            case 0: // Single card
                return CollectionsSingleton.singletonList("核心議題與當前指引");
            case 1: // Timeline
                return Arrays.asList("過去起因與累積背景", "當前現狀與核心議題", "未來發展與潛在走向");
            case 2: // Four Elements
                return Arrays.asList("火元素（行動/熱情/意志）", "水元素（情感/直覺/關係）", "風元素（理智/思考/溝通）", "土元素（物質/現實/資源）");
            case 3: // Tower Breakout
                return Arrays.asList("當前現狀與表面現象", "遭遇盲點與核心障礙", "隱藏因素與轉化契機", "高塔突破點（關鍵覺醒）", "最終展望與學習成果");
            default:
                return getCustomMeanings(3);
        }
    }

    private void updateCustomPositions() {
        positionAdapter.updatePositions(getCustomMeanings(customCount));
    }

    private List<String> getCustomMeanings(int count) {
        List<String> list = new ArrayList<>();
        List<String> current = positionAdapter.getPositions();
        for (int i = 0; i < count; i++) {
            if (i < current.size() && !current.get(i).trim().isEmpty()) {
                list.add(current.get(i));
            } else {
                list.add("位置 " + (i + 1) + " 的代表意涵");
            }
        }
        return list;
    }

    // Helper static class to avoid Collections.singletonList type casting hassle
    private static class CollectionsSingleton {
        static List<String> singletonList(String item) {
            List<String> l = new ArrayList<>();
            l.add(item);
            return l;
        }
    }
}
