package com.example.alipay.myreadingapplication.util;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.InputType;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.example.alipay.myreadingapplication.R;

import java.util.List;
import static android.content.DialogInterface.BUTTON_NEGATIVE;



/**
 * 提示信息的管理
 */

public class PromptManager {
    private static long mExitTime = 0;// 再按一次退出


    //dismiss dialog
    public static void dismissDialog(MaterialDialog mDialog) {
        try {
        if (null != mDialog && mDialog.isShowing()) {
                mDialog.dismiss();
           }
        }catch (Exception e){
                e.printStackTrace();
        }
    }
    /**
     *
     * @param mDialog
     */
    public static void animationDismissDialog(final MaterialDialog mDialog) {
        if (null != mDialog && mDialog.isShowing()) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(1f,0f);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float percent  = (float) animation.getAnimatedValue();
                    mDialog.getWindow().getDecorView().setAlpha(percent);
                }
            });
            valueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mDialog.dismiss();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            valueAnimator.setDuration(500).start();
        }
    }

    /**
     * 显示message的dailog
     *
     * @param fragmentActivity
     * @param title
     * @param message
     * @return
     */
    public static MaterialDialog showMessageDialog(final FragmentActivity fragmentActivity, String title, String message) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(fragmentActivity)
                .theme(Theme.LIGHT)
                .title(title)
                .content(Html.fromHtml(message))
                .positiveText(R.string.is_ensure)
                .cancelable(true);
        return builder.show();
    }

    /**
     * 显示带有按钮的Dialog
     *
     * @param fragmentActivity
     * @param title
     * @param message
     * @return
     */
    public static MaterialDialog showMessageWithBtnDialog(final Activity fragmentActivity, String title, String message, final OnDialogClickInvoke onDialogClickInvoke) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(fragmentActivity)
                .theme(Theme.LIGHT)
                .title(title)
                .content(message)
                .positiveText(R.string.is_ensure)
                .negativeText(R.string.is_cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        dialog.dismiss();
                        if (onDialogClickInvoke != null) {
                            onDialogClickInvoke.onPositiveInvoke();
                        }
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        dialog.dismiss();
                        onDialogClickInvoke.onNegativeInvoke();
                    }
                })
                .cancelable(true);
        try {
            return builder.show();
        } catch (Exception e) {
            return builder.build();
        }
    }


    /**
     * 显示加载滚动的dialog
     */
    public static MaterialDialog showIndeterminateProgressDialog(final FragmentActivity fragmentActivity, int resId, DialogInterface.OnCancelListener cancelListener) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(fragmentActivity)
                .theme(Theme.LIGHT)
                .content(resId)
                .cancelable(false)
                .progress(true, 0)
                .progressIndeterminateStyle(false)
                .cancelListener(cancelListener);
        return builder.show();
    }
    /**
     * 显示加载滚动的dialog
     */
    public static MaterialDialog showIndeterminateProgressDialog(final FragmentActivity fragmentActivity, String content, DialogInterface.OnCancelListener cancelListener) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(fragmentActivity)
                .theme(Theme.LIGHT)
                .content(content)
                .cancelable(false)
                .progress(true, 0)
                .progressIndeterminateStyle(false)
                .cancelListener(cancelListener);
        return builder.show();
    }

    /**
     * 显示加载滚动的显示加载滚动的dialog
     */
    public static MaterialDialog showIndeterminateProgressDialog(final FragmentActivity fragmentActivity, String message) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(fragmentActivity)
                .theme(Theme.LIGHT)
                .content(message)
                .progress(true, 0)
                .cancelable(false)
                .cancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                })
                .progressIndeterminateStyle(false);
        return builder.show();
    }
    /**
     * 显示加载滚动的dialog
     */
    public static MaterialDialog showIndeterminateProgressDialog(final Context context, String message) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context)
                .theme(Theme.LIGHT)
                .content(message)
                .progress(true, 0)
                .cancelable(false)
                .cancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                })
                .progressIndeterminateStyle(false);
        return builder.build();
    }

    /**
     * 显示加载滚动的显示加载滚动的dialog
     */
    public static MaterialDialog showIndeterminateProgressDialog(final Context context, String message, Theme theme) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context)
                .theme(theme)
                .content(message)
                .progress(true, 0)
                .cancelable(false)
                .cancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                })
                .progressIndeterminateStyle(false);
        return builder.build();
    }


    public static void showTriaiThreeBtn(final FragmentActivity activity, int resId, MaterialDialog.ButtonCallback callback) {
        new MaterialDialog.Builder(activity)
                .title(R.string.app_name)
                .theme(Theme.LIGHT)
                .content(resId)
                .positiveText("同意")//同意
                .negativeText("取消")//取消
                .cancelable(false)
                .callback(callback)
                .positiveText(R.string.is_ensure)
                .show();
    }


    /**
     * 标题栏中间弹出menu 点击回调
     */
    public interface PopMenuCallback<T> {
        void onPopMenuItemClick(List<T> list, int position);
    }

    public static void showChangeExpenseDetail(final Activity activity, String content, final PositiveHandle positiveHandle) {
        new MaterialDialog.Builder(activity)
                .title("")
                .content(content)
                .positiveText(R.string.is_ensure)
                .negativeText(R.string.is_cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        positiveHandle.clickSure();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        dialog.dismiss();
                    }

                })
                .show();
    }

    /**
     * 显示带有输入框的dialog
     *
     * @param activity
     * @param titleId
     * @param content
     * @param onSimpleDialogClickInvoke
     */
    public static void showEditTextDialog(FragmentActivity activity, int titleId, int content, int defaultResid, final OnSimpleDialogClickInvoke onSimpleDialogClickInvoke) {
        new MaterialDialog.Builder(activity)
                .title(titleId)
                .theme(Theme.LIGHT)
                .inputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE)
                .negativeText("取消")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .positiveText("确定")
                .input(defaultResid, 0, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        if (onSimpleDialogClickInvoke != null) {
                            onSimpleDialogClickInvoke.onInputOkInvoke(input);
                        }
                    }
                }).show();
    }
    /**
     * 显示带有输入框的dialog
     *
     * @param activity
     * @param titleId
     * @param content
     * @param onSimpleDialogClickInvoke
     */
    public static MaterialDialog showEditTextDialog(FragmentActivity activity, String titleId, String content, String defaultResid, final TwoAction onSimpleDialogClickInvoke) {
       return new MaterialDialog.Builder(activity)
                .title(titleId)
                .theme(Theme.LIGHT)
               .cancelable(false)
                .inputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .negativeText("取消")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        dialog.dismiss();
                        if (onSimpleDialogClickInvoke != null) {
                            onSimpleDialogClickInvoke.onNegativeInvoke(null);
                        }
                    }
                })
                .positiveText("确定")
               .autoDismiss(false)
                .input(defaultResid,content,false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        if (onSimpleDialogClickInvoke != null) {
                            onSimpleDialogClickInvoke.onPositiveInvoke(input);
                        }
                    }
                }).show();
    }

    public static class TwoAction {
        public void onPositiveInvoke(CharSequence input) {
        }

        public void onNegativeInvoke(CharSequence input) {
        }

        public void onNeutral(CharSequence input) {

        }
    }


    static EditText editText = null;
    static CheckBox smsChk = null;
    static CheckBox emailChk = null;

    /**
     * 点击确认回调
     */
    public interface PositiveHandle {
        void clickSure();
    }


    public static MaterialDialog showAleartLoadDown(final Activity mActivity, String content, String titlem, final DocumentDownFile mDocumentDownFile) {
        MaterialDialog dialog = new MaterialDialog.Builder(mActivity)
                .title(titlem)
                .content(content)
                .positiveText(R.string.is_ensure)
                .theme(Theme.LIGHT)
                .negativeText(R.string.is_cancel)
                .cancelable(false)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        mDocumentDownFile.onSureClick();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        mDocumentDownFile.onCloseClick();
                    }
                })
                .show();
        return dialog;
    }

/*
    public static void showSendErrorDialogForBack(final Activity mActivity, String errorContent, final DialogCallBack mDialogCallBack) {

        ToastUtils.showShort(errorContent);

        if (null != mDialogCallBack) {
            mDialogCallBack.mDialogCallBack();
        }
    }*/


    /**
     * 显示不带title的list
     */
    public static void showListNoTitle(Activity pFragmentActivity, int array, final OnListItemSelectedInvoke pOnListItemSelectedInvoke) {
        new MaterialDialog.Builder(pFragmentActivity)
                .theme(Theme.LIGHT)
                .items(array)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if (pOnListItemSelectedInvoke != null) {
                            pOnListItemSelectedInvoke.onListItemSelected(dialog, view, which, text);
                        }
                    }
                }).positiveText(R.string.is_cancel)
                .show();
    }

    /**
     * 显示不带title的list
     */
    public static void showListNoTitle(Context pFragmentActivity, String[] array,String cancel, final OnListItemSelectedInvoke pOnListItemSelectedInvoke) {
        new MaterialDialog.Builder(pFragmentActivity)
                .items(array)
                .theme(Theme.LIGHT)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if (pOnListItemSelectedInvoke != null) {
                            pOnListItemSelectedInvoke.onListItemSelected(dialog, view, which, text);
                        }
                    }
                }).positiveText(cancel)
                .show();
    }
    /**
     * 显示不带title的list
     */
    public static void showListNoTitle(Context pFragmentActivity, String[] array, final OnListItemSelectedInvoke pOnListItemSelectedInvoke) {
        showListNoTitle(pFragmentActivity,array,"取消",pOnListItemSelectedInvoke);
    }


    public interface DocumentDownFile {
        void onSureClick();

        void onCloseClick();
    }

    public interface TheCustomInterface{
        void onSureClick();

        void onCloseClick();
    }


    /**
     * list item 选择回调事件
     */
    public interface OnListItemSelectedInvoke {
        void onListItemSelected(MaterialDialog pMaterialDialog, View pView, int pWhich, CharSequence pCharSequence);
    }

    /**
     * 回复的点击事件
     */
    public interface OnReplayItemSelectedInvoke {
        void onListItemSelected(MaterialDialog pMaterialDialog, View pView, int pWhich, CharSequence pCharSequence);
        void onReplayItemSelected(MaterialDialog pMaterialDialog, View pView, int pWhich, CharSequence pCharSequence);
    }

    /**
     * 自定义dialog
     * @param activity 类
     * @param content 内容
     * @param titlem  标题
     * @param mSureStr 确认按钮
     * @param mBackStr 返回按钮
     * @param theCustomInterface 事件回调
     */
    public static void showTheCustomDialog(Activity activity,String content, String titlem,String mSureStr,String mBackStr,final TheCustomInterface theCustomInterface){
                new MaterialDialog.Builder(activity).title(titlem)
                .content(content)
                .positiveText(mSureStr)
                .theme(Theme.LIGHT)
                .negativeText(mBackStr)
                .cancelable(false)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        dialog.dismiss();
                        theCustomInterface.onSureClick();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        dialog.dismiss();
                        theCustomInterface.onCloseClick();
                    }
                }).show();

    }

    public static MaterialDialog showSimpleSubmitDialog(Context context, String title, String content, MaterialDialog.ButtonCallback cb) {
        MaterialDialog show = new MaterialDialog.Builder(context)
                .title(title)
                .theme(Theme.LIGHT)
                .content(content)
                .positiveText("确定")
                .negativeText("取消")
                .callback(cb)
                .show();
        return show;
    }

    /**
     * 只有确定按钮
     * @param context
     * @param title
     * @param content
     * @param positive
     * @param cb
     * @return
     */
    public static MaterialDialog showOnlyPositiveDialog(Context context, String title, String content, String positive, boolean isCancelable, MaterialDialog.ButtonCallback cb) {
        MaterialDialog show = new MaterialDialog.Builder(context)
                .title(title)
                .theme(Theme.LIGHT)
                .content(content)
                .cancelable(isCancelable)
                .positiveText(positive)
                .callback(cb)
                .show();
        return show;
    }

    public static MaterialDialog dialogShow(Activity activity, String title, int posi, int nega, MaterialDialog.ButtonCallback cb) {
        MaterialDialog show = new MaterialDialog.Builder(activity)
                .title(title)
                .theme(Theme.LIGHT)
                .positiveText(posi)
                .negativeText(nega)
                .callback(cb)
                .show();
        return show;
    }


    /**
     * 初始化dialog
     */
    public static MaterialDialog showMaterialLoadView(Context context) {
        MaterialDialog materialDialog = showMaterialLoadView(context,context.getString(R.string.loading_tip));
        return materialDialog;
    }

    /**
     * 初始化dialog
     */
    public static MaterialDialog showMaterialLoadView(Context context, String text, boolean isAllowCancelable) {
        if(context == null || !(context instanceof Activity)){
            return null;
        }
        MaterialDialog materialDialog = PromptManager.showIndeterminateProgressDialog(context, text);
        materialDialog.setCancelable(isAllowCancelable);
        materialDialog.setCanceledOnTouchOutside(isAllowCancelable);
        if (!materialDialog.isShowing()) {
            materialDialog.show();
        }
        return materialDialog;
    }

    /**
     * 初始化dialog
     */
    public static MaterialDialog showMaterialLoadView(Context context, String text ) {
         return showMaterialLoadView(context,text,true);
    }

    public static void hideMaterialLoadView(MaterialDialog dialog){
        if (dialog !=null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public static ProgressDialog showProcessDialog(Context context,String title,DialogInterface.OnClickListener cancelListener){
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(title);
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(0);
        progressDialog.setCancelable(false);
        progressDialog.setButton(BUTTON_NEGATIVE,"取消",cancelListener);
        progressDialog.show();
        return progressDialog;
    }

}
