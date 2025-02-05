package g.support.photo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;

import java.io.File;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import g.api.tools.T;

public class PhotoSelectImageView extends androidx.appcompat.widget.AppCompatImageView implements PhotoSelectManager.PhotoSelectDataListener, PhotoSelectManager.PhotoCropDataListener {
    private Object mHolder;
    private boolean isFragment = false;
    private int totalNum = 1;
    private String title;

    public PhotoSelectImageView(Context context) {
        super(context);
        setup(context);
    }

    public PhotoSelectImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context);
    }

    public PhotoSelectImageView(Context context, AttributeSet attrs, int defStyle) {
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

    public void setTitle(String title) {
        this.title = title;
    }


    private void setup(Context context) {
        PhotoSelectManager.getInstance().setSelectDataListener(this).setCropDataListener(this);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doClick();
            }
        });
    }

    public void doClick() {
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
    }

    private void onListItemSelected(CharSequence value, int number, int requestCode) {
        if (mHolder != null) {
            if (mHolder instanceof FragmentActivity) {
                PhotoSelectManager.getInstance().onListItemSelected((FragmentActivity) mHolder, number, requestCode, null, totalNum);
            } else if (mHolder instanceof Fragment) {
                PhotoSelectManager.getInstance().onListItemSelected((Fragment) mHolder, number, requestCode, null, totalNum);
            }
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        PhotoSelectManager.getInstance().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSelectData(PhotoAlbumData photoAlbumData, int requestCode) {
        if (photoAlbumData != null && photoAlbumData.datas != null && photoAlbumData.datas.size() != 0) {
            if (pl != null) {
                String photoFilePath = photoAlbumData.datas.get(0).photoFilePath;
                boolean isNeedCrop = pl.onSelectData(photoFilePath, requestCode);
                if (isNeedCrop) {
                    if (mHolder != null) {
                        if (mHolder instanceof FragmentActivity) {
                            PhotoUtils.toCrop((FragmentActivity) mHolder, Uri.parse("file://" + photoFilePath), 1, 1, 200, 200);
                        } else if (mHolder instanceof Fragment) {
                            PhotoUtils.toCrop((Fragment) mHolder, Uri.parse("file://" + photoFilePath), 1, 1, 200, 200);
                        }
                    }
                }
            }
            if (requestCode == PhotoConfig.REQUEST_CODE_CAMERA)
                T.mediaScanFile(getContext(), new File(photoAlbumData.datas.get(0).photoFilePath));
        }
    }

    @Override
    public void onCropData(Bitmap bitmap) {
        if (bitmap != null) {
            File path = T.getCacheDir(getContext());
            String filePath = path.getAbsolutePath() + "/IMG_CROP_" + T.getFormatTime("yyyyMMdd_HHmmss") + "_R.jpg";
            T.saveBitmapDefault(bitmap, filePath);
            if (pl != null) {
                pl.onCropData(filePath);
            }
        }

    }

    private PhotoExDataListener pl;

    public void setOnPhotoSelectDataListener(PhotoExDataListener pl) {
        this.pl = pl;
    }


    public interface PhotoExDataListener {
        /**
         * 返回拍照或选取单张图片路径
         *
         * @param photoFilePath
         * @param requestCode
         * @return 是否需要裁剪
         */
        boolean onSelectData(String photoFilePath, int requestCode);

        /**
         * 返回裁剪后的图片路径
         *
         * @param photoFilePath
         */
        void onCropData(String photoFilePath);
    }
}
