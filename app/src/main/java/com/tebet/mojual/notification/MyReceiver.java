package com.tebet.mojual.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.tebet.mojual.MainActivity;

/**
 * Created by hacked on 7/19/2017.
 */

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        intent = new Intent(context, MainActivity.class);
        context.startService(intent);
    }
}
