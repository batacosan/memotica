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
            Cursor c, ct;

            //ヘルパーとデータベース生成
            helper = new TestOpenHelper(this);
            SQLiteDatabase db = helper.getWritableDatabase();

            //データベースからすべてのデータ取得
            c = db.rawQuery("SELECT uuid, title, content, updated FROM testdb", null);
            //取得したデータの位置が末尾かを表す変数宣言
            boolean index = c.moveToFirst();

            //データベースの内容をListItemオブジェクトに詰め替え
            final ArrayList<ListItem> data = new ArrayList<>();
            final ArrayList<String> id_list = new ArrayList<>();  //uuidのリスト
            while(index) {
                //uuidのリストを作成
                id_list.add(c.getString(0));
                ListItem item = new ListItem();
                item.setId((new Random()).nextLong());
                item.setTitle(c.getString(1));
                item.setContent(c.getString(2));

                //メモの更新日時を取得
                String[] args = {c.getString(0)};
                String   date_string;
                ct = db.rawQuery("SELECT updated1, updated2, updated3 FROM datedb where uuid=?", args);
                ct.moveToFirst();
                // メモの更新日時を表示用に整形
                date_string = ct.getString(0) + "\r\n" + ct.getString(1)
                                                            + "\r\n" + ct.getString(2);
                item.setUpdated(date_string);

                data.add(item);
                index = c.moveToNext();
                ct.moveToNext();
            }

            //　List_Viewにデータをセット
            final MyListAdapter adapter = new MyListAdapter(this, data, R.layout.list_item);
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
                        //データベースから削除対象のメモを削除
                        deleteData(id_list.get(i));
                        adapter.delete(i);
                        return true;    // クリックの処理は発生させない
                    }
                }
            );
    }
}
