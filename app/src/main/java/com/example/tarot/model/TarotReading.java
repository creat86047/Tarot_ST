package com.example.tarot.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TarotReading implements Serializable {
    private String question;
    private String spreadName;
    private List<String> positionMeanings;
    private List<TarotCard> drawnCards;
    private long timestamp;

    public TarotReading(String question, String spreadName, List<String> positionMeanings) {
        this.question = question;
        this.spreadName = spreadName;
        this.positionMeanings = positionMeanings != null ? positionMeanings : new ArrayList<>();
        this.drawnCards = new ArrayList<>();
        this.timestamp = System.currentTimeMillis();
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getSpreadName() {
        return spreadName;
    }

    public void setSpreadName(String spreadName) {
        this.spreadName = spreadName;
    }

    public List<String> getPositionMeanings() {
        return positionMeanings;
    }

    public void setPositionMeanings(List<String> positionMeanings) {
        this.positionMeanings = positionMeanings;
    }

    public List<TarotCard> getDrawnCards() {
        return drawnCards;
    }

    public void setDrawnCards(List<TarotCard> drawnCards) {
        this.drawnCards = drawnCards;
    }

    public void addDrawnCard(TarotCard card) {
        if (drawnCards == null) {
            drawnCards = new ArrayList<>();
        }
        drawnCards.add(card);
    }

    public boolean isComplete() {
        return positionMeanings != null && drawnCards != null && drawnCards.size() == positionMeanings.size();
    }

    public long getTimestamp() {
        return timestamp;
    }

    /**
     * 產生高質量的 AI 解牌 Prompt (供 ChatGPT / Claude / Gemini / DeepSeek 等使用)
     */
    public String generateAIPrompt() {
        StringBuilder sb = new StringBuilder();
        sb.append("你是一位經驗豐富、直覺敏銳且具備深厚象徵學底蘊的專業塔羅牌解牌大師。\n");
        sb.append("請針對以下「塔羅高塔玩家報告」的占卜紀錄進行深度且具體精準的解析與指引：\n\n");
        
        sb.append("【占卜問題】：").append(question.trim().isEmpty() ? "（未指定問題，請綜合當前能量狀態解析）" : question).append("\n");
        sb.append("【牌陣類別】：").append(spreadName).append("\n\n");

        sb.append("----------------------------------------\n");
        sb.append("【抽牌結果與位置象徵意涵】：\n");

        for (int i = 0; i < drawnCards.size(); i++) {
            TarotCard card = drawnCards.get(i);
            String position = (i < positionMeanings.size()) ? positionMeanings.get(i) : ("位置 " + (i + 1));
            
            sb.append("\n▶ 第 ").append(i + 1).append(" 張牌 — 【").append(position).append("】\n");
            sb.append("   - 牌名：").append(card.getName()).append(" (").append(card.getEnglishName()).append(")\n");
            sb.append("   - 狀態：").append(card.getOrientationText()).append("\n");
            sb.append("   - 類別/元素：").append(card.getArcana()).append(" / ").append(card.getSuit()).append("\n");
            sb.append("   - 當前關鍵字：").append(card.getCurrentKeywords()).append("\n");
            sb.append("   - 核心牌義：").append(card.getDescription()).append("\n");
        }

        sb.append("\n----------------------------------------\n");
        sb.append("【牌陣整體數據】：\n");
        int uprightCount = 0;
        int reversedCount = 0;
        int majorCount = 0;
        int minorCount = 0;
        for (TarotCard card : drawnCards) {
            if (card.isReversed()) reversedCount++; else uprightCount++;
            if ("大阿爾克那".equals(card.getArcana())) majorCount++; else minorCount++;
        }
        sb.append(" - 總抽牌數：").append(drawnCards.size()).append(" 張\n");
        sb.append(" - 正位/逆位比例：正位 ").append(uprightCount).append(" 張 / 逆位 ").append(reversedCount).append(" 張\n");
        sb.append(" - 大牌/小牌比例：大阿爾克那 ").append(majorCount).append(" 張 / 小阿爾克那 ").append(minorCount).append(" 張\n\n");

        sb.append("【解牌要求】：\n");
        sb.append("1. 請個別解析每一張牌在其指定位置所傳達的訊息與內在動機。\n");
        sb.append("2. 請進行跨牌之間的綜合分析，探討牌卡間的能量流動、正逆位平衡與核心課題。\n");
        sb.append("3. 請特別針對「高塔（Tower）」般的突發轉機、突破口與潛在盲點給予醒悟與建議。\n");
        sb.append("4. 給予問卜者溫暖、有建設性且可操作的行動方案建議。\n");

        return sb.toString();
    }
}
