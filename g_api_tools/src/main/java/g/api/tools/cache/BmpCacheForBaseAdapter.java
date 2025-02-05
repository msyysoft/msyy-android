package g.api.tools.cache;

import android.widget.AbsListView;
import android.widget.ImageView;

public abstract class BmpCacheForBaseAdapter extends BmpCacheForAdapter implements AbsListView.OnScrollListener {
    //AbsListView中可见的第一张图片的下标
    private int mFirstVisibleItem;
    //AbsListView中可见的图片的数量
    private int mVisibleItemCount;
    //记录是否是第一次进入
    public boolean isFirstEnter = true;
    private AbsListView mAbsListView;
    private AbsListView.OnScrollListener mAbsListViewScrollListener;

    public BmpCacheForBaseAdapter(AbsListView absListView) {
        super();
        this.mAbsListView = absListView;
        this.mAbsListView.setOnScrollListener(this);
    }

    public void setAbsListViewScrollListener(AbsListView.OnScrollListener scrollListener) {
        this.mAbsListViewScrollListener = scrollListener;
    }

    /**
     * 我们的本意是通过onScrollStateChanged获知:每次AbsListView停止滑动时加载图片
     * 但是存在一个特殊情况:
     * 当第一次入应用的时候,此时并没有滑动屏幕的操作即不会调用onScrollStateChanged,但应该加载图片.
     * 所以在此处做一个特殊的处理.
     * 即代码:
     * if (isFirstEnterThisActivity && visibleItemCount > 0) {
     * loadBitmaps(firstVisibleItem, visibleItemCount);
     * isFirstEnterThisActivity = false;
     * }
     * ------------------------------------------------------------
     * 其余的都是正常情况.
     * 所以我们需要不断保存:firstVisibleItem和visibleItemCount
     * 从而便于中在onScrollStateChanged()判断当停止滑动时加载图片
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mAbsListViewScrollListener != null)
            mAbsListViewScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        mFirstVisibleItem = firstVisibleItem;
        mVisibleItemCount = visibleItemCount;
        if (isFirstEnter && visibleItemCount > 0) {
            loadBitmaps(firstVisibleItem, visibleItemCount);
            isFirstEnter = false;
        }
    }

    /**
     * AbsListView停止滑动时加载图片
     * 其余情况下取消所有正在加载或者等待加载的任务
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mAbsListViewScrollListener != null)
            mAbsListViewScrollListener.onScrollStateChanged(view, scrollState);
        if (scrollState == SCROLL_STATE_IDLE) {
            loadBitmaps(mFirstVisibleItem, mVisibleItemCount);
        } else {
            cancelAllTasks();
        }
    }

    /**
     * 为AbsListView的item加载图片
     *
     * @param firstVisibleItem AbsListView中可见的第一张图片的下标
     * @param visibleItemCount AbsListView中可见的图片的数量
     */
    private void loadBitmaps(int firstVisibleItem, int visibleItemCount) {
        for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
            loadBitmaps(i);
        }
    }

    @Override
    public ImageView getImageViewByTag(int position) {
        return (ImageView) mAbsListView.findViewWithTag(position);
    }
}
