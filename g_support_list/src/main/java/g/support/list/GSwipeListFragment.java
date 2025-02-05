package g.support.list;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.fragment.app.Fragment;

import g.api.adapter.GBaseAdapter;
import g.api.tools.T;
import g.support.GSupportUtils;
import g.support.list.R;

public abstract class GSwipeListFragment extends Fragment implements AdapterView.OnItemClickListener {

    protected MyPTRFatherSwipeListView lv_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(getRootLayoutRes(), container, false);
        setup(rootView);
        return T.getNoParentView(rootView);
    }

    final protected void setupListView(View rootView) {
        lv_list = (MyPTRFatherSwipeListView) rootView.findViewById(R.id.lv_list);
        float gd_modular = GSupportUtils.getThemeDimension(rootView.getContext(), R.attr.gd_modular, 20);
        lv_list.addHeaderView(GSupportUtils.getSimpleDivider(lv_list.getContext(), Color.TRANSPARENT, (int) gd_modular, false, false), null, false);
        lv_list.addFooterView(GSupportUtils.getSimpleDivider(lv_list.getContext(), Color.TRANSPARENT, (int) gd_modular, false, false), null, false);
        lv_list.setOnItemClickListener(this);

        GBaseAdapter adapter = getListAdapter();
        if (adapter != null) {
            lv_list.setAdapter(adapter);
        }
    }

    protected int getRootLayoutRes() {
        return R.layout.g_layout_swipe_list;
    }

    protected GBaseAdapter getListAdapter() {
        return null;
    }

    protected void setup(View rootView) {
        rootView.setBackgroundColor(GSupportUtils.getThemeColor(rootView.getContext(), R.attr.gc_background, Color.WHITE));
        setupListView(rootView);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
