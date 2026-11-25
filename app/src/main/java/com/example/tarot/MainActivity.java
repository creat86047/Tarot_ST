package com.example.tarot;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tarot.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.btnStartReading.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SetupActivity.class);
            startActivity(intent);
        });

        binding.btnBrowseDeck.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DeckBrowseActivity.class);
            startActivity(intent);
        });

        binding.btnAbout.setOnClickListener(v -> showAboutDialog());
    }

    private void showAboutDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.about_dialog_title)
                .setMessage(R.string.about_dialog_message)
                .setPositiveButton(R.string.about_dismiss, (dialog, which) -> dialog.dismiss())
                .show();
    }
}
