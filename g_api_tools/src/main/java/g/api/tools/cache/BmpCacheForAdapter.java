package g.api.tools.cache;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.util.HashSet;
import java.util.Set;

/**
 * Bitmap Cache For Adapter
 */
public abstract class BmpCacheForAdapter {
    private BmpCache mLruCache;
    private Bitmap mDefaultBitmap;
    //记录所有正在加载或等待加载的任务
    private Set<LoadBitmapAsyncTask> mTaskSet;

    public BmpCacheForAdapter() {
        mTaskSet = new HashSet<LoadBitmapAsyncTask>();
        mLruCache = BmpCache.getInstance();
        mLruCache.evictAll();

    }

    public void setDefaultBitmap(Bitmap mDefaultBitmap) {
        this.mDefaultBitmap = mDefaultBitmap;
    }

    public void setLoadImageView(ImageView imageView, int position) {
        imageView.setTag(position);
        setImageViewBitmap(imageView, position);
    }

    private void setImageViewBitmap(ImageView imageView, int position) {
        Bitmap bitmap = getBitmapFromLruCache(position);
        if (bitmap != null) {
            if (imageView != null)
                imageView.setImageBitmap(bitmap);
        } else {
            if (imageView != null)
                imageView.setImageBitmap(mDefaultBitmap);
        }
    }

    /***
     * 将图片存储到LruCache
     */
    private void addBitmapToLruCache(int position, Bitmap bitmap) {
        if (getBitmapFromLruCache(position) == null) {
            mLruCache.put(getCacheKey(position), bitmap);
        }
    }

    /**
     * 从LruCache缓存获取图片
     */
    private Bitmap getBitmapFromLruCache(int position) {
        return mLruCache.get(getCacheKey(position));
    }


    /**
     * 加载图片
     *
     * @param i itemView的position
     */
    public void loadBitmaps(int i) {
        Bitmap bitmap = getBitmapFromLruCache(i);
        if (bitmap == null) {
            LoadBitmapAsyncTask task = new LoadBitmapAsyncTask();
            mTaskSet.add(task);
            task.execute(i);
        }
    }

    /**
     * 取消所有正在加载或等待加载的任务
     */
    public void cancelAllTasks() {
        if (mTaskSet != null) {
            for (LoadBitmapAsyncTask task : mTaskSet) {
                task.cancel(false);
            }
        }
    }

    /**
     * 加载图片的异步任务
     */
    private class LoadBitmapAsyncTask extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected Integer doInBackground(Integer... params) {
            Bitmap bitmap = getBitmapInBackground(params[0]);
            if (bitmap != null) {
                //加载完后,将其缓存到LrcCache
                addBitmapToLruCache(params[0], bitmap);
            }
            return params[0];
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            //加载完成后,找到其对应的ImageView显示图片
            setImageViewBitmap(getImageViewByTag(result), result);
            mTaskSet.remove(this);
        }
    }

    public abstract Bitmap getBitmapInBackground(int position);

    public abstract String getCacheKey(int position);

    public abstract ImageView getImageViewByTag(int position);
}
