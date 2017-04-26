package com.example.intelligentmanager.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.intelligentmanager.utils.DateUtils;
import com.example.intelligentmanager.R;
import com.example.intelligentmanager.db.MyDataBaseHelper;

/**
 * Created by 易水柔 on 2017/3/20.
 */
public class SystemSendActivity extends Activity {
    private EditText editText;
    private Button button;
    private Button button1;
    private String time;
    private String content;
    private MyDataBaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.systemsendactivity);
        editText=(EditText)findViewById(R.id.send_content);
        button=(Button)findViewById(R.id.send_button);
        button1=(Button)findViewById(R.id.sys_exit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time= DateUtils.getCurrentDate();
                content=editText.getText().toString().trim();
                ContentValues values=new ContentValues();
                values.put("time",time);
                values.put("mcontent",content);
                dbHelper=new MyDataBaseHelper(SystemSendActivity.this,"User.db",null,1);
                SQLiteDatabase db=dbHelper.getReadableDatabase();
                db.insert("systemsend",null,values);
                values.clear();
                db.close();
                Toast.makeText(SystemSendActivity.this, "推送成功", Toast.LENGTH_SHORT).show();

            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
