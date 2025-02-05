package g.support.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.recyclerview.widget.RecyclerView;

import g.api.tools.TouchEventUtil;

public class MyPTRChildRecyclerView extends RecyclerView implements MyPTRChildI{

    private MyPTRRefreshLayout refreshLayout;
    private MyPTRFatherI myPTRFatherI;
    private boolean fatherRequestEvent = false;
    private TouchEventUtil touchEventUtil;

    public MyPTRChildRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context);
    }

    private void setup(Context context) {
        touchEventUtil = new TouchEventUtil();
        touchEventUtil.setTouchEventIFace(new TouchEventUtil.TouchEventIFace() {
            @Override
            public void isShouldMove(boolean isShouldMove) {
                if (myPTRFatherI != null) {
                    myPTRFatherI.requestMyDisallowInterceptTouchEvent(isShouldMove);
                }
                if (refreshLayout != null) {
                    refreshLayout.requestMyDisallowInterceptTouchEvent(isShouldMove);
                }
            }
        });
    }

    public void requestFatherDisallowInterceptTouchEvent(boolean b) {
        fatherRequestEvent = b;
    }

    public boolean isShouldMove() {
        return touchEventUtil.isShouldMove();
    }

    public void setRefreshLayout(MyPTRRefreshLayout refreshLayout) {
        this.refreshLayout = refreshLayout;
    }

    public void setMyPTRFatherI(MyPTRFatherI myPTRFatherI) {
        this.myPTRFatherI = myPTRFatherI;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!fatherRequestEvent)
            touchEventUtil.dispatchTouchEvent(ev);
        else
            requestMyOnTouch(ev);
        return super.dispatchTouchEvent(ev);
    }

    private void requestMyOnTouch(MotionEvent ev) {
        if (refreshLayout != null) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!fatherRequestEvent) {
                        refreshLayout.requestMyDisallowInterceptTouchEvent(true);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (!fatherRequestEvent) {
                        refreshLayout.requestMyDisallowInterceptTouchEvent(false);
                    }
                    break;
            }
        }
    }
}
