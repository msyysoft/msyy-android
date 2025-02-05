package g.api.views.loadmoreview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * @author huqiu.lhq
 */
public class LoadMoreListViewContainer extends LoadMoreContainerBase<ListView, AbsListView.OnScrollListener> {

    public LoadMoreListViewContainer(Context context) {
        super(context);
    }

    public LoadMoreListViewContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initTOnScrollListener(ListView listView) {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (null != mOnScrollListener) {
                    mOnScrollListener.onScrollStateChanged(view, scrollState);
                }
                LoadMoreListViewContainer.this.onScrollStateChanged(null, scrollState);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (null != mOnScrollListener) {
                    mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }
                LoadMoreListViewContainer.this.onScroll(null, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        });
    }

    @Override
    protected void addFooterView(View view) {
        mAbsListView.addFooterView(view);
    }

    @Override
    protected void removeFooterView(View view) {
        mAbsListView.removeFooterView(view);
    }

    @Override
    protected ListView retrieveAbsListView() {
        return (ListView) getChildAt(0);
    }
}
