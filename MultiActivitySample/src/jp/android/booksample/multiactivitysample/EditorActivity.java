package jp.android.booksample.multiactivitysample;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * �ڍוҏW�A�N�e�B�r�e�B�N���X
 * @author �͍s
 *
 */
public class EditorActivity extends Activity implements OnClickListener {

	/**
	 * �ҏW���̍���ID
	 */
	private long editorId;

	/**
	 * �ҏW���̍���ID
	 */
	private Memo memo;

	/**
	 * �ҏW���̍��ڂ̃|�W�V����
	 */
	private int position;

	/**
	 * �A�N�e�B�r�e�B�N�����ɌĂ΂��
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor);

		// �C�x���g�n���h���̐ݒ�
		Button operate_save = (Button) findViewById(R.id.operate_save);
		operate_save.setOnClickListener(this);

		// Extra���w�肳�ꂽ�ꍇ�A���̐ݒ�𔽉f����
		Intent intent = getIntent();

		memo = (Memo)intent.getSerializableExtra("DATA");
        position = intent.getIntExtra("position", 0);

        //�������ڂ̕ҏW���[�h
        if (memo != null)
		{
			EditText editor_title = (EditText) findViewById(R.id.editor_title);
			EditText editor_body = (EditText) findViewById(R.id.editor_body);

			// ID,�^�C�g���A�{����ݒ�
			editorId = memo.getId();
			editor_title.setText(memo.getTitle());
			editor_body.setText(memo.getBody());
		}
		//�V�K�ǉ����[�h
		else
		{
			editorId = -1;
		}
	}

	/**
	 * �N���b�N���̋���
	 */
	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		//�ۑ�
		case R.id.operate_save:
			EditText editor_title = (EditText) findViewById(R.id.editor_title);
			EditText editor_body = (EditText) findViewById(R.id.editor_body);

			//�^�C�g����������
			if(editor_title.getText() == null || editor_title.getText().toString().trim().length() == 0)
			{
				MainActivity.showDialog(this, "�x��", "�\�肪�����͂ł�");
			}

			// ���ʂ�ʒm����C���e���g
			Intent it = new Intent();

			//�������ڂ̕ҏW
			if (editorId != -1) {
				memo.setId((int) editorId);
			}

            //�������ڂ̕ҏW
			if(memo != null)
			{
	            memo.setTitle(editor_title.getText().toString());
	 			memo.setBody(editor_body.getText().toString());
			}
			//�V�K���ڂ̒ǉ�
			else
			{
				memo = new Memo(editorId,editor_title.getText().toString(),editor_body.getText().toString());
			}


 			//intent�Ƀf�[�^���l����
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