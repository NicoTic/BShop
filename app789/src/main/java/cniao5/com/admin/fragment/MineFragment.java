package cniao5.com.admin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import cniao5.com.admin.LoginActivity;

import cniao5.com.admin.MyAddressActivity;
import cniao5.com.admin.MyOrderActivity;
import cniao5.com.admin.NewOrderActivity;
import cniao5.com.admin.bean.User;
import cniao5.com.admin.widget.CnToolbar;
import cniao5.com.cniao5shop.R;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * descriptiom:个人中心
 * Created by Administration
 */
public class MineFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "HeadlinesFragment";
    //我的
    private RelativeLayout myOrder_rl;
    private RelativeLayout myFavorite_rl;
    private RelativeLayout myAddress_rl;

    private CircleImageView circleImageView;
    private TextView userName;
    private LinearLayout detailLinear;

    private Button loginOutBtn;

    private User user;
    private String imgUrl = "http://img.my.csdn.net/uploads/201404/13/1397393290_5765.jpeg";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine,container,false);
        initView(view);
        initListener();
        return  view;
    }

    private void initView(View view) {
        circleImageView = (CircleImageView) view.findViewById(R.id.img_head);
        userName = (TextView) view.findViewById(R.id.txt_username);
        detailLinear = (LinearLayout) view.findViewById(R.id.detail_mine_layout);
        loginOutBtn = (Button) view.findViewById(R.id.btn_logout);
        user = new User();
        user.setHeadImg(imgUrl);

        myOrder_rl = (RelativeLayout) view.findViewById(R.id.my_order);
        myFavorite_rl = (RelativeLayout) view.findViewById(R.id.my_favorite);
        myAddress_rl = (RelativeLayout) view.findViewById(R.id.my_address);
    }

    private void initListener() {
        circleImageView.setOnClickListener(this);
        userName.setOnClickListener(this);
        loginOutBtn.setOnClickListener(this);

        myOrder_rl.setOnClickListener(this);
        myFavorite_rl.setOnClickListener(this);
        myAddress_rl.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_head:
                Intent loginIntent = new Intent(getContext(), LoginActivity.class);
                startActivityForResult(loginIntent, 1);
                break;
            case R.id.txt_username:
                break;
            case R.id.btn_logout:
                hideUser();
                break;
            case R.id.my_order:
                Intent orderIntent = new Intent();
                orderIntent.setClass(getActivity(), MyOrderActivity.class);
                startActivity(orderIntent);
                break;
            case R.id.my_favorite:
                Intent favoriteIntent = new Intent();
                favoriteIntent.setClass(getActivity(), NewOrderActivity.class);
                startActivity(favoriteIntent);
                break;
            case R.id.my_address:
                Intent addressIntent = new Intent();
                addressIntent.setClass(getActivity(), MyAddressActivity.class);
                startActivity(addressIntent);
                break;
        }
    }

    private void hideUser() {
        detailLinear.setVisibility(View.GONE);
        userName.setText("点击登录");
       circleImageView.setImageResource(R.drawable.default_head);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==0){
                    Bundle bundle = data.getExtras();
                    User user = (User) bundle.getSerializable("user");
                    Toast.makeText(getContext(),"Sucess!",Toast.LENGTH_SHORT).show();
                    showUser(user);
                }
        }

    }

    /**
     * 更新头像等信息
     * @param user
     */
    private void showUser(User user) {
        detailLinear.setVisibility(View.VISIBLE);
        userName.setText(user.getName());
        Glide.with(getContext()).load(imgUrl)
                .dontAnimate()
                .placeholder(R.drawable.default_head)
                .into(circleImageView);
    }

}
