package com.example.intelligentmanager.activity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.intelligentmanager.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

/**
 * Created by 易水柔 on 2017/3/22.
 */
public class AddTransactionActivity extends AppCompatActivity implements View.OnTouchListener {
    private Toolbar mToolbar;
    private EditText title;
    private EditText strtime;
    private EditText endtime;
    private EditText content;
    private Button add;
    private Spinner spinner;

    private int stYear;
    private int stMonth;
    private int stDay;
    private int stHour;
    private int stMinute;
    private static AlarmManager am;
    private static PendingIntent pi;
    private int edYear = 0;
    private int edMonth = 0;
    private int edDay = 0;
    private int edHour = 0;
    private int edMinute = 0;

    private String calId = "";
    private int importance=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_items);
        mToolbar=(Toolbar)findViewById(R.id.addtransaction_toolbar);
        add=(Button)findViewById(R.id.btnRight);
        title=(EditText)findViewById(R.id.tv_title);
        strtime=(EditText)findViewById(R.id.tv_beginTime);
        endtime=(EditText)findViewById(R.id.tv_endTime);
        content=(EditText)findViewById(R.id.tv_textContent);
        spinner=(Spinner)findViewById(R.id.schedule_spinner);
        strtime.setOnTouchListener(this);
        endtime.setOnTouchListener(this);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String calanderRemiderURL = "";
                if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
                    calanderRemiderURL = "content://com.android.calendar/reminders";
                } else {
                    calanderRemiderURL = "content://calendar/reminders";
                }
                Cursor userCursor = getContentResolver().query(
                        Uri.parse("content://com.android.calendar/calendars"), null,
                        null, null, null);
                if (userCursor.getCount() > 0) {
                    userCursor.moveToFirst();
                    calId = userCursor.getString(userCursor.getColumnIndex("_id"));

                }
                long startMillis = 0;
                long endMillis = 0;
                Calendar beginTime = Calendar.getInstance();
                beginTime.set(stYear, stMonth, stDay, stHour, stMinute);
                startMillis = beginTime.getTimeInMillis();  //插入日历时要取毫秒计时
                Calendar endTime = Calendar.getInstance();
                endTime.set(edYear, edMonth, edDay, edHour, edMinute);
                endMillis = endTime.getTimeInMillis();

                ContentValues eValues = new ContentValues();  //插入事件
                TimeZone tz = TimeZone.getDefault();//获取默认时区

                //插入日程
                eValues.put(CalendarContract.Events.DTSTART, startMillis);
                eValues.put(CalendarContract.Events.DTEND, endMillis);
                eValues.put(CalendarContract.Events.TITLE, title.getText().toString().trim());
                eValues.put(CalendarContract.Events.DESCRIPTION, content.getText().toString().trim());
                eValues.put(CalendarContract.Events.CALENDAR_ID, calId);
                eValues.put(CalendarContract.Events.EVENT_TIMEZONE, tz.getID());
                if (ActivityCompat.checkSelfPermission(AddTransactionActivity.this, Manifest.permission.WRITE_CALENDAR) !=
                        PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Uri uri = getContentResolver().insert(CalendarContract.Events.CONTENT_URI, eValues);
                //插完日程之后必须再插入以下代码段才能实现提醒功能
                long id = Long.parseLong( uri.getLastPathSegment() );
                ContentValues values = new ContentValues();
                values.put( "event_id", id );
                //提前10分钟有提醒
                values.put( "minutes", 10 );
                getContentResolver().insert(Uri.parse(calanderRemiderURL), values);

                Intent intent = new Intent("ELITOR_CLOCK");
                Log.i("------》",importance+"");
                Log.i("title------》",title.getText().toString()+"");
                intent.putExtra("importance",importance);
                intent.putExtra("title",title.getText().toString().trim());
                intent.putExtra("content",content.getText().toString().trim());
                pi = PendingIntent.getService(AddTransactionActivity.this, UUID.randomUUID().hashCode(),intent,PendingIntent.FLAG_UPDATE_CURRENT);
                am = (AlarmManager)getSystemService(ALARM_SERVICE);
                long time = startMillis-600*1000;
                Log.e("triggle time : ",time+"");
                am.set(AlarmManager.RTC_WAKEUP,time,pi);

                Toast.makeText(AddTransactionActivity.this, "添加成功!!!", Toast.LENGTH_LONG).show();

            }
        });

        setSupportActionBar(mToolbar);
        mToolbar.setTitle("添加日程");
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
    public  static  void startAlarmThree(){
        am.cancel(pi);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.tv_beginTime:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                    final int inType = strtime.getInputType();
                    strtime.setInputType(InputType.TYPE_NULL);
                    strtime.onTouchEvent(event);
                    strtime.setInputType(inType);
                    strtime.setSelection(strtime.getText().length());
                    builder.setTitle("选取起始时间");
                    builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            StringBuffer sb = new StringBuffer();
                            stYear = datePicker.getYear();
                            stMonth = datePicker.getMonth() ;//注意，月份的下标是从0开始的
                            stDay = datePicker.getDayOfMonth();
                            stHour = timePicker.getCurrentHour();
                            stMinute = timePicker.getCurrentMinute();
                            sb.append(String.format("%d-%02d-%02d",
                                    stYear,
                                    stMonth+1,
                                    stDay));
                            sb.append("  ");
                            sb.append(stHour)
                                    .append(":").append(stMinute);

                            strtime.setText(sb);
                            dialog.cancel();
                        }
                    });
                    Dialog dialog = builder.create();
                    dialog.show();

                }
                break;
            case R.id.tv_endTime:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                    final int inType = endtime.getInputType();
                    endtime.setInputType(InputType.TYPE_NULL);
                    endtime.onTouchEvent(event);
                    endtime.setInputType(inType);
                    endtime.setSelection(endtime.getText().length());

                    builder.setTitle("选取结束时间");
                    builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            StringBuffer sb = new StringBuffer();
                            edYear = datePicker.getYear();
                            edMonth = datePicker.getMonth() ;
                            edDay = datePicker.getDayOfMonth();
                            edHour = timePicker.getCurrentHour();
                            edMinute = timePicker.getCurrentMinute();
                            sb.append(String.format("%d-%02d-%02d",
                                    edYear,
                                    edMonth+1,
                                    edDay));
                            sb.append("  ");
                            sb.append(edHour)
                                    .append(":").append(edMinute);

                            endtime.setText(sb);
                            dialog.cancel();
                        }
                    });
                    Dialog dialog = builder.create();
                    dialog.show();
                }
                break;
        }
        return true;
    }
}

