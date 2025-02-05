package g.api.views.dynamicgridview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import g.api.adapter.GAdapter;

public class DynamicGridAdapter<MyAdatperData> extends BaseDynamicGridAdapter implements GAdapter<MyAdatperData> {
    protected DynamicGridAdapter(Context context, int columnCount) {
        super(context, columnCount);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public void setDatas(List<MyAdatperData> myAdatperDatas) {
        set(myAdatperDatas);
    }

    @Override
    public void addDatas(List<MyAdatperData> myAdatperDatas) {
        add(myAdatperDatas);
    }

    @Override
    public List<MyAdatperData> getDatas() {
        return (List<MyAdatperData>) getItems();
    }

    @Override
    public boolean hasDatas() {
        return getItems() != null && getItems().size() > 0;
    }

    @Override
    public int getRealCount() {
        if (hasDatas())
            return getItems().size();
        return 0;
    }

    @Override
    public MyAdatperData getItem(int position) {
        return (MyAdatperData) super.getItem(position);
    }
}
