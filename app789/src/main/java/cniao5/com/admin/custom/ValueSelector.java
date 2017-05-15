package cniao5.com.admin.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cniao5.com.cniao5shop.R;

/**
 * Created by Administration on 2017/4/20.
 */
public class ValueSelector extends LinearLayout {
    private Context mContext;
    private LayoutInflater mInflater;

    private TextView addTextView,plushTextView,valueTextView;
    //当前值
    private int currentValue;
    //最小值
    private int minValue = Integer.MIN_VALUE;
    //最大值
    private int maxValue = Integer.MAX_VALUE;
    //是否按下加减按钮
    private boolean addButtonIsPressed,plushButtonIsPressed;
    //监听器
    private  OnButtonClickListener mOnButtonClickListener;

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public ValueSelector(Context context) {
        this(context,null);
    }

    public ValueSelector(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ValueSelector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
    }

    private void initView(final Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.add_sub_number_layout,this,true);

        addTextView = (TextView) view.findViewById(R.id.add);
        plushTextView = (TextView) view.findViewById(R.id.plush);
        valueTextView = (TextView) view.findViewById(R.id.number);

        addTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                addNumber();

                if(mOnButtonClickListener!=null){
                    mOnButtonClickListener.onButtonAddClick(view,currentValue);
                }
            }
        });

        plushTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                subNumber();

                if(mOnButtonClickListener!=null){
                    mOnButtonClickListener.onButtonSubClick(view,currentValue);
                }
            }
        });
    }

    /**
     * 减少数值的方法
     */
    private void subNumber() {
       currentValue = getValue();
        if(currentValue>=minValue){
            currentValue--;
            if(currentValue<0){
                currentValue=0;
            }
           valueTextView.setText(String.valueOf(currentValue));
        }
    }

    /**
     * 增加数值的方法
     */
    private void addNumber() {
        currentValue = getValue();
        if(currentValue<maxValue){
           valueTextView.setText(String.valueOf(currentValue+1));
        }
    }

    /**
     * 获得中间的值
     * @return
     */
    public int getValue(){
        String val = valueTextView.getText().toString();

        if(val !=null && !"".equals(val))
            this.currentValue = Integer.parseInt(val);
        return currentValue;
    }

    /**
     * 设置中间的值
     * @param newValue
     */
    public void setValue(int newValue){
        currentValue = newValue;
        if(currentValue<minValue){
            currentValue = minValue;
        }else if(currentValue>maxValue){
            currentValue = maxValue;
        }
        valueTextView.setText(String.valueOf(currentValue));
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.mOnButtonClickListener = onButtonClickListener;
    }

    public interface  OnButtonClickListener{

        void onButtonAddClick(View view,int value);
        void onButtonSubClick(View view,int value);
    }
}
