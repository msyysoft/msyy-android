package g.api.tools.cache;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.LruCache;

public class BmpCache extends LruCache<String, Bitmap> {
    private BmpCache(int maxSize) {
        super(maxSize);
    }

    private volatile static BmpCache instance = null;

    public static BmpCache getInstance() {
        if (instance == null) {
            synchronized (BmpCache.class) {
                if (instance == null) {
                    instance = new BmpCache((int) (Runtime.getRuntime().maxMemory() / 5f)) {
                        @Override
                        protected int sizeOf(String cacheKey, Bitmap bitmap) {
                            if (Build.VERSION.SDK_INT >= 12)
                                return bitmap.getByteCount();//12以下低版本不能用此方法
                            else
                                return bitmap.getRowBytes() * bitmap.getHeight();
                        }
                    };
                    instance.evictAll();
                }
            }
        }
        return instance;
    }
}
