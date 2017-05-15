package cniao5.com.admin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cniao5.com.admin.bean.User;
import cniao5.com.admin.database.InfoBaseHelper;
import cniao5.com.admin.util.InputValidation;
import cniao5.com.cniao5shop.R;

/**
 * Created by Administration on 2017/4/27.
 */
public class RegistActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText nameEditText;
    private EditText phoneEditText;
    private EditText passwordEditText;
    private EditText confirmEditText;
    private Button registButton;
    private AppCompatTextView appCompatTextViewLoginLink;

    private InputValidation inputValidation;
    private InfoBaseHelper inforeHelper;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_layout);
        initView();
        initListener();
        initDatabase();
    }

    private void initDatabase() {
        inputValidation = new InputValidation(RegistActivity.this);
        inforeHelper = new InfoBaseHelper(RegistActivity.this);
        user = new User();
    }

    private void initListener() {
        registButton.setOnClickListener(this);
        appCompatTextViewLoginLink.setOnClickListener(this);
    }

    private void initView() {
        nameEditText = (EditText) findViewById(R.id.inputName);
        phoneEditText = (EditText) findViewById(R.id.inputPhone);
        passwordEditText = (EditText) findViewById(R.id.inputPassword);
        confirmEditText = (EditText) findViewById(R.id.confirmPassword);
        registButton = (Button) findViewById(R.id.btn_regist);
        appCompatTextViewLoginLink = (AppCompatTextView) findViewById(R.id.textViewLinkLogin);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_regist:
                postDataToSQLite();
                break;
            case R.id.textViewLinkLogin:
                finish();
                break;
        }
    }

    private void postDataToSQLite() {
        if(!inputValidation.isInputEditTextFilled(nameEditText,"姓名不能为空！")){
            return;
        }
        if(!inputValidation.isInputEditTextFilled(nameEditText,phoneEditText,"手机号码不能为空！")){
            return;
        }
        if(!inputValidation.isInputEditTextFilled(phoneEditText,passwordEditText,"密码不能为空!")){
            return;
        }
        if(!inputValidation.isInputEditTextFilled(passwordEditText,confirmEditText,"请输入确认密码！")){
            return;
        }
        if(!passwordEditText.getText().toString().trim().equals(confirmEditText.getText().toString().trim())){
            Toast.makeText(RegistActivity.this,"请确认密码是否输入正确！！",Toast.LENGTH_SHORT).show();
        }else{
            if(!inforeHelper.checkUser(phoneEditText.getText().toString().trim())){
                user.setName(nameEditText.getText().toString().trim());
                user.setPhoneNumber(phoneEditText.getText().toString().trim());
                user.setPassWord(passwordEditText.getText().toString().trim());

                inforeHelper.addUser(user);
                Toast.makeText(RegistActivity.this,"注册成功！",Toast.LENGTH_SHORT).show();
                emptyInputEditText();
            }else{
                Toast.makeText(RegistActivity.this,"账号已存在！",Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void emptyInputEditText() {
        nameEditText.setText(null);
        phoneEditText.setText(null);
        passwordEditText.setText(null);
        confirmEditText.setText(null);
    }
}
