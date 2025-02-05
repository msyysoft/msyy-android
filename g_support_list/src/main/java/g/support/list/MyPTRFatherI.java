package g.support.list;

import android.view.MotionEvent;

/**
 * 处理下拉刷新控件冲突的父接口
 */
public interface MyPTRFatherI {

    boolean onInterceptTouchEvent(MotionEvent ev);

    void requestMyDisallowInterceptTouchEvent(boolean b);
}
