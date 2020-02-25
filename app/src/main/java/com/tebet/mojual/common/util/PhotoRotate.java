package com.tebet.mojual.common.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

import timber.log.Timber;

public class PhotoRotate {
    public static long MAX_UPLOAD_SIZE = 1 * 1000 * 1000;
    public static long MIN_UPLOAD_SIZE = 6 * 100 * 1000;
    public static long imageSize = 0;

    public static void rotatePhoto(File imageFile, float angle, boolean isLandscape, RotateCallback callback) {
        new AsyncTask<Void, Void, File>() {
            private WeakReference<Bitmap> rotateBitmap;

            @Override
            protected void onPreExecute() {
                //if you want to show progress dialog
            }

            @Override
            protected File doInBackground(Void... params) {
                Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                Matrix matrix = new Matrix();
                FileOutputStream fos2 = null;
                OutputStream os2 = null;
                try {
                    matrix.postRotate(angle);
                    if (isLandscape) {
                        matrix.postRotate(-90);
                    }
                    rotateBitmap = new WeakReference<>(Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true));

                    if (rotateBitmap.get() != null) {
                        imageSize = imageFile.length();
                        imageFile.delete();
                        fos2 = new FileOutputStream(imageFile);
                        os2 = new BufferedOutputStream(fos2);
                        int quality = 100;
                        if (imageSize > 0) { // decrease quality if size > MAX_UPLOAD_SIZE
                            if (imageSize > MAX_UPLOAD_SIZE) {
                                quality = (int) (100 * (MIN_UPLOAD_SIZE * 1.0f / imageSize));
                                if (quality < 95) {
                                    quality = 95;
                                }
                            }
                            else if (imageSize > MIN_UPLOAD_SIZE) {
                                quality = 97;
                            }
                        }
                        rotateBitmap.get().compress(Bitmap.CompressFormat.JPEG, quality, os2);
                    }
                    return imageFile;
                } catch (Exception e) {
                    Timber.e(e);
                } finally {
                    if (fos2 != null) {
                        try {
                            fos2.close();
                        } catch (IOException e) {
                            Timber.e(e);
                        }
                    }
                    if (os2 != null) {
                        try {
                            os2.close();
                        } catch (IOException e) {
                            Timber.e(e);
                        }
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(File file) {
                super.onPostExecute(file);
                if (callback != null) {
                    callback.onRotateComplete(file);
                }
            }
        }.execute();
    }


    public interface RotateCallback {
        void onRotateComplete(File file);
    }
}
