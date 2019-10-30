package com.tebet.mojual.common.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.core.content.FileProvider
import timber.log.Timber
import java.io.File

object FileHandler {
    fun openFile(context: Context, type: String, file: File) {
        var file = file
        val intent = Intent(Intent.ACTION_VIEW)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        var mExposedFileUri: Uri? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            try {
                file = File(context.filesDir, file.name)
                mExposedFileUri = FileProvider.getUriForFile(context, "com.tebet.mojual", file)
            } catch (ex: Exception) {
                Timber.e(ex)
            }

        } else {
            mExposedFileUri = Uri.fromFile(file)
        }

        if (mExposedFileUri != null) {
            val resInfoList = context.packageManager.queryIntentActivities(
                intent,
                PackageManager.MATCH_DEFAULT_ONLY
            )
            for (resolveInfo in resInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                context.grantUriPermission(
                    packageName,
                    mExposedFileUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
            intent.setDataAndType(mExposedFileUri, type)
            val pm = context.packageManager
            if (intent.resolveActivity(pm) != null) {
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "No app on devices can open PDF file", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(context, "No app on devices can open PDF file", Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun openFile(context: Context, file: File) {
        openFile(context, "application/pdf", file)
    }
}
