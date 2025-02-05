package g.support.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

import g.api.tools.TouchEventUtil;
import g.api.views.swipelistview.SwipeListViewIFaces;
import g.api.views.swipelistview.SwipeMenuListView;

public class MyPTRFatherSwipeListView extends SwipeMenuListView implements MyPTRFatherI {
    private MyPTRRefreshLayout refreshLayout;
    private TouchEventUtil touchEventUtil;
    private boolean childRequestEvent = false;

    public MyPTRFatherSwipeListView(Context context) {
        super(context);
        setup(context);
    }

    public MyPTRFatherSwipeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context);
    }

    public MyPTRFatherSwipeListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup(context);
    }

    private void setup(Context context) {
        touchEventUtil = new TouchEventUtil();
        touchEventUtil.setTouchEventIFace(new TouchEventUtil.TouchEventIFace() {
            @Override
            public void isShouldMove(boolean isShouldMove) {
                if (refreshLayout != null) {
                    refreshLayout.requestMyDisallowInterceptTouchEvent(isShouldMove);
                }
            }
        });
        setOnSwipeListener(new SwipeListViewIFaces.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
                if (refreshLayout != null) {
                    refreshLayout.requestMyDisallowInterceptTouchEvent(true);
                }
            }

            @Override
            public void onSwipeEnd(int position) {
                if (refreshLayout != null) {
                    refreshLayout.requestMyDisallowInterceptTouchEvent(false);
                }
            }
        });

        setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                smoothOpenMenu(position);
                return true;
            }
        });
    }

    public void setRefreshLayout(MyPTRRefreshLayout refreshLayout) {
        this.refreshLayout = refreshLayout;
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

    //适配在swipemenu打开状态时 关闭它会和PTR滑动冲突
    @Override
    public boolean canScrollVertically(int direction) {
        if (hasSwipeMenuOpen())
            return true;
        return super.canScrollVertically(direction);
    }
}
