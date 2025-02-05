package g.support.photo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import g.api.tools.T;
import g.api.views.dynamicgridview.DynamicGridView;
import g.api.views.dynamicgridview.DynamicGridViewIFaces;

public class PhotoSelectGridView extends DynamicGridView implements PhotoSelectManager.PhotoSelectDataListener {
    private PhotoSelectGridAdapter adapter;
    private Object mHolder;
    private boolean isFragment = false;
    private int totalNum = PhotoConfig.PHOTO_SELECT_DEFAULT_TOTAL_NUM;
    private boolean isAllowEdit = false;

    public PhotoSelectGridView(Context context) {
        super(context);
        setup(context);
    }

    public PhotoSelectGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context);
    }

    public PhotoSelectGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup(context);
    }

    /**
     * 参数只能是所在的FragmentActivity或者Fragment
     *
     * @param holder
     */
    public void setHolder(Object holder) {
        if (mHolder != null || holder == null)
            return;
        if (holder instanceof FragmentActivity) {
            isFragment = false;
            mHolder = holder;
        } else if (holder instanceof Fragment) {
            isFragment = true;
            mHolder = holder;
        }
    }

    public void setTotalNum(int totalNum) {
        if (totalNum > 0 && totalNum <= PhotoConfig.PHOTO_SELECT_DEFAULT_TOTAL_NUM) {
            this.totalNum = totalNum;
            adapter.setTotalNum(totalNum);
            adapter.notifyDataSetChanged();
        }
    }

    public void setItemWidth(int itemsWidth) {
        adapter.setItemWidth(itemsWidth);
        adapter.notifyDataSetChanged();
    }

    public void setIsAllowEdit(boolean isAllowEdit) {
        this.isAllowEdit = isAllowEdit;
    }

    private void setup(Context context) {
        setAddMode(true);
        PhotoSelectManager.getInstance().setSelectDataListener(this);
        adapter = new PhotoSelectGridAdapter(this);
        setSelector(new ColorDrawable(Color.TRANSPARENT));
        super.setAdapter(adapter);
        super.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (adapter.getItemViewType(position) == adapter.FLAG_ADD_ITEM) {
                    if (mHolder != null) {
                        if (mHolder instanceof FragmentActivity) {
                            new AlertDialog.Builder((FragmentActivity) mHolder).setItems(new CharSequence[]{"拍照", "相册", "取消"}, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    onListItemSelected(null, which, 9527);
                                }
                            }).show();
                        } else if (mHolder instanceof Fragment) {
                            new AlertDialog.Builder(((Fragment) mHolder).getActivity()).setItems(new CharSequence[]{"拍照", "相册", "取消"}, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    onListItemSelected(null, which, 9527);
                                }
                            }).show();
                        }
                    }
                } else {
                    if (mHolder != null) {
                        if (mHolder instanceof FragmentActivity) {
                            PhotoUtils.toBig(((FragmentActivity) mHolder), new ArrayList<String>(getDatas()), null, null, null, position, 0);
                        } else if (mHolder instanceof Fragment) {
                            PhotoUtils.toBig(((Fragment) mHolder).getActivity(), new ArrayList<String>(getDatas()), null, null, null, position, 0);
                        }
                    }
                }
            }
        });
        setOnDropListener(new DynamicGridViewIFaces.OnDropListener() {
            @Override
            public void onActionDrop() {
                stopEditMode();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setIsEditMode(false);
                        adapter.notifyDataSetChanged();
                    }
                }, 500);
            }
        });
        setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                boolean b = isAllowEdit && adapter.getItemViewType(position) == adapter.FLAG_DATA_ITEM;
                if (b) {
                    adapter.setIsEditMode(true);
                    adapter.notifyDataSetChanged();
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            startEditMode(position);
                        }
                    });
                }
                return b;
            }
        });
    }

    private void onListItemSelected(CharSequence value, int number, int requestCode) {
        if (mHolder != null) {
            PhotoAlbumData photoAlbumData = new PhotoAlbumData();
            if (adapter.hasDatas())
                photoAlbumData.datas = adapter.getDatas();
            if (mHolder instanceof FragmentActivity) {
                PhotoSelectManager.getInstance().onListItemSelected((FragmentActivity) mHolder, number, requestCode, photoAlbumData, totalNum);
            } else if (mHolder instanceof Fragment) {
                PhotoSelectManager.getInstance().onListItemSelected((Fragment) mHolder, number, requestCode, photoAlbumData, totalNum);
            }
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        PhotoSelectManager.getInstance().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSelectData(PhotoAlbumData photoAlbumData, int requestCode) {
        if (photoAlbumData != null && photoAlbumData.datas != null && photoAlbumData.datas.size() != 0)
            if (adapter != null) {
                switch (requestCode) {
                    case PhotoConfig.REQUEST_CODE_CAMERA: {
                        if (adapter.hasDatas())
                            adapter.getDatas().addAll(photoAlbumData.datas);
                        else
                            setDatas(photoAlbumData.datas);
                        T.mediaScanFile(getContext(), new File(photoAlbumData.datas.get(0).photoFilePath));
                        break;
                    }
                    case PhotoConfig.REQUEST_CODE_ALBUM: {
                        setDatas(photoAlbumData.datas);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }
    }

    private void setDatas(List<PhotoData> datas) {
        adapter.setDatas(datas);
    }

    public void setExDatas(List<String> datas) {
        List<PhotoData> list = new ArrayList<PhotoData>();
        for (String filePath : datas) {
            list.add(new PhotoData(filePath, PhotoConfig.REQUEST_CODE_OTHER, 0, true));
        }
        setDatas(list);
        adapter.notifyDataSetChanged();
    }

    @Deprecated
    @Override
    public void setAdapter(ListAdapter adapter) {
    }

    @Deprecated
    @Override
    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
    }

    /**
     * 获取选取之后的图片路径list
     *
     * @return
     */
    public List<String> getDatas() {
        List<PhotoData> photoDatas = adapter.getDatas();
        if (photoDatas == null || photoDatas.size() == 0)
            return null;
        List<String> returnList = new ArrayList<String>();
        for (PhotoData photoData : photoDatas)
            returnList.add(photoData.photoFilePath);
        return returnList;
    }
}
