package example.com.doubandemo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.VerifySMSCodeListener;
import example.com.utils.EmptyEdit;

/**
 * Created by Administration on 2017/4/28.
 */
public class SMSVertificationActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText phoneEditText,verficationEdit;
    private Button messageButton,complateButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_verification);
        initBmob();
        initView();
        initListener();

    }

    private void initBmob() {
        //初始化
        Bmob.initialize(this,"9710f3e08159a447f9dfca5360f51380");
    }

    private void initListener() {
        messageButton.setOnClickListener(this);
        complateButton.setOnClickListener(this);
    }

    private void initView() {
        phoneEditText = (EditText) findViewById(R.id.msm_phone_edit);
        verficationEdit = (EditText) findViewById(R.id.verfication_edit);
        messageButton = (Button) findViewById(R.id.message_btn);
        complateButton = (Button) findViewById(R.id.complate_btn);
        initPhone();
    }

    private void initPhone() {
        Intent intent = getIntent();
        String myPhoneNumber = intent.getStringExtra("phone");
        phoneEditText.setText(myPhoneNumber);
    }

    @Override
    public void onClick(View view) {
        final String phoneNumber = phoneEditText.getText().toString().trim();
        String passWord = verficationEdit.getText().toString().trim();
        switch (view.getId()){
            case R.id.message_btn:
                if(TextUtils.isEmpty(phoneNumber)){
                    Toast.makeText(this, "请输入手机号码！", Toast.LENGTH_SHORT).show();
                }else if(phoneNumber.length()!=11){
                    Toast.makeText(this, "请输入11位有效手机号码", Toast.LENGTH_SHORT).show();
                }else{
                    /**
                     * 进行获取验证码操作和倒计时一分钟操作
                     */
                    BmobSMS.requestSMSCode(this, phoneNumber, "短信模板", new RequestSMSCodeListener() {
                        @Override
                        public void done(Integer integer, BmobException e) {
                            if(e==null){
                                messageButton.setClickable(false);
                                messageButton.setBackgroundColor(Color.GRAY);
                                Toast.makeText(SMSVertificationActivity.this, "验证码发送成功，请尽快使用", Toast.LENGTH_SHORT).show();

                                new CountDownTimer(60000, 1000) {
                                    @Override
                                    public void onTick(long l) {
                                        messageButton.setBackgroundResource(R.drawable.button_shape_gray);
                                        messageButton.setText(l/1000 + "秒");
                                    }

                                    @Override
                                    public void onFinish() {
                                        messageButton.setClickable(true);
                                        messageButton.setBackgroundResource(R.drawable.button_shape);
                                        messageButton.setText("重新发送");
                                    }
                                }.start();
                            }else {
                                Toast.makeText(SMSVertificationActivity.this, "验证码发送失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                break;
            case R.id.complate_btn:
                if(TextUtils.isEmpty(phoneNumber)||TextUtils.isEmpty(passWord)||phoneNumber.length()!=11){
                    Log.e("MESSAGE:", "6");
                    Toast.makeText(this, "手机号或验证码输入不合法", Toast.LENGTH_SHORT).show();
                }
                else {
                    BmobSMS.verifySmsCode(this, phoneNumber, passWord, new VerifySMSCodeListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){//短信验证码已验证成功
                                Log.i("bmob", "验证通过");
                                EmptyEdit.emptyEditText(phoneEditText);
                                Intent toLoginIntent = new Intent();
                                toLoginIntent.setClass(SMSVertificationActivity.this,LoginActivity.class);
                                startActivity(toLoginIntent);
                                finish();
                                Toast.makeText(SMSVertificationActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            }else{
                                Log.i("bmob", "验证失败：code ="+e.getErrorCode()+",msg = "+e.getLocalizedMessage());
                                Toast.makeText(SMSVertificationActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                break;
        }
    }
}


    }
