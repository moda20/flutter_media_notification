package com.example.medianotification;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

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
                show(title, author, play, image, length, offset, bgColor, titleColor, subtitleColor, iconColor);
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

    public static void show(String title, String author, boolean play, byte[] image, int length, int offset, String bgColor, String titleColor, String subtitleColor, String iconColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, importance);
            channel.enableVibration(false);
            channel.setSound(null, null);
            NotificationManager notificationManager = registrar.context().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        nPanel = new NotificationPanel(registrar.context(), title, author, play, image, length, offset, bgColor, titleColor, subtitleColor, iconColor);
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
}




