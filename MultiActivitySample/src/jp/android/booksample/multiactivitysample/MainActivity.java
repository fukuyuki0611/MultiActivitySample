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
 *���C���̃A�N�e�B�r�e�B�N���X
 * @author �͍s
 *
 */
public class MainActivity extends ListActivity {

	/**
	 * �f�[�^�x�[�X�e�[�u������
	 */
	public static final String TABLE_NAME = Messages.getString("MainActivity.0"); //$NON-NLS-1$

	/**
	 * �f�[�^�x�[�X�e�[�u���́uID�v�������L�[��
	 */
	public static final String COLUMN_ID = Messages.getString("MainActivity.1"); //$NON-NLS-1$

	/**
	 * �f�[�^�x�[�X�e�[�u���́u�^�C�g���v�������L�[��
	 */
	public static final String COLUMN_TITLE = Messages.getString("MainActivity.2"); //$NON-NLS-1$

	/**
	 * �f�[�^�x�[�X�e�[�u���́u�{���v�������L�[��
	 */
	public static final String COLUMN_BODY = Messages.getString("MainActivity.3"); //$NON-NLS-1$

	/**
	 * SQL��where����(ID)
	 */
	private static final String SQL_WHERE_ID = COLUMN_ID + Messages.getString("MainActivity.4"); //$NON-NLS-1$

	/**
	 * �ҏW��ʂŃf�[�^�̒ǉ����s���ۂ̃��N�G�X�g�̃L�[
	 */
	private static final int REQUEST_ADD = 1;

	/**
	 * �ҏW��ʂŃf�[�^�̕ҏW���s���ۂ̃��N�G�X�g�̃L�[
	 */
	private static final int REQUEST_EDIT = 2;

	/**
	 * ���ڂ̃��X�g
	 */
	private ArrayList<Memo> memosList;

	/**
	 * �폜�̃L�[
	 */
	private final static int END_CODE = 3;

	/**
	 * �{��ʂ̃��X�g�r���[
	 */
	private ListView list;

	/**
	 * �A�N�e�B�r�e�B�N�����ɌĂ΂��
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//DB�ǉ�
		MemoDBOpenHelper helper = new MemoDBOpenHelper(this);
		SQLiteDatabase db = helper.getReadableDatabase();

		// �f�[�^�x�[�X�N�G���̔��s(id��)
		Cursor c = db.query(TABLE_NAME,  new String[] {COLUMN_ID, COLUMN_TITLE, COLUMN_BODY }, null, null, null, null, COLUMN_ID +Messages.getString("MainActivity.6")); //$NON-NLS-1$

		// �\������l�̗p��
		String[] from = new String[] { COLUMN_TITLE, COLUMN_BODY };
		int[] to = new int[] { android.R.id.text1, android.R.id.text2 };
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_2, c, from, to, 0);

		setListAdapter(adapter);

		// memoList��������
		initMemoList(c);

	    //�R���e�L�X�g���j���[�̂��߂Ƀ��X�g�r���[��o�^
		list = getListView();
	    registerForContextMenu(list);

	}

	/**
	 * memoList��������
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
	 *	�ҏW��ʂ���߂��Ă����Ƃ��̏���
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent it) {

		if (resultCode == RESULT_OK) {

			//DB�ڑ�
			MemoDBOpenHelper helper = new MemoDBOpenHelper(this);
			SQLiteDatabase db = helper.getWritableDatabase();

			// �ǉ�����f�[�^��p�ӂ���
			ContentValues values = new ContentValues();

			//�ҏW��ʂ���f�[�^�̎󂯎��
        	Memo memo = (Memo)it.getSerializableExtra(Messages.getString("MainActivity.7")); //$NON-NLS-1$
			values.put(COLUMN_TITLE, memo.getTitle());
			values.put(COLUMN_BODY, memo.getBody());


			switch (requestCode) {

			//�V�K���ڂ̒ǉ�
			case REQUEST_ADD:

				// �}���N�G���̑��M
				db.insert(TABLE_NAME, null, values);
				break;

			//�������ڂ̕ύX
			case REQUEST_EDIT:

				long id = memo.getId();

				// �X�V�N�G���̑��M
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
	 * ���j���[���ڐݒ�
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/**
	 * �I�v�V�������j���[�̍��ڂ��������̋������`
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean result;

		switch (item.getItemId()) {

		// �u�ǉ��v
		case R.id.operate_additem:

			// �ҏW��ʂ�\������
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), EditorActivity.class);

			// �A�N�e�B�r�e�B�̕\��(�ǉ����[�h)
			startActivityForResult(intent, REQUEST_ADD);

			result = true;
			break;

		// �u�폜�v
		case R.id.operate_deleteitem:


			SimpleCursorAdapter adapter = (SimpleCursorAdapter) getListAdapter();

			// ���ڂ�����΍폜
			if (adapter.getCount() > 0) {

				// ���X�g��Ō�̍��ڂ�ID���擾����
				long id = adapter.getItemId(adapter.getCount() - 1);

				// �f�[�^�̍폜����
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
	 * ���X�g�r���[�N���b�N���̃C�x���g
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		// �ǂݍ��ݗp�f�[�^�x�[�X�I�u�W�F�N�g�̎擾
		MemoDBOpenHelper helper = new MemoDBOpenHelper(this);
		SQLiteDatabase db = helper.getReadableDatabase();

		String[] columns = new String[] { COLUMN_TITLE, COLUMN_BODY };
		String[] whereargs = new String[] { Long.toString(id) };

		// �l�̎擾
		Cursor c = db.query(TABLE_NAME, columns, SQL_WHERE_ID, whereargs, null,
				null, null);
		c.moveToFirst();

		// �ҏW��ʂ�\��
		Intent intent = new Intent(getApplicationContext(), EditorActivity.class);
        Memo memo = memosList.get(position);

        //intent�Ƀf�[�^���߂�
        intent.putExtra(Messages.getString("MainActivity.10"), memo); //$NON-NLS-1$
        intent.putExtra(Messages.getString("MainActivity.11"), position); //$NON-NLS-1$

        //�ڍ�Activity�̃X�^�[�g(�ҏW���[�h)
        startActivityForResult(intent, REQUEST_EDIT);

	}

	/**
	 * �ēǍ�
	 */
	private void reloadCursor() {
		MemoDBOpenHelper helper = new MemoDBOpenHelper(this);
		SQLiteDatabase db = helper.getReadableDatabase();

		// �f�[�^�x�[�X�N�G���̔��s
		Cursor c = db.query(TABLE_NAME,  new String[] {COLUMN_ID, COLUMN_TITLE, COLUMN_BODY }, null, null, null, null, COLUMN_ID +Messages.getString("MainActivity.12")); //$NON-NLS-1$


		// �J�[�\���̕ύX
		SimpleCursorAdapter adapter = (SimpleCursorAdapter) getListAdapter();
		adapter.swapCursor(c);

		//���X�g������
		initMemoList(c);
	}

	/**
	 * �_�C�A���O�\��
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

	// �R���e�L�X�g���j���[(���������ɕ\�������)
	@Override
	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, view, menuInfo);
		menu.add(0, END_CODE, 0, R.string.context_delete);
	}


	// �R���e�L�X�g���j���[�N���b�N���̋�����`
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		switch(item.getItemId())
		{

		//DB����폜
		case END_CODE:

			//�폜�Ώ�ID
			long id = memosList.get(info.position).getId();

			// �f�[�^�̍폜����
			MemoDBOpenHelper helper = new MemoDBOpenHelper(this);
			SQLiteDatabase db = helper.getWritableDatabase();

			String[] whereargs = new String[] { Long.toString(id) };
			db.delete(TABLE_NAME, SQL_WHERE_ID, whereargs);

			//�ēǂݍ���
			reloadCursor();

			return true;

		default:
			Toast.makeText(this, "default", Toast.LENGTH_SHORT).show();
			return super.onContextItemSelected(item);
		}
	}

}