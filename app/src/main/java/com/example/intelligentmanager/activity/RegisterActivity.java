package com.example.intelligentmanager.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.intelligentmanager.R;
import com.example.intelligentmanager.db.MyDataBaseHelper;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 易水柔 on 2017/3/7.
 */
public class RegisterActivity extends Activity implements View.OnClickListener {
    private MyDataBaseHelper dbHelper;
    private int imageId;
    private Button button;
    private int flag=0;
    private String  edt_account;
    private String edt_password;
    private String edt_name;
    private String edt_motto;
    private String spin_gender;

    private EditText account;
    private EditText password;
    private EditText name;
    private Button register;
    private Button back;
    private EditText motto;
    private Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        account=(EditText)findViewById(R.id.reg_id_account);
        password=(EditText)findViewById(R.id.reg_id_password);
        name=(EditText)findViewById(R.id.reg_id_name);
        button=(Button)findViewById(R.id.reg_id_pickphoto);
        register=(Button)findViewById(R.id.reg_button_register);
        back=(Button)findViewById(R.id.reg_button_back);
        motto=(EditText)findViewById(R.id.reg_id_motto);
        spinner=(Spinner)findViewById(R.id.reg_id_gender);

        button.setOnClickListener(this);
        register.setOnClickListener(this);
        back.setOnClickListener(this);
        List<String> list = new ArrayList<String>();
        list.add("男");
        list.add("女");
        dbHelper = new MyDataBaseHelper(this, "User.db", null, 1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        spin_gender="男";
                        break;
                    case 1:
                        spin_gender="女";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reg_button_register:
                edt_account=account.getText().toString().trim();
                edt_password=password.getText().toString().trim();
                edt_name=name.getText().toString().trim();
                edt_motto=motto.getText().toString().trim();
                //将数据插入数据库中
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Cursor curor=db.query("user",null,null,null,null,null,null);
                if(curor.moveToFirst()){
                    do{
                        String temp=curor.getString(curor.getColumnIndex("username"));
                        if(temp.equals(edt_account)){
                            flag++;
                        }
                    }while(curor.moveToNext());
                }
                if(flag==0){
                        ContentValues values = new ContentValues();
                        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(imageId)).getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        values.put("username",edt_account);
                        values.put("password",edt_password);
                        values.put("motto",edt_motto);
                        values.put("gender",spin_gender);
                        values.put("name",edt_name);
                        values.put("head",baos.toByteArray());
                        db.insertOrThrow("user",null,values);
                        values.clear();
                        values.put("username",edt_account);
                        values.put("record",0);
                        db.insert("ranker",null,values);
                        values.clear();
                        db.close();

                        Intent in=new Intent(RegisterActivity.this,LoginActivity.class);
                        startActivity(in);
                }else
                {
                    Toast.makeText(this,"您输入的账号已存在，请重新输入！",Toast.LENGTH_SHORT).show();
                    flag=0;
                }
                break;

            case R.id.reg_button_back:
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.reg_id_pickphoto:
                Intent intent1 = new Intent(RegisterActivity.this,HeadActivity.class);
                startActivityForResult(intent1, 0x11);//启动intent对应的Activity
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0x11 && resultCode==0x11){   //判断是否为处理结果
            Bundle bundle = data.getExtras();  //获取传递的数据包
            imageId = bundle.getInt("imageId");//获取头像的id
            ImageView iv = (ImageView) findViewById(R.id.imageView1);
            iv.setImageResource(imageId);
        }
    }
}
