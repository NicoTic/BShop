package example.com.utils;

import android.widget.EditText;

/**
 * Created by Administration on 2017/4/28.
 */
public class EmptyEdit {

    public static void emptyEditText(EditText phoneEdit) {
        phoneEdit.setText(null);
    }

    public static void emptyEditText(EditText phoneEdit,EditText passWord) {
        phoneEdit.setText(null);
        passWord.setText(null);
    }

    public static void emptyEditText(EditText phoneEdit,EditText passWord,EditText userName) {
        phoneEdit.setText(null);
        passWord.setText(null);
        userName.setText(null);
    }
}
