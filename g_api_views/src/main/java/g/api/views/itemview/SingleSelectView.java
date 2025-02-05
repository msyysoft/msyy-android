package g.api.views.itemview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Checkable;
import android.widget.LinearLayout;

public class SingleSelectView extends LinearLayout implements Checkable {
    private boolean mChecked;
    private Drawable selectedDrawable, unselectDrawable;
    private OnCheckListener onCheckListener;

    public SingleSelectView(Context context) {
        super(context);
    }

    public SingleSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void init(int layoutResId, Drawable selectedDrawable, Drawable unselectDrawable) {
        this.selectedDrawable = selectedDrawable;
        this.unselectDrawable = unselectDrawable;
        LayoutInflater.from(getContext()).inflate(layoutResId, this, true);
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void setChecked(boolean checked) {
        mChecked = checked;
        if (onCheckListener != null)
            onCheckListener.onCheck(this, mChecked);
        select();
    }

    @Override
    public void toggle() {
        mChecked = !mChecked;
        if (onCheckListener != null)
            onCheckListener.onCheck(this, mChecked);
        select();
    }

    private void select() {
        if (Build.VERSION.SDK_INT >= 16) {
            setBackground(mChecked ? selectedDrawable : unselectDrawable);
        } else {
            setBackgroundDrawable(mChecked ? selectedDrawable : unselectDrawable);
        }
    }

    public void setOnCheckListener(OnCheckListener onCheckListener) {
        this.onCheckListener = onCheckListener;
    }

    public interface OnCheckListener {
        void onCheck(View v, boolean check);
    }
}