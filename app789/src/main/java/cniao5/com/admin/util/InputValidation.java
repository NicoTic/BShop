package cniao5.com.admin.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Administration on 2017/4/26.
 */
public class InputValidation {
    private Context context;

    public InputValidation(Context context) {
        this.context = context;
    }

    public boolean isInputEditTextFilled(EditText inputEdit,String message){
        String value = inputEdit.getText().toString().trim();
        if(value.isEmpty()){
            hideKeybordFrom(inputEdit);
            Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }

    }

    public boolean isInputEditTextFilled(EditText inputEdit1,EditText inputEdit2,String message){
        String value1 = inputEdit1.getText().toString().trim();
        String value2 =  inputEdit2.getText().toString().trim();
        if(!value1.isEmpty()&&value2.isEmpty()){
            hideKeybordFrom(inputEdit2);
            Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void hideKeybordFrom(View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
