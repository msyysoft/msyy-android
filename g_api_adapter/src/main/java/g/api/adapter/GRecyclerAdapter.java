package g.api.adapter;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public abstract class GRecyclerAdapter<MyAdapterData, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements GAdapter<MyAdapterData> {
    protected List<MyAdapterData> datas;
    protected Context context;

    public GRecyclerAdapter(Context context) {
        this.context = context;
        this.datas = new ArrayList<>();
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
    public int getItemCount() {
        return getRealCount();
    }

    public MyAdapterData getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
}
