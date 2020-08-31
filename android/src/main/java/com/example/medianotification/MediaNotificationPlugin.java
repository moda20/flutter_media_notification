package com.example.medianotification;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;

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

import androidx.annotation.NonNull;
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
public class MediaNotificationPlugin implements MethodCallHandler, FlutterPlugin {
    private static final String CHANNEL_ID = "com.moda.twenty/media_notification";
    private static final String BACK_CHANNEL_ID = "com.moda.twenty/media_notification_back";
    private static Registrar registrar;
    private static NotificationPanel nPanel;
    private static MethodChannel channel;
    private static MethodChannel back_channel;

    private MediaNotificationPlugin(Registrar r) {
        registrar = r;
    }

    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), CHANNEL_ID);
        MediaNotificationPlugin instance = new MediaNotificationPlugin(registrar);
        instance.initInstance(registrar.messenger(), registrar.context());
    }

    private void initInstance(BinaryMessenger binaryMessenger, Context context) {
        MediaNotificationPlugin.channel  = new MethodChannel(binaryMessenger, CHANNEL_ID);
        MediaNotificationPlugin.back_channel  = new MethodChannel(binaryMessenger, BACK_CHANNEL_ID);
        MediaNotificationPlugin.channel.setMethodCallHandler(new MediaNotificationPlugin(registrar));
    }


    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
        initInstance(binding.getBinaryMessenger(), binding.getApplicationContext());
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        MediaNotificationPlugin.channel.setMethodCallHandler(null);
        MediaNotificationPlugin.back_channel.setMethodCallHandler(null);
        MediaNotificationPlugin.channel = null;
        MediaNotificationPlugin.back_channel = null;
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
                final byte[] bgImage = call.argument("bgImage");
                final int length = call.argument("length");
                final int offset = call.argument("offset");
                final int bgLength = call.argument("bgLength");
                final int bgOffset = call.argument("bgOffset");
                final boolean play = call.argument("play");
                final String bgColor = call.argument("bgColor");
                final String bgImageBackgroundColor = call.argument("bgImageBackgroundColor");
                final String titleColor = call.argument("titleColor");
                final String subtitleColor = call.argument("subtitleColor");
                final String iconColor = call.argument("iconColor");
                final String bigLayoutIconColor = call.argument("bigLayoutIconColor");
                final String iconId = call.argument("iconId");
                final String timestamp = call.argument("timestamp");
                show(title, author, play, image, length, offset, iconId, bgColor, titleColor, subtitleColor, iconColor, bigLayoutIconColor, bgImage, bgLength, bgOffset, bgImageBackgroundColor, timestamp);
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
            case "setTimestamp":
                String newTimestamp = call.argument("timestamp");
                setTimestamp(newTimestamp);
                result.success(null);
                break;
            default:
                result.notImplemented();
        }
    }

    public static void callEvent(String event) {

        MediaNotificationPlugin.back_channel.invokeMethod(event, null, new Result() {
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

    public static void show(String title, String author, boolean play, byte[] image, int length, int offset, String iconId, String bgColor, String titleColor, String subtitleColor, String iconColor, String bigLayoutIconColor ,byte[] bgImage, int bgLength, int bgOffset, String bgImageBackgroundColor, String timestamp) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, importance);
            channel.enableVibration(false);
            channel.setSound(null, null);
            NotificationManager notificationManager = registrar.context().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        nPanel = new NotificationPanel(registrar.context(), title, author, play, image, length, offset, getResourceId(iconId), bgColor, titleColor, subtitleColor, iconColor, bigLayoutIconColor, bgImage, bgLength, bgOffset, bgImageBackgroundColor, timestamp);
    }

    private void hide() {
        if(nPanel!=null){
            nPanel.notificationCancel();
        }
    }

    public void setTitle(String title) {
        if(nPanel!=null){
            nPanel.setTitle(title);
        }
    }
    public void setSubTitle(String subtitle) {
        if(nPanel!=null){
            nPanel.setSubtitle(subtitle);
        }
    }
    public void setTimestamp(String timestamp) {
        if(nPanel!=null){
            nPanel.setTimeStamp(timestamp);
        }
    }
    public void togglePlayPause() {
        if(nPanel!=null){
            nPanel.togglePlayPause();
        }
    }

    public void setTo(boolean play) {
        if(nPanel!=null){
            nPanel.setTo(play);
        }
    }
    public void setIcon(String iconName) {
        if(nPanel!=null){
            //find the iconId
            int iconId = getResourceId(iconName);
            nPanel.setIcon(iconId);
        }
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




