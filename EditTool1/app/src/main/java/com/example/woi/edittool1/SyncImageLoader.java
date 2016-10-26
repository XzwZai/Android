package com.example.woi.edittool1;

/**
 * Created by woi on 2016/10/25.
 */

import android.graphics.Bitmap;
import android.os.Handler;

import java.lang.ref.SoftReference;
import java.util.HashMap;

public class SyncImageLoader {

    private Object lock = new Object();

    private boolean mAllowLoad = true;

    private boolean firstLoad = true;

    private int mStartLoadLimit = 0;

    private int mStopLoadLimit = 0;

    final Handler handler = new Handler();

    private HashMap<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();

    public interface OnImageLoadListener {
        public void onImageLoad(Integer t, Bitmap bitmap);
        public void onError(Integer t);
    }

    public void setLoadLimit(int startLoadLimit,int stopLoadLimit){
        if(startLoadLimit > stopLoadLimit){
            return;
        }
        mStartLoadLimit = startLoadLimit;
        mStopLoadLimit = stopLoadLimit;
    }

    public void restore(){
        mAllowLoad = true;
        firstLoad = true;
    }

    public void lock(){
        mAllowLoad = false;
        firstLoad = false;
    }

    public void unlock(){
        mAllowLoad = true;
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    public void loadImage(Integer t, final int imgIdx, int W, int H, int X,
                          OnImageLoadListener listener) {
        final OnImageLoadListener mListener = listener;
        final Integer mt = t;
        final int _imgIdx = imgIdx;
        final int _W = W, _H = H, _X = X;
        new Thread(new Runnable() {

            @Override
            public void run() {
                if(!mAllowLoad){
                    synchronized (lock) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }

                if(mAllowLoad && firstLoad){
                    loadImage(_imgIdx, _W, _H, _X, mt, mListener);
                }

                if(mAllowLoad && mt <= mStopLoadLimit && mt >= mStartLoadLimit){
                    loadImage(_imgIdx, _W, _H, _X, mt, mListener);
                }
            }

        }).start();
    }

    private void loadImage(int ImageIdx, int W, int H, int X, final Integer mt,
                           final OnImageLoadListener mListener) {
        String padding = X == 0 ? "free" :
                (String.valueOf(X) + "xcompress");
        String imageKey = String.valueOf(ImageIdx) + padding;
        if (imageCache.containsKey(imageKey)) {
            SoftReference<Bitmap> softReference = imageCache.get(imageKey);
            final Bitmap d = softReference.get();
            if (d != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mAllowLoad) {
                            mListener.onImageLoad(mt, d);
                        }
                    }
                });
                return;
            }
        }
        final Bitmap bitmap = MemoryManager.loadBitmap(ImageIdx, W, H, X);
        if (bitmap != null) {
            imageCache.put(imageKey, new SoftReference<Bitmap>(bitmap));
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (mAllowLoad) {
                    mListener.onImageLoad(mt, bitmap);
                }
            }
        });
    }
}
