package com.sampl.example.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *
 * @author �͍s
 *
 */
public class MyDBHelper extends SQLiteOpenHelper {

	 static Context con;

	 /**
	  * �R���X�g���N�^
	  * @param context
	  */
     public MyDBHelper(Context context){
       super(context, "todo", null, 1);
       con = context;
     }

     @Override
     public void onUpgrade(SQLiteDatabase db, int oldVersion, int nreVersion){

     }

     @Override
     public void onCreate(SQLiteDatabase db){

    	 //table create
       db.execSQL(
         "CREATE TABLE todolist("+
         "  _id INTEGER not null,"+
         "  name text not null,"+
         "  limit_date text,"+
         "  is_finished INTEGER not null DEFAULT 0"+
         ");");

       //Test Row insert
//       db.execSQL("INSERT INTO todolist(_id, name, limit_date, is_finished) values (1, 'hoge1', '', 0);");
//       db.execSQL("INSERT INTO todolist(_id, name, limit_date, is_finished) values (2, 'hoge2', '', 0);");
     }

     /**
      * �f�[�^�̍X�V
      * @param db
      * @param task
      */
     public void onUpdate(SQLiteDatabase db, Content task){

    	 db.execSQL(
         "UPDATE todolist SET "+
         "name='"+task.getName()+"', "+
         "limit_date='"+task.getLimitDate()+"', "+
         "is_finished="+task.getIsFinished()+
         " WHERE _id="+task.getId()+";");
     }

     /**
      *�f�[�^�̍폜
      * @param db
      * @param task
      */
     public void onDelete(SQLiteDatabase db, Content task){
       db.execSQL("DELETE FROM todolist WHERE _id="+task.getId());
     }
}
