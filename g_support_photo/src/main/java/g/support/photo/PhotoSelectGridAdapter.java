package g.support.photo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import g.api.tools.T;
import g.api.views.dynamicgridview.DynamicGridAdapter;

class PhotoSelectGridAdapter extends DynamicGridAdapter<PhotoData> {
    final public static int FLAG_ADD_ITEM = 0;
    final public static int FLAG_DATA_ITEM = 1;
    private DisplayImageOptions options;
    private int totalNum = PhotoConfig.PHOTO_SELECT_DEFAULT_TOTAL_NUM;
    private int itemsWidth = 0;
    private boolean isEditMode = false;

    public PhotoSelectGridAdapter(AbsListView absListView) {
        super(absListView.getContext(), 4);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(new ColorDrawable(Color.TRANSPARENT))
                .cacheInMemory(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    public void setTotalNum(int totalNum) {
        if (totalNum > 0 && totalNum <= PhotoConfig.PHOTO_SELECT_DEFAULT_TOTAL_NUM)
            this.totalNum = totalNum;
    }

    public void setItemWidth(int itemsWidth) {
        this.itemsWidth = itemsWidth;
    }

    public boolean isDatasFull() {
        if (hasDatas() && getRealCount() >= totalNum)
            return true;
        if (isEditMode)
            return true;
        return false;
    }

    public void setIsEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
    }

    @Override
    public int getCount() {
        if (isDatasFull())
            return getRealCount();
        return getRealCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {

        if (hasDatas()) {
            if (isDatasFull())
                return FLAG_DATA_ITEM;
            if (position == getRealCount())
                return FLAG_ADD_ITEM;
            return FLAG_DATA_ITEM;
        }
        return FLAG_ADD_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        switch (getItemViewType(position)) {
            case FLAG_ADD_ITEM: {
                HolderView mAddHolderView = null;
                if (convertView == null) {
                    mAddHolderView = new HolderView();
                    convertView = LayoutInflater.from(context).inflate(R.layout.photo_adapter_grid_photo_select, null);
                    mAddHolderView.iv_item = (ImageView) convertView.findViewById(R.id.iv_item);
                    mAddHolderView.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
                    mAddHolderView.iv_item.setImageDrawable(T.getDrawableSelector(context.getResources().getDrawable(R.mipmap.photo_ic_add_big_on), context.getResources().getDrawable(R.mipmap.photo_ic_add_big_off)));
                    mAddHolderView.iv_delete.setVisibility(View.GONE);
                    if (itemsWidth <= 0) {
                        itemsWidth = (context.getResources().getDisplayMetrics().widthPixels - T.dip2px(context, 10 + 8 + 5 + 5 + 8 + 10)) / 3;
                    }
                    RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) mAddHolderView.iv_item.getLayoutParams();
                    lParams.width = itemsWidth;
                    lParams.height = itemsWidth;
                    convertView.setTag(mAddHolderView);
                } else {
                    mAddHolderView = (HolderView) convertView.getTag();
                }
                return convertView;
            }
            default: {
                PhotoData photoData = getItem(position);
                HolderView mHolderView = null;
                if (convertView == null) {
                    mHolderView = new HolderView();
                    convertView = LayoutInflater.from(context).inflate(R.layout.photo_adapter_grid_photo_select, null);
                    mHolderView.iv_item = (ImageView) convertView.findViewById(R.id.iv_item);
                    mHolderView.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
                    mHolderView.iv_delete.setVisibility(View.VISIBLE);
                    if (itemsWidth <= 0) {
                        itemsWidth = (context.getResources().getDisplayMetrics().widthPixels - T.dip2px(context, 10 + 8 + 5 + 5 + 8 + 10)) / 3;
                    }
                    RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) mHolderView.iv_item.getLayoutParams();
                    lParams.width = itemsWidth;
                    lParams.height = itemsWidth;
                    convertView.setTag(mHolderView);
                } else {
                    mHolderView = (HolderView) convertView.getTag();
                }
                mHolderView.iv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        remove(getItem(position));
                    }
                });
                ImageLoader.getInstance().displayImage(PhotoUtils.getDisplayUri(photoData.photoFilePath), mHolderView.iv_item, options);
                return convertView;
            }
        }
    }

    private static class HolderView {
        ImageView iv_item;
        ImageView iv_delete;
    }
}