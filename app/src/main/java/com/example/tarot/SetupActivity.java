package com.example.tarot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tarot.data.TarotDeck;
import com.example.tarot.databinding.ActivitySetupBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SetupActivity extends AppCompatActivity {

    private static final String STATE_QUESTION = "setup.question";
    private static final String STATE_PRESET_INDEX = "setup.presetIndex";
    private static final String STATE_CUSTOM_COUNT = "setup.customCount";
    private static final String STATE_POSITIONS = "setup.positions";

    private ActivitySetupBinding binding;
    private PositionAdapter positionAdapter;
    private int customCount = 3;
    private int currentPresetIndex = 1;
    private int customSpreadMaxCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        customSpreadMaxCards = Math.max(1, Math.min(
                getResources().getInteger(R.integer.custom_spread_max_cards), TarotDeck.CARD_COUNT));
        if (savedInstanceState != null) {
            customCount = Math.max(1, Math.min(savedInstanceState.getInt(STATE_CUSTOM_COUNT, 3),
                    customSpreadMaxCards));
            currentPresetIndex = Math.max(0, Math.min(savedInstanceState.getInt(STATE_PRESET_INDEX, 1), 4));
            binding.etQuestion.setText(savedInstanceState.getString(STATE_QUESTION, ""));
        }

        ArrayList<String> savedPositions = savedInstanceState == null
                ? null : savedInstanceState.getStringArrayList(STATE_POSITIONS);
        List<String> initialPositions = savedPositions != null
                ? savedPositions : getPresetMeanings(currentPresetIndex);
        positionAdapter = new PositionAdapter(initialPositions);
        binding.rvPositions.setLayoutManager(new LinearLayoutManager(this));
        binding.rvPositions.setAdapter(positionAdapter);

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
                return applyPresetTextStyle(super.getView(position, convertView, parent), false);
            }

            @Override
            public View getDropDownView(int position, View convertView, android.view.ViewGroup parent) {
                return applyPresetTextStyle(super.getDropDownView(position, convertView, parent), true);
            }

            private View applyPresetTextStyle(View view, boolean dropdown) {
                if (view instanceof TextView) {
                    ((TextView) view).setTextColor(androidx.core.content.ContextCompat.getColor(
                            SetupActivity.this, R.color.tarot_text_primary));
                    if (dropdown) {
                        view.setBackgroundColor(androidx.core.content.ContextCompat.getColor(
                                SetupActivity.this, R.color.tarot_surface_variant));
                    }
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        binding.spinnerPresets.setAdapter(adapter);
        binding.spinnerPresets.setSelection(currentPresetIndex);
        binding.tvCardCount.setText(String.valueOf(customCount));

        binding.spinnerPresets.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view,
                                       int position, long id) {
                boolean selectionChanged = position != currentPresetIndex;
                currentPresetIndex = position;
                binding.layoutCustomCount.setVisibility(position == 4 ? View.VISIBLE : View.GONE);
                if (selectionChanged) {
                    if (position == 4) {
                        updateCustomPositions();
                    } else {
                        positionAdapter.updatePositions(getPresetMeanings(position));
                    }
                }
                updateCountButtons();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });

        binding.btnMinusCard.setOnClickListener(v -> {
            if (customCount > 1) {
                customCount--;
                binding.tvCardCount.setText(String.valueOf(customCount));
                updateCustomPositions();
                updateCountButtons();
            }
        });
        binding.btnPlusCard.setOnClickListener(v -> {
            if (customCount < customSpreadMaxCards) {
                customCount++;
                binding.tvCardCount.setText(String.valueOf(customCount));
                updateCustomPositions();
                updateCountButtons();
            }
        });
        updateCountButtons();

        binding.btnConfirmSetup.setOnClickListener(v -> startDraw(presets));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_QUESTION, binding.etQuestion.getText().toString());
        outState.putInt(STATE_PRESET_INDEX, binding.spinnerPresets.getSelectedItemPosition());
        outState.putInt(STATE_CUSTOM_COUNT, customCount);
        outState.putStringArrayList(STATE_POSITIONS, new ArrayList<>(positionAdapter.getPositions()));
    }

    private void startDraw(String[] presets) {
        String question = binding.etQuestion.getText().toString().trim();
        if (question.isEmpty()) {
            question = getString(R.string.hint_question);
        }

        List<String> positionMeanings = positionAdapter.getPositions();
        if (positionMeanings.isEmpty()) {
            Toast.makeText(this, R.string.toast_need_position, Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedIndex = binding.spinnerPresets.getSelectedItemPosition();
        String spreadName = presets[selectedIndex];
        Intent intent = new Intent(this, DrawActivity.class);
        intent.putExtra("EXTRA_QUESTION", question);
        intent.putExtra("EXTRA_SPREAD_NAME", spreadName);
        intent.putStringArrayListExtra("EXTRA_POSITIONS", new ArrayList<>(positionMeanings));
        startActivity(intent);
    }

    private List<String> getPresetMeanings(int presetIndex) {
        switch (presetIndex) {
            case 0:
                return Collections.singletonList(getString(R.string.position_single));
            case 1:
                return Arrays.asList(getString(R.string.position_timeline_past),
                        getString(R.string.position_timeline_present),
                        getString(R.string.position_timeline_future));
            case 2:
                return Arrays.asList(getString(R.string.position_fire), getString(R.string.position_water),
                        getString(R.string.position_air), getString(R.string.position_earth));
            case 3:
                return Arrays.asList(getString(R.string.position_tower_current),
                        getString(R.string.position_tower_obstacle),
                        getString(R.string.position_tower_hidden),
                        getString(R.string.position_tower_breakthrough),
                        getString(R.string.position_tower_outlook));
            default:
                return createCustomDefaults(customCount);
        }
    }

    private void updateCustomPositions() {
        positionAdapter.updatePositions(getCustomMeanings(customCount));
    }

    private List<String> getCustomMeanings(int count) {
        List<String> current = positionAdapter.getPositions();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            if (i < current.size() && !current.get(i).trim().isEmpty()) {
                list.add(current.get(i));
            } else {
                list.add(createCustomDefault(i + 1));
            }
        }
        return list;
    }

    private List<String> createCustomDefaults(int count) {
        List<String> defaults = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            defaults.add(createCustomDefault(i + 1));
        }
        return defaults;
    }

    private String createCustomDefault(int position) {
        return getString(R.string.position_custom_format, position);
    }

    private void updateCountButtons() {
        binding.btnMinusCard.setEnabled(customCount > 1);
        binding.btnPlusCard.setEnabled(customCount < customSpreadMaxCards);
    }
}
