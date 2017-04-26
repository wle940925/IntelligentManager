package com.example.intelligentmanager.finance.View.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.intelligentmanager.R;
import com.example.intelligentmanager.db.MyDataBaseHelper;
import com.example.intelligentmanager.finance.Model.bean.Bill;
import com.example.intelligentmanager.finance.Model.db.BillTableDao;
import com.example.intelligentmanager.finance.MyApplication;
import com.example.intelligentmanager.finance.Utils.TimeUtils;

import java.util.List;

/*个人财务管理首页*/
public class FinanceMainActivity extends AppCompatActivity {
    private Button btn_finance_record;
    private Button btn_finance_bill;
    private Button btn_creditcard_add;
    private Button btn_sec_bill;
    private TextView monthly_income, monthly_expense, monthly_balance;
    private float toin = 0.0f;
    private float toout = 0.0f;
    private Toolbar mToolbar;
    private String username;
    private AlertDialog alertDialog;
    private MyDataBaseHelper dbHelper;


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_main);
        dbHelper = new MyDataBaseHelper(this, "User.db", null, 1);
        Bundle bundle=this.getIntent().getExtras();
        username=bundle.getString("username");
        initData();

        monthly_income = (TextView) this.findViewById(R.id.monthly__income);

        monthly_expense = (TextView) this.findViewById(R.id.monthly_expense);

        monthly_balance = (TextView) this.findViewById(R.id.text_detail_balance);
        mToolbar = (Toolbar) this.findViewById(R.id.finance_toolbar);
        mToolbar.setTitle("财务管理");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*记录一笔账单*/
        btn_finance_record = (Button) this.findViewById(R.id.btn_finance_record);
        btn_finance_record.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinanceMainActivity.this, RecordActivity.class);
                startActivity(intent);//启动另一activity
            }
        });

        /*查看账单统计情况及具体账单条目*/
        btn_finance_bill = (Button) this.findViewById(R.id.btn_finance_bill);
        btn_finance_bill.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinanceMainActivity.this, BillInfoActivity.class);
                startActivity(intent);//启动另一activity

            }
        });

          /*添加银行卡消费记录*/
        btn_creditcard_add = (Button) this.findViewById(R.id.btn_creditcard_add);
        btn_creditcard_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //doReadSMS();
            }
        });
        btn_sec_bill=(Button)findViewById(R.id.secret_bill);
        btn_sec_bill.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(FinanceMainActivity.this).create();
                alertDialog.setView(new EditText(FinanceMainActivity.this));
                alertDialog.show();
                Window window = alertDialog.getWindow();
                window.setContentView(R.layout.secret_bill_password);
                final EditText pass = (EditText) window.findViewById(R.id.edt_pass);
                Button btn_yes = (Button) window.findViewById(R.id.pass_btn_yes);
                Button btn_no=(Button)window.findViewById(R.id.pass_btn_cancel);
                btn_yes.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SQLiteDatabase db = dbHelper.getReadableDatabase();
                        Cursor c=db.query("user",null,"username=?",new String[]{username},null,null,null);
                        if (c.moveToNext()){
                            if(pass.getText().toString().trim().equals(c.getString(c.getColumnIndex("password")))){
                                alertDialog.dismiss();
                                Intent intent=new Intent(FinanceMainActivity.this,SecretBillActivity.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(getApplicationContext(),"密码输入错误",Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            }
                        }

                    }
                });
                btn_no.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
        monthly_income.setText(toin + "");
        monthly_expense.setText(toout + "");
        float month_totalin, month_tatalout, month_balance;
        month_totalin = toin;
        month_tatalout = toout;
        month_balance = month_totalin - month_tatalout;
        monthly_balance.setText(String.valueOf(month_balance));
    }

    private void initData() {
        toin = 0.0f;
        toout = 0.0f;
        BillTableDao billTableDao = new BillTableDao(MyApplication.getContext());
        List<Bill> bills = billTableDao.getBillList(TimeUtils.getFirstDayTimeOfMonth());//月账单
        for (Bill bill : bills) {
            if (bill.getTypeId().equals("7") || bill.getTypeId().equals("8") || bill.getTypeId().equals("9") || bill
                    .getTypeId().equals("10") ||
                    bill.getTypeId().equals("11") || bill.getTypeId().equals("12") || bill.getTypeId().equals("13")) {
                toin += Float.parseFloat(bill.getMoney());
            } else {
                toout += Float.parseFloat(bill.getMoney().trim());
            }
        }

    }


}

