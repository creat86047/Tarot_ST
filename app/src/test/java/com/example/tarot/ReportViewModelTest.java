package com.example.tarot;

import androidx.lifecycle.SavedStateHandle;

import com.example.tarot.model.TarotCard;
import com.example.tarot.model.TarotReading;
import com.example.tarot.viewmodel.ReportViewModel;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReportViewModelTest {

    @Test
    public void summaryCountsOrientationAndArcanaAndPromptUsesReading() {
        TarotReading reading = new TarotReading("這次要注意什麼？", "雙牌", Arrays.asList("一", "二"));
        TarotCard major = new TarotCard(16, "高塔", "The Tower", "大阿爾克那", "主牌",
                "突破", "抗拒變化", "迎來改變。", "先檢視抗拒。");
        TarotCard minor = new TarotCard(22, "權杖首牌", "Ace of Wands", "小阿爾克那", "權杖",
                "靈感", "受阻", "開始創作。", "整理動力。");
        minor.setReversed(true);
        reading.setDrawnCards(Arrays.asList(major, minor));

        ReportViewModel viewModel = new ReportViewModel(new SavedStateHandle());
        viewModel.initialize(reading);
        ReportViewModel.Summary summary = viewModel.getSummary();

        assertEquals(1, summary.getUprightCount());
        assertEquals(1, summary.getReversedCount());
        assertEquals(1, summary.getMajorCount());
        assertEquals(1, summary.getMinorCount());
        assertTrue(viewModel.generatePrompt().contains("這次要注意什麼？"));
        assertTrue(viewModel.generatePrompt().contains("整理動力。"));
    }

    @Test
    public void initializeDoesNotOverwriteAReadingRestoredFromSavedState() {
        SavedStateHandle handle = new SavedStateHandle();
        TarotReading restored = new TarotReading("已保存問題", "牌陣", Arrays.asList("位置"));
        handle.set("report.reading", restored);

        ReportViewModel viewModel = new ReportViewModel(handle);
        viewModel.initialize(new TarotReading("新問題", "新牌陣", Arrays.asList("新位置")));

        assertEquals("已保存問題", viewModel.getReading().getQuestion());
    }
}
