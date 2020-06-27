package com.example.memotica;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TestOpenHelper extends SQLiteOpenHelper {

    // データーベースのバージョン
    private static final int DATABASE_VERSION = 1;

    // データーベース名
    private static final String DATABASE_NAME = "TestDB.db";
    private static final String TABLE_NAME = "testdb";
    private static final String COLUMN_NAME_UUID = "uuid";
    private static final String COLUMN_NAME_TITLE = "title";
    private static final String COLUMN_NAME_CONTENT = "content";
    private static final String COLUMN_NAME_UPDATED = "updated";


    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_NAME_UUID     + " TEXT PRIMARY KEY, " +
                    COLUMN_NAME_TITLE    + " TEXT, " +
                    COLUMN_NAME_CONTENT  + " TEXT, " +
                    COLUMN_NAME_UPDATED  + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;


    TestOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // テーブル作成
        // SQLiteファイルがなければSQLiteファイルが作成される
        db.execSQL(
                SQL_CREATE_ENTRIES
        );

        Log.d("debug", "onCreate(SQLiteDatabase db)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // アップデートの判別
        db.execSQL(
                SQL_DELETE_ENTRIES
        );
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void createData(String id) {
        // 新しくメモをデータベースに保存
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("uuid", id);
        cv.put("title", "");
        cv.put("content", "");
        cv.put("updated", "yyyy/mm/dd");
        db.insert(TABLE_NAME, null, cv);
    }

    public void saveData(String id, String title, String content ) {
        // データを更新
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("title", title);
        cv.put("content", content);
        String[] args = {id};
        db.update(TABLE_NAME, cv, "uuid=?", args);
    }

    public void deleteMemo(String uuid){
        // データを削除
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = {uuid};
        db.delete(TABLE_NAME, "uuid=?", args);
    }
}
