package g.support;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import g.api.tools.T;
import g.api.views.loadmoreview.LoadMoreContainerBase;
import g.api.views.swipelistview.SwipeMenuItem;
import g.api.views.textview.VerticalImageSpan;
import g.support.list.MyLoadMoreFooterView;
import g.support.list.MyLoadingLayout;
import g.support.list.MyPTRRefreshLayout;
import g.support.list.R;

public class GSupportUtils {

    public static View getSimpleDivider(Context context, @ColorInt int color, int heightInPx, boolean useTopLine, boolean useBottomLine) {
        View view = LayoutInflater.from(context).inflate(R.layout.g_view_item_simple_divider, null);
        view.findViewById(R.id.tv_top_line).setVisibility(useTopLine ? View.VISIBLE : View.GONE);
        view.findViewById(R.id.tv_bottom_line).setVisibility(useBottomLine ? View.VISIBLE : View.GONE);
        TextView tv_divider = (TextView) view.findViewById(R.id.tv_divider);
        tv_divider.getLayoutParams().height = heightInPx;
        tv_divider.setBackgroundColor(color);
        return view;
    }

    public static void setDefaultPTR(MyPTRRefreshLayout refreshLayout, Object holder) {
        TextView tv_title = refreshLayout.getHeader().getTitleView();
        TextView tv_update = refreshLayout.getHeader().getUpdateView();
        tv_title.setTextColor(getThemeColor(refreshLayout.getContext(), R.attr.gc_gray_1, Color.GRAY));
        tv_update.setTextColor(getThemeColor(refreshLayout.getContext(), R.attr.gc_gray_2, Color.GRAY));
        refreshLayout.setLastUpdateTimeRelateObject(holder);
        // the following are default settings
        refreshLayout.setLoadingMinTime(500);
        refreshLayout.setResistance(1.7f);
        refreshLayout.setRatioOfHeaderHeightToRefresh(1.2f);
        refreshLayout.setDurationToClose(500);
        refreshLayout.setDurationToCloseHeader(800);
        // default is false
        refreshLayout.setPullToRefresh(false);
        // default is true
        refreshLayout.setKeepHeaderWhenRefresh(true);
    }

    public static void setDefaultLoadMore(LoadMoreContainerBase load_more_container) {
        load_more_container.setAutoLoadMore(true);
        MyLoadMoreFooterView footerView = new MyLoadMoreFooterView(load_more_container.getContext());
        load_more_container.setLoadMoreView(footerView);
        load_more_container.setLoadMoreUIHandler(footerView);
    }

    public static void setDefaultPTRError(MyPTRRefreshLayout refreshLayout, String returnErrStr, String wantErrStr) {
        if ("网络异常".equals(returnErrStr)) {
            refreshLayout.setResultState(MyPTRRefreshLayout.STATE_NET_ERROR);
            refreshLayout.refreshComplete();
        } else {
            refreshLayout.setResultOtherError(wantErrStr);
            refreshLayout.setResultState(MyPTRRefreshLayout.STATE_OTHER_ERROR);
            refreshLayout.refreshComplete();
        }
    }

    public static void setDefaultLoadingLayoutError(MyLoadingLayout loadingLayout, String returnErrStr, String wantErrStr) {
        if ("网络异常".equals(returnErrStr)) {
            loadingLayout.showResultNetError(wantErrStr);
        } else {
            loadingLayout.showResultOtherError(wantErrStr);
        }
    }


    public static SwipeMenuItem getDefaultSwipeMenu(Context context, String title, @ColorInt int color) {
        SwipeMenuItem menuItem = new SwipeMenuItem(context);
        menuItem.setBackground(new ColorDrawable(color));
        menuItem.setWidth(T.dip2px(context, 60));
        menuItem.setTitle(title);
        menuItem.setTitleSize(13);
        menuItem.setTitleColor(Color.WHITE);
        return menuItem;
    }


    public static SpannableString getSpannableString(Context context, int iconRes, String text) {
        Drawable image = context.getResources().getDrawable(iconRes);
        image.setBounds(0, 0, T.dip2px(context, 20f), T.dip2px(context, 20f));
        SpannableString sb = new SpannableString(text);
        VerticalImageSpan imageSpan = new VerticalImageSpan(image);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

    /**
     * 获取API19及以上的fit状态栏高度
     *
     * @param context
     * @return
     */
    public static int getFitStatusBarHeight19(Context context) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ? T.getStatusBarHeight(context) : 0;
    }

    /**
     * 适配API19及以上状态栏
     *
     * @param view
     */
    public static void fitSystemWindow19(View view) {
        if (view != null) {
            view.getLayoutParams().height = getFitStatusBarHeight19(view.getContext());
        }
    }

    /**
     * 获取主题内的颜色
     *
     * @param context
     * @param attrRes
     * @param defValue
     * @return
     */
    public static int getThemeColor(Context context, @AttrRes int attrRes, @ColorInt int defValue) {
        if (context == null)
            return defValue;
        TypedArray array = context.getTheme().obtainStyledAttributes(new int[]{attrRes});
        int value = array.getColor(0, defValue);
        array.recycle();
        return value;
    }

    /**
     * 获取主题内的尺寸
     *
     * @param context
     * @param attrRes
     * @param defValue
     * @return
     */
    public static float getThemeDimension(Context context, @AttrRes int attrRes, float defValue) {
        if (context == null)
            return defValue;
        TypedArray array = context.getTheme().obtainStyledAttributes(new int[]{attrRes});
        float value = array.getDimension(0, defValue);
        array.recycle();
        return value;
    }

    /**
     * 获取主题
     *
     * @param context
     * @param attrRes
     * @return
     */

    public static int getTheme(Context context, @AttrRes int attrRes) {
        if (context == null)
            return 0;
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(attrRes, outValue, true);
        return outValue.resourceId;
    }
}
