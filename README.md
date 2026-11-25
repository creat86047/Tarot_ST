# Tarot ST

一個離線塔羅抽牌與牌義瀏覽 Android App。使用者可選擇預設或自訂牌陣、逐張抽取並鎖定牌卡，查看正逆位解讀，並產生可複製或分享的 AI 解牌 Prompt。

## 功能

- 78 張傳統塔羅牌：22 張大阿爾克那與 56 張小阿爾克那；小阿爾克那逐牌提供正逆位關鍵字與不同解讀。
- 每次抽牌都會洗牌並為牌卡隨機設定正位或逆位；抽出的牌不會重複。
- 單牌、三牌時間線、四要素、高塔五牌陣，或最多 78 張的自訂牌陣。
- 牌庫瀏覽頁顯示各牌正逆位關鍵字與牌義。
- 抽牌與報告由 `ViewModel` 管理，透過 `SavedStateHandle` 保存工作階段；設定頁以 instance state 保存輸入。
- 牌卡及占卜紀錄以 `Parcelable` 傳遞；系統重建工作階段時保留已抽牌順序與正逆位。
- 報告可產生、複製或分享 AI 解牌 Prompt，不會將占卜資料傳送至網路。

## 建置

需求：Android Studio（含 Android SDK 37）及專案 wrapper 指定的 JDK toolchain。從專案根目錄執行：

```powershell
.\gradlew.bat assembleDebug
```

安裝在已連接的 Android 裝置或模擬器：

```powershell
.\gradlew.bat installDebug
```

## 測試

```powershell
.\gradlew.bat testDebugUnitTest
```

單元測試涵蓋洗牌牌庫的完整性、小阿爾克那牌義、工作階段恢復、報告統計，以及 AI Prompt 的內容與缺值處理。儀器測試涵蓋 Parcelable round trip。

## 專案結構

- `app/src/main/java/com/example/tarot/data`：塔羅牌庫與洗牌。
- `app/src/main/java/com/example/tarot/model`：`TarotCard`、`TarotReading`。
- `app/src/main/java/com/example/tarot`：Activity、ViewBinding UI 與 RecyclerView adapters。
- `app/src/main/java/com/example/tarot/viewmodel`：抽牌工作階段及報告統計 / Prompt 邏輯。
- `app/src/main/res`：畫面、字串及自訂牌陣上限設定。

牌陣自訂上限可在 `app/src/main/res/values/integers.xml` 的 `custom_spread_max_cards` 調整；上限不應超過 78 張牌庫。

## 後續改造路線

本階段先把抽牌與報告的業務狀態從 Activity 移入 `ViewModel`，並以 `SavedStateHandle` 支援系統重建。接續建議：

1. **牌庫資料資產化**：把 78 張牌整理成 JSON / assets，加入 schema 驗證及資料完整性測試，讓牌義內容不必與洗牌邏輯耦合。
2. **占卜歷史（按產品需求）**：若要跨啟動保存紀錄，再引入 repository 與 Room；目前 App 主張離線且不留歷史，故不預先加入資料庫。
3. **發佈識別碼決策**：上架前由發佈者選擇唯一 namespace / applicationId；這會改變安裝識別，故不在此階段代選。
4. **Kotlin / Compose**：屬較大規模遷移，先等功能與設計穩定後再評估，不作為當前必要工作。

## 發佈前檢查

目前 `namespace` 與 `applicationId` 仍是範例值 `com.example.tarot`。正式上架前，請先確定發佈者擁有的唯一套件名稱，再一併更新 Gradle namespace/application ID、Java package、測試與 Manifest 引用。此版本先保留原識別碼，避免替發佈者預先決定 App 身分。
