package g.api.views.loadmoreview;

import android.view.View;

public interface LoadMoreContainer<TOnScrollListener> {

    public void setShowLoadingForFirstPage(boolean showLoading);

    public void setAutoLoadMore(boolean autoLoadMore);

    public void setOnScrollListener(TOnScrollListener l);

    public void setLoadMoreView(View view);

    public void setLoadMoreUIHandler(LoadMoreUIHandler handler);

    public void setLoadMoreHandler(LoadMoreHandler handler);

    /**
     * When data has loaded
     *
     * @param emptyResult
     * @param hasMore
     */
    public void loadMoreFinish(boolean emptyResult, boolean hasMore);

    /**
     * When something unexpected happened while loading the data
     *
     * @param errorCode
     * @param errorMessage
     */
    public void loadMoreError(int errorCode, String errorMessage);
}
