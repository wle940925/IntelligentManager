package com.example.intelligentmanager.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.intelligentmanager.R;
import com.example.intelligentmanager.activity.HeadActivity;
import com.example.intelligentmanager.db.MyDataBaseHelper;

import java.io.ByteArrayOutputStream;

/**
 * Created by 易水柔 on 2017/3/20.
 */
public class PersonFragment extends Fragment {
    private MyDataBaseHelper dpHelper;
    private ImageView iv_personalInfo_photo;
    private EditText tv_personalInfo_nickname;
    private TextView tv_personalInfo_account;
    private EditText tv_personalInfo_motto;
    private EditText tv_personalInfo_gender;
    private TextView custon_name;
    private int imageId;
    private Button button;
    private String username;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_personal_info,null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dpHelper=new MyDataBaseHelper(getActivity(),"User.db",null,1);
        username=getArguments().getString("username");
        iv_personalInfo_photo=(ImageView)getActivity().findViewById(R.id.iv_personalInfo_photo);
        tv_personalInfo_account=(TextView) getActivity().findViewById(R.id.tv_personalInfo_account);
        tv_personalInfo_nickname=(EditText)getActivity().findViewById(R.id.tv_personalInfo_nickname);
        tv_personalInfo_motto=(EditText)getActivity().findViewById(R.id.tv_personalInfo_motto);
        tv_personalInfo_gender=(EditText)getActivity().findViewById(R.id.tv_personalInfo_gender);
        custon_name=(TextView) getActivity().findViewById(R.id.custom_name);
        SQLiteDatabase db=dpHelper.getReadableDatabase();
        Cursor c=db.query("user",null,"username=?",new String[]{username},null,null,null);
        if (c.moveToFirst()){
            do {
                String name= c.getString(c.getColumnIndex("name"));
                byte [] headimg=c.getBlob(c.getColumnIndex("head"));
                Bitmap headbmp= BitmapFactory.decodeByteArray(headimg,0,headimg.length);
                String gender=c.getString(c.getColumnIndex("gender"));
                String motto=c.getString(c.getColumnIndex("motto"));

                iv_personalInfo_photo.setImageBitmap(headbmp);
                tv_personalInfo_nickname.setText(name);
                tv_personalInfo_account.setText(username);
                tv_personalInfo_motto.setText(motto);
                tv_personalInfo_gender.setText(gender);
            }while (c.moveToNext());
        }
        iv_personalInfo_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),HeadActivity.class);
                startActivityForResult(intent, 0x11);//启动intent对应的Activity

            }
        });
        button=(Button)getActivity().findViewById(R.id.bt_personalinfo_change);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=tv_personalInfo_nickname.getText().toString().trim();
                String gender=tv_personalInfo_gender.getText().toString().trim();
                String motto=tv_personalInfo_motto.getText().toString().trim();
                BitmapDrawable bd=(BitmapDrawable)iv_personalInfo_photo.getDrawable();
                Log.i("tag", String.valueOf(bd));
                Bitmap bitmap=bd.getBitmap();
                //Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(imageId)).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                ContentValues values=new ContentValues();
                values.put("head",baos.toByteArray());
                values.put("name",name);
                values.put("gender",gender);
                values.put("motto",motto);
                SQLiteDatabase database=dpHelper.getWritableDatabase();
                database.update("user",values,"username=?",new String[]{username});
                database.close();
                values.clear();
                tv_personalInfo_nickname.setText(name);
                tv_personalInfo_gender.setText(gender);
                tv_personalInfo_motto.setText(motto);
                custon_name.setText(name);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==0x11 && resultCode==0x11){   //判断是否为处理结果
            Bundle bundle = data.getExtras();  //获取传递的数据包
            imageId = bundle.getInt("imageId");//获取头像的id
            iv_personalInfo_photo.setImageResource(imageId);
            ImageView iv=(ImageView)getActivity().findViewById(R.id.custom_pic);
            iv.setImageResource(imageId);
        }
    }
}
