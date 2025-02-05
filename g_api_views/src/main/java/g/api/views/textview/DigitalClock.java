package g.api.views.textview;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.AttributeSet;

import java.util.Calendar;

public class DigitalClock extends androidx.appcompat.widget.AppCompatTextView {
    private final static String m24 = "kk:mm";
    private Calendar mCalendar;
    private FormatChangeObserver mFormatChangeObserver;
    private Runnable mTicker;
    private Handler mHandler;
    private boolean mTickerStopped = false;
    private String mFormat = m24;
    private OnTimeTickListener onTimeTickListener;

    public DigitalClock(Context context) {
        super(context);
        initClock(context);
    }

    public DigitalClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        initClock(context);
    }

    private void initClock(Context context) {
        if (mCalendar == null) {
            mCalendar = Calendar.getInstance();
        }
        mFormatChangeObserver = new FormatChangeObserver();
        getContext().getContentResolver().registerContentObserver(Settings.System.CONTENT_URI, true, mFormatChangeObserver);
        setFormat(mFormat);
    }

    @Override
    protected void onAttachedToWindow() {
        mTickerStopped = false;
        super.onAttachedToWindow();
        mHandler = new Handler();
        /**
         * requests a tick on the next hard-second boundary
         */
        mTicker = new Runnable() {
            public void run() {
                if (mTickerStopped)
                    return;
                mCalendar.setTimeInMillis(System.currentTimeMillis());
                setDText(mCalendar);
                if (onTimeTickListener != null) {
                    onTimeTickListener.onTimeTick(mCalendar);
                }
                invalidate();
                long now = SystemClock.uptimeMillis();
                long next = now + (1000 - now % 1000);
                mHandler.postAtTime(mTicker, next);
            }
        };
        mTicker.run();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mTickerStopped = true;
    }

    public void setFormat(String format) {
        if (format != null) {
            mFormat = format;
        } else {
            mFormat = m24;
        }
    }

    public void setDText(Calendar mCalendar) {
        CharSequence dtext = DateFormat.format(mFormat, mCalendar);
        setText(dtext);
    }

    public void setOnTimeTickListener(OnTimeTickListener onTimeTickListener) {
        this.onTimeTickListener = onTimeTickListener;
    }

    public interface OnTimeTickListener {
        public void onTimeTick(Calendar mCalendar);
    }

    private class FormatChangeObserver extends ContentObserver {
        public FormatChangeObserver() {
            super(new Handler());
        }

        @Override
        public void onChange(boolean selfChange) {
            setFormat(mFormat);
        }
    }
}
