package cniao5.com.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cniao5.com.admin.bean.User;
import cniao5.com.admin.database.InfoBaseHelper;
import cniao5.com.admin.fragment.MineFragment;
import cniao5.com.admin.util.InputValidation;
import cniao5.com.admin.widget.ClearEditText.ClearEditText;
import cniao5.com.admin.widget.CnToolbar;
import cniao5.com.cniao5shop.R;

/**
 * Created by Administration on 2017/4/26.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private CnToolbar cnToolbar;
    private ClearEditText phoneEdit;
    private ClearEditText passwordEdit;
    private Button loginButton;

    private AppCompatTextView textViewLinkRegist;

    private InputValidation inputValidation;
    private InfoBaseHelper infoBaseHelper;

    private User user;
    private List<User> userList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        initView();
        initToolbar();
        initListener();
        initDatabase();
    }

    private void initDatabase() {
        infoBaseHelper = new InfoBaseHelper(LoginActivity.this);
        inputValidation = new InputValidation(LoginActivity.this);
        user = new User();
        userList = new ArrayList<>();
    }

    private void initListener() {
        loginButton.setOnClickListener(this);
        textViewLinkRegist.setOnClickListener(this);
    }

    private void initView() {
        phoneEdit = (ClearEditText) findViewById(R.id.textPhone);
        passwordEdit = (ClearEditText) findViewById(R.id.textPassword);
        loginButton = (Button) findViewById(R.id.btn_login);
        textViewLinkRegist = (AppCompatTextView) findViewById(R.id.textViewLinkRegist);
    }

    private void initToolbar() {
        cnToolbar = (CnToolbar) findViewById(R.id.toolbar);
        cnToolbar.setTitle("用户登录");
        cnToolbar .setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                verifyFromSQLite();
                break;
            case R.id.textViewLinkRegist:
                Intent intentRegist = new Intent(getApplicationContext(),RegistActivity.class);
                startActivity(intentRegist);
                break;
        }
    }

    /**
     * 判断号码和密码是否输入，以及判断是否已经存在于数据库中
     */
    private void verifyFromSQLite() {
        if(!inputValidation.isInputEditTextFilled(phoneEdit,"号码输入不能为空！")){
            return;
        }if(!inputValidation.isInputEditTextFilled(phoneEdit,passwordEdit,"密码输入不能为空！")){
            return;
        }
        if(infoBaseHelper.checkUser(phoneEdit.getText().toString().trim(),passwordEdit.getText().toString().trim())){
//            Intent accountIntent = new Intent(getApplicationContext(),UserListActivity.class);
//            accountIntent.putExtra("PHONE",phoneEditText.getText().toString().trim());
//            emptyInputEditText();
//            startActivity(accountIntent);
            Intent toFragmentIntent = new Intent();
            toFragmentIntent.setClass(LoginActivity.this,MineFragment.class);
            Bundle bundle2 = new Bundle();
            User user = infoBaseHelper.getUser(phoneEdit.getText().toString().trim(),passwordEdit.getText().toString().trim());
            bundle2.putSerializable("user",user);
            toFragmentIntent.putExtras(bundle2);
            setResult(0, toFragmentIntent);
            emptyInputEditText();
            LoginActivity.this.finish();
        }
        else{
            Toast.makeText(LoginActivity.this,"错误的号码或者密码",Toast.LENGTH_SHORT).show();
        }
    }

    private void emptyInputEditText() {
        phoneEdit.setText(null);
        passwordEdit.setText(null);
    }
}
