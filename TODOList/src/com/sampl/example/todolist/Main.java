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
 * ���X�g�ꗗ��ʃA�N�e�B�r�e�C�[
 * ��ʒ�`�Gmain.xml
 * @author �͍s
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

		//DB�ڑ�
		helper = new MyDBHelper(this);
	    db = helper.getReadableDatabase();
	    Cursor c = db.query("todolist", new String[] { "_id", "name", "limit_date ", "is_finished"},
	            null, null, null, null, "_id DESC", null);

	    //ListView������
	    list = (ListView)findViewById(R.id.listView1);
	    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
	    tasks = new ArrayList<Content>();

	    //adapter��DB���當�����ǉ�
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

	    //�R���e�L�X�g���j���[�̂��߂Ƀ��X�g�r���[��o�^
	    registerForContextMenu(list);

	    //�{�^����o�^
	    Button addButton = (Button)findViewById(R.id.button1);
	    ClickListener listener = new ClickListener();
	    addButton.setOnClickListener(listener);

	    //�A�C�e���N���b�N���X�i�[
	    list.setOnItemClickListener(new AdapterView.OnItemClickListener()
	    {
	    	@Override
	    	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	    	{
	    		Intent intent = new Intent(getApplicationContext(), Edit.class);
	            Content content = tasks.get(position);

	            //intent�Ƀf�[�^���߂�
	            intent.putExtra("DATA", content);
	            intent.putExtra("position", position);

	            //�ڍ�Activity�̃X�^�[�g
	            startActivityForResult(intent, 0);
	          }
	    }
	    );
	}

	//�ڍ׉�ʂ���߂��Ă����Ƃ��̏���
	@Override
	public void onActivityResult(int requestCode, int resCode, Intent it){

		switch(resCode)
		{
	        case Activity.RESULT_OK:

	        	//�T�u��ʂ���f�[�^�̎󂯎��
	        	Content t = (Content)it.getSerializableExtra("DATA");
	        	int pos = (Integer) it.getIntExtra("position", 0);

	        	//�C���X�^���X�̓���ւ�
	        	String removeName = tasks.get(pos).getName();
	        	tasks.set(pos, t);

	        	//DB�X�V
	        	helper.onUpdate(db, t);

	        	//�A�_�v�^�̏�������
	        	adapter.remove(removeName);
	        	adapter.insert(t.getName(), pos);
	        	adapter.notifyDataSetChanged();
		}
	}

	// �R���e�L�X�g���j���[
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

	//TODO �R���e�L�X�g���j���[�N���b�N���̃��X�i
	/**
	 *
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
	      switch(item.getItemId())
	      {
	      //�폜
	      case END_CODE:
	      String removeName = tasks.get(info.position).getName();

	      //DB����폜
	      helper.onDelete(db, tasks.get(info.position));

	      //ArrayList����폜
	      tasks.remove(info.position);

	      //�A�_�v�^�̏�������
	      adapter.remove(removeName);
	      adapter.notifyDataSetChanged();
	        return true;

	      default:
	          Toast.makeText(this, "default", Toast.LENGTH_SHORT).show();
	        return super.onContextItemSelected(item);
	      }
	}

	    //�{�^���N���b�N���̃��X�i
	/**
	 *
	 * @author �͍s
	 *
	 */
	class ClickListener implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			switch(v.getId())
	        {
			//add�{�^��
	        case R.id.button1:

	        	//�e�L�X�g�̃C���X�^���X���擾
	          EditText editText = (EditText)findViewById(R.id.editText1);
	          String strText = editText.getText().toString();

	          if(strText.length() != 0)
	          {

	        	  //�܂�EditText�̃e�L�X�g���폜
	            editText.setText("");

	            //DB�ɒǉ�
	            db.execSQL(
	              "INSERT INTO todolist(_id, name, limit_date, is_finished) values("+
	              "(SELECT max(_id)+1 FROM todolist),"+
	              "'"+strText+"', '', 0);");

	            //DB�ɒǉ��������e���擾
	            Cursor c = db.query("todolist", new String[] { "_id", "name", "limit_date ", "is_finished"},
	                    null, null, null, null, "_id DESC", "0,1");
	            c.moveToFirst();

	            Content t = new Content(Integer.parseInt(c.getString(0)), c.getString(1), c.getString(2), Integer.parseInt(c.getString(3)));
	                tasks.add(0, t);

	            //���X�g�ɒǉ�
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