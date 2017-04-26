package com.example.intelligentmanager.activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.intelligentmanager.R;
import com.example.intelligentmanager.bean.CommunityBean;
import com.example.intelligentmanager.db.MyDataBaseHelper;

import java.util.Calendar;

/**
 * Created by 易水柔 on 2017/3/9.
 */
public class CommuncityMainActivity extends AppCompatActivity {
    private MyDataBaseHelper dbHelper;
    private Toolbar mToolbar;
    private CommunityBean[] communityBeens;

    private ListView communcity_listview;
    Calendar c = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.communcity_main);
        communcity_listview = (ListView) findViewById(R.id.lv_main);
        mToolbar=(Toolbar)findViewById(R.id.communcity_toolbar);

        mToolbar.setTitle("圈子");
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
        dbHelper = new MyDataBaseHelper(this, "User.db", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "select * from user inner join content on user.username = content.username order by content.id " +
                "desc";
        Cursor cursor = db.rawQuery(sql, null);
        communityBeens = new CommunityBean[cursor.getCount()];
        int postion = 0;
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String usernamer = cursor.getString(cursor.getColumnIndex("username"));
                byte[] headimg = cursor.getBlob(cursor.getColumnIndex("head"));
                String content = cursor.getString(cursor.getColumnIndex("mcontent"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                int gnumber = cursor.getInt(cursor.getColumnIndex("gnumber"));
                Bitmap headbmp = BitmapFactory.decodeByteArray(headimg, 0, headimg.length);
                communityBeens[postion] = new CommunityBean();
                communityBeens[postion].setName(name);
                communityBeens[postion].setUserName(usernamer);
                communityBeens[postion].setContent(content);
                communityBeens[postion].setImg(headbmp);
                communityBeens[postion].setTime(time);
                communityBeens[postion].setNumber(gnumber);
                communityBeens[postion].setGoodImg(getResources().getDrawable(R.drawable.ic_thank));
                postion++;
            } while (cursor.moveToNext());
        }
        db.close();
        MyCommuncityAdapter adapter = new MyCommuncityAdapter(this);
        communcity_listview.setAdapter(adapter);


    }

    public class MyCommuncityAdapter extends BaseAdapter {
        private LayoutInflater mInflater = null;

        public MyCommuncityAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return communityBeens.length;
        }

        @Override
        public Object getItem(int position) {
            return communityBeens[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.communcity_items, null);
                holder.item_header = (ImageView) convertView.findViewById(R.id.communcity_item_header);
                holder.item_username = (TextView) convertView.findViewById(R.id.communcity_item_name);
                holder.item_content = (TextView) convertView.findViewById(R.id.communcity_item_content);
                holder.item_time = (TextView) convertView.findViewById(R.id.communcity_item_time);
                holder.iv_share_name = (TextView) convertView.findViewById(R.id.tv_share_names);
                holder.communcity_item_praise = (ImageView) convertView.findViewById(R.id.communcity_item_praise);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.communcity_item_praise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                    Cursor c = db.query("content", null, "id=?", new String[]{Integer.toString(communityBeens.length
                            - position)}, null, null, null);
                    int gnumber = 0;
                    while (c.moveToNext()) {
                        gnumber = c.getInt(c.getColumnIndex("gnumber"));
                        gnumber++;
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("gnumber", gnumber);
                        db.update("content", contentValues, "id=?", new String[]{Integer.toString(communityBeens
                                .length - position)});
                        contentValues.clear();
                    }
                    db.close();
                    communityBeens[position].setGoodImg(getResources().getDrawable(R.drawable.ic_thanked));
                    holder.communcity_item_praise.setImageDrawable(communityBeens[position].getGoodImg());
                    holder.iv_share_name.setText("共" + gnumber + "个人点赞！");
                }
            });
            holder.item_header.setImageBitmap(communityBeens[position].getImg());
            holder.item_username.setText(communityBeens[position].getName());
            holder.item_content.setText(communityBeens[position].getContent());
            holder.item_time.setText(communityBeens[position].getTime());
            holder.communcity_item_praise.setImageDrawable(communityBeens[position].getGoodImg());
            holder.iv_share_name.setText("共" + communityBeens[position].getNumber() + "个人点赞！");
            return convertView;
        }
    }

    static class ViewHolder {
        public ImageView item_header;
        public ImageView communcity_item_praise;
        public TextView item_username;
        public TextView item_content;
        public TextView item_time;
        public TextView iv_share_name;

    }
}
