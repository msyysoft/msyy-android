package g.support.photo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import g.support.app.FragmentShellActivity;

public class PhotoUtils {
    /**
     * 获得裁剪的Intent
     *
     * @param uri
     * @param aspectX
     * @param aspectY
     * @param outputX
     * @param outputY
     */
    public static Intent toCropIntent(Uri uri, int aspectX, int aspectY, int outputX, int outputY) {
        Intent intent = new Intent();
        intent.setAction("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("return-data", true);
        return intent;
    }

    /**
     * 裁剪
     *
     * @param activity
     * @param uri
     * @param aspectX
     * @param aspectY
     * @param outputX
     * @param outputY
     */
    public static void toCrop(Activity activity, Uri uri, int aspectX, int aspectY, int outputX, int outputY) {
        Intent intent = toCropIntent(uri, aspectX, aspectY, outputX, outputY);
        activity.startActivityForResult(intent, PhotoConfig.REQUEST_CODE_CROP);
    }

    /**
     * 裁剪
     *
     * @param fragment
     * @param uri
     * @param aspectX
     * @param aspectY
     * @param outputX
     * @param outputY
     */
    public static void toCrop(Fragment fragment, Uri uri, int aspectX, int aspectY, int outputX, int outputY) {
        Intent intent = toCropIntent(uri, aspectX, aspectY, outputX, outputY);
        fragment.startActivityForResult(intent, PhotoConfig.REQUEST_CODE_CROP);
    }

    /**
     * 相机是否可用
     *
     * @param context
     * @return
     */
    public static boolean isCameraUseAble(Context context) {
        PackageManager packageManager = context.getPackageManager();
        return packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    /**
     * 获得拍照Intent，不能拍照返回null
     *
     * @param context
     * @param outputFile
     * @return
     */
    public static Intent toCameraIntent(Context context, File outputFile) {
        if (!isCameraUseAble(context)) {
            Toast.makeText(context, "抱歉，我们发现您的手机貌似没有摄像头", Toast.LENGTH_LONG).show();
            return null;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfoList == null || resolveInfoList.size() == 0) {
            Toast.makeText(context, "抱歉，暂时无法使用相机", Toast.LENGTH_LONG).show();
            return null;
        }
        Uri imageUri = Uri.fromFile(outputFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        return intent;
    }

    /**
     * 拍照
     *
     * @param activity
     * @param outputFile
     * @param requestCode
     */
    public static void toCamera(Activity activity, File outputFile, int requestCode) {
        Intent intent = toCameraIntent(activity, outputFile);
        if (intent != null)
            activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 拍照
     *
     * @param fragment
     * @param outputFile
     * @param requestCode
     */
    public static void toCamera(Fragment fragment, File outputFile, int requestCode) {
        Intent intent = toCameraIntent(fragment.getActivity(), outputFile);
        if (intent != null)
            fragment.startActivityForResult(intent, requestCode);
    }


    /**
     * 打开自定义相册
     *
     * @param fragment
     * @param requestCode
     * @param selectData
     */
    public static void toAlbum(Fragment fragment, int requestCode, PhotoAlbumData selectData, int totalNum) {
        Bundle args = new Bundle();
        args.putSerializable(PhotoConfig.PHOTO_SELECT_DATAS_STR, selectData);
        args.putInt(PhotoConfig.PHOTO_SELECT_TOTAL_NUM, totalNum);
        Intent intent = FragmentShellActivity.createIntent(fragment.getActivity(), PhotoAlbumListFragment.class, args);
        if (intent != null)
            fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * 打开自定义相册
     *
     * @param activity
     * @param requestCode
     * @param selectData
     */
    public static void toAlbum(Activity activity, int requestCode, PhotoAlbumData selectData, int totalNum) {
        Bundle args = new Bundle();
        args.putSerializable(PhotoConfig.PHOTO_SELECT_DATAS_STR, selectData);
        args.putInt(PhotoConfig.PHOTO_SELECT_TOTAL_NUM, totalNum);
        Intent intent = FragmentShellActivity.createIntent(activity, PhotoAlbumListFragment.class, args);
        if (intent != null)
            activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 打开预览
     *
     * @param fragment
     * @param requestCode
     * @param selectData
     */
    public static void toPreview(Fragment fragment, int requestCode, PhotoAlbumData selectData) {
        Bundle args = new Bundle();
        args.putSerializable(PhotoConfig.PHOTO_SELECT_DATAS_STR, selectData);
        Intent intent = FragmentShellActivity.createIntent(fragment.getActivity(), PhotoPreviewFragment.class, args);
        if (intent != null)
            fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * 查看大图
     *
     * @param context
     * @param photos
     */
    public static void toBig(Context context, ArrayList<String> photos, int position) {
        toBig(context, photos, null, null, null, position, 0);
    }

    /**
     * 查看大图
     */
    public static void toBig(Context context, ArrayList<String> photos_1, ArrayList<String> photos_2, String title_1, String title_2, int position_1, int position_2) {
        Bundle args = new Bundle();
        args.putStringArrayList("PHOTOS_LIST_1", photos_1);
        args.putStringArrayList("PHOTOS_LIST_2", photos_2);
        args.putString("TITLE_1", title_1);
        args.putString("TITLE_2", title_2);
        args.putInt("POSITION_1", position_1);
        args.putInt("POSITION_2", position_2);
        Intent intent = FragmentShellActivity.createIntent(context, PhotoBigFragment.class, args);
        if (intent != null) {
            context.startActivity(intent);
        }
    }

    /**
     * 获取全部图片地址
     *
     * @return
     */
    public static List<PhotoData> getAllPhotosFromDisk(Context context) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Uri uri = intent.getData();
        List<PhotoData> list = new ArrayList<PhotoData>();
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);//managedQuery(uri, proj, null, null, null);
        try {
            while (cursor.moveToNext()) {
                File photoFile = new File(cursor.getString(0));
                if (photoFile.exists() && photoFile.isFile())
                    list.add(new PhotoData(photoFile.getAbsolutePath(), PhotoConfig.REQUEST_CODE_ALBUM, photoFile.length(), false));
            }
            cursor.close();
        } catch (Exception e) {
        }

        return list;
    }

    /**
     * 获取相册
     *
     * @param context
     * @return
     */
    public static List<PhotoAlbumData> getAllAlbum(Context context) {
        List<PhotoAlbumData> returnDatas = new ArrayList<PhotoAlbumData>();
        List<PhotoData> allimglist = getAllPhotosFromDisk(context);
        if (allimglist.size() != 0) {
            Set<String> set = new TreeSet<String>();

            for (PhotoData photoData : allimglist) {
                String photoAlbumName = getAlbumName(photoData.photoFilePath);
                if (photoAlbumName != null) {
                    photoData.photoAlbumName = photoAlbumName;
                    set.add(photoData.photoAlbumName);
                }
            }

            for (String albumName : set) {
                PhotoAlbumData photoAlbumData = new PhotoAlbumData();
                photoAlbumData.albumName = albumName;
                returnDatas.add(photoAlbumData);
            }

            for (PhotoAlbumData photoAlbumData : returnDatas) {
                for (PhotoData photoData : allimglist) {
                    if (photoData.photoAlbumName != null && photoAlbumData.albumName.equals(photoData.photoAlbumName)) {
                        photoAlbumData.datas.add(photoData);
                    }
                }
                if (photoAlbumData.datas.size() != 0) {
                    photoAlbumData.firstData = photoAlbumData.datas.get(0);
                }
            }
        }
        return returnDatas;
    }

    /**
     * 获取图片所在的上级文件夹，即相册名
     *
     * @param photoFilePath
     * @return
     */
    public static String getAlbumName(String photoFilePath) {
        try {
            String filename[] = photoFilePath.split("/");
            return filename[filename.length - 2];
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 处理图片地址来源，供ImageLoader使用
     *
     * @param uri
     * @return
     */
    public static String getDisplayUri(String uri) {
        if (uri != null && !uri.equals("")) {
            if (uri.startsWith("http")) {//来源网络
                return uri;
            } else if (uri.startsWith("file")) {//来源本地
                return uri;
            } else if (uri.startsWith("drawable")) {//来源apk
                return uri;
            } else {//来源本地
                return "file:///" + uri;
            }
        }
        return uri;
    }
}
