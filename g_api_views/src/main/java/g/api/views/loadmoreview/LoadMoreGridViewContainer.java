package g.api.views.loadmoreview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;

import g.api.views.gridview.GridViewWithHeaderAndFooter;


/**
 * @author huqiu.lhq
 */
public class LoadMoreGridViewContainer extends LoadMoreContainerBase<GridViewWithHeaderAndFooter, AbsListView.OnScrollListener> {


    public LoadMoreGridViewContainer(Context context) {
        super(context);
    }

    public LoadMoreGridViewContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initTOnScrollListener(GridViewWithHeaderAndFooter listView) {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (null != mOnScrollListener) {
                    mOnScrollListener.onScrollStateChanged(view, scrollState);
                }
                LoadMoreGridViewContainer.this.onScrollStateChanged(null, scrollState);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (null != mOnScrollListener) {
                    mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }
                LoadMoreGridViewContainer.this.onScroll(null, firstVisibleItem, visibleItemCount, totalItemCount);
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
    protected GridViewWithHeaderAndFooter retrieveAbsListView() {
        return (GridViewWithHeaderAndFooter) getChildAt(0);
    }
}