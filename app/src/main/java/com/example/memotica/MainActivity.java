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
        import java.util.Random;
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

            //データベースからすべてのデータ取得
            c = db.rawQuery("SELECT uuid, title, content, updated FROM testdb", null);
            //取得したデータの位置が末尾かを表す変数宣言
            boolean index = c.moveToFirst();

            //データベースの内容をListItemオブジェクトに詰め替え
            ArrayList<ListItem> data = new ArrayList<>();
            while(index) {
                ListItem item = new ListItem();
                item.setId((new Random()).nextLong());
                item.setTitle(c.getString(1));
                item.setContent(c.getString(2));
                item.setUpdated(c.getString(3));
                Log.i("debug", "c.getString3 = " + c.getString(3));
                data.add(item);
                index = c.moveToNext();
            }

            //　List_Viewにデータをセット
            MyListAdapter adapter = new MyListAdapter(this, data, R.layout.list_item);
            ListView list = findViewById(R.id.memo_list);
            list.setAdapter(adapter);

            /*
            //データベースからすべてのデータ取得
            c = db.rawQuery("SELECT uuid, title, content, updated FROM testdb", null);
            // データをリストへ追加
            final ArrayList<String> id_list = new ArrayList<>();     // idのリスト
            final ArrayList<String> title   = new ArrayList<>();     // タイトルのリスト
            final ArrayList<String> content = new ArrayList<>();     //内容のリスト
            final ArrayList<String> updated = new ArrayList<>();     //内容のリスト
            boolean index = c.moveToFirst();
            */

            /*
            while (index) {
                id_list.add(c.getString(0));
                title.add(c.getString(1));
                content.add(c.getString(2));
                updated.add(c.getString(3));
                Log.i("debug", "c.getString0 = " + c.getString(0));
                Log.i("debug", "c.getString1 = " + c.getString(1));
                Log.i("debug", "c.getString2 = " + c.getString(2));
                index = c.moveToNext();
            }
            */

            /*
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
            */

    }
}
