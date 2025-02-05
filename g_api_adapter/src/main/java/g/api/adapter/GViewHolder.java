package g.api.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
public abstract class GViewHolder<MyAdapterData> {
    protected Context context;
    private View convertView;

    public GViewHolder(Context context) {
        this.context = context;
        @LayoutRes int id = onSetConvertViewResId();
        if (id != 0)
            convertView = LayoutInflater.from(context).inflate(id, null);
    }

    protected abstract
    @LayoutRes
    int onSetConvertViewResId();

    public View getConvertView() {
        return convertView;
    }

    protected void setConvertView(View convertView) {
        this.convertView = convertView;
    }

    protected View findViewById(int id) {
        return convertView.findViewById(id);
    }

    protected <T extends View> T findView(@IdRes int id) {
        return (T) findViewById(id);
    }

    public void showData(int position, GBaseAdapter<MyAdapterData> adapter){

    }
}
