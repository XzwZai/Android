package com.example.woi.edittool1;

/**
 * Created by woi on 2016/10/25.
 */
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;

public class MemoryManager {
    private static LruCache<String, Bitmap> mMemoryCache;
    private static int maxMemory, cacheSize;
    private static Context context;
    public static void init(Context mContext){
        maxMemory = (int)(Runtime.getRuntime().maxMemory() / 1024);
        cacheSize = maxMemory / 3;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize){
            protected int sizeOf(String key, Bitmap bitmap){
                return bitmap.getByteCount() / 1024;
            }
        };
        context = mContext;
    }

    public static void addBitmapToMemoryCache(String key, Bitmap bitmap){
        if(getBitmapFromMemCache(key) == null){
            mMemoryCache.put(key, bitmap);
        }
    }

    private static Bitmap getBitmapFromMemCache(String key){
        return mMemoryCache.get(key);
    }

    public static Bitmap loadBitmap(int resId, int frameWidth, int frameHeight, int compressLevel){
        String padding = compressLevel == 0 ? "free" :
                (String.valueOf(compressLevel) + "xcompress");
        String imageKey = String.valueOf(resId) + padding;
        Bitmap bitmap = getBitmapFromMemCache(imageKey);
        if(bitmap != null) return bitmap;
        else{
            Bitmap buf = compressLevel != 0 ? decodeSampledBitmapFromResource(context.getResources(),
                    resId, frameWidth, frameHeight) :
                    BitmapFactory.decodeResource(context.getResources(), resId);
            Object object[] = new Object[]{imageKey, buf};
            (new BitmapWorkerTask()).execute(object);
            return buf;
        }
    }

    private static int calInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight){
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if(height > reqHeight || width > reqWidth){
            int heightRatio = Math.round((float)height / (float)reqHeight);
            int widthRatio = Math.round((float)width / (float)reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
}