package com.sampl.example.todolist;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * リスト一覧画面アクティビテイー
 * 画面定義；main.xml
 * @author 範行
 *
 */
public class Main extends Activity {

	static ArrayAdapter<String> adapter;
	static MyDBHelper helper;
	static SQLiteDatabase db;
	static ArrayList<Content> tasks;
	final static int END_CODE = 1;
	static ListView list;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		//DB接続
		helper = new MyDBHelper(this);
	    db = helper.getReadableDatabase();
	    Cursor c = db.query("todolist", new String[] { "_id", "name", "limit_date ", "is_finished"},
	            null, null, null, null, "_id DESC", null);

	    //ListView初期化
	    list = (ListView)findViewById(R.id.listView1);
	    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
	    tasks = new ArrayList<Content>();

	    //adapterにDBから文字列を追加
	    boolean isEof = c.moveToFirst();
	    while(isEof)
	    {
	    	Content t = new Content(Integer.parseInt(c.getString(0)), c.getString(1), c.getString(2), Integer.parseInt(c.getString(3)));
	    	tasks.add(t);
	    	adapter.add(c.getString(1));
	    	isEof = c.moveToNext();
	    }
	    c.close();

	    list.setAdapter(adapter);

	    //コンテキストメニューのためにリストビューを登録
	    registerForContextMenu(list);

	    //ボタンを登録
	    Button addButton = (Button)findViewById(R.id.button1);
	    ClickListener listener = new ClickListener();
	    addButton.setOnClickListener(listener);

	    //アイテムクリックリスナー
	    list.setOnItemClickListener(new AdapterView.OnItemClickListener()
	    {
	    	@Override
	    	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	    	{
	    		Intent intent = new Intent(getApplicationContext(), Edit.class);
	            Content content = tasks.get(position);

	            //intentにデータをつめる
	            intent.putExtra("DATA", content);
	            intent.putExtra("position", position);

	            //詳細Activityのスタート
	            startActivityForResult(intent, 0);
	          }
	    }
	    );
	}

	//詳細画面から戻ってきたときの処理
	@Override
	public void onActivityResult(int requestCode, int resCode, Intent it){

		switch(resCode)
		{
	        case Activity.RESULT_OK:

	        	//サブ画面からデータの受け取り
	        	Content t = (Content)it.getSerializableExtra("DATA");
	        	int pos = (Integer) it.getIntExtra("position", 0);

	        	//インスタンスの入れ替え
	        	String removeName = tasks.get(pos).getName();
	        	tasks.set(pos, t);

	        	//DB更新
	        	helper.onUpdate(db, t);

	        	//アダプタの書き換え
	        	adapter.remove(removeName);
	        	adapter.insert(t.getName(), pos);
	        	adapter.notifyDataSetChanged();
		}
	}

	// コンテキストメニュー
	/**
	 *
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, view, menuInfo);
		AdapterContextMenuInfo adapterinfo = (AdapterContextMenuInfo)menuInfo;
		ListView listView = (ListView)view;
		menu.setHeaderTitle((String)listView.getItemAtPosition(adapterinfo.position));
		menu.add(0, END_CODE, 0, R.string.context_delete);
	}

	//TODO コンテキストメニュークリック時のリスナ
	/**
	 *
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
	      switch(item.getItemId())
	      {
	      //削除
	      case END_CODE:
	      String removeName = tasks.get(info.position).getName();

	      //DBから削除
	      helper.onDelete(db, tasks.get(info.position));

	      //ArrayListから削除
	      tasks.remove(info.position);

	      //アダプタの書き換え
	      adapter.remove(removeName);
	      adapter.notifyDataSetChanged();
	        return true;

	      default:
	          Toast.makeText(this, "default", Toast.LENGTH_SHORT).show();
	        return super.onContextItemSelected(item);
	      }
	}

	    //ボタンクリック時のリスナ
	/**
	 *
	 * @author 範行
	 *
	 */
	class ClickListener implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			switch(v.getId())
	        {
			//addボタン
	        case R.id.button1:

	        	//テキストのインスタンスを取得
	          EditText editText = (EditText)findViewById(R.id.editText1);
	          String strText = editText.getText().toString();

	          if(strText.length() != 0)
	          {

	        	  //まずEditTextのテキストを削除
	            editText.setText("");

	            //DBに追加
	            db.execSQL(
	              "INSERT INTO todolist(_id, name, limit_date, is_finished) values("+
	              "(SELECT max(_id)+1 FROM todolist),"+
	              "'"+strText+"', '', 0);");

	            //DBに追加した内容を取得
	            Cursor c = db.query("todolist", new String[] { "_id", "name", "limit_date ", "is_finished"},
	                    null, null, null, null, "_id DESC", "0,1");
	            c.moveToFirst();

	            Content t = new Content(Integer.parseInt(c.getString(0)), c.getString(1), c.getString(2), Integer.parseInt(c.getString(3)));
	                tasks.add(0, t);

	            //リストに追加
	            ListView list = (ListView)findViewById(R.id.listView1);
	            adapter.insert(c.getString(1), 0);
	            list.setAdapter(adapter);

	          }
	        }
	      }
	    }


	    @Override
	    protected void onResume() {
	        super.onResume();
	    }

	    @Override
	    protected void onPause() {
	      super.onPause();
	    }

	}