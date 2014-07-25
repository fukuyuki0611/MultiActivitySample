package jp.android.booksample.multiactivitysample;

import java.io.Serializable;

/**
 *���ڂ̃f�[�^�N���X
 * @author �͍s
 *
 */
public class Memo implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -126614933606455355L;

	private long id;//ID
	private String title;//�^�C�g��
	private String body;//�{��

	/**
	 * �R���X�g���N�^
	 * @param title
	 * @param body
	 */
	public Memo(long id,String title,String body)
	{
		this.id = id;
		this.title = title;
		this.body = body;
	}

	/**
	 * �ȉ��Agetter�y��setter���\�b�h
	 */

	/**
	 *
	 * @return
	 */
	public long getId() {
		return id;
	}

	/**
	 *
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 *
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 *
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 *
	 * @return
	 */
	public String getBody() {
		return body;
	}

	/**
	 *
	 * @param body
	 */
	public void setBody(String body) {
		this.body = body;
	}

}
