/*
 * Copyright (C) 2011 Patrik Akerfeldt
 * Copyright (C) 2011 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package g.api.views.viewpager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.ViewPager;

@SuppressLint("ClickableViewAccessibility")
public class ViewPagerIndicator extends View implements ViewPager.OnPageChangeListener {
    protected Paint mPaintUnselect;
    protected Paint mPaintSelected;
    protected float mSelectedRadius;
    protected float mUnselectRadius;
    protected float mRadius;
    protected float mCenterSpacing;
    protected ViewPager mViewPager;
    protected ViewPager.OnPageChangeListener mListener;
    protected int mCurrentPage;
    protected int mSnapPage;
    protected int mCurrentOffset;
    protected int mScrollState;
    protected int mScreenWidth;
    protected boolean mCentered;
    protected boolean mSnap;
    protected int mItemsCount;
    protected boolean mForInfiniteLoop;
    protected boolean mIsChanged = false;

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        final int defaultSelectedColor = 0xFFFFF000;
        final int defaultUnSelectColor = 0xFFFFFFFF;
        final float defaultRadius = 8f;
        final boolean defaultCentered = false;
        final boolean defaultSnap = false;
        final boolean defaultForInfiniteLoop = false;
        mCentered = defaultCentered;
        mPaintUnselect = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintUnselect.setStyle(Style.FILL);
        mPaintUnselect.setColor(defaultUnSelectColor);
        mPaintSelected = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintSelected.setStyle(Style.FILL);
        mPaintSelected.setColor(defaultSelectedColor);
        mSelectedRadius = mUnselectRadius = mRadius = defaultRadius;
        mCenterSpacing = 5 * mRadius;
        mSnap = defaultSnap;
        mForInfiniteLoop = defaultForInfiniteLoop;
    }

    public Paint getPaintUnselect() {
        return mPaintUnselect;
    }

    public Paint getPaintSelected() {
        return mPaintSelected;
    }

    public boolean isCentered() {
        return mCentered;
    }

    public void setCentered(boolean centered) {
        mCentered = centered;
        invalidate();
    }

    public int getSelectedColor() {
        return mPaintSelected.getColor();
    }

    public void setSelectedColor(int fillColor) {
        mPaintSelected.setColor(fillColor);
        invalidate();
    }

    public int getUnselectColor() {
        return mPaintUnselect.getColor();
    }

    public void setUnselectColor(int strokeColor) {
        mPaintUnselect.setColor(strokeColor);
        invalidate();
    }

    public void setSelectedRadius(float mSelectedRadius) {
        this.mSelectedRadius = mSelectedRadius;
        mRadius = Math.max(mSelectedRadius, mUnselectRadius);
        invalidate();
    }

    public void setUnselectRadius(float mUnselectRadius) {
        this.mUnselectRadius = mUnselectRadius;
        mRadius = Math.max(mSelectedRadius, mUnselectRadius);
        invalidate();
    }

    public void setRadius(float mRadius) {
        this.mRadius = mRadius;
        mCenterSpacing = 5 * mRadius;
        this.mUnselectRadius = this.mSelectedRadius = this.mRadius = mRadius;
        invalidate();
    }

    public void setCenterSpacing(float mCenterSpacing) {
        this.mCenterSpacing = mCenterSpacing;
    }

    public boolean isSnap() {
        return mSnap;
    }

    public void setSnap(boolean snap) {
        mSnap = snap;
        if (!mSnap) {
            mForInfiniteLoop = false;
        }
        invalidate();
    }

    public boolean isForInfiniteLoop() {
        return mForInfiniteLoop;
    }

    public void setForInfiniteLoop(boolean forInfiniteLoop) {
        mForInfiniteLoop = forInfiniteLoop;
        if (mForInfiniteLoop) {
            mSnap = true;
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mItemsCount == 0)
            return;
        int longSize;
        int longPaddingBefore;
        int longPaddingAfter;
        int shortPaddingBefore;
        longSize = getWidth();
        longPaddingBefore = getPaddingLeft();
        longPaddingAfter = getPaddingRight();
        shortPaddingBefore = getPaddingTop();
        int count = mItemsCount;
        int iLoop = 0;
        if (mForInfiniteLoop && mItemsCount >= 3) {
            count = mItemsCount - 1;
            iLoop = 1;
        }
        final float shortOffset = shortPaddingBefore + mRadius;
        float longOffset;
        if (mCentered) {
            longOffset = ((longSize - longPaddingBefore - longPaddingAfter) / 2.0f) - (((mItemsCount - 1) * mCenterSpacing) / 2.0f);
        } else {
            longOffset = (longSize - longPaddingAfter - mRadius) - ((mItemsCount - 1) * mCenterSpacing);
        }
        float dX;
        float dY;
        //Draw stroked circles
        for (; iLoop < count; iLoop++) {
            float drawLong = longOffset + (iLoop * mCenterSpacing);
            dX = drawLong;
            dY = shortOffset;
            if (mSnap) {
                if (iLoop != mSnapPage) {
                    canvas.drawCircle(dX, dY, mUnselectRadius, mPaintUnselect);
                }
            } else {
                canvas.drawCircle(dX, dY, mUnselectRadius, mPaintUnselect);
            }
        }
        //Draw the filled circle according to the current scroll
        float cx = (mSnap ? mSnapPage : mCurrentPage) * mCenterSpacing;
        if (!mSnap && (mScreenWidth != 0)) {
            cx += (mCurrentOffset * 1.0f / mScreenWidth) * mCenterSpacing;
        }
        cx = Math.min(cx, mCenterSpacing * (mItemsCount - 1));
        dX = longOffset + cx;
        dY = shortOffset;
        canvas.drawCircle(dX, dY, mSelectedRadius, mPaintSelected);
    }

    //	@Override
    public void setViewPager(ViewPager view) {
        if (view.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mViewPager = view;
        if (mForInfiniteLoop && mItemsCount >= 3) {
            setCurrentItem(1);
        } else {
            mForInfiniteLoop = false;
        }
        mViewPager.setOnPageChangeListener(this);
        updatePageSize();
        invalidate();
    }

    private void updatePageSize() {
        if (mViewPager != null) {
            mScreenWidth = mViewPager.getWidth();
        }
    }

    //	@Override
    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        mViewPager.setCurrentItem(item, false);
        mCurrentPage = item;
        mSnapPage = item;
        invalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mListener != null) {
            mListener.onPageScrollStateChanged(state);
        }
        if (ViewPager.SCROLL_STATE_IDLE == state) {
            if (mForInfiniteLoop && mItemsCount >= 3) {
                if (mIsChanged) {
                    mIsChanged = false;
                    setCurrentItem(mSnapPage);
                }
            }
        }
        mScrollState = state;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mListener != null) {
            mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
        mCurrentPage = position;
        mCurrentOffset = positionOffsetPixels;
        updatePageSize();
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
        if (mListener != null) {
            mListener.onPageSelected(position);
        }
        if (mSnap || mScrollState == ViewPager.SCROLL_STATE_IDLE) {
            mCurrentPage = position;
            mSnapPage = position;
        }
        if (mForInfiniteLoop && mItemsCount >= 3) {
            mIsChanged = true;
            if (position > mItemsCount - 2) {
                mCurrentPage = 1;
                mSnapPage = 1;
            } else if (position < 1) {
                mCurrentPage = mItemsCount - 2;
                mSnapPage = mItemsCount - 2;
            } else {
                mCurrentPage = position;
                mSnapPage = position;
            }
        }
        invalidate();
    }

    //	@Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mListener = listener;
    }

    /*
     * (non-Javadoc)
     * @see android.view.View#onMeasure(int, int)
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureLong(widthMeasureSpec), measureShort(heightMeasureSpec));
    }

    /**
     * Determines the width of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @return The width of the view, honoring constraints from measureSpec
     */
    private int measureLong(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            //We were told how big to be
            result = specSize;
        } else {
            //Calculate the width according the views count
            final int count = mItemsCount;
            result = (int) (getPaddingLeft() + getPaddingRight() + (count * 2 * mSelectedRadius) + (count - 1) * mSelectedRadius + 1);
            //Respect AT_MOST value if that was what is called for by measureSpec
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    /**
     * Determines the height of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @return The height of the view, honoring constraints from measureSpec
     */
    private int measureShort(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            //We were told how big to be
            result = specSize;
        } else {
            //Measure the height
            result = (int) (2 * mSelectedRadius + getPaddingTop() + getPaddingBottom() + 1);
            //Respect AT_MOST value if that was what is called for by measureSpec
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        mCurrentPage = savedState.currentPage;
        mSnapPage = savedState.currentPage;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPage = mCurrentPage;
        return savedState;
    }

    public int getItemsCount() {
        return mItemsCount;
    }

    public void setItemsCount(int mItemsCount) {
        this.mItemsCount = mItemsCount;
    }

    static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        int currentPage;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPage = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPage);
        }
    }

    public static <T> List<T> getInfiniteLoopDatas(List<T> headerPicUrls) {
        if (headerPicUrls == null || headerPicUrls.size() == 0)
            return headerPicUrls;
        List<T> urls = new ArrayList<T>();
        urls.add(headerPicUrls.get(headerPicUrls.size() - 1));
        urls.addAll(headerPicUrls);
        urls.add(headerPicUrls.get(0));
        return urls;
    }

}
