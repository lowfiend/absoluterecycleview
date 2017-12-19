package com.neyo.absoluterecycleview.sample.common;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.neyo.absoluterecycleview.R;

/**
 * Created by Neyo on 2017/12/19.
 */

public class CustomLoadingDialog extends Dialog {

    public CustomLoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        ImageView imageView = (ImageView) findViewById(R.id.spinnerImageView);
        AnimationDrawable spinner = (AnimationDrawable) imageView.getBackground();
        spinner.start();
    }

    public static CustomLoadingDialog setDialog(Context context, CharSequence message, boolean cancelable,
            OnCancelListener cancelListener) {
        CustomLoadingDialog customLoadingDialog = new CustomLoadingDialog(context, R.style.Custom_Progress);
        customLoadingDialog.setTitle("");
        customLoadingDialog.setContentView(R.layout.progress_custom);
        if (message == null || message.length() == 0) {
            customLoadingDialog.findViewById(R.id.message).setVisibility(View.GONE);
        } else {
            TextView txt = (TextView) customLoadingDialog.findViewById(R.id.message);
            txt.setText(message);
        }
        customLoadingDialog.setCancelable(cancelable);
        customLoadingDialog.setOnCancelListener(cancelListener);
        customLoadingDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = customLoadingDialog.getWindow().getAttributes();
        lp.dimAmount = 0.2f;
        customLoadingDialog.getWindow().setAttributes(lp);
        return customLoadingDialog;
    }
}
