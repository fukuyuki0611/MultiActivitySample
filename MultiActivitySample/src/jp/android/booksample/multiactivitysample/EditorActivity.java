package jp.android.booksample.multiactivitysample;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * 詳細編集アクティビティクラス
 * @author 範行
 *
 */
public class EditorActivity extends Activity implements OnClickListener {

	/**
	 * 編集中の項目ID
	 */
	private long editorId;

	/**
	 * 編集中の項目ID
	 */
	private Memo memo;

	/**
	 * 編集中の項目のポジション
	 */
	private int position;

	/**
	 * アクティビティ起動時に呼ばれる
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor);

		// イベントハンドラの設定
		Button operate_save = (Button) findViewById(R.id.operate_save);
		operate_save.setOnClickListener(this);

		// Extraが指定された場合、その設定を反映する
		Intent intent = getIntent();

		memo = (Memo)intent.getSerializableExtra("DATA");
        position = intent.getIntExtra("position", 0);

        //既存項目の編集モード
        if (memo != null)
		{
			EditText editor_title = (EditText) findViewById(R.id.editor_title);
			EditText editor_body = (EditText) findViewById(R.id.editor_body);

			// ID,タイトル、本文を設定
			editorId = memo.getId();
			editor_title.setText(memo.getTitle());
			editor_body.setText(memo.getBody());
		}
		//新規追加モード
		else
		{
			editorId = -1;
		}
	}

	/**
	 * クリック時の挙動
	 */
	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		//保存
		case R.id.operate_save:
			EditText editor_title = (EditText) findViewById(R.id.editor_title);
			EditText editor_body = (EditText) findViewById(R.id.editor_body);

			//タイトルが未入力
			if(editor_title.getText() == null || editor_title.getText().toString().trim().length() == 0)
			{
				MainActivity.showDialog(this, "警告", "表題が未入力です");
			}

			// 結果を通知するインテント
			Intent it = new Intent();

			//既存項目の編集
			if (editorId != -1) {
				memo.setId((int) editorId);
			}

            //既存項目の編集
			if(memo != null)
			{
	            memo.setTitle(editor_title.getText().toString());
	 			memo.setBody(editor_body.getText().toString());
			}
			//新規項目の追加
			else
			{
				memo = new Memo(editorId,editor_title.getText().toString(),editor_body.getText().toString());
			}


 			//intentにデータを詰込む
            it.putExtra("DATA", memo);
            it.putExtra("position", position);

			setResult(RESULT_OK, it);

			finish();
			break;

		default:
			break;
		}
	}

}