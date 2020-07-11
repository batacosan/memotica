package com.example.memotica;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestOpenHelper extends SQLiteOpenHelper {

    // データーベースのバージョンと名前
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TestDB.db";

    // メモテーブル
    private static final String TEST_TABLE_NAME = "testdb";
    private static final String COLUMN_NAME_UUID = "uuid";
    private static final String COLUMN_NAME_TITLE = "title";
    private static final String COLUMN_NAME_CONTENT = "content";
    private static final String COLUMN_NAME_UPDATED = "updated";

    //　更新日時テーブル
    private static final String DATE_TABLE_NAME = "datedb";
    private static final String COLUMN_NAME_DATE_UUID = "uuid";
    private static final String COLUMN_NAME_UPDATED1 = "updated1";
    private static final String COLUMN_NAME_UPDATED2 = "updated2";
    private static final String COLUMN_NAME_UPDATED3 = "updated3";

    //　メモテーブルのカラム定義
    private static final String SQL_CREATE_ENTRIES_TEST =
        "CREATE TABLE " + TEST_TABLE_NAME + " (" +
            COLUMN_NAME_UUID     + " TEXT PRIMARY KEY, " +
            COLUMN_NAME_TITLE    + " TEXT, " +
            COLUMN_NAME_CONTENT  + " TEXT, " +
            COLUMN_NAME_UPDATED  + " TEXT)";

    //　更新日時テーブルのカラム定義
    private static final String SQL_CREATE_ENTRIES_DATE =
        "CREATE TABLE " + DATE_TABLE_NAME + " (" +
            COLUMN_NAME_DATE_UUID    + " TEXT, " +
            COLUMN_NAME_UPDATED1     + " TEXT, " +
            COLUMN_NAME_UPDATED2     + " TEXT, " +
            COLUMN_NAME_UPDATED3     + " TEXT)";

    private static final String SQL_DELETE_ENTRIES_TEST =
        "DROP TABLE IF EXISTS " + TEST_TABLE_NAME;

    private static final String SQL_DELETE_ENTRIES_DATE =
        "DROP TABLE IF EXISTS " + DATE_TABLE_NAME;

    TestOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // テーブル作成
        // SQLiteファイルがなければSQLiteファイルが作成される
        db.execSQL(
                SQL_CREATE_ENTRIES_TEST
        );

        db.execSQL(
                SQL_CREATE_ENTRIES_DATE
        );

        Log.d("debug", "onCreate(SQLiteDatabase db)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // アップデートの判別
        db.execSQL(
                SQL_DELETE_ENTRIES_TEST
        );

        db.execSQL(
                SQL_DELETE_ENTRIES_DATE
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
        db.insert(TEST_TABLE_NAME, null, cv);

        cv.clear();
        // 日時の保存
        cv.put("uuid", id);
        cv.put("updated1", "");
        cv.put("updated2", "");
        cv.put("updated3", "");
        db.insert(DATE_TABLE_NAME, null, cv);

    }

    public void saveData(String id, String title, String content ) {
        // データを更新
        SQLiteDatabase db = this.getWritableDatabase();

        //日時を取得
        final DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        final Date date = new Date(System.currentTimeMillis());
        String current_date = df.format(date);

        ContentValues cv = new ContentValues();
        cv.put("title", title);
        cv.put("content", content);
        cv.put("updated", current_date);
        String[] args = {id};
        db.update(TEST_TABLE_NAME, cv, "uuid=?", args);

        // 更新日の更新
        Cursor c = db.rawQuery("SELECT updated1, updated2" +
                                    " FROM " + DATE_TABLE_NAME +
                                     " WHERE uuid=?", args);

        cv.clear();
        c.moveToFirst();
        cv.put("updated1", current_date);
        cv.put("updated2", c.getString(0));
        cv.put("updated3", c.getString(1));
        db.update(DATE_TABLE_NAME, cv, "uuid=?", args);
        c.close();
    }

    public void deleteMemo(String uuid){
        // データを削除
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = {uuid};
        db.delete(TEST_TABLE_NAME, "uuid=?", args);
        db.delete(DATE_TABLE_NAME, "uuid=?", args);
    }
}
