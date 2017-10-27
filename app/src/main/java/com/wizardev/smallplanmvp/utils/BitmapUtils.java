package com.wizardev.smallplanmvp.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by wizardev on 17-3-3.
 */

public class BitmapUtils {
    /**
     * 获得缩放比例
     * @param options
     * @param reqHeight
     * @param reqWidth
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqHeight, int reqWidth){
        int inSampleSize = 1;
        int height = options.outHeight;
        int width = options.outWidth;
        if (height>reqHeight || width>reqWidth){
            if (width > height) {
                inSampleSize = Math.round((float)height / (float)reqHeight);
            } else {
                inSampleSize = Math.round((float)width / (float)reqWidth);
            }
        }
        return inSampleSize;
    }

    /**
     * 根据缩放比例获得缩放后的Bitmap
     * @param res
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
}
