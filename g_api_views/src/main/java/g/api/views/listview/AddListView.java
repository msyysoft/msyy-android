package g.api.views.listview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

import g.api.views.R;
import g.api.views.abslistview.AddAbsListView;

public class AddListView extends ListView implements AddAbsListView{
    private boolean isAddMode = false;

    public AddListView(Context context) {
        super(context);
        init(context, null);
    }

    public AddListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AddListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (context != null && attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AddAbsListView);
            isAddMode = typedArray.getBoolean(R.styleable.AddAbsListView_isAddMode, false);
            typedArray.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int myHeightMeasureSpec = isAddMode ? MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST) : heightMeasureSpec;

        super.onMeasure(widthMeasureSpec, myHeightMeasureSpec);
    }

    public boolean isAddMode() {
        return isAddMode;
    }

    public void setAddMode(boolean addMode) {
        isAddMode = addMode;
    }

    /**
     * 禁止多点触摸
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getPointerCount() <= 1)
            return super.onTouchEvent(ev);
        else
            return true;
    }
}
