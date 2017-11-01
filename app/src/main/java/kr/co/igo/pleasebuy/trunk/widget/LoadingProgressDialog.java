package kr.co.igo.pleasebuy.trunk.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.WindowManager;
import android.widget.ProgressBar;

import kr.co.igo.pleasebuy.R;


/**
 * 서버와 통신중 사용되는 ProgressDialog
 */
public class LoadingProgressDialog extends Dialog {

    public LoadingProgressDialog(Context context) {
        super(context, R.style.LoadingProgressDialog);
    }

    public static LoadingProgressDialog show(Context context, CharSequence title, CharSequence message) {
        return show(context, title, message, false);
    }

    public static LoadingProgressDialog show(Context context, CharSequence title, CharSequence message, boolean indeterminate) {
        return show(context, title, message, indeterminate, false, null);
    }

    public static LoadingProgressDialog show(Context context, CharSequence title, CharSequence message, boolean indeterminate, boolean cancelable) {
        return show(context, title, message, indeterminate, cancelable, null);
    }

    public static LoadingProgressDialog show(Context context, CharSequence title, CharSequence message, boolean indeterminate, boolean cancelable, OnCancelListener cancelListener) {
        LoadingProgressDialog dialog = new LoadingProgressDialog(context);
        dialog.setTitle(title);
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(cancelListener);

        ProgressBar progress = new ProgressBar(context);
        progress.setIndeterminate(indeterminate);

        dialog.addContentView(progress, new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT));
        dialog.show();

        return dialog;
    }
}