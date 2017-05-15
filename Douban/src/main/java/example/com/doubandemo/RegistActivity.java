package example.com.doubandemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import example.com.utils.ToastUtils;

/**
 * Created by Administration on 2017/4/27.
 */
public class RegistActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText phoneEdit,passwEdit,nameEdit;
    private Button nextButton;
    private String imgUrl = "http://developer.android.com/images/home/aw_dac.png";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist_activity_layout);
        initView();
        initListener();
    }

    private void initListener() {
        nextButton.setOnClickListener(this);
    }
    private void initView() {
        phoneEdit = (EditText) findViewById(R.id.regist_phone_Edit);
        passwEdit = (EditText) findViewById(R.id.regist_password_Edit);
        nameEdit = (EditText) findViewById(R.id.regist_username_Edit);
        nextButton = (Button) findViewById(R.id.nextButton);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.nextButton:
                registData();
                break;
        }
    }

    private void registData() {
        String phoneNumber = phoneEdit.getText().toString().trim();
        String passWordNumber = passwEdit.getText().toString().trim();
        String userName = nameEdit.getText().toString().trim();
        if(TextUtils.isEmpty(phoneNumber)){
            ToastUtils.showShort(RegistActivity.this,"手机号不能为空");
        }else if(TextUtils.isEmpty(passWordNumber)){
            ToastUtils.showShort(RegistActivity.this,"密码不能为空");
        }else if (TextUtils.isEmpty(userName)){
            ToastUtils.showShort(RegistActivity.this,"昵称不能为空");
        }else{
            final BmobUser user = new BmobUser();
            user.setUsername(userName);
            user.setPassword(passWordNumber);
            user.setMobilePhoneNumber(phoneNumber);

            user.signUp(RegistActivity.this, new SaveListener() {
                @Override
                public void onSuccess() {
                   // ToastUtils.showShort(RegistActivity.this,"注册成功！");
                    Intent verifivateIntent = new Intent();
                    verifivateIntent.setClass(RegistActivity.this,SMSVertificationActivity.class);
                    verifivateIntent.putExtra("phone",user.getMobilePhoneNumber());
                    startActivity(verifivateIntent);
                }

                @Override
                public void onFailure(int i, String s) {
                    ToastUtils.showShort(RegistActivity.this, s);
                    Log.i("failed",s);
                }
            });
        }
    }
}

