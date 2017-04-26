package com.example.intelligentmanager.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.intelligentmanager.R;
import com.example.intelligentmanager.db.MyDataBaseHelper;
import com.example.intelligentmanager.fragment.Home_fragment;
import com.example.intelligentmanager.fragment.PersonFragment;
import com.example.intelligentmanager.fragment.SystemSendFragment;
import com.example.intelligentmanager.service.SmsObserverService;
import com.example.intelligentmanager.service.StepCounterService;
import com.example.intelligentmanager.service.StepDetector;
import com.example.intelligentmanager.service.TurningService;

public class MainActivity extends AppCompatActivity {
    private MyDataBaseHelper dbHelper;
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView lvLeftMenu;
    private String[] lvs = {"首页", "信息管理","系统推送","注销"};
    private ArrayAdapter arrayAdapter;
    private int currentFragmentIndex = 0;
    private int total_step = 0;   //走的总步数
    private int record = 0;//打卡记录
    private Thread thread;  //定义线程对象
    private Intent service;
    private Intent service2;
    private ImageView pic;
    private TextView cu_name;
    private String username;
    private int flag=0;
    private Intent service3;

    Handler handler = new Handler() {// Handler对象用于更新当前步数,定时发送消息，调用方法查询数据用于显示？？？？？？？？？？
        //主要接受子线程发送的数据, 并用此数据配合主线程更新UI
        //Handler运行在主线程中(UI线程中), 它与子线程可以通过Message对象来传递数据,
        //Handler就承担着接受子线程传过来的(子线程用sendMessage()方法传递Message对象，(里面包含数据)
        //把这些消息放入主线程队列中，配合主线程进行更新UI。

        @Override                  //这个方法是从父类/接口 继承过来的，需要重写一次
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);        // 此处可以更新UI

            countStep();          //调用步数方法
            if (total_step > 10) {
                record++;
                total_step = 0;

            } else {
                record = 0;
                total_step = 0;
            }
            SQLiteDatabase db=dbHelper.getReadableDatabase();
            ContentValues contentValues=new ContentValues();
            contentValues.put("record",record);
            String selection = "username=?" ;
            String[] selectionArgs = new  String[]{ username };
            db.update("ranker",contentValues,selection,selectionArgs);
                contentValues.clear();
            stopService(service);
            StepDetector.CURRENT_SETP=0;
            startService(service);



        }
    };


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new MyDataBaseHelper(this, "User.db", null, 1);
        findViews(); //获取控件
        Bundle bundle=this.getIntent().getExtras();
        username = bundle.getString("username");
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selection = "username=?" ;
        String[] selectionArgs = new  String[]{ username };
        Cursor cursor=db.query("user", null, selection, selectionArgs, null, null, null, null);
        Cursor cursor1=db.query("ranker",null,selection,selectionArgs,null,null,null);
        while (cursor.moveToNext()) {
            String name= cursor.getString(cursor.getColumnIndex("name"));
            byte [] headimg=cursor.getBlob(cursor.getColumnIndex("head"));
            Bitmap headbmp= BitmapFactory.decodeByteArray(headimg,0,headimg.length);
            pic.setImageBitmap(headbmp);
            cu_name.setText(name);
        }
        while(cursor1.moveToNext()){
            record=cursor1.getInt(cursor1.getColumnIndex("record"));
        }
        db.close();
        service3=new Intent(MainActivity.this, TurningService.class);
        startService(service3);
        service2=new Intent(MainActivity.this, SmsObserverService.class);
        startService(service2);
        service = new Intent(MainActivity.this, StepCounterService.class);
        startService(service);
        if (thread == null) {
            thread = new Thread() {// 子线程用于监听当前步数的变化
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    super.run();
                    int temp = 0;
                    while (true) {
                        try {//24小时通知系统更新步数并上传record86400000
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        if (StepCounterService.FLAG) {
                            Message msg = new Message();
                            if (temp != StepDetector.CURRENT_SETP) {
                                temp = StepDetector.CURRENT_SETP;
                            }
                            handler.sendMessage(msg);// 通知主线程
                        } else {
                        }
                    }
                }
            };
            thread.start();
        }

        toolbar.setTitle("智慧小管家");//设置Toolbar标题
        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new Home_fragment();
        Bundle bundle1=new Bundle();
        bundle1.putString("username",username);
        fragment.setArguments(bundle1);
        ft.replace(R.id.fragment_layout, fragment);
        ft.commit();

        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }
        };

        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        //设置菜单列表
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lvs);
        lvLeftMenu.setAdapter(arrayAdapter);

        lvLeftMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (currentFragmentIndex != position) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    Fragment fragment = null;

                    switch (position) {
                        case 0:
                            fragment = new Home_fragment();
                            Bundle bundle1=new Bundle();
                            bundle1.putString("username",username);
                            fragment.setArguments(bundle1);
                            toolbar.setTitle("智慧小管家");
                            break;
                        case 1:
                            fragment=new PersonFragment();
                            Bundle bundle2=new Bundle();
                            bundle2.putString("username",username);
                            fragment.setArguments(bundle2);
                            toolbar.setTitle("信息管理");
                            break;
                        case 2:
                            toolbar.setTitle("系统推送");
                            fragment=new SystemSendFragment();
                            break;
                        case 3:
                            stopService(service);
                            stopService(service2);
                            stopService(service3);
                            System.exit(0);//系统退出
                            break;

                    }
                    ft.replace(R.id.fragment_layout, fragment);
                    ft.commit();
                    currentFragmentIndex = position;
                }
                mDrawerLayout.closeDrawers();
            }
        });
    }


    private void countStep() {

        if (StepDetector.CURRENT_SETP % 2 == 0) {
            total_step = StepDetector.CURRENT_SETP;
        } else {
            total_step = StepDetector.CURRENT_SETP + 1;
        }

        total_step = StepDetector.CURRENT_SETP;

    }


    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
        pic = (ImageView) findViewById(R.id.custom_pic);
        cu_name=(TextView)findViewById(R.id.custom_name);


//        int w = View.MeasureSpec.makeMeasureSpec(0,
//                View.MeasureSpec.UNSPECIFIED);
//        int h = View.MeasureSpec.makeMeasureSpec(0,
//                View.MeasureSpec.UNSPECIFIED);
//        pic.measure(w, h);
//        Log.d("-->",pic.getWidth()+"");
        lvLeftMenu = (ListView) findViewById(R.id.lv_left_menu);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(service);
        stopService(service2);
    }
}
