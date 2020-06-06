package com.example.memotica;

        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Intent;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.ListView;
        import android.widget.TextView;

        import java.util.ArrayList;
        import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private TestOpenHelper helper;
    private SQLiteDatabase db;

    //メモのUUID
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Cursor c;

        //ヘルパーとデータベース生成
        helper = new TestOpenHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        //新規作成ボタンが押されたときの処理
        Button new_button = findViewById(R.id.newmemo_button);
        new_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewMemo();
            }
        });

        //データベースからすべてのデータ取得
        c = db.rawQuery("SELECT content FROM testdb", null);
        // データをリストへ追加
        final ArrayList<String> content = new ArrayList<>();
        boolean index = c.moveToFirst();
        while(index) {
            content.add(c.getString(0));
            Log.d("debug", "c.getString = " + c.getString(0));
            index = c.moveToNext();
        }
        //　List_Viewにデータをセット
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, content);
        ListView list = findViewById(R.id.memo_list);
        list.setAdapter(adapter);
    }

    private void createNewMemo(){
        // 新しいidの作成
        String uuid = UUID.randomUUID().toString();
        id = uuid;
        helper = new TestOpenHelper(this);
        helper.createDate(uuid);
        startIntent(uuid);
    }

    private void startIntent(String memo_id){
        // インテントの作成
        Intent intent = new Intent(this, CreateMemoActivity.class);
        intent.putExtra("id", memo_id);
        // メモのアクティビティを開始
        startActivity(intent);
    }
}
