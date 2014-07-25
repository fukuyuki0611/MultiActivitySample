package jp.android.booksample.multiactivitysample;

import java.util.ArrayList;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

/**
 *メインのアクティビティクラス
 * @author 範行
 *
 */
public class MainActivity extends ListActivity {

	/**
	 * データベーステーブル名称
	 */
	public static final String TABLE_NAME = Messages.getString("MainActivity.0"); //$NON-NLS-1$

	/**
	 * データベーステーブルの「ID」を示すキー名
	 */
	public static final String COLUMN_ID = Messages.getString("MainActivity.1"); //$NON-NLS-1$

	/**
	 * データベーステーブルの「タイトル」を示すキー名
	 */
	public static final String COLUMN_TITLE = Messages.getString("MainActivity.2"); //$NON-NLS-1$

	/**
	 * データベーステーブルの「本文」を示すキー名
	 */
	public static final String COLUMN_BODY = Messages.getString("MainActivity.3"); //$NON-NLS-1$

	/**
	 * SQLのwhere条件(ID)
	 */
	private static final String SQL_WHERE_ID = COLUMN_ID + Messages.getString("MainActivity.4"); //$NON-NLS-1$

	/**
	 * 編集画面でデータの追加を行う際のリクエストのキー
	 */
	private static final int REQUEST_ADD = 1;

	/**
	 * 編集画面でデータの編集を行う際のリクエストのキー
	 */
	private static final int REQUEST_EDIT = 2;

	/**
	 * 項目のリスト
	 */
	private ArrayList<Memo> memosList;

	/**
	 * 削除のキー
	 */
	private final static int END_CODE = 3;

	/**
	 * 本画面のリストビュー
	 */
	private ListView list;

	/**
	 * アクティビティ起動時に呼ばれる
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//DB追加
		MemoDBOpenHelper helper = new MemoDBOpenHelper(this);
		SQLiteDatabase db = helper.getReadableDatabase();

		// データベースクエリの発行(id順)
		Cursor c = db.query(TABLE_NAME,  new String[] {COLUMN_ID, COLUMN_TITLE, COLUMN_BODY }, null, null, null, null, COLUMN_ID +Messages.getString("MainActivity.6")); //$NON-NLS-1$

		// 表示する値の用意
		String[] from = new String[] { COLUMN_TITLE, COLUMN_BODY };
		int[] to = new int[] { android.R.id.text1, android.R.id.text2 };
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_2, c, from, to, 0);

		setListAdapter(adapter);

		// memoListを初期化
		initMemoList(c);

	    //コンテキストメニューのためにリストビューを登録
		list = getListView();
	    registerForContextMenu(list);

	}

	/**
	 * memoListを初期化
	 * @param c
	 */
	private void initMemoList(Cursor c)
	{
		boolean isExist = c.moveToFirst();
		memosList = new ArrayList<Memo>();

		while(isExist)
	    {
	    	Memo  memo = new Memo(Long.parseLong(c.getString(0)), c.getString(1), c.getString(2));
	    	memosList.add(memo);
	    	isExist = c.moveToNext();
	    }
	}

	/**
	 *	編集画面から戻ってきたときの処理
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent it) {

		if (resultCode == RESULT_OK) {

			//DB接続
			MemoDBOpenHelper helper = new MemoDBOpenHelper(this);
			SQLiteDatabase db = helper.getWritableDatabase();

			// 追加するデータを用意する
			ContentValues values = new ContentValues();

			//編集画面からデータの受け取り
        	Memo memo = (Memo)it.getSerializableExtra(Messages.getString("MainActivity.7")); //$NON-NLS-1$
			values.put(COLUMN_TITLE, memo.getTitle());
			values.put(COLUMN_BODY, memo.getBody());


			switch (requestCode) {

			//新規項目の追加
			case REQUEST_ADD:

				// 挿入クエリの送信
				db.insert(TABLE_NAME, null, values);
				break;

			//既存項目の変更
			case REQUEST_EDIT:

				long id = memo.getId();

				// 更新クエリの送信
				String[] whereargs = new String[] { Long.toString(id) };
				db.update(TABLE_NAME, values, SQL_WHERE_ID, whereargs);
				break;


			default:
				break;
			}
			reloadCursor();
		}
		super.onActivityResult(requestCode, resultCode, it);
	}

	/**
	 * メニュー項目設定
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/**
	 * オプションメニューの項目を押下時の挙動を定義
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean result;

		switch (item.getItemId()) {

		// 「追加」
		case R.id.operate_additem:

			// 編集画面を表示する
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), EditorActivity.class);

			// アクティビティの表示(追加モード)
			startActivityForResult(intent, REQUEST_ADD);

			result = true;
			break;

		// 「削除」
		case R.id.operate_deleteitem:


			SimpleCursorAdapter adapter = (SimpleCursorAdapter) getListAdapter();

			// 項目があれば削除
			if (adapter.getCount() > 0) {

				// リスト上最後の項目のIDを取得する
				long id = adapter.getItemId(adapter.getCount() - 1);

				// データの削除処理
				MemoDBOpenHelper helper = new MemoDBOpenHelper(this);
				SQLiteDatabase db = helper.getWritableDatabase();

				String[] whereargs = new String[] { Long.toString(id) };
				db.delete(TABLE_NAME, SQL_WHERE_ID, whereargs);

				reloadCursor();
			}
			else
			{
				showDialog(this, Messages.getString("MainActivity.8"), Messages.getString("MainActivity.9")); //$NON-NLS-1$ //$NON-NLS-2$
			}
			result = true;
			break;

		default:
			result = super.onOptionsItemSelected(item);
			break;
		}

		return result;
	}


	/**
	 * リストビュークリック時のイベント
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		// 読み込み用データベースオブジェクトの取得
		MemoDBOpenHelper helper = new MemoDBOpenHelper(this);
		SQLiteDatabase db = helper.getReadableDatabase();

		String[] columns = new String[] { COLUMN_TITLE, COLUMN_BODY };
		String[] whereargs = new String[] { Long.toString(id) };

		// 値の取得
		Cursor c = db.query(TABLE_NAME, columns, SQL_WHERE_ID, whereargs, null,
				null, null);
		c.moveToFirst();

		// 編集画面を表示
		Intent intent = new Intent(getApplicationContext(), EditorActivity.class);
        Memo memo = memosList.get(position);

        //intentにデータをつめる
        intent.putExtra(Messages.getString("MainActivity.10"), memo); //$NON-NLS-1$
        intent.putExtra(Messages.getString("MainActivity.11"), position); //$NON-NLS-1$

        //詳細Activityのスタート(編集モード)
        startActivityForResult(intent, REQUEST_EDIT);

	}

	/**
	 * 再読込
	 */
	private void reloadCursor() {
		MemoDBOpenHelper helper = new MemoDBOpenHelper(this);
		SQLiteDatabase db = helper.getReadableDatabase();

		// データベースクエリの発行
		Cursor c = db.query(TABLE_NAME,  new String[] {COLUMN_ID, COLUMN_TITLE, COLUMN_BODY }, null, null, null, null, COLUMN_ID +Messages.getString("MainActivity.12")); //$NON-NLS-1$


		// カーソルの変更
		SimpleCursorAdapter adapter = (SimpleCursorAdapter) getListAdapter();
		adapter.swapCursor(c);

		//リスト初期化
		initMemoList(c);
	}

	/**
	 * ダイアログ表示
	 * @param context
	 * @param title
	 * @param text
	 */
	public static void showDialog(Context context, String title, String text) {
		AlertDialog.Builder ad = new AlertDialog.Builder(context);
		ad.setTitle(title);
		ad.setMessage(text);
		ad.setPositiveButton(Messages.getString("MainActivity.13"), null); //$NON-NLS-1$
		ad.show();
	}

	// コンテキストメニュー(長押し時に表示される)
	@Override
	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, view, menuInfo);
		menu.add(0, END_CODE, 0, R.string.context_delete);
	}


	// コンテキストメニュークリック時の挙動定義
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		switch(item.getItemId())
		{

		//DBから削除
		case END_CODE:

			//削除対象ID
			long id = memosList.get(info.position).getId();

			// データの削除処理
			MemoDBOpenHelper helper = new MemoDBOpenHelper(this);
			SQLiteDatabase db = helper.getWritableDatabase();

			String[] whereargs = new String[] { Long.toString(id) };
			db.delete(TABLE_NAME, SQL_WHERE_ID, whereargs);

			//再読み込み
			reloadCursor();

			return true;

		default:
			Toast.makeText(this, "default", Toast.LENGTH_SHORT).show();
			return super.onContextItemSelected(item);
		}
	}

}