package com.example.intelligentmanager.finance.View.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.intelligentmanager.R;
import com.google.repacked.apache.commons.lang3.ObjectUtils;
import com.google.repacked.kotlin.properties.NULL_VALUE;

/*开通支付宝同步功能，登录支付宝
*created by 聂敏萍 in 2017/03/16
 */

public class AddAlipayActivity extends AppCompatActivity {

    public  static final String SP_INFOS = "SPDATA_ALIPAY";
    public  static final String ALIPAYACCOUNT = "AlipayAccount";
    public  static final String ALIPAYPWD = "AlipayPwd";

    private ImageButton ib_alipayBack;
    private EditText edt_alipayAccount, edt_alipayPwd;
    private Button btn_syncAlipay;
    private CheckBox cb_autoLoginAlipay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alipay);

        ib_alipayBack = (ImageButton)this.findViewById(R.id.ib_alipayBack);
        ib_alipayBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(AddAlipayActivity.this,FinanceMainActivity.class);
                startActivity(intent1);
            }
        });

        edt_alipayAccount = (EditText)this.findViewById(R.id.edt_alipayAccout);
        edt_alipayPwd = (EditText)this.findViewById(R.id.edt_alipayPwd);

//        String alipay_account = edt_alipayAccount.getText().toString().trim();//获取输入的账号
//        String alipay_pwd = edt_alipayPwd.getText().toString().trim();//获取输入的密码
//        if(cb_autoLoginAlipay.isChecked()){   //登录成功则执行
//            rememberMe(alipay_account,alipay_pwd);
//        }
        cb_autoLoginAlipay = (CheckBox)this.findViewById(R.id.cb_autoLoginAlipay);
        checkIfRemember();

        //开通支付宝同步
        btn_syncAlipay = (Button)this.findViewById(R.id.btn_syncAlipay);

    }

    //从SharedPreferences中读取用户的支付宝账户和密码
    public  void checkIfRemember(){
        SharedPreferences sp = getSharedPreferences(SP_INFOS,MODE_PRIVATE);
        //获得Preferences
        String alipayAccount = sp.getString(ALIPAYACCOUNT,null);
        String alipayPwd = sp.getString(ALIPAYPWD, null);
        if(alipayAccount !=null&& alipayPwd !=null){
            this.edt_alipayAccount.setText(alipayAccount);
            this.edt_alipayPwd.setText(alipayPwd);
            cb_autoLoginAlipay.setChecked(true);
        }
    }

    //将用户的支付宝账号和密码存入sharedPreferences
    public void rememberMe(String alipayAccount,String alipayPwd){
        SharedPreferences sp  = getSharedPreferences(SP_INFOS,MODE_PRIVATE);
        //获得Preferences
        SharedPreferences.Editor editor = sp.edit();//实例化Editor
        editor.putString(ALIPAYACCOUNT,alipayAccount);
        editor.putString(ALIPAYPWD,alipayPwd);
        editor.commit();
    }
}
