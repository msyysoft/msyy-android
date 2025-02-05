package g.support.loading;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

import androidx.appcompat.app.AlertDialog;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

public class DialogUnDismissHandler extends Handler {
    // Button clicks have Message.what as the BUTTON{1,2,3} constant
    private static final int MSG_DISMISS_DIALOG = 1;

    private WeakReference<DialogInterface> mDialog;

    public DialogUnDismissHandler(DialogInterface dialog) {
        mDialog = new WeakReference<DialogInterface>(dialog);
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {

            case DialogInterface.BUTTON_POSITIVE:
            case DialogInterface.BUTTON_NEGATIVE:
            case DialogInterface.BUTTON_NEUTRAL:
                ((DialogInterface.OnClickListener) msg.obj).onClick(mDialog.get(), msg.what);
                break;

            //屏蔽Dialog点击自动消失
            //case MSG_DISMISS_DIALOG:
            //    ((DialogInterface) msg.obj).dismiss();
        }
    }

    public static void setUnDismiss(AlertDialog dialog) {
        try {
            Field field = dialog.getClass().getDeclaredField("mAlert");
            field.setAccessible(true);
            // 获得mAlert变量的值
            Object obj = field.get(dialog);
            field = obj.getClass().getDeclaredField("mHandler");
            field.setAccessible(true);
            // 修改mHandler变量的值，使用新的ButtonHandler类
            field.set(obj, new DialogUnDismissHandler(dialog));
        } catch (Exception e) {
        }
    }
}