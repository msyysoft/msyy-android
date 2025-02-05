package g.support.loading;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;

import java.util.Calendar;
import java.util.Locale;

import g.api.tools.T;

public class DateTimeSelectDialog {
    private AlertDialog dialog;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private OnPosListener onPosListener;
    private OnNegListener onNegListener;

    public DateTimeSelectDialog(Context context, CharSequence title, Calendar cal) {
        this(context, title, cal, true, true);
    }

    public DateTimeSelectDialog(Context context, CharSequence title, Calendar calendar, boolean showDataPicker, boolean showTimePicker) {
        String posStr = "确定";
        String negStr = "取消";

        AlertDialog.Builder builder = new AlertDialog.Builder(context, T.getTheme(context, R.attr.g_theme_dialog));

        View view = LayoutInflater.from(context).inflate(R.layout.loading_dialog_date_time_select, null);

        datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        datePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        timePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
        timePicker.setIs24HourView(true);

        if (calendar != null) {
            Calendar cal = Calendar.getInstance(Locale.CHINA);
            cal.setTimeInMillis(calendar.getTimeInMillis());
            datePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
            timePicker.setCurrentMinute(0);
        }

        datePicker.setVisibility(showDataPicker ? View.VISIBLE : View.GONE);
        timePicker.setVisibility(showTimePicker ? View.VISIBLE : View.GONE);

        builder.setView(view);
        if (!T.isEmpty(title))
            builder.setTitle(title);
        builder.setPositiveButton(posStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (onPosListener != null) {
                    Calendar cal = Calendar.getInstance(Locale.CHINA);
                    cal.set(Calendar.YEAR, datePicker.getYear());
                    cal.set(Calendar.MONTH, datePicker.getMonth());
                    cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                    cal.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    onPosListener.onClick(DateTimeSelectDialog.this, dialog, which, cal);
                } else {
                    dialog.dismiss();
                }
            }
        });
        builder.setNegativeButton(negStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (onNegListener != null) {
                    onNegListener.onClick(DateTimeSelectDialog.this, dialog, which);
                } else {
                    dialog.dismiss();
                }
            }
        });

        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
    }

    public void setOnPosListener(OnPosListener onPosListener) {
        this.onPosListener = onPosListener;
    }

    public void setOnNegListener(OnNegListener onNegListener) {
        this.onNegListener = onNegListener;
    }

    public AlertDialog getDialog() {
        return dialog;
    }

    public DatePicker getDatePicker() {
        return datePicker;
    }

    public TimePicker getTimePicker() {
        return timePicker;
    }

    public void show() {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    public void dissmis() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public interface OnPosListener {
        void onClick(DateTimeSelectDialog dateTimeSelectDialog, DialogInterface dialog, int which, Calendar selectCal);
    }

    public interface OnNegListener {
        void onClick(DateTimeSelectDialog dateTimeSelectDialog, DialogInterface dialog, int which);
    }

}
