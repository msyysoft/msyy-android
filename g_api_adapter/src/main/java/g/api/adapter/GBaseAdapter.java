package g.api.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class GBaseAdapter<MyAdapterData> extends BaseAdapter implements GAdapter<MyAdapterData> {
    protected List<MyAdapterData> datas;
    protected Object tag;
    protected Bundle args;

    @Override
    public void setDatas(List<MyAdapterData> datas) {
        this.datas = datas;
    }

    @Override
    public void addDatas(List<MyAdapterData> datas) {
        if (this.datas != null)
            this.datas.addAll(datas);
    }

    @Override
    public List<MyAdapterData> getDatas() {
        if (datas == null)
            return new ArrayList<MyAdapterData>();
        return datas;
    }

    @Override
    public boolean hasDatas() {
        return datas != null && datas.size() > 0;
    }

    @Override
    public int getRealCount() {
        if (hasDatas())
            return datas.size();
        return 0;
    }

    @Override
    public int getCount() {
        return getRealCount();
    }

    @Override
    public MyAdapterData getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public Bundle getArgs() {
        return args;
    }

    public void setArgs(Bundle args) {
        this.args = args;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (isSimpleMode()) {
            GViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = genViewHolder(parent.getContext(), position);
                if (viewHolder == null)
                    return convertView;
                convertView = viewHolder.getConvertView();
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (GViewHolder) convertView.getTag();
            }
            viewHolder.showData(position, this);
        }
        return convertView;
    }

    protected boolean isSimpleMode() {
        return false;
    }

    protected GViewHolder genViewHolder(Context context, int position) {
        return null;
    }


}
