package g.support.list;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow;

import java.util.List;

import g.api.adapter.simple.GSimpleAdapter;
import g.api.adapter.simple.GSimpleViewHolder;


public class PopAbsListView<AdapterData> extends PopupWindow implements OnItemClickListener {
    private OnItemClickListener onItemClickListener;
    private AbsListView absListView;
    private GSimpleAdapter<AdapterData> adapter;

    public PopAbsListView(Context context, int width, int height, View contentView, AbsListView absListView, Class<? extends GSimpleViewHolder> viewHolderCls) {
        super(context);
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setWidth(width);
        setHeight(height);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (contentView!=null){
            setContentView(contentView);
        }else {
            setContentView(absListView);
        }
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

    public AbsListView getAbsListView() {
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
        this.dismiss();
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
    public void showAtLocation(View parent, int gravity, int x, int y) {
        if (showCheck())
            super.showAtLocation(parent, gravity, x, y);
    }

    @Override
    public void showAsDropDown(View anchor) {
        if (showCheck())
            super.showAsDropDown(anchor);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        if (showCheck())
            super.showAsDropDown(anchor, xoff, yoff);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        if (showCheck())
            super.showAsDropDown(anchor, xoff, yoff, gravity);
    }

}
