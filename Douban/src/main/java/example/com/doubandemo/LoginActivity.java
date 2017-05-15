package example.com.doubandemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import example.com.utils.EmptyEdit;
import example.com.utils.ToastUtils;

/**
 * Created by Administration on 2017/4/11.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private AppCompatEditText phoneEdit,passwEdit;
    private Button loginButton;
    private AppCompatTextView forgetPassText,toRegistText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_layout);
        initBmob();
        initView();
        initListener();
    }

    private void initBmob() {
        Bmob.initialize(this,"9710f3e08159a447f9dfca5360f51380");
    }

    private void initListener() {
        loginButton.setOnClickListener(this);
        forgetPassText.setOnClickListener(this);
        toRegistText.setOnClickListener(this);

    }

    private void initView() {
        phoneEdit = (AppCompatEditText) findViewById(R.id.login_phone_Edit);
        passwEdit = (AppCompatEditText) findViewById(R.id.login_password_Edit);
        loginButton = (Button) findViewById(R.id.login_button);
        forgetPassText = (AppCompatTextView) findViewById(R.id.to_forget_PasswText);
        toRegistText = (AppCompatTextView) findViewById(R.id.to_regist_Text);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_button:

                login();
                break;
            case R.id.to_forget_PasswText:
                break;
            case R.id.to_regist_Text:
                Intent registIntent = new Intent();
                registIntent.setClass(LoginActivity.this,RegistActivity.class);
                startActivity(registIntent);
                break;
        }
    }

    private void login() {
        // 获取用户输入的用户名和密码
        String username = phoneEdit.getText().toString();
        String password = passwEdit.getText().toString();
        // 非空验证
        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            ToastUtils.showShort(LoginActivity.this, "用户名或密码不能为空!");
        }
            // 使用BmobSDK提供的登录功能
            final BmobUser user = new BmobUser();
            user.setUsername(username);
            user.setPassword(password);

            user.login(LoginActivity.this, new SaveListener() {
                @Override
                public void onSuccess() {
                    ToastUtils.showShort(LoginActivity.this,"登陆成功!");
                    Intent returnIntent = new Intent();
                    returnIntent.setClass(LoginActivity.this,MainActivity.class);

                    Bundle bundle2 = new Bundle();
                    String userName = user.getUsername();
                    bundle2.putString("username",userName);
                    Log.i("username",userName);
                    returnIntent.putExtras(bundle2);
                    setResult(0, returnIntent);
                    EmptyEdit.emptyEditText(phoneEdit,passwEdit);
                    finish();
                }

                @Override
                public void onFailure(int i, String s) {
                    ToastUtils.showShort(LoginActivity.this,s);
                }
            });
            return;
        }
    }
