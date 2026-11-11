package com.example.tarot.data;

import com.example.tarot.model.TarotCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TarotDeck {

    private static List<TarotCard> cachedDeck = null;

    /**
     * 取得完整的 78 張傳統塔羅牌定義數據
     */
    public static synchronized List<TarotCard> getAllCards() {
        if (cachedDeck != null) {
            return cachedDeck;
        }

        List<TarotCard> cards = new ArrayList<>(78);

        // --- 1. 大阿爾克那 Major Arcana (22張) ---
        cards.add(new TarotCard(0, "愚者", "The Fool", "大阿爾克那", "主牌",
                "新的開始、冒險、自由、純真、潛能、順應直覺",
                "魯莽、盲目冒險、缺乏考量、停滯不前、輕率",
                "象徵踏入全新旅程的勇氣，帶著赤子之心迎向未知。"));
        cards.add(new TarotCard(1, "魔術師", "The Magician", "大阿爾克那", "主牌",
                "創造力、行動力、資源整合、溝通技巧、意志力",
                "意志薄弱、欺瞞、計畫受阻、技術不熟練、空談",
                "象徵掌握四元素與自身潛能，將理想轉化為現實。"));
        cards.add(new TarotCard(2, "女祭司", "The High Priestess", "大阿爾克那", "主牌",
                "直覺、潛意識、沉靜智慧、隱秘、觀察與沉思",
                "情緒壓抑、過度冷漠、忽略直覺、表面化、秘辛外洩",
                "象徵內在的靈性智慧與平靜，聆聽內心深處聲音。"));
        cards.add(new TarotCard(3, "皇后", "The Empress", "大阿爾克那", "主牌",
                "豐盛、母愛、滋養、創造力、自然、感官享受",
                "過度依賴、缺乏安全感、創意受阻、浪費、窒息的愛",
                "象徵無條件的愛與大自然的滋養，代表收穫與豐盛。"));
        cards.add(new TarotCard(4, "皇帝", "The Emperor", "大阿爾克那", "主牌",
                "權威、秩序、領導力、穩定結構、理性控制",
                "專制獨裁、固執缺乏彈性、控制慾過強、混亂無序",
                "象徵建立秩序與架構，以理性與堅定力量掌握局勢。"));
        cards.add(new TarotCard(5, "教皇", "The Hierophant", "大阿爾克那", "主牌",
                "傳統價值、信仰、指引、道德、學習與傳承",
                "僵化守舊、盲從、反叛權威、價值觀衝突、偽善",
                "象徵精神上的引路人與體制內的智慧學習。"));
        cards.add(new TarotCard(6, "戀人", "The Lovers", "大阿爾克那", "主牌",
                "愛與結合、選擇、契合、價值觀整合、和諧關係",
                "價值觀分歧、猶豫不決、選擇錯誤、關係破裂",
                "象徵靈魂與關係的連結，以及面臨重要選擇的警覺。"));
        cards.add(new TarotCard(7, "戰車", "The Chariot", "大阿爾克那", "主牌",
                "意志力、意志勝利、克服衝突、勇往直前、自律",
                "失控、方向迷失、過度衝動、挫折阻礙、意志渙散",
                "象徵駕馭對立的力量，以強大決心突破重重阻礙。"));
        cards.add(new TarotCard(8, "力量", "The Strength", "大阿爾克那", "主牌",
                "內在勇氣、柔克剛、包容力、情緒自控、堅韌",
                "自我懷疑、情緒失控、軟弱無力、衝動魯莽",
                "象徵以愛與耐心撫平狂暴，發揮內在真正的力量。"));
        cards.add(new TarotCard(9, "隱士", "The Hermit", "大阿爾克那", "主牌",
                "內省、尋求真理、獨處思考、智慧指引、沉澱",
                "孤立封閉、過度偏執、逃避現實、孤獨感",
                "象徵提燈照亮心靈深處，在孤獨中發現智慧。"));
        cards.add(new TarotCard(10, "命運之輪", "Wheel of Fortune", "大阿爾克那", "主牌",
                "轉機、命運契機、週期循環、順應變化、幸運",
                "運氣不佳、抗拒改變、循環阻礙、意外連連",
                "象徵生命起伏的定律，把握變化帶來的契機。"));
        cards.add(new TarotCard(11, "正義", "Justice", "大阿爾克那", "主牌",
                "公平公正、因果報應、理性判斷、法律與責任",
                "不公不義、偏見、逃避責任、判斷失誤",
                "象徵天平與寶劍的裁決，承擔行為的後果與真相。"));
        cards.add(new TarotCard(12, "倒吊人", "The Hanged Man", "大阿爾克那", "主牌",
                "換位思考、犧牲奉獻、等待時機、覺察與轉念",
                "無謂犧牲、抗拒等待、僵局、盲目奉獻",
                "象徵暫停與順應，以全新角度重新看待局勢。"));
        cards.add(new TarotCard(13, "死神", "Death", "大阿爾克那", "主牌",
                "結束與重生、告別過去、轉型、必然的蛻變",
                "抗拒改變、恐懼未知、沉溺過去、僵局延續",
                "象徵舊有模式的終結，為新生命讓出空間。"));
        cards.add(new TarotCard(14, "節制", "Temperance", "大阿爾克那", "主牌",
                "調和平衡、適度、溝通融合、自我調解、平靜",
                "失去平衡、極端衝動、缺乏耐性、溝通障礙",
                "象徵兩端能量的調和，尋求最適當的平衡之道。"));
        cards.add(new TarotCard(15, "惡魔", "The Devil", "大阿爾克那", "主牌",
                "物質慾望、束縛、執著、誘惑、發現內在陰暗面",
                "擺脫束縛、覺醒解脫、克服誘惑、重獲自由",
                "象徵看清無形的執念與枷鎖，掌握覺醒的主導權。"));
        cards.add(new TarotCard(16, "高塔", "The Tower", "大阿爾克那", "主牌",
                "突發巨變、幻象破滅、突破瓶頸、真相大白、重塑",
                "抗拒變革、緊抓殘局、延遲爆發、內在動盪",
                "象徵崩解虛幻的結構，雖然震撼卻是重獲新生的契機。"));
        cards.add(new TarotCard(17, "星星", "The Star", "大阿爾克那", "主牌",
                "希望、靈感、治癒、平靜、指引、願景",
                "失望、失去信心、茫然無措、空想絕望",
                "象徵黑暗過後的希望曙光，內心深處的撫慰與信心。"));
        cards.add(new TarotCard(18, "月亮", "The Moon", "大阿爾克那", "主牌",
                "潛意識、不安、幻象、直覺、隱藏的焦慮",
                "化解疑慮、真相浮現、克服恐懼、直覺清晰",
                "象徵面對迷霧與不安，信任直覺走過幽暗。"));
        cards.add(new TarotCard(19, "太陽", "The Sun", "大阿爾克那", "主牌",
                "光明、成功、喜悅、活力、自信、清晰真相",
                "暫時陰霾、延遲成功、過度自信、活力下降",
                "象徵生命力與喜悅的綻放，萬物顯化於光明之中。"));
        cards.add(new TarotCard(20, "審判", "Judgement", "大阿爾克那", "主牌",
                "召喚、覺醒、自我審視、重大決定、重生契機",
                "自我懷疑、逃避召喚、悔恨、拖延裁決",
                "象徵聽到內心的號角聲，迎向靈魂的覺醒與重生。"));
        cards.add(new TarotCard(21, "世界", "The World", "大阿爾克那", "主牌",
                "圓滿完成、完美整合、成就、階段終點與新起點",
                "未竟之業、欠缺臨門一腳、延遲圓滿、停滯",
                "象徵一個旅程的圓滿落幕，獲得全面的理解與昇華。"));

        // --- 2. 小阿爾克那：權杖 Minor Arcana - Wands (14張) ---
        addSuitCards(cards, 22, "權杖", "Wands", "火元素",
                "熱情、行動、能量、創造力", "焦躁、受阻、缺乏動力、衝動");

        // --- 3. 小阿爾克那：聖盃 Minor Arcana - Cups (14張) ---
        addSuitCards(cards, 36, "聖盃", "Cups", "水元素",
                "情感、直覺、愛、關係與感受", "情感冷漠、過度感傷、失落、混亂");

        // --- 4. 小阿爾克那：寶劍 Minor Arcana - Swords (14張) ---
        addSuitCards(cards, 50, "寶劍", "Swords", "風元素",
                "理智、思想、溝通、挑戰與決策", "執念、傷害、溝通受阻、焦慮過度");

        // --- 5. 小阿爾克那：錢幣 Minor Arcana - Pentacles (14張) ---
        addSuitCards(cards, 64, "錢幣", "Pentacles", "土元素",
                "物質、財富、工作、實際結果與穩定", "財務損失、不穩定、缺乏耐心、貪婪");

        cachedDeck = Collections.unmodifiableList(cards);
        return cachedDeck;
    }

    private static void addSuitCards(List<TarotCard> list, int startId, String suitCn, String suitEn, String element,
                                     String posKeywordBase, String negKeywordBase) {
        String[] cardNames = {"首牌 (Ace)", "二", "三", "四", "五", "六", "七", "八", "九", "十", "侍者", "騎士", "王后", "國王"};
        String[] cardEng = {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Page", "Knight", "Queen", "King"};

        for (int i = 0; i < 14; i++) {
            int id = startId + i;
            String name = suitCn + cardNames[i];
            String engName = cardEng[i] + " of " + suitEn;
            String posKw = suitCn + " - " + posKeywordBase + " [" + cardNames[i] + "]";
            String negKw = suitCn + " - " + negKeywordBase + " [" + cardNames[i] + "]";
            String desc = "象徵" + element + "在" + name + "層面的能量顯化與指引。";

            list.add(new TarotCard(id, name, engName, "小阿爾克那", suitCn, posKw, negKw, desc));
        }
    }

    /**
     * 隨機洗牌，並為每張牌賦予隨機的正/逆位 (50% 正位 / 50% 逆位)
     */
    public static List<TarotCard> createShuffledDeck() {
        List<TarotCard> allCards = getAllCards();
        List<TarotCard> deck = new ArrayList<>(allCards.size());

        Random random = new Random();
        for (TarotCard original : allCards) {
            TarotCard copy = original.copy();
            copy.setReversed(random.nextBoolean()); // 隨機正/逆位
            deck.add(copy);
        }

        Collections.shuffle(deck, random); // 完全打亂 78 張牌順序
        return deck;
    }
}
