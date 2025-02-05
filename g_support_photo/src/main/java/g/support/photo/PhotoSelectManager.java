package g.support.photo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import g.api.tools.T;

/**
 * 照片选取管家
 */
class PhotoSelectManager {

    /**
     * 照片动作监听器
     */
    public interface PhotoSelectDataListener {
        void onSelectData(PhotoAlbumData photoAlbumData, int requestCode);
    }

    public interface PhotoCropDataListener {
        void onCropData(Bitmap bitmap);
    }

    private PhotoSelectDataListener selectDataListener;
    private PhotoCropDataListener cropDataListener;
    private static PhotoSelectManager sInstance;

    private File mPhotoDir;
    private File mPhotoTempFile;

    private PhotoSelectManager() {

    }

    public static PhotoSelectManager getInstance() {
        if (sInstance == null) {
            sInstance = new PhotoSelectManager();
        }
        return sInstance;
    }

    public PhotoSelectManager setSelectDataListener(PhotoSelectDataListener selectDataListener) {
        this.selectDataListener = selectDataListener;
        return this;
    }

    public PhotoSelectManager setCropDataListener(PhotoCropDataListener cropDataListener) {
        this.cropDataListener = cropDataListener;
        return this;
    }

    public void onListItemSelected(Fragment fragment, int number, int requestCode, PhotoAlbumData selectData, int totalNum) {
        Context context = fragment.getActivity();
        if (context == null)
            return;
        if (mPhotoDir == null)
            mPhotoDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        if (!mPhotoDir.exists())
            mPhotoDir.mkdirs();
        if (requestCode == 9527) {
            switch (number) {
                case 0:
                    mPhotoTempFile = new File(mPhotoDir, "IMG_" + T.getFormatTime("yyyyMMdd_HHmmss") + "_R.jpg");
                    PhotoUtils.toCamera(fragment, mPhotoTempFile, PhotoConfig.REQUEST_CODE_CAMERA);
                    break;
                case 1:
                    PhotoUtils.toAlbum(fragment, PhotoConfig.REQUEST_CODE_ALBUM, selectData, totalNum);
                    break;
            }
        }
    }

    public void onListItemSelected(FragmentActivity activity, int number, int requestCode, PhotoAlbumData selectData, int totalNum) {
        if (activity == null)
            return;
        if (mPhotoDir == null)
            mPhotoDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        if (!mPhotoDir.exists())
            mPhotoDir.mkdirs();
        if (requestCode == 9527) {
            switch (number) {
                case 0:
                    mPhotoTempFile = new File(mPhotoDir, "IMG_" + T.getFormatTime("yyyyMMdd_HHmmss") + "_R.jpg");
                    PhotoUtils.toCamera(activity, mPhotoTempFile, PhotoConfig.REQUEST_CODE_CAMERA);
                    break;
                case 1:
                    PhotoUtils.toAlbum(activity, PhotoConfig.REQUEST_CODE_ALBUM, selectData, totalNum);
                    break;
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (selectDataListener == null) {
            return;
        }
        switch (requestCode) {
            case PhotoConfig.REQUEST_CODE_CAMERA: {
                List<PhotoData> resultList = new ArrayList<PhotoData>();
                resultList.add(new PhotoData(mPhotoTempFile.getAbsolutePath(), PhotoConfig.REQUEST_CODE_CAMERA, mPhotoTempFile.length(), true));
                selectDataListener.onSelectData(new PhotoAlbumData(resultList), requestCode);
                break;
            }
            case PhotoConfig.REQUEST_CODE_ALBUM: {
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        PhotoAlbumData photoAlbumData = (PhotoAlbumData) bundle.getSerializable(PhotoConfig.PHOTO_SELECT_DATAS_STR);
                        selectDataListener.onSelectData(photoAlbumData, requestCode);
                    }
                }
                break;
            }
            case PhotoConfig.REQUEST_CODE_PREVIEW: {
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        PhotoAlbumData photoAlbumData = (PhotoAlbumData) bundle.getSerializable(PhotoConfig.PHOTO_SELECT_DATAS_STR);
                        selectDataListener.onSelectData(photoAlbumData, requestCode);
                    }
                }
                break;
            }
            case PhotoConfig.REQUEST_CODE_CROP: {
                if (data != null && cropDataListener != null) {
                    Bitmap bmp = data.getParcelableExtra("data");
                    cropDataListener.onCropData(bmp);
                }
            }
        }
    }
}
