package g.support.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import g.api.views.listview.AddListView;

public class MyPTRFatherListView extends AddListView implements MyPTRFatherI {
    private boolean childRequestEvent = false;

    public MyPTRFatherListView(Context context) {
        super(context);
    }

    public MyPTRFatherListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (childRequestEvent) {
            return false;
        } else {
            try {// java.lang.IllegalArgumentException: pointerIndex out of range
                return super.onInterceptTouchEvent(ev);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public void requestMyDisallowInterceptTouchEvent(boolean b) {
        childRequestEvent = b;
    }
}
