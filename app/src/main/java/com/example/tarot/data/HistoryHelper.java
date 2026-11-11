package com.example.tarot.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tarot.model.TarotReading;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HistoryHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tarot_history.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_READINGS = "readings";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_QUESTION = "question";
    private static final String COLUMN_SPREAD_NAME = "spread_name";
    private static final String COLUMN_TIMESTAMP = "timestamp";
    private static final String COLUMN_JSON = "reading_json";

    public HistoryHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_READINGS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_QUESTION + " TEXT, " +
                COLUMN_SPREAD_NAME + " TEXT, " +
                COLUMN_TIMESTAMP + " LONG, " +
                COLUMN_JSON + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_READINGS);
        onCreate(db);
    }

    public long saveReading(TarotReading reading) {
        if (reading == null) return -1;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_QUESTION, reading.getQuestion());
            values.put(COLUMN_SPREAD_NAME, reading.getSpreadName());
            values.put(COLUMN_TIMESTAMP, reading.getTimestamp());
            values.put(COLUMN_JSON, reading.toJSONObject().toString());

            return db.insert(TABLE_READINGS, null, values);
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public List<TarotReading> getAllReadings() {
        List<TarotReading> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_READINGS, null, null, null, null, null, COLUMN_TIMESTAMP + " DESC");

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String jsonStr = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JSON));
                try {
                    JSONObject json = new JSONObject(jsonStr);
                    TarotReading reading = TarotReading.fromJSONObject(json);
                    list.add(reading);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            cursor.close();
        }
        return list;
    }

    public void deleteReading(long timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_READINGS, COLUMN_TIMESTAMP + " = ?", new String[]{String.valueOf(timestamp)});
    }

    public void clearAllHistory() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_READINGS, null, null);
    }
}
