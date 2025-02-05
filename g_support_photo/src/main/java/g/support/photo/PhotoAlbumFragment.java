package g.support.photo;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import g.api.adapter.GBaseAdapter;
import g.api.event.GEvent;
import g.api.event.GEventStatus;
import g.api.tools.T;
import g.support.loading.SimpleDialog;

/**
 * 相册
 */
public class PhotoAlbumFragment extends Fragment {
    private PhotoAlbumData photoAlbumData;
    private Map<String, PhotoData> selectMap;
    private TextView tv_preview, tv_ok_button;
    private PhotoAlbumGridAdapter adapter;
    private int totalNum = PhotoConfig.PHOTO_SELECT_DEFAULT_TOTAL_NUM;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        PhotoAlbumData selectData = null;
        if (args != null) {
            photoAlbumData = (PhotoAlbumData) args.getSerializable(PhotoConfig.PHOTO_ALBUM_DATAS_STR);
            Serializable serializable = args.getSerializable(PhotoConfig.PHOTO_SELECT_DATAS_STR);
            if (serializable != null) {
                selectData = (PhotoAlbumData) args.getSerializable(PhotoConfig.PHOTO_SELECT_DATAS_STR);
            }
            totalNum = args.getInt(PhotoConfig.PHOTO_SELECT_TOTAL_NUM, PhotoConfig.PHOTO_SELECT_DEFAULT_TOTAL_NUM);
        }
        if (selectData == null)
            selectData = new PhotoAlbumData();

        fitDatas(selectData);
    }

    private void fitDatas(PhotoAlbumData selectData) {
        if (selectMap != null) {
            selectMap.clear();
            selectMap = null;
        }
        selectMap = new LinkedHashMap<String, PhotoData>();
        for (PhotoData dataAlbum : photoAlbumData.datas) {
            dataAlbum.isSelect = false;
        }
        loop:
        for (PhotoData dataSelect : selectData.datas) {
            selectMap.put(dataSelect.photoFilePath, dataSelect);
            for (PhotoData dataAlbum : photoAlbumData.datas) {
                if (dataSelect.photoFilePath.equals(dataAlbum.photoFilePath)) {
                    dataAlbum.isSelect = true;
                    selectMap.put(dataAlbum.photoFilePath, dataAlbum);
                    continue loop;
                }
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case PhotoConfig.REQUEST_CODE_PREVIEW: {
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        PhotoAlbumData photoAlbumData = (PhotoAlbumData) bundle.getSerializable(PhotoConfig.PHOTO_SELECT_DATAS_STR);
                        if (photoAlbumData != null && photoAlbumData.datas != null) {
                            fitDatas(photoAlbumData);
                            updateButtonStatus();
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
                break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.photo_fragment_photo_album, container, false);
        setup(rootView);
        return T.getNoParentView(rootView);
    }

    private void setup(View view) {
        TextView tv_top_title = (TextView) view.findViewById(R.id.tv_top_title);
        if (photoAlbumData != null)
            tv_top_title.setText(photoAlbumData.albumName);
        view.findViewById(R.id.iv_top_back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        tv_preview = (TextView) view.findViewById(R.id.tv_preview);
        tv_preview.setTextColor(T.getColorStateList(PhotoConfig.PHOTO_THEME_COLOR, 0xFF000000, 0xFF000000, 0xFFCCCCCC));
        tv_preview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectMap.size() != 0) {
                    ArrayList<PhotoData> list = new ArrayList<PhotoData>();
                    Iterator iterator = selectMap.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry entry = (Map.Entry) iterator.next();
                        list.add((PhotoData) entry.getValue());
                    }
                    PhotoUtils.toPreview(PhotoAlbumFragment.this, PhotoConfig.REQUEST_CODE_PREVIEW, new PhotoAlbumData(list));
                }
            }
        });

        tv_ok_button = (TextView) view.findViewById(R.id.tv_ok_button);
        tv_ok_button.setTextColor(T.getColorStateList(0xFFFFFFFF, 0xFF000000, 0xFF000000, 0xFFCCCCCC));
        GradientDrawable selectedDrawable = new GradientDrawable();
        selectedDrawable.setColor(PhotoConfig.PHOTO_THEME_COLOR);
        selectedDrawable.setAlpha(0xCC);
        selectedDrawable.setCornerRadius(T.dip2px(view.getContext(), 5f));
        GradientDrawable unSelectDrawable = new GradientDrawable();
        unSelectDrawable.setColor(PhotoConfig.PHOTO_THEME_COLOR);
        unSelectDrawable.setCornerRadius(T.dip2px(view.getContext(), 5f));
        if (Build.VERSION.SDK_INT >= 16) {
            tv_ok_button.setBackground(T.getDrawableSelector(selectedDrawable, unSelectDrawable));
        } else {
            tv_ok_button.setBackgroundDrawable(T.getDrawableSelector(selectedDrawable, unSelectDrawable));
        }
        updateButtonStatus();
        tv_ok_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if (selectMap.size() != 0) {
                    ArrayList<PhotoData> returnList = new ArrayList<PhotoData>();
                    Iterator iterator = selectMap.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry entry = (Map.Entry) iterator.next();
                        returnList.add((PhotoData) entry.getValue());
                    }
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PhotoConfig.PHOTO_SELECT_DATAS_STR, new PhotoAlbumData(returnList));
                    intent.putExtras(bundle);
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                } else {
                    T.showToast(v.getContext(), "您未选取任何图片");
                }
            }
        });
        GridView gv_album = (GridView) view.findViewById(R.id.gv_album);
        adapter = new PhotoAlbumGridAdapter(onItemClickClass);
        adapter.setDatas(photoAlbumData.datas);
        gv_album.setAdapter(adapter);
    }

    private void updateButtonStatus() {
        int selectNum = selectMap.size();
        if (selectNum == 0) {
            tv_preview.setEnabled(false);
            tv_preview.setClickable(false);
        } else {
            tv_preview.setEnabled(true);
            tv_preview.setClickable(true);
        }
        String text = "确定" + "(" + selectNum + "/" + totalNum + ")";
        tv_ok_button.setText(text);
    }

    private OnItemClickClass onItemClickClass = new OnItemClickClass() {
        @Override
        public void OnItemClick(View v, int position, BaseAdapter adapter, CheckBox checkBox, ImageView imageView) {
            PhotoData photoData = photoAlbumData.datas.get(position);

            if (photoData.isSelect) {
                selectMap.remove(photoData.photoFilePath);
                photoData.isSelect = false;
                checkBox.setChecked(false);
                imageView.setVisibility(photoData.isSelect ? View.VISIBLE : View.INVISIBLE);
                updateButtonStatus();

            } else {
                if (selectMap.size() >= totalNum) {
                    T.showToast(v.getContext(), "图片数量已达上限", true);
                    return;
                }
                selectMap.put(photoData.photoFilePath, photoData);
                photoData.isSelect = true;
                checkBox.setChecked(true);
                imageView.setVisibility(photoData.isSelect ? View.VISIBLE : View.INVISIBLE);
                updateButtonStatus();
            }
        }
    };

    private interface OnItemClickClass {
        void OnItemClick(View v, int Position, BaseAdapter adapter, CheckBox checkBox, ImageView imageView);
    }

    private static class PhotoAlbumGridAdapter extends GBaseAdapter<PhotoData> {

        private OnItemClickClass onItemClickClass;
        private DisplayImageOptions options;
        private ArrayList<String> bigList = new ArrayList<String>();

        public PhotoAlbumGridAdapter(OnItemClickClass onItemClickClass) {
            this.onItemClickClass = onItemClickClass;
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(new ColorDrawable(Color.TRANSPARENT))
                    .cacheInMemory(true)
                    .considerExifParams(true)
                    .displayer(new FadeInBitmapDisplayer(100))
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        }

        @Override
        public int getCount() {
            return getRealCount();
        }

        @Override
        public void setDatas(List<PhotoData> photoDatas) {
            super.setDatas(photoDatas);
            bigList.clear();
            for (PhotoData data : photoDatas) {
                bigList.add(data.photoFilePath);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holderView;
            Context context = parent.getContext();
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.photo_adapter_grid_photo_album_item, null);
                holderView = new ViewHolder();
                holderView.iv_item_0 = (ImageView) convertView.findViewById(R.id.iv_item_0);
                holderView.iv_item_1 = (ImageView) convertView.findViewById(R.id.iv_item_1);
                holderView.cb_select = (CheckBox) convertView.findViewById(R.id.cb_select);
                holderView.rl_check = (RelativeLayout) convertView.findViewById(R.id.rl_check);
                int itemsWidth = context.getResources().getDisplayMetrics().widthPixels - T.dip2px(context, 3 + 3 + 3 + 3);
                RelativeLayout.LayoutParams lParams_0 = (RelativeLayout.LayoutParams) holderView.iv_item_0.getLayoutParams();
                RelativeLayout.LayoutParams lParams_1 = (RelativeLayout.LayoutParams) holderView.iv_item_1.getLayoutParams();
                lParams_0.width = itemsWidth / 3;
                lParams_1.width = itemsWidth / 3;
                lParams_0.height = itemsWidth / 3;
                lParams_1.height = itemsWidth / 3;
                lParams_0.height = itemsWidth / 3;
                convertView.setTag(holderView);
            } else {
                holderView = (ViewHolder) convertView.getTag();
            }
            PhotoData photoData = getItem(position);
            holderView.iv_item_0.setTag(position);
            ImageLoader.getInstance().displayImage(PhotoUtils.getDisplayUri(photoData.photoFilePath), holderView.iv_item_0, options);
            holderView.cb_select.setChecked(photoData.isSelect);
            holderView.iv_item_1.setVisibility(photoData.isSelect ? View.VISIBLE : View.INVISIBLE);
            holderView.rl_check.setOnClickListener(new OnPhotoClick(position, this, holderView.cb_select, holderView.iv_item_1, onItemClickClass));
            holderView.iv_item_0.setOnClickListener(onPhotoClickListener);
            holderView.iv_item_0.setOnLongClickListener(onPhotoLongClickListener);
            return convertView;
        }

        private OnClickListener onPhotoClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = Integer.parseInt(v.getTag().toString());
                PhotoUtils.toBig(v.getContext(),bigList,null,null,null,position,0);
            }
        };

        private View.OnLongClickListener onPhotoLongClickListener= new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = Integer.parseInt(v.getTag().toString());
                final PhotoData d = getItem(position);
                if (!d.isSelect) {
                    AlertDialog dialog =  SimpleDialog.createDialog(v.getContext(), "提示", "是否要删除图片？", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new GEvent(PhotoConfig.PHOTO_DELETE).postWithType(GEventStatus.SUCC);
                            File file = new File(d.photoFilePath);
                            file.delete();
                            getDatas().remove(d);
                            bigList.remove(d.photoFilePath);
                            notifyDataSetChanged();
                        }
                    }, true);
                    if (dialog!=null)
                        dialog.show();
                }
                return true;
            }
        };

        private static class ViewHolder {
            ImageView iv_item_0, iv_item_1;
            CheckBox cb_select;
            RelativeLayout rl_check;
        }

        private static class OnPhotoClick implements OnClickListener {
            private int position;
            private BaseAdapter adapter;
            private OnItemClickClass onItemClickClass;
            private CheckBox checkBox;
            private ImageView imageView;

            public OnPhotoClick(int position, BaseAdapter adapter, CheckBox checkBox, ImageView imageView, OnItemClickClass onItemClickClass) {
                this.position = position;
                this.adapter = adapter;
                this.checkBox = checkBox;
                this.imageView = imageView;
                this.onItemClickClass = onItemClickClass;
            }

            @Override
            public void onClick(View v) {
                if (onItemClickClass != null) {
                    onItemClickClass.OnItemClick(v, position, adapter, checkBox, imageView);
                }
            }
        }
    }
}
