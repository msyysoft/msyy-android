package g.support.list;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;

import java.util.List;

import g.api.adapter.simple.GSimpleAdapter;
import g.api.adapter.simple.GSimpleViewHolder;


public class AbsListView<AdapterData> extends FrameLayout implements OnItemClickListener {
    private OnItemClickListener onItemClickListener;
    private android.widget.AbsListView absListView;
    private GSimpleAdapter<AdapterData> adapter;

    public AbsListView(Context context, android.widget.AbsListView absListView, Class<? extends GSimpleViewHolder> viewHolderCls) {
        super(context);

        this.absListView = absListView;
        this.absListView.setOnItemClickListener(this);
        adapter = new GSimpleAdapter<AdapterData>();
        adapter.setViewHolderClass(this, viewHolderCls);
        this.absListView.setAdapter(adapter);
    }

    public void setDatas(List<AdapterData> dataList) {
        adapter.setDatas(dataList);
        adapter.notifyDataSetChanged();
    }

    public android.widget.AbsListView getAbsListView() {
        return absListView;
    }

    public AdapterData getItem(int position) {
        return adapter.getItem(position);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(parent, view, position, id);
        }
        setVisibility(GONE);
    }

    /**
     * @param onItemClickListener the onItemClickListener to set
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    protected boolean showCheck() {
        return adapter != null && adapter.getCount() > 0;
    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility==VISIBLE)
        {
            if (showCheck())
                super.setVisibility(visibility);
        }else {

        }
    }
}
