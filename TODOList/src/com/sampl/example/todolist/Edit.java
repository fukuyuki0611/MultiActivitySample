package com.sampl.example.todolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;

/**
 * 編集画面アクティビティクラス
 * 画面定義:todo.xml
 * @author 範行
 *
 */
public class Edit extends Activity {

	static Content tasks;
    static int position;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        setTitle(R.string.edit_title);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);

        if(getIntent().getExtras() != null){
            Intent intent = getIntent();

            tasks = (Content)intent.getSerializableExtra("DATA");
            position = intent.getIntExtra("position", 0);

            //TODOタイトル
            EditText edit = (EditText)findViewById(R.id.todo_value);
            edit.setText(tasks.getName());

            //日付
            String strDate = tasks.getLimitDate();
            String[] dates = strDate.split("-");
            if(dates[0].toString() != "" && dates[1].toString() != "" && dates[2].toString() != ""){
                DatePicker datepicker = (DatePicker)findViewById(R.id.datePicker1);
                datepicker.init(Integer.valueOf(dates[0]).intValue(), Integer.valueOf(dates[1]).intValue(), Integer.valueOf(dates[2]).intValue(), (OnDateChangedListener) this);
            }

            //チェックボックス
            CheckBox cb = (CheckBox)findViewById(R.id.checkBox1);

            if(tasks.getIsFinished() == 1){
                cb.setChecked(true);
            }
        }

        //完了ボタン　クリックリスナ
        Button addButton = (Button)findViewById(R.id.button_done);
        ClickListener listener = new ClickListener();
        addButton.setOnClickListener(listener);
    }

    /**
     *
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     *
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * ボタンクリック時のリスナ
     * @author 範行
     *
     */
    class ClickListener implements OnClickListener
    {
    	@ Override
        public void onClick(View v){

    		switch(v.getId())
            {

            //doneボタンが押されたら、データをまとめて元のActivityへ
            case R.id.button_done:

            	Intent it = new Intent();

                EditText editText = (EditText)findViewById(R.id.todo_value);

                //名を更新
                tasks.setName(editText.getText().toString());

                //intentにデータをつめる
                it.putExtra("DATA", tasks);
                it.putExtra("position", position);

                setResult(RESULT_OK, it);
                finish();
            }
        }
    }

}
