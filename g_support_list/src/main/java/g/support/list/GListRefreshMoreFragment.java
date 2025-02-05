package g.support.list;

import android.view.View;

import g.api.views.loadmoreview.LoadMoreContainer;
import g.api.views.loadmoreview.LoadMoreHandler;
import g.api.views.loadmoreview.LoadMoreListViewContainer;
import g.support.GSupportUtils;
import g.support.list.R;

public class GListRefreshMoreFragment extends GListRefreshFragment {
    protected LoadMoreListViewContainer load_more_container;
    protected int mPageNum = 1;

    @Override
    protected int getRootLayoutRes() {
        return R.layout.g_layout_refresh_more_list;
    }

    @Override
    protected void setup(View rootView) {
        super.setup(rootView);
        setupMoreView(rootView);
    }


    private void setupMoreView(View rootView) {
        load_more_container = (LoadMoreListViewContainer) rootView.findViewById(R.id.load_more_container);
        GSupportUtils.setDefaultLoadMore(load_more_container);
        load_more_container.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                onRefresh(true, false);
            }
        });
    }

    public void onRefresh(boolean isLoadMore, boolean isFromPTR, int... flag) {
        super.onRefresh(isLoadMore, isFromPTR, flag);
        if (isLoadMore)
            mPageNum++;
        else
            mPageNum = 1;
    }
}
