package com.tebet.mojual.common.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build.VERSION;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import androidx.loader.content.CursorLoader;
import timber.log.Timber;

/**
 * Created by heo on 8/15/17.
 */

public class RealPathUtil {
    public static String getRealPath(Context context, Uri uri) {
        if (VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            return getRealPathFromURI_API19(context, uri);
        } else {
            return getRealPathFromURI_API11to18(context, uri);
        }
    }

    @SuppressLint("NewApi")
    static String getRealPathFromURI_API19(Context context, Uri uri) {
        String filePath = "";
        try {
            String wholeID = DocumentsContract.getDocumentId(uri);

            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];

            String[] column = {MediaStore.Images.Media.DATA};

            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";

            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    column, sel, new String[]{id}, null);

            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
        } catch (Exception e) {
            try {
                Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
                if (cursor == null) { // Source is Dropbox or other similar local file path
                    filePath = uri.getPath();
                } else {
                    cursor.moveToFirst();
                    int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    filePath = cursor.getString(idx);
                    cursor.close();
                }
            } catch (Exception e1) {
                Timber.e(e1);
            }
        }
        return filePath;
    }


    @SuppressLint("NewApi")
    static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        String result = null;

        CursorLoader cursorLoader = new CursorLoader(
                context,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if (cursor != null) {
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
        }
        return result;
    }
}
