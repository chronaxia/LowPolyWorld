package com.chronaxia.lowpolyworld.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by reikyZ on 16/10/1.
 */

public class BitmapUtils {
    public static Bitmap loadBitmapRes(Context context, int resID) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeResource(context.getResources(), resID, options);
    }

    public static Bitmap zoomBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmapOut = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return bitmapOut;
    }

    public static String saveImage(Bitmap bmp, int quality) {
        if (bmp == null) {
            return null;
        }
        String path = Environment.getExternalStorageDirectory().getPath() + File.separator + "LowPolyWorld" + File.separator ;
        File appDir = new File(path);
        if (appDir == null) {
            return null;
        }
        String fileName = System.currentTimeMillis() + ".png";
        File file = new File(appDir, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, quality, fos);
            fos.flush();
            return file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
