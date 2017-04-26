package com.example.intelligentmanager.fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.intelligentmanager.R;
import com.example.intelligentmanager.db.MyDataBaseHelper;
import com.example.intelligentmanager.bean.SystemSendBean;

/**
 * Created by 易水柔 on 2017/3/20.
 */
public class SystemSendFragment extends Fragment {
   private ListView listView;
    private SystemSendBean systemSendBean[];
    private MyDataBaseHelper dbHelper;
    public SystemSendFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.system_send,null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView=(ListView) getActivity().findViewById(R.id.system_list);
        dbHelper=new MyDataBaseHelper(getActivity(),"User.db",null,1);
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor cursor=db.query("systemsend",null,null,null,null,null,null);

        systemSendBean=new SystemSendBean[cursor.getCount()];
        int position=0;
        if(cursor.moveToFirst()){
            do {
                String content = cursor.getString(cursor.getColumnIndex("mcontent"));
                String time = cursor.getString(cursor.getColumnIndex("time"));

                systemSendBean[position] = new SystemSendBean();
                systemSendBean[position].setContent(content);
                systemSendBean[position].setTime(time);
                position++;
            }while (cursor.moveToNext());
        }
        SystemListAdapter adapter=new SystemListAdapter(getActivity());
        listView.setAdapter(adapter);

        db.close();
    }
    public class SystemListAdapter extends BaseAdapter {
        private LayoutInflater mInflater = null;

        public SystemListAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return systemSendBean.length;
        }

        @Override
        public Object getItem(int position) {
            return systemSendBean[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder=null;
            if(convertView==null){
                holder=new ViewHolder();
                convertView=mInflater.inflate(R.layout.systemsend_items,null);
                holder.item_content=(TextView)convertView.findViewById(R.id.sys_item_content);
                holder.item_time=(TextView)convertView.findViewById(R.id.sys_item_time);
                convertView.setTag(holder);
            }else {
                holder=(ViewHolder) convertView.getTag();
            }
            holder.item_content.setText(systemSendBean[position].getContent());
            holder.item_time.setText(systemSendBean[position].getTime());
            return convertView;
        }
    }
    static class ViewHolder {
        public TextView item_content;
        public TextView item_time;
    }
}

