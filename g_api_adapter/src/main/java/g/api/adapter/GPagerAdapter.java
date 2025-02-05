package g.api.adapter;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import g.api.adapter.lib.RecyclingPagerAdapter;


public abstract class GPagerAdapter<MyAdapterData> extends RecyclingPagerAdapter implements GAdapter<MyAdapterData> {
    protected List<MyAdapterData> datas;
    protected Context context;

    public GPagerAdapter(Context context) {
        this.context = context;
    }

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

    public MyAdapterData getItem(int i) {
        return datas.get(i);
    }
}
