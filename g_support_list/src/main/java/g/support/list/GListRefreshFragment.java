package g.support.list;

import android.view.View;

import g.api.views.pull2refreshview.PtrDefaultHandler;
import g.api.views.pull2refreshview.PtrFrameLayout;
import g.api.views.pull2refreshview.PtrHandler;
import g.support.GSupportUtils;
import g.support.list.R;

public class GListRefreshFragment extends GListFragment {

    protected MyPTRRefreshLayout ptr_refresh;

    @Override
    protected int getRootLayoutRes() {
        return R.layout.g_layout_refresh_list;
    }

    @Override
    protected void setup(View rootView) {
        super.setup(rootView);
        setupRefreshView(rootView);
    }

    final protected void setupRefreshView(View rootView) {
        ptr_refresh = (MyPTRRefreshLayout) rootView.findViewById(R.id.ptr_refresh);
        GSupportUtils.setDefaultPTR(ptr_refresh, this);
        ptr_refresh.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                onRefresh(false, true);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, lv_list, header);
            }
        });
    }

    public void onRefresh(boolean isLoadMore, boolean isFromPTR, int... flag) {
        if (getActivity() == null)
            return;
    }
}
