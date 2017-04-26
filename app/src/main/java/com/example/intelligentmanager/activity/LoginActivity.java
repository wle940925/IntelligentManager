package com.example.intelligentmanager.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.intelligentmanager.R;
import com.example.intelligentmanager.db.MyDataBaseHelper;

/**
 * Created by ASUS on 2017/3/7.
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    private MyDataBaseHelper dbHelper;
    private EditText account;
    private EditText password;
    private Button login;
    private CheckBox rememberAccount;
    private Button register;
    private String acc_text;
    private String pass_text;

    public static final String SP_INFOS = "SPDATA_LOGIN";
    public static final String USERID = "UserID";
    public static final String PASSWORD = "PassWord";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout1);
        dbHelper = new MyDataBaseHelper(this, "User.db", null, 1);
        account = (EditText) findViewById(R.id.id_account);
        password = (EditText) findViewById(R.id.id_password);
        login = (Button) findViewById(R.id.button_login);
        register = (Button) findViewById(R.id.button_register);
        rememberAccount = (CheckBox) this.findViewById(R.id.cb_autoLogin);
        checkIfRemember();

        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_login:
                acc_text = account.getText().toString().trim();
                pass_text = password.getText().toString().trim();
                if ("123".equals(acc_text) && "123".equals(pass_text)) { //管理员账号密码
                    Intent intent1 = new Intent(LoginActivity.this, SystemSendActivity.class);
                    startActivity(intent1);
                } else {
                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                    String selection = "username=?";
                    String[] selectionArgs = new String[]{acc_text};
                    Cursor cursor = db.query("user", null, selection, selectionArgs, null, null, null);
                    if (cursor.moveToFirst()) {
                        do {
                            String username = cursor.getString(cursor.getColumnIndex("username"));
                            String password = cursor.getString(cursor.getColumnIndex("password"));
                            if (username.equals(acc_text) && pass_text.equals(password)) {

                                if (rememberAccount.isChecked()) {
                                    String uid = this.account.getText().toString().trim(); // 获得输入的帐号
                                    String pwd = this.password.getText().toString().trim(); // 获得输入的密码
                                    rememberMe(uid, pwd); // 将用户的帐号与密码存入SharedPreferences
                                }
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("username", username);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        } while (cursor.moveToNext());
                    } else {
                        Toast.makeText(LoginActivity.this, "账户或密码错误，请重新输入", Toast.LENGTH_LONG).show();
                    }
                }

                break;
            case R.id.button_register:
                Intent intent2 = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent2);
                break;
        }
    }

    // 方法：从SharedPreferences中读取用户的帐号和密码
    public void checkIfRemember() {
        SharedPreferences sp = getSharedPreferences(SP_INFOS, MODE_PRIVATE);
        // 获得Preferences
        String uid = sp.getString(USERID, null); // 取Preferences中的帐号
        String pwd = sp.getString(PASSWORD, null); // 取Preferences中的密码
        if (uid != null && pwd != null) {
            this.account.setText(uid); // 给账号控件赋值
            this.password.setText(pwd); // 给密码控件赋值
            rememberAccount.setChecked(true);
        }
    }

    // 方法：将用户的账号和密码存入SharedPreferences
    public void rememberMe(String uid, String pwd) {
        SharedPreferences sp = getSharedPreferences(SP_INFOS, MODE_PRIVATE);
        // 获得Preferences
        SharedPreferences.Editor editor = sp.edit(); // 获得Editor
        editor.putString(USERID, uid); // 将用户的帐号存入Preferences
        editor.putString(PASSWORD, pwd); // 将密码存入Preferences
        editor.commit();
    }
}
