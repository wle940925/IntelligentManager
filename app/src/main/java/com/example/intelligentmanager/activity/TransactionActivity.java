package com.example.intelligentmanager.activity;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.intelligentmanager.R;
import com.example.intelligentmanager.bean.TransactionBean;
import com.example.intelligentmanager.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;


/**
 * Created by 易水柔 on 2017/3/16.
 */
public class TransactionActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {
    private Toolbar mToolbar;
    private static AlarmManager am;
    private static PendingIntent pi;
    private String calId;
    private Button button;
    private ListView listView;
    private TransactionBean transactionBean[];
    private android.app.AlertDialog alertDialog;
    private EditText change_title;
    private EditText change_strtime;
    private EditText change_endtime;
    private EditText change_content;
    private Button btn_change;
    private Button btn_cancel;
    private Spinner spinner;
    private int select_position;
    private int importance;

//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case SUCCESS:
//                    JSONAnalysis(msg.obj.toString());
//                    break;
//                case FAILURE:
//                    Toast.makeText(TransactionActivity.this, "获取数据失败", Toast.LENGTH_SHORT)
//                            .show();
//                    break;
//                case ERRORCODE:
//                    Toast.makeText(TransactionActivity.this, "获取的CODE码不为200！",
//                            Toast.LENGTH_SHORT).show();
//                    break;
//                default:
//                    break;
//            }
//        }
//    };

//    private void JSONAnalysis(String string) {
//        JSONObject object = null;
//        try {
//            object = new JSONObject(string);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        JSONObject ObjectInfo = object.optJSONObject("weatherinfo");
//        String city = ObjectInfo.optString("city");
//        String tem2 = ObjectInfo.optString("temp2");
//        String tem1 = ObjectInfo.optString("temp1");
//        String weather = ObjectInfo.optString("weather");
//        //String time = ObjectInfo.optString("ptime");
//        tem1 = tem1.trim();
//        tem2 = tem2.trim();
//        int temp1 = getnumber(tem1);
//        // int temp2=getnumber(tem2);
//        String notice;
//        if (temp1 < 10) {
//            notice = "天气寒冷，建议多穿衣服，防止感冒！";
//        } else if (temp1 >= 10 && temp1 <= 16) {
//            notice = "天气微凉，注意保暖！";
//        } else if (temp1 > 16 && temp1 <= 23) {
//            notice = "天气温和，美好的一天！";
//        } else {
//            notice = "天气炎热，注意防晒！";
//        }
//        text.setText("当前位置：" + city + "，天气情况：" + weather + ",温度：" + tem1 + "～" + tem2  + "," + notice);
//
//    }

//    private int getnumber(String str) {
//        int number = 0;
//        String t1 = "";
//        if (str != null && !"".equals(str)) {
//            for (int i = 0; i < str.length(); i++) {
//                if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
//                    t1 += str.charAt(i);
//                }
//            }
//        }
//        try {
//            number = Integer.parseInt(t1);
//        } catch (Exception e) {
//
//        }
//        return number;
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction);
        NotificationManager manager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        manager.cancel(1);
        mToolbar = (Toolbar) findViewById(R.id.transaction_toolbar);
//        sp = (Spinner) findViewById(R.id.transaction_spinner);
//        text = (TextView) findViewById(R.id.transaction_text);
        button = (Button) findViewById(R.id.transaction_add);
        listView = (ListView) findViewById(R.id.transaction_listview);
        queryTransaction();
        final MyTransactionAdapter adapter1 = new MyTransactionAdapter(this);
        listView.setAdapter(adapter1);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChangeDialog(position);
                select_position=position;
            }
        });
        listView.setOnItemLongClickListener((new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                DeleteDialog(position);
                return true;
            }
        }));

        mToolbar.setTitle("日程");
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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionActivity.this, AddTransactionActivity.class);
                startActivity(intent);
                finish();
            }
        });

//        List<String> list = new ArrayList<String>();
//        list.add("广州");
//        list.add("深圳");
//        list.add("上海");
//        list.add("北京");
//        //深圳101280601，，101280101广州
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        sp.setAdapter(adapter);
//        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                switch (position) {
//                    case 0:
//                        cityCode = "101280101";
//                        break;
//                    case 1:
//                        cityCode = "101280601";
//                        break;
//                    case 2:
//                        cityCode="101020100";
//                        break;
//                    case 3:
//                        cityCode="101010100";
//                        break;
//                }
//                getweather();
//            }

//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


        //String weatherJson = queryStringForGet(weatherUrl);


    }

    private void ChangeDialog(int position) {
        alertDialog = new android.app.AlertDialog.Builder(TransactionActivity.this).create();
        alertDialog.setView(new EditText(this));
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.schedule_change);
        change_title=(EditText)window.findViewById(R.id.change_title);
        change_strtime=(EditText)window.findViewById(R.id.change_beginTime);
        change_endtime=(EditText)window.findViewById(R.id.change_endTime);
        change_content=(EditText)window.findViewById(R.id.change_textContent);
        btn_change=(Button)window.findViewById(R.id.btn_change);
        btn_cancel=(Button)window.findViewById(R.id.btn_cancel);
        spinner=(Spinner)window.findViewById(R.id.change_spinner);

        change_title.setText(transactionBean[position].getTitle());
        change_strtime.setText(DateUtils.getLongToString(transactionBean[position].getDtstart()));
        change_endtime.setText(DateUtils.getLongToString(transactionBean[position].getDtend()));
        change_content.setText(transactionBean[position].getContent());

        change_strtime.setOnTouchListener(this);
        change_endtime.setOnTouchListener(this);
        btn_change.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);


        List<String> list = new ArrayList<String>();
        list.add("一般提醒");
        list.add("重要提醒");
        list.add("紧急提醒");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        importance = 0;
                        break;
                    case 1:
                        importance = 1;
                        break;
                    case 2:
                        importance = 2;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    private void DeleteDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TransactionActivity.this);
        builder.setMessage("确定删除文件？");
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DeleteItem(position);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    private void DeleteItem(int position){
        int rows = getContentResolver().delete(CalendarContract.Events.CONTENT_URI,
                CalendarContract.Events.TITLE + "=?", new String[]{transactionBean[position].getTitle()});
        queryTransaction();
        MyTransactionAdapter adapter = (MyTransactionAdapter) listView.getAdapter();
        adapter.notifyDataSetChanged();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void queryTransaction() {
        Cursor c = getContentResolver().query(CalendarContract.Events.CONTENT_URI, null, null, null, "dtstart asc");
        transactionBean = new TransactionBean[c.getCount()];
        int postion = 0;
        if (c.moveToFirst()) {
            do {
                String title = c.getString(c.getColumnIndex(CalendarContract.Events.TITLE));
                String dtstart = c.getString(c.getColumnIndex(CalendarContract.Events.DTSTART));
                String content=c.getString(c.getColumnIndex(CalendarContract.Events.DESCRIPTION));
                String dtend=c.getString(c.getColumnIndex(CalendarContract.Events.DTEND));
                Log.i("title", title);
                Log.i("time", dtstart);
                transactionBean[postion] = new TransactionBean();
                transactionBean[postion].setDtstart(Long.parseLong(dtstart));
                transactionBean[postion].setDtend(Long.parseLong(dtend));
                transactionBean[postion].setContent(content);
                transactionBean[postion].setTitle(title);
                transactionBean[postion].setTime(DateUtils.getDateToString(Long.parseLong(dtstart)));
                postion++;

            } while (c.moveToNext());
        }


    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.change_beginTime:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                    View view = View.inflate(this, R.layout.rundating, null);
                    final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
                    final TimePicker timePicker = (android.widget.TimePicker) view.findViewById(R.id.time_picker);
                    builder.setView(view);
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(System.currentTimeMillis());
                    datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH),
                            null);

                    timePicker.setIs24HourView(true);
                    timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
                    timePicker.setCurrentMinute(Calendar.MINUTE);
                    final int inType = change_strtime.getInputType();
                    change_strtime.setInputType(InputType.TYPE_NULL);
                    change_strtime.onTouchEvent(event);
                    change_strtime.setInputType(inType);
                    change_strtime.setSelection(change_strtime.getText().length());
                    builder.setTitle("选取起始时间");
                    builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            StringBuffer sb = new StringBuffer();
                            sb.append(String.format("%d-%02d-%02d",
                                    datePicker.getYear(),
                                    datePicker.getMonth() +1,
                                    datePicker.getDayOfMonth()));
                            sb.append("  ");
                            sb.append(timePicker.getCurrentHour())
                                    .append(":").append(timePicker.getCurrentMinute());

                            change_strtime.setText(sb);
                            dialog.cancel();
                        }
                    });
                    Dialog dialog = builder.create();
                    dialog.show();

                }
                break;
            case R.id.change_endTime:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                    View view = View.inflate(this, R.layout.rundating, null);
                    final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
                    final TimePicker timePicker = (android.widget.TimePicker) view.findViewById(R.id.time_picker);
                    builder.setView(view);
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(System.currentTimeMillis());
                    datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH),
                            null);

                    timePicker.setIs24HourView(true);
                    timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
                    timePicker.setCurrentMinute(Calendar.MINUTE);
                    final int inType = change_endtime.getInputType();
                    change_endtime.setInputType(InputType.TYPE_NULL);
                    change_endtime.onTouchEvent(event);
                    change_endtime.setInputType(inType);
                    change_endtime.setSelection(change_endtime.getText().length());

                    builder.setTitle("选取结束时间");
                    builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            StringBuffer sb = new StringBuffer();
                            sb.append(String.format("%d-%02d-%02d",
                                    datePicker.getYear(),
                                    datePicker.getMonth() +1,
                                    datePicker.getDayOfMonth()));
                            sb.append("  ");
                            sb.append(timePicker.getCurrentHour())
                                    .append(":").append(timePicker.getCurrentMinute());
                            change_endtime.setText(sb);
                            dialog.cancel();
                        }
                    });
                    Dialog dialog = builder.create();
                    dialog.show();
                }
                break;
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_change:
                ContentValues values=new ContentValues();
                Cursor userCursor = getContentResolver().query(
                        Uri.parse("content://com.android.calendar/calendars"), null,
                        null, null, null);
                if (userCursor.getCount() > 0) {
                    userCursor.moveToFirst();
                    calId = userCursor.getString(userCursor.getColumnIndex("_id"));

                }
                TimeZone tz = TimeZone.getDefault();//获取默认时区
                values.put(CalendarContract.Events.DTSTART, DateUtils.getStringToDate(change_strtime.getText().toString().trim()));
                values.put(CalendarContract.Events.DTEND, DateUtils.getStringToDate(change_endtime.getText().toString().trim()));
                values.put(CalendarContract.Events.TITLE, change_title.getText().toString().trim());
                values.put(CalendarContract.Events.DESCRIPTION, change_content.getText().toString().trim());
                values.put(CalendarContract.Events.CALENDAR_ID, calId);
                values.put(CalendarContract.Events.EVENT_TIMEZONE, tz.getID());

                Uri uri = getContentResolver().insert(CalendarContract.Events.CONTENT_URI, values);
                //插完日程之后必须再插入以下代码段才能实现提醒功能
                long id = Long.parseLong( uri.getLastPathSegment() );
                ContentValues evalues = new ContentValues();
                evalues.put( "event_id", id );
                //提前10分钟有提醒
                evalues.put( "minutes", 10 );
                getContentResolver().insert(Uri.parse("content://com.android.calendar/reminders"), evalues);

                Intent intent = new Intent("ELITOR_CLOCK");
                intent.putExtra("importance",importance);
                intent.putExtra("title",change_title.getText().toString().trim());
                intent.putExtra("content",change_content.getText().toString().trim());
                pi = PendingIntent.getService(TransactionActivity.this, UUID.randomUUID().hashCode(),intent,PendingIntent.FLAG_UPDATE_CURRENT);
                am = (AlarmManager)getSystemService(ALARM_SERVICE);
                long time = DateUtils.getStringToDate(change_strtime.getText().toString().trim())-600*1000;
                Log.e("triggle time : ",time+"");
                am.set(AlarmManager.RTC_WAKEUP,time,pi);
                Toast.makeText(TransactionActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                DeleteItem(select_position);
                alertDialog.dismiss();
                break;
            case R.id.btn_cancel:
                alertDialog.dismiss();
                break;
        }
    }

//    private void getweather() {
//        final String weatherUrl = "http://www.weather.com.cn/data/cityinfo/" + cityCode
//                + ".html";
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                HttpURLConnection connection = null;
//                try {
//                    URL url = new URL(weatherUrl);
//                    connection = (HttpURLConnection) url.openConnection();
//                    connection.setRequestMethod("GET");
//                    connection.setConnectTimeout(8000);
//                    connection.setReadTimeout(8000);
//                    int statusCode = connection.getResponseCode();
//                    if (statusCode == 200) {
//                        InputStream is = connection.getInputStream();
//                        String result = HttpUtils.readMyInputStream(is);
//                        Message msg = new Message();
//                        msg.obj = result;
//                        msg.what = SUCCESS;
//                        handler.sendMessage(msg);
//                    } else {
//                        Message msg = new Message();
//                        msg.what = ERRORCODE;
//                        handler.sendMessage(msg);
//                    }
//
//                } catch (Exception e) {
//                    Message msg = new Message();
//                    msg.what = FAILURE;
//                    handler.sendMessage(msg);
//                }
//            }
//        }).start();
//    }

    public class MyTransactionAdapter extends BaseAdapter {
        private LayoutInflater mInflater = null;

        public MyTransactionAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return transactionBean.length;
        }

        @Override
        public Object getItem(int position) {
            return transactionBean[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.text1, null);
                holder.title = (TextView) convertView.findViewById(R.id.transaction_title);
                holder.strtime = (TextView) convertView.findViewById(R.id.transaction_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.title.setText(transactionBean[position].getTitle());
            holder.strtime.setText(transactionBean[position].getTime());

            return convertView;
        }
    }

    static class ViewHolder {
        public TextView title;
        public TextView strtime;
    }
}
