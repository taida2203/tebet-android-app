package com.tebet.mojual.common.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.tebet.mojual.common.BuildConfig;

import java.io.File;
import java.util.List;

import timber.log.Timber;

public class FileHandler {
    public static void openFile(Context context, String type, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri mExposedFileUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            try {
                file = new File(context.getFilesDir(), file.getName());
                mExposedFileUri = FileProvider.getUriForFile(context, "com.tebet.mojual", file);
            } catch (Exception ex) {
                Timber.e(ex);
            }
        } else {
            mExposedFileUri = Uri.fromFile(file);
        }

        if (mExposedFileUri != null) {
            List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                context.grantUriPermission(packageName, mExposedFileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            intent.setDataAndType(mExposedFileUri, type);
            PackageManager pm = context.getPackageManager();
            if (intent.resolveActivity(pm) != null) {
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "No app on devices can open PDF file", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "No app on devices can open PDF file", Toast.LENGTH_SHORT).show();
        }
    }

    public static void openFile(Context context, File file) {
        openFile(context, "application/pdf", file);
    }
}
