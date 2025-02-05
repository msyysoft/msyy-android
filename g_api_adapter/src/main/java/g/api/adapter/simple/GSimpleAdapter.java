package g.api.adapter.simple;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import g.api.adapter.GBaseAdapter;

/**
 * A adapter using View Holder to display the item of a list view;
 *
 * @param <MyAdapterData>
 * @author http://www.liaohuqiu.net
 */
public class GSimpleAdapter<MyAdapterData> extends GBaseAdapter<MyAdapterData> {

    protected GSimpleVHCreatorSuper<MyAdapterData> mViewHolderCreator;
    protected GSimpleVHCreatorSuper<MyAdapterData> mLazyCreator;
    protected boolean mForceCreateView = false;

    public GSimpleAdapter() {
    }

    public GSimpleAdapter(final Object enclosingInstance, final Class<?> cls) {
        setViewHolderClass(enclosingInstance, cls);
    }

    /**
     * @param viewHolderCreator The view holder creator will create a View Holder that extends {@link GSimpleViewHolder}
     */
    public GSimpleAdapter(GSimpleVHCreatorSuper<MyAdapterData> viewHolderCreator) {
        mViewHolderCreator = viewHolderCreator;
    }

    public void setViewHolderCreator(GSimpleVHCreatorSuper<MyAdapterData> viewHolderCreator) {
        mViewHolderCreator = viewHolderCreator;
    }

    public void setViewHolderClass(final Object enclosingInstance, final Class<?> cls, final Object... args) {
        mLazyCreator = GSimpleVHCreator.create(enclosingInstance, cls, args);
    }

    public GSimpleViewHolder<MyAdapterData> createViewHolder(int position) {
        if (mViewHolderCreator == null && mLazyCreator == null) {
            throw new RuntimeException("view holder creator is null");
        }
        if (mViewHolderCreator != null) {
            return mViewHolderCreator.createViewHolder(position);
        }
        if (mLazyCreator != null) {
            return mLazyCreator.createViewHolder(position);
        }
        return null;
    }

    public void forceCreateView(boolean yes) {
        mForceCreateView = yes;
    }

    @SuppressWarnings("unchecked")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GSimpleViewHolder<MyAdapterData> holderBase = null;
        if (mForceCreateView || convertView == null || (!(convertView.getTag() instanceof GSimpleViewHolder<?>))) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            holderBase = createViewHolder(position);
            if (holderBase != null) {
                convertView = holderBase.createView(inflater, this);
                if (convertView != null) {
                    if (!mForceCreateView) {
                        convertView.setTag(holderBase);
                    }
                }
            }
        } else {
            holderBase = (GSimpleViewHolder<MyAdapterData>) convertView.getTag();
        }
        if (holderBase != null) {
            holderBase.setItemData(position, convertView);
            holderBase.showData(position, GSimpleAdapter.this);
        }
        return convertView;
    }
}
