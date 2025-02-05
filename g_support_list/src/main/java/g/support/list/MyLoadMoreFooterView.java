package g.support.list;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import g.api.views.loadmoreview.LoadMoreContainer;
import g.api.views.loadmoreview.LoadMoreUIHandler;
import g.support.loading.LoadingLayout;

public class MyLoadMoreFooterView extends RelativeLayout implements LoadMoreUIHandler {

    protected LoadingLayout loadingLayout;

    public MyLoadMoreFooterView(Context context) {
        this(context, null);
    }

    public MyLoadMoreFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyLoadMoreFooterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        loadingLayout = new LoadingLayout(context);
        loadingLayout.setLoading(true, null);
        loadingLayout.setVisibility(GONE);
        addView(loadingLayout);
    }

    @Override
    public void onLoading(LoadMoreContainer container) {
        loadingLayout.setVisibility(VISIBLE);
    }

    @Override
    public void onLoadFinish(LoadMoreContainer container, boolean empty, boolean hasMore) {
        if (hasMore) {
            loadingLayout.setVisibility(VISIBLE);
        } else {
            loadingLayout.setVisibility(GONE);
            //loadingLayout.setResult("无更多数据");
        }
    }

    @Override
    public void onWaitToLoadMore(LoadMoreContainer container) {
        loadingLayout.setVisibility(VISIBLE);
    }

    @Override
    public void onLoadError(LoadMoreContainer container, int errorCode, String errorMessage) {
        loadingLayout.setVisibility(GONE);
    }
}
