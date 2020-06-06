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
        Cursor c = (Cursor) db.rawQuery("SELECT content FROM testdb WHERE uuid=?", args);

        //新規のメモにテキストをセット
        c.moveToFirst();
        EditText content_field = findViewById(R.id.edit_content);
        final String content = c.getString(0);
        content_field.setText(content);

        //メモの内容をデータベースへ保存しメイン画面へ
        Button insert_button = findViewById(R.id.button_insert);
        insert_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText content_field = findViewById(R.id.edit_content);

                String text = content_field.getText().toString();
                helper.saveData(id, content);

                finish();
            }
        });
    }



}
