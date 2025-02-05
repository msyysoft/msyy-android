package g.api.views.viewpager;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class MyHandlerViewPager extends ViewPager {
    private Handler handler;
    private boolean isRun;

    public MyHandlerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyHandlerViewPager(Context context) {
        super(context);
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        doDispatchTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    protected void doDispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (handler != null && getAdapter() != null) {
            if (action == MotionEvent.ACTION_DOWN) {
                isRun = false;
                handler.removeCallbacksAndMessages(null);
                //System.out.println("InfiniteLoopViewPager  dispatchTouchEvent =====>>> ACTION_DOWN");
            } else if (action == MotionEvent.ACTION_UP) {
                isRun = true;
                handler.removeCallbacksAndMessages(null);
                handler.sendEmptyMessage(1);
                //System.out.println("InfiniteLoopViewPager  dispatchTouchEvent =====>>> ACTION_UP");
            }
        }
    }

    public boolean getIsRun() {
        return isRun;
    }

    public void setIsRun(boolean isRun) {
        this.isRun = isRun;
    }
}
