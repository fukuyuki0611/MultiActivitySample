package com.sampl.example.todolist;

import java.io.Serializable;

/**
 * データクラス
 * @author 範行
 *
 */
public class Content implements Serializable {

	private static final long serialVersionUID = -403227781427280505L;

	  int id;
	  String name;
	  String limit_date;
	  int is_finished;

	  /**
	   *
	   * @param id
	   * @param name
	   * @param limit_date
	   * @param is_finished
	   */
	  Content(int id, String name, String limit_date, int is_finished){
	    this.id = id;
	    this.name = name;
	    this.limit_date = limit_date;
	    this.is_finished = is_finished;
	  }

	  /**
	   *
	   * @return
	   */
	  public int getId()
	  {
	    return this.id;
	  }

	  /**
	   *
	   * @return
	   */
	  public String getName()
	  {
	    return this.name;
	  }

	  /**
	   *
	   * @param name
	   */
	  public void setName(String name)
	  {
	    this.name = name;
	  }

	  /**
	   *
	   * @return
	   */
	  public String getLimitDate()
	  {
	    return this.limit_date;
	  }

	  /**
	   *
	   * @param date
	   */
	  public void setLimitDate(String date)
	  {
	    this.limit_date = date;
	  }

	  /**
	   *
	   * @return
	   */
	  public int getIsFinished()
	  {
	    return this.is_finished;
	  }

	  /**
	   *
	   * @param flg
	   */
	  public void setIsFinished(int flg)
	  {
	    this.is_finished = flg;
	  }
}
