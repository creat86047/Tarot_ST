package com.example.tarot;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tarot.databinding.ActivityReportBinding;
import com.example.tarot.model.TarotCard;
import com.example.tarot.model.TarotReading;
import com.example.tarot.viewmodel.ReportViewModel;

import java.util.List;

public class ReportActivity extends AppCompatActivity {

    public static final String EXTRA_READING = ReportViewModel.EXTRA_READING;

    private ActivityReportBinding binding;
    private ReportViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(ReportViewModel.class);
        viewModel.initialize(getParcelableReading(getIntent()));

        renderReading();
        binding.btnCopyPrompt.setOnClickListener(v -> copyPromptToClipboard());
        binding.btnShare.setOnClickListener(v -> shareReportText());
        binding.btnNewReading.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    @SuppressWarnings("deprecation")
    private TarotReading getParcelableReading(Intent intent) {
        return intent.getParcelableExtra(EXTRA_READING);
    }

    private void renderReading() {
        TarotReading reading = viewModel.getReading();
        if (reading == null) {
            binding.tvReportQuestion.setText(R.string.report_missing_reading);
            binding.tvReportStats.setText(R.string.report_no_stats);
            binding.etAiPrompt.setText(R.string.report_missing_prompt);
            binding.rvReportCards.setAdapter(null);
            binding.btnCopyPrompt.setEnabled(false);
            binding.btnShare.setEnabled(false);
            return;
        }

        String question = reading.getQuestion() == null || reading.getQuestion().trim().isEmpty()
                ? getString(R.string.unspecified_question) : reading.getQuestion();
        binding.tvReportQuestion.setText(getString(R.string.report_question_format, question));

        List<TarotCard> cards = reading.getDrawnCards();
        ReportViewModel.Summary summary = viewModel.getSummary();

        String spreadName = reading.getSpreadName() == null
                ? getString(R.string.unspecified_spread) : reading.getSpreadName();
        binding.tvReportStats.setText(getString(R.string.report_stats_format,
                spreadName, summary.getUprightCount(), summary.getReversedCount(),
                summary.getMajorCount(), summary.getMinorCount()));

        DrawnCardAdapter adapter = new DrawnCardAdapter(reading.getPositionMeanings(), cards);
        binding.rvReportCards.setLayoutManager(new LinearLayoutManager(this));
        binding.rvReportCards.setAdapter(adapter);
        binding.etAiPrompt.setText(viewModel.generatePrompt());
    }

    private void copyPromptToClipboard() {
        String prompt = binding.etAiPrompt.getText().toString();
        if (prompt.isEmpty()) {
            Toast.makeText(this, R.string.toast_no_prompt_to_copy, Toast.LENGTH_SHORT).show();
            return;
        }

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null) {
            clipboard.setPrimaryClip(ClipData.newPlainText(getString(R.string.clip_label), prompt));
            Toast.makeText(this, R.string.toast_prompt_copied, Toast.LENGTH_LONG).show();
        }
    }

    private void shareReportText() {
        String prompt = binding.etAiPrompt.getText().toString();
        if (prompt.isEmpty()) {
            Toast.makeText(this, R.string.toast_no_report_to_share, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        TarotReading reading = viewModel.getReading();
        String question = reading == null || reading.getQuestion() == null
                ? getString(R.string.unspecified_question) : reading.getQuestion();
        shareIntent.putExtra(Intent.EXTRA_SUBJECT,
                getString(R.string.share_report_subject, question));
        shareIntent.putExtra(Intent.EXTRA_TEXT, prompt);
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_report_chooser)));
    }
}
