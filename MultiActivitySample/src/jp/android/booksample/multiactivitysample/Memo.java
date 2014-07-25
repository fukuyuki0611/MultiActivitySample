package jp.android.booksample.multiactivitysample;

import java.io.Serializable;

/**
 *項目のデータクラス
 * @author 範行
 *
 */
public class Memo implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -126614933606455355L;

	private long id;//ID
	private String title;//タイトル
	private String body;//本文

	/**
	 * コンストラクタ
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
	 * 以下、getter及びsetterメソッド
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
