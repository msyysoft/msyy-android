package g.support.loading;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class LoadingDialog extends Dialog {
    public LoadingDialog(Context context, boolean isUseMessage, String message) {
        super(context, R.style.loading_dialog_loading_style);
        init(context, isUseMessage, message);
    }

    private void init(Context context, boolean isUseMessage, String message) {
        setContentView(R.layout.loading_dialog);
        getWindow().getAttributes().gravity = Gravity.CENTER;
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        TextView tv_message = (TextView) findViewById(R.id.tv_message);
        if (isUseMessage) {
            tv_message.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= 11) {
                if (message != null && !message.equals("")) {
                    tv_message.setText(message);
                } else {
                    tv_message.setText("加载中");
                }
                JumpingBeans.with(tv_message).appendJumpingDots().build();
            } else {
                if (message != null && !message.equals("")) {
                    tv_message.setText(message + "...");
                } else {
                    tv_message.setText("加载中...");
                }
            }
        } else {
            tv_message.setVisibility(View.GONE);
        }
    }
}