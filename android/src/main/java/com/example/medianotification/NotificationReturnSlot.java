package com.example.medianotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

public class NotificationReturnSlot extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case "prev":
                MediaNotificationPlugin.callEvent("prev");
                break;
            case "next":
                MediaNotificationPlugin.callEvent("next");
                break;
            case "toggle":
                String action = !MediaNotificationPlugin.getnPanel().isPlay()?"play":"pause";
                MediaNotificationPlugin.getnPanel().togglePlayPause();
                MediaNotificationPlugin.callEvent(action);
                break;
            case "select":
                Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                context.sendBroadcast(closeDialog);
                String packageName = context.getPackageName();
                PackageManager pm = context.getPackageManager();
                Intent launchIntent = pm.getLaunchIntentForPackage(packageName);
                context.startActivity(launchIntent);

                MediaNotificationPlugin.callEvent("select");
        }
    }
}

