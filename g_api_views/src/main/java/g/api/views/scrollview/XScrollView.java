package g.api.views.scrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * 自定义ScrollView
 */
public class XScrollView extends ScrollView {
    private OnScrollListener onScrollListener;

    public XScrollView(Context context) {
        this(context, null);
    }

    public XScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollListener != null) {
            onScrollListener.onScrollChanged(l, t, oldl, oldt);
        }
    }

    public interface OnScrollListener {
        void onScrollChanged(int left, int top, int oldLeft, int oldTop);
    }

    public boolean isAtTop() {
        return getScrollY() == 0;
    }

    public boolean isAtBottom() {
        View childView = getChildAt(0);
        if (childView == null)
            return false;
        return getScrollY() == (childView.getHeight() - getHeight());
    }

}
