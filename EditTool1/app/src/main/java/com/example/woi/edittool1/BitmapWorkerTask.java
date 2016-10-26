package com.example.woi.edittool1;

import android.graphics.Bitmap;
import android.os.AsyncTask;

/**
 * Created by woi on 2016/10/25.
 */
public class BitmapWorkerTask extends AsyncTask<Object, Void, Void> {
    protected Void doInBackground(Object... objects) {
        MemoryManager.addBitmapToMemoryCache((String)objects[0], (Bitmap)objects[1]);
        return null;
    }
}