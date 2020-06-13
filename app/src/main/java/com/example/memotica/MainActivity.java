package com.example.memotica;

        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Intent;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.AdapterView;
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
    }

    private void createNewMemo(){
        // 新しいidの作成
        String uuid = UUID.randomUUID().toString();
        id = uuid;
        helper = new TestOpenHelper(this);
        helper.createData(uuid);
        startIntent(uuid);
    }

    private void deleteData(String data_id){
        // データベースから指定のuuidのデータを削徐
        helper = new TestOpenHelper(this);
        helper.deleteMemo(data_id);
    }

    private void startIntent(String memo_id){
        // インテントの作成
        Intent intent = new Intent(this, CreateMemoActivity.class);
        intent.putExtra("id", memo_id);
        // メモのアクティビティを開始
        startActivity(intent);
    }

    @Override
    protected  void onResume() {
        super.onResume();
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
        c = db.rawQuery("SELECT uuid, content FROM testdb", null);
        // データをリストへ追加
        final ArrayList<String> id_list = new ArrayList<>();     // idのリスト
        final ArrayList<String> content = new ArrayList<>();     //内容のリスト
        boolean index = c.moveToFirst();
        while (index) {
            id_list.add(c.getString(0));
            content.add(c.getString(1));
            Log.i("debug", "c.getString = " + c.getString(0));
            index = c.moveToNext();
        }
        //　List_Viewにデータをセット
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, content);
        ListView list = findViewById(R.id.memo_list);
        list.setAdapter(adapter);

        //アイテムを編集
        list.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    id = id_list.get(i);
                    startIntent(id);
                }
            }
        );

        // 長押しでメモを削除
        list.setOnItemLongClickListener(
            new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    deleteData(id_list.get(i));
                    adapter.remove((String) ((TextView) view).getText());
                    return true;    // クリックの処理は発生させない
                }
            }
        );
    }
}
