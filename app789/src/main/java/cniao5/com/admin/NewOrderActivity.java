package cniao5.com.admin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cniao5.com.admin.custom.AlipaySuccessView;
import cniao5.com.admin.fragment.CartFragment;
import cniao5.com.admin.fragment.HomeFragment;
import cniao5.com.admin.widget.CnToolbar;
import cniao5.com.cniao5shop.R;

/**
 * Created by Administration on 2017/5/3.
 */
public class NewOrderActivity extends AppCompatActivity{
    private Button returnButton;
    private CnToolbar payToolbar;
    private AlipaySuccessView alipaySuccessView;
    private int radius1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_order_layout);
        initView();
        initlistener();
        initToolbar();

    }

    private void initToolbar() {
        payToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initlistener() {
    }

    private void initView() {
        returnButton = (Button) findViewById(R.id.return_button);
        payToolbar = (CnToolbar) findViewById(R.id.pay_tool_bar);

        alipaySuccessView = (AlipaySuccessView) findViewById(R.id.successview);

        alipaySuccessView.addCircleAnimatorEndListner(new AlipaySuccessView.OnCircleFinishListener() {
            @Override
            public void onCircleDone() {
                returnButton.setText("支付成功！");
                alipaySuccessView.setPaintColor(ContextCompat.getColor(NewOrderActivity.this,R.color.colorPrimary));
            }
        });


        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alipaySuccessView.loadCircle(radius1 / 2);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(7000);
                            Intent intent=new Intent(NewOrderActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
//                Intent intent=new Intent(NewOrderActivity.this,MainActivity.class);
//                startActivity(intent);
//                finish();
            }
        });

    }

//    @Override
//    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.return_button:
//                Toast.makeText(getApplicationContext(),"支付成功",Toast.LENGTH_SHORT).show();
//                Intent intent=new Intent(this,MainActivity.class);
//                startActivity(intent);
//                finish();
//                break;
//        }
//    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            Log.e("measure ", ".......>  " + alipaySuccessView.getWidth() + "  " + alipaySuccessView.getHeight());
            //在布局中取长宽中较小的值作为圆的半径
            radius1 = Math.min(alipaySuccessView.getWidth(), alipaySuccessView.getHeight());

        }
    }
}
