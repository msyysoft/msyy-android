package g.api.views.pull2refreshview;

import android.view.View;
import android.widget.AbsListView;

import androidx.core.view.ViewCompat;

public abstract class PtrDefaultHandler implements PtrHandler {

//    public static boolean canChildScrollUp(View view) {
//        if (android.os.Build.VERSION.SDK_INT < 14) {
//            if (view instanceof AbsListView) {
//                final AbsListView absListView = (AbsListView) view;
//                return absListView.getChildCount() > 0
//                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
//                        .getTop() < absListView.getPaddingTop());
//            } else {
//                return view.getScrollY() > 0;
//            }
//        } else {
//            return view.canScrollVertically(-1);
//        }
//    }

    /**
     * copy from SwipeRefreshLayout
     *
     * @param mTarget
     * @return
     */
    public static boolean canChildScrollUp(View mTarget) {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mTarget instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTarget;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(mTarget, -1) || mTarget.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mTarget, -1);
        }
    }

    /**
     * Default implement for check can perform pull to refresh
     *
     * @param frame
     * @param content
     * @param header
     * @return
     */
    public static boolean checkContentCanBePulledDown(PtrFrameLayout frame, View content, View header) {
        return !canChildScrollUp(content);
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return checkContentCanBePulledDown(frame, content, header);
    }
}