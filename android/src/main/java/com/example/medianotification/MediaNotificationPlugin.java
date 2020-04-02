package com.example.medianotification;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;


import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import android.support.v4.media.MediaBrowserCompat;
import androidx.media.MediaBrowserServiceCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.RatingCompat;
import androidx.media.app.NotificationCompat.MediaStyle;
import androidx.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.KeyEvent;


/**
 * MediaNotificationPlugin
 */
public class MediaNotificationPlugin implements MethodCallHandler {
    private static final String CHANNEL_ID = "media_notification";
    private static Registrar registrar;
    private static NotificationPanel nPanel;
    private static MethodChannel channel;

    private MediaNotificationPlugin(Registrar r) {
        registrar = r;
    }

    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        MediaNotificationPlugin plugin = new MediaNotificationPlugin(registrar);

        MediaNotificationPlugin.channel = new MethodChannel(registrar.messenger(), "media_notification");
        MediaNotificationPlugin.channel.setMethodCallHandler(new MediaNotificationPlugin(registrar));
    }

    public static NotificationPanel getnPanel() {
        return nPanel;
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        switch (call.method) {
            case "show":
                final String title = call.argument("title");
                final String author = call.argument("author");
                final byte[] image = call.argument("image");
                final int length = call.argument("length");
                final int offset = call.argument("offset");
                final boolean play = call.argument("play");
                final String bgColor = call.argument("bgColor");
                final String titleColor = call.argument("titleColor");
                final String subtitleColor = call.argument("subtitleColor");
                final String iconColor = call.argument("iconColor");
                final String iconId = call.argument("iconId");
                show(title, author, play, image, length, offset, iconId, bgColor, titleColor, subtitleColor, iconColor);
                result.success(null);
                break;
            case "hide":
                hide();
                result.success(null);
                break;
            case "setTitle":
                String Newtitle = call.argument("title");
                setTitle(Newtitle);
                result.success(null);
                break;
            case "setIcon":
                String icon = call.argument("icon");
                setIcon(icon);
                result.success(null);
                break;
            case "setSubtitle":
                String NewSubtitle = call.argument("subtitle");
                setSubTitle(NewSubtitle);
                result.success(null);
                break;
            case "togglePlayPause":
                togglePlayPause();
                result.success(null);
                break;
            case "setToPlayPause":
                boolean isPlay = call.argument("play");
                setTo(isPlay);
                result.success(null);
                break;
            default:
                result.notImplemented();
        }
    }

    public static void callEvent(String event) {

        MediaNotificationPlugin.channel.invokeMethod(event, null, new Result() {
            @Override
            public void success(Object o) {
                // this will be called with o = "some string"
            }

            @Override
            public void error(String s, String s1, Object o) {
            }

            @Override
            public void notImplemented() {
            }
        });
    }

    public static void show(String title, String author, boolean play, byte[] image, int length, int offset, String iconId, String bgColor, String titleColor, String subtitleColor, String iconColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, importance);
            channel.enableVibration(false);
            channel.setSound(null, null);
            NotificationManager notificationManager = registrar.context().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        nPanel = new NotificationPanel(registrar.context(), title, author, play, image, length, offset, getResourceId(iconId), bgColor, titleColor, subtitleColor, iconColor);
    }

    private void hide() {
        nPanel.notificationCancel();
    }

    public void setTitle(String title) {
        nPanel.setTitle(title);
    }
    public void setSubTitle(String subtitle) {
        nPanel.setSubtitle(subtitle);
    }
    public void togglePlayPause() {
        nPanel.togglePlayPause();
    }

    public void setTo(boolean play) {
        nPanel.setTo(play);
    }
    public void setIcon(String iconName) {
        //find the iconId
        int iconId = getResourceId(iconName);
        nPanel.setIcon(iconId);
    }

    //This will get the resourceID based on the resource String, the resource String needs to be in the res folder
    private static int getResourceId(String resource) {
        if(resource.length()==0)
            return 0;
        String[] parts = resource.split("/");
        String resourceType = parts[0];
        String resourceName = parts[1];
        return registrar.context().getResources().getIdentifier(resourceName, resourceType, registrar.context().getApplicationContext().getPackageName());
    }
}




