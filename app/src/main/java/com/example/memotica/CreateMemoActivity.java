package com.example.memotica;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateMemoActivity extends AppCompatActivity {

    private TestOpenHelper helper;
    private EditText title_field;
    private EditText content_field;

    private String id;      //メモのid

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_memo);

        //intentからメモのid取得
        Intent intent = this.getIntent();
        id = intent.getStringExtra("id");

        //ヘルパーとデータベースを生成
        helper = new TestOpenHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        //データベースから指定のidのメモを取得
        String[] args = {id};
        Cursor c = (Cursor) db.rawQuery("SELECT title, content FROM testdb WHERE uuid=?", args);

        //新規のメモにテキストをセット
        c.moveToFirst();
        title_field = findViewById(R.id.edit_title);
        content_field = findViewById(R.id.edit_content);
        final String title = c.getString(0);
        title_field.setText(title);
        final String content = c.getString(1);
        content_field.setText(content);

        //メモの内容をデータベースへ保存しメイン画面へ
        Button insert_button = findViewById(R.id.button_insert);
        insert_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String title = title_field.getText().toString();
            String content = content_field.getText().toString();
            helper.saveData(id, title, content);

            finish();
            }
        });
    }
}
