package com.example.intelligentmanager.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.intelligentmanager.R;
import com.example.intelligentmanager.bean.SportsBean;
import com.example.intelligentmanager.db.MyDataBaseHelper;
import com.example.intelligentmanager.utils.DateUtils;

import java.util.Calendar;

/**
 * Created by 易水柔 on 2017/3/9.
 */
public class SportsMainActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {
    private MyDataBaseHelper dbHelper;
    private TextView rank;
    private TextView username;
    private TextView stepcounters;
    private ImageView imageView;
    private Toolbar mToolbar;

    private ListView mystepview;
    private ListView steprankview;
    private String usname;
    private String name;
    private SportsBean sportsBean[];
    private int index = 0;
    private EditText startEt;
    private EditText plan;
    private EditText startplace;
    private Button btn;
    private AlertDialog alertDialog;
    private LayoutInflater mInflater = null;

    private BaseAdapter Adapter1;
    private BaseAdapter Adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sports_main);

        Bundle bundle = this.getIntent().getExtras();
        usname = bundle.getString("username");
        dbHelper = new MyDataBaseHelper(this, "User.db", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "select * from user inner join ranker on user.username = ranker.username order by ranker.record " +
                "desc";
        Cursor cursor = db.rawQuery(sql, null);
        sportsBean = new SportsBean[cursor.getCount()];
        int postion = 0;
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String usernamer = cursor.getString(cursor.getColumnIndex("username"));
                byte[] headimg = cursor.getBlob(cursor.getColumnIndex("head"));
                int record = cursor.getInt(cursor.getColumnIndex("record"));
                Bitmap headbmp = BitmapFactory.decodeByteArray(headimg, 0, headimg.length);
                if (usernamer.equals(usname)){
                    index=postion;
                }
                sportsBean[postion] = new SportsBean();
                sportsBean[postion].setImg(headbmp);
                sportsBean[postion].setName(name);
                sportsBean[postion].setUsername(usernamer);
                sportsBean[postion].setRecord(record);
                sportsBean[postion].setRanki(++postion);
            } while (cursor.moveToNext());
        }




        final Cursor c = db.query("user", null, "username=?", new String[]{usname}, null, null, null);
        while (c.moveToNext()) {
            name = c.getString(c.getColumnIndex("name"));
        }
        db.close();
        mToolbar=(Toolbar)findViewById(R.id.sports_toolbar);
        mystepview = (ListView) findViewById(R.id.mystep_listview);
        MySecAdapter adapter1 = new MySecAdapter(this);
        mystepview.setAdapter(adapter1);

        steprankview = (ListView) findViewById(R.id.steprank_listView);
        MyAdapter adapter = new MyAdapter(this);
        steprankview.setAdapter(adapter);
        registerForContextMenu(steprankview);

        mToolbar.setTitle("健身");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public class MySecAdapter extends BaseAdapter {
        private LayoutInflater mInflater = null;

        private MySecAdapter(Context context) {
            //根据context上下文加载布局，这里的是Demo17Activity本身，即this
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public Object getItem(int position) {
            return sportsBean[index];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.items, null);
                holder.rank = (TextView) convertView.findViewById(R.id.rangking);
                holder.username = (TextView) convertView.findViewById(R.id.friends_id);
                holder.stepcounters = (TextView) convertView.findViewById(R.id.step_counters);
                holder.imageView = (ImageView) convertView.findViewById(R.id.items_img);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.rank.setText(Integer.toString(sportsBean[index].getRanki()));
            holder.username.setText(sportsBean[index].getName());
            holder.stepcounters.setText(Integer.toString(sportsBean[index].getRecord()));
            holder.imageView.setImageBitmap(sportsBean[index].getImg());
            return convertView;
        }
    }

    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater = null;

        private MyAdapter(Context context) {
            //根据context上下文加载布局，这里的是Demo17Activity本身，即this
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return sportsBean.length;
        }

        @Override
        public Object getItem(int position) {
            return sportsBean[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.items, null);
                holder.imageView = (ImageView) convertView.findViewById(R.id.items_img);
                holder.rank = (TextView) convertView.findViewById(R.id.rangking);
                holder.username = (TextView) convertView.findViewById(R.id.friends_id);
                holder.stepcounters = (TextView) convertView.findViewById(R.id.step_counters);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.rank.setText(Integer.toString(sportsBean[position].getRanki()));
            holder.username.setText(sportsBean[position].getName());
            holder.stepcounters.setText(Integer.toString(sportsBean[position].getRecord()));
            holder.imageView.setImageBitmap(sportsBean[position].getImg());
            return convertView;
        }
    }


    static class ViewHolder {
        public ImageView imageView;
        public TextView username;
        public TextView rank;
        public TextView stepcounters;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(Menu.NONE, Menu.NONE, 1, "分享");
        menu.add(Menu.NONE, Menu.NONE, 2, "发起约跑");
        super.onCreateContextMenu(menu, v, menuInfo);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getOrder()) {
            case 1:
                String text = "我已经坚持运动超过了" + sportsBean[index].getRecord() + "天了！在圈子里排名第" +
                        sportsBean[index].getRanki() +
                        "名，快来给我点个赞吧！";
                ContentValues contentValues = new ContentValues();
                contentValues.put("mcontent", text);
                contentValues.put("username", usname);
                contentValues.put("gnumber", 0);
                contentValues.put("time", DateUtils.getCurrentDate());
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                db.insert("content", null, contentValues);
                contentValues.clear();
                db.close();
                Toast.makeText(SportsMainActivity.this, "" + "分享成功", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                showPopupWindow();
                break;
        }
        return true;
    }
    private void showPopupWindow() {
        alertDialog = new AlertDialog.Builder(SportsMainActivity.this).create();
        alertDialog.setView(new EditText(this));
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.rundating_main);
        startEt = (EditText) window.findViewById(R.id.etStartTime);
        plan = (EditText) window.findViewById(R.id.runplan);
        startplace = (EditText) window.findViewById(R.id.startplace);
        btn = (Button) window.findViewById(R.id.dating_yes);
        startEt.setOnTouchListener(this);
        btn.setOnClickListener(this);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = View.inflate(this, R.layout.rundating, null);
            final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
            final TimePicker timePicker = (android.widget.TimePicker) view.findViewById(R.id.time_picker);
            builder.setView(view);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);

            timePicker.setIs24HourView(true);
            timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
            timePicker.setCurrentMinute(Calendar.MINUTE);

            if (v.getId() == R.id.etStartTime) {
                final int inType = startEt.getInputType();
                startEt.setInputType(InputType.TYPE_NULL);
                startEt.onTouchEvent(event);
                startEt.setInputType(inType);
                startEt.setSelection(startEt.getText().length());

                builder.setTitle("选取起始时间");
                builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        StringBuffer sb = new StringBuffer();
                        sb.append(String.format("%d-%02d-%02d",
                                datePicker.getYear(),
                                datePicker.getMonth() + 1,
                                datePicker.getDayOfMonth()));
                        sb.append("  ");
                        sb.append(timePicker.getCurrentHour())
                                .append(":").append(timePicker.getCurrentMinute());

                        startEt.setText(sb);
                        dialog.cancel();
                    }
                });
                Dialog dialog = builder.create();
                dialog.show();
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", usname);
        contentValues.put("time", DateUtils.getCurrentDate());
        contentValues.put("mcontent", name + "发起约跑计划：" + plan.getText().toString() + "，集合地点" + startplace.getText()
                .toString()
                + "，集合时间：" + startEt.getText().toString() + ",快来响应吧！");
        contentValues.put("gnumber", 0);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.insert("content", null, contentValues);
        Toast.makeText(SportsMainActivity.this, "" + "发起约跑", Toast.LENGTH_SHORT).show();
        contentValues.clear();
        db.close();
        alertDialog.dismiss();


    }
}

