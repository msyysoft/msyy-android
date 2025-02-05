package g.api.views.menuview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MenuImageHorizontalScrollView extends HorizontalScrollView implements OnClickListener {
    private InsideHandler mHandler;
    private Context mContext;
    private ImageView mImageViews[];
    private LinearLayout mLinearLayout;
    private OnMenuItemActionListener mMenuItemActionListener;
    private int[] mSelectedIconArr, mUnselectIconArr;
    private float mMenuItemWidth;
    private int mShowMenuItemNum = 1;
    private int mNowMenuItemIndex = -1;
    private boolean mAutoScroll = false;

    public MenuImageHorizontalScrollView(Context context) {
        super(context);
        this.mContext = context;
    }

    public MenuImageHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public MenuImageHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    public boolean setData(int[] selectedIconArr, int[] unselectIconArr, float menuItemWidth, int showMenuItemNum, OnMenuItemActionListener menuItemActionListener) {
        if (selectedIconArr != null && unselectIconArr != null && selectedIconArr.length > 0 && selectedIconArr.length == unselectIconArr.length && showMenuItemNum > 0 && menuItemActionListener != null) {
            this.mSelectedIconArr = selectedIconArr;
            this.mUnselectIconArr = unselectIconArr;
            this.mMenuItemWidth = menuItemWidth;
            this.mShowMenuItemNum = showMenuItemNum;
            this.mMenuItemActionListener = menuItemActionListener;
            return true;
        } else
            return false;
    }

    public void setAutoScroll(boolean autoScroll) {
        this.mAutoScroll = autoScroll;
    }

    public void init() {
        mLinearLayout = new LinearLayout(mContext);
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mLinearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        removeAllViews();
        addView(mLinearLayout);
        mImageViews = new ImageView[mSelectedIconArr.length];
        mLinearLayout.removeAllViews();
        for (int i = 0; i < mImageViews.length; i++) {
            mImageViews[i] = getNewMenuItem(i, mMenuItemWidth);
            mLinearLayout.addView(mImageViews[i], i);
        }
        mHandler = new InsideHandler();
        mHandler.setMenu(mImageViews);
        mHandler.setListener(mMenuItemActionListener);
    }

    private ImageView getNewMenuItem(int position, float menuItemWidth) {
        ImageView imageView = new ImageView(mContext);
        LinearLayout.LayoutParams lLayoutParams = new LinearLayout.LayoutParams((int) menuItemWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(lLayoutParams);
        imageView.setScaleType(ScaleType.FIT_CENTER);
        imageView.setTag(position);
        imageView.setImageResource(mUnselectIconArr[position]);
        imageView.setOnClickListener(this);
        return imageView;
    }

    public void setSelection(int position) {
        for (int i = 0; i < mImageViews.length; i++) {
            if (position == i) {
                mImageViews[i].setClickable(false);
                mImageViews[i].setImageResource(mSelectedIconArr[i]);
                if (mAutoScroll && mImageViews.length >= 5) {
                    smoothScrollTo((int) (mMenuItemWidth * (i - mShowMenuItemNum / 2)), 0);
                }
                mHandler.sendEmptyMessageDelayed(i, 1000);
            } else {
                mImageViews[i].setImageResource(mUnselectIconArr[i]);
            }
        }
        if (this.mNowMenuItemIndex != position) {
            this.mNowMenuItemIndex = position;
            Message msg = Message.obtain();
            msg.what = 225;
            msg.arg1 = mNowMenuItemIndex;
            mHandler.sendMessageDelayed(msg, 180);
        }
    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < mImageViews.length; i++) {
            if ((v.getTag() + "").equals(i + "")) {
                if (!mMenuItemActionListener.onMenuItemClick(i)) {
                    setSelection(i);
                }
                return;
            }
        }
    }

    private static class InsideHandler extends Handler {
        private List<WeakReference<ImageView>> menu_reference_list;
        private WeakReference<OnMenuItemActionListener> menuItemActionLitener_reference;

        public void setMenu(ImageView... imageViews) {
            menu_reference_list = new ArrayList<WeakReference<ImageView>>();
            for (ImageView imageView : imageViews) {
                menu_reference_list.add(new WeakReference<ImageView>(imageView));
            }
        }

        public void setListener(OnMenuItemActionListener menuItemActionListener) {
            if (menuItemActionListener != null) {
                menuItemActionLitener_reference = new WeakReference<OnMenuItemActionListener>(menuItemActionListener);
            }
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (menu_reference_list != null && menu_reference_list.get(0) != null && menu_reference_list.get(0).get() != null) {
                int result = msg.what;
                if (result >= 0 && result < menu_reference_list.size()) {
                    menu_reference_list.get(result).get().setClickable(true);
                    return;
                } else if (result == 225) {
                    if (menuItemActionLitener_reference != null && menuItemActionLitener_reference.get() != null) {
                        menuItemActionLitener_reference.get().onMenuItemSelected(msg.arg1);
                    }
                }
            }
        }
    }
}
