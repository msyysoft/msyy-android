package g.api.adapter;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public abstract class GRecyclerViewHolder<MyAdapterData> extends RecyclerView.ViewHolder implements View.OnClickListener {

    protected MyAdapterData data;

    public GRecyclerViewHolder(View itemView, GRecyclerAdapter<MyAdapterData, ? extends RecyclerView.ViewHolder> adapter) {
        super(itemView);
        if (isOpenItemClick())
            itemView.setOnClickListener(this);
    }

    public void showData(int position, GRecyclerAdapter<MyAdapterData, ? extends RecyclerView.ViewHolder> adapter) {
        setData(adapter.getItem(position));
        itemView.setTag(position);
    }

    public boolean isOpenItemClick() {
        return false;
    }

    public void setData(MyAdapterData data) {
        this.data = data;
    }

    public MyAdapterData getData() {
        return data;
    }


    @Override
    public void onClick(View v) {
        onItemClick(v, data);
    }

    public void onItemClick(View v, MyAdapterData data) {
    }

}
