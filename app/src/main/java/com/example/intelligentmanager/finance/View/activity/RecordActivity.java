package com.example.intelligentmanager.finance.View.activity;

import android.app.DatePickerDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.intelligentmanager.R;
import com.example.intelligentmanager.databinding.ActivityRecordBinding;
import com.example.intelligentmanager.finance.Model.bean.Bill;
import com.example.intelligentmanager.finance.Model.config.Global;
import com.example.intelligentmanager.finance.Model.db.BillTableDao;
import com.example.intelligentmanager.finance.View.adapter.ExpressionPagerAdapter;
import com.example.intelligentmanager.finance.ViewModel.MainPageViewModel;
import com.example.intelligentmanager.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class RecordActivity extends BaseActivity<MainPageViewModel,ActivityRecordBinding>  {
    private EditText finance_record_money,finance_remarks;
    private TextView showdate;
    private Button finance_record_save,finance_record_back;
    private int year;
    private int month;
    private int day;

    private List<String> list = new ArrayList<String>();
    private Spinner mySpinner;
    private ArrayAdapter<String> adapter;

    private String accountId,money,typeId,time,remarks;

    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setViewModel(new MainPageViewModel(this));
        setBinding(DataBindingUtil.<ActivityRecordBinding>setContentView(this, R.layout.activity_record));
        getBinding().setMainPageModel(getViewModel());
        initView();

        //获取输入金额
        finance_record_money = (EditText)this.findViewById(R.id.finance_record_money);

        //获取输入备注
        finance_remarks = (EditText)this.findViewById(R.id.finance_remarks) ;


        showdate = (TextView)this.findViewById(R.id.finance_showdate);

        //初始化Calendar日历对象
        Calendar mycalendar = Calendar.getInstance();

        year = mycalendar.get(Calendar.YEAR); //获取Calendar对象中的年
        month = mycalendar.get(Calendar.MONTH);//获取Calendar对象中的月
        day = mycalendar.get(Calendar.DAY_OF_MONTH);//获取Calendar对象中的日

        showdate.setText(year + "-" + (month + 1) + "-" + day); //显示当前的年月日

        //添加单击事件--设置日期
        showdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建DatePickerDialog对象
                DatePickerDialog dpd = new DatePickerDialog(RecordActivity.this, Datelistener, year, month, day);
                dpd.show();//显示DatePickerDialog组件
            }
        });

        mySpinner = (Spinner)this.findViewById(R.id.finance_account_spinner);
        //第一步：初始化list列表
        list.add("现金");
        list.add("饭卡");
        list.add("支付宝");
        list.add("微信钱包");
        list.add("银行卡");
        list.add("其他");
        //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
        adapter = new ArrayAdapter<String>(RecordActivity.this,android.R.layout.simple_spinner_item, list);
        //第三步：为适配器设置下拉列表下拉时的菜单样式。
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //第四步：将适配器添加到下拉列表上
        mySpinner.setAdapter(adapter);
        //第五步：为下拉列表设置某个Item被选中事件的响应
        mySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                accountId = adapter.getItem(arg2);
            }
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                arg0.setVisibility(View.VISIBLE);
            }
        });

        finance_record_save = (Button)this.findViewById(R.id.btn_finance_record_save);
        finance_record_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time = String.valueOf(DateUtils.getStringToLong(showdate.getText().toString()));
                Log.i("tag",time);
                money = finance_record_money.getText().toString().trim();
                remarks = finance_remarks.getText().toString().trim();
                typeId = Global.CURRENT_TYPE;
                Bill bill = new Bill(-1,accountId,money,typeId,time,"1",remarks,1);
//                BillTableDao.saveBill(bill);
                if(BillTableDao.saveBill(bill)){
                    Toast.makeText(getApplicationContext(),"保存成功",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(),"保存失败",Toast.LENGTH_LONG).show();
                }
            }
        });

        finance_record_back = (Button)this.findViewById(R.id.btn_finance_record_back);
        finance_record_back.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(RecordActivity.this,FinanceMainActivity.class);
//                startActivity(intent);
                finish();
            }
        });
    }
    private DatePickerDialog.OnDateSetListener Datelistener = new DatePickerDialog.OnDateSetListener() {
        /**params：view：该事件关联的组件
         * params：myyear：当前选择的年
         * params：monthOfYear：当前选择的月
         * params：dayOfMonth：当前选择的日
         */
        @Override
        public void onDateSet(DatePicker view, int myyear, int monthOfYear, int dayOfMonth) {

            //修改year、month、day的变量值，以便以后单击按钮时，DatePickerDialog上显示上一次修改后的值
            year = myyear;
            month = monthOfYear;
            day = dayOfMonth;
            //更新日期
            updateDate();

        }

        //当DatePickerDialog关闭时，更新日期显示
        private void updateDate() {
            //在TextView上显示日期
            showdate.setText( year + "-" + (month + 1) + "-" + day);
        }
    };
    private void initView() {
        //初始化toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("记一笔");
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        initTypeShow();
    }

    /*
    初始化viewpager+gridView
     */
    private void initTypeShow() {
        final List<View> views = new ArrayList<>();

        views.add(getViewModel().getGridChildView(1));
        views.add(getViewModel().getGridChildView(2));

        viewPager.setAdapter(new ExpressionPagerAdapter(views));

        viewPager.setOnPageChangeListener(getViewModel().getOnPagerChangeListener());
        getViewModel().setCurDial(0);
    }
}
