package com.example.medianotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

/**
 * Created by dmitry on 14.08.18.
 */

public class NotificationPanel {
    private Context parent;
    private NotificationManager nManager;
    private NotificationCompat.Builder nBuilder;
    private RemoteViews remoteView;
    private RemoteViews bigRemoteView;
    private String title;
    private String author;
    private String image;
    private boolean play;
    private String bgColor;
    private String titleColor;
    private String subtitleColor;
    private String iconColor;
    public NotificationPanel(Context parent, String title, String author, boolean play, byte[] image, int length, int offset, String bgColor, String titleColor, String subtitleColor, String iconColor ) {
        this.parent = parent;
        this.title = title;
        this.author = author;
        this.play = play;
        this.bgColor = bgColor;
        this.titleColor=titleColor;
        this.subtitleColor=subtitleColor;
        this.iconColor=iconColor;

        nBuilder = new NotificationCompat.Builder(parent, "media_notification")
                .setContentTitle("Player")
                .setSmallIcon(R.drawable.ic_stat_music_note)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOngoing(this.play)
                .setOnlyAlertOnce(true)
                .setVibrate(new long[]{0L})
                .setSound(null);

        remoteView = new RemoteViews(parent.getPackageName(), R.layout.notificationlayout);
        bigRemoteView = new RemoteViews(parent.getPackageName(), R.layout.bignotificationlayout);

        remoteView.setTextViewText(R.id.title, title);
        bigRemoteView.setTextViewText(R.id.title, title);
        remoteView.setTextViewText(R.id.author, author);
        bigRemoteView.setTextViewText(R.id.author, author);

        if (image != null) {

            Bitmap BmpImage = BitmapFactory.decodeByteArray(image,offset,length);

            remoteView.setImageViewBitmap(R.id.img, BmpImage);
            bigRemoteView.setImageViewBitmap(R.id.img, BmpImage);
        }else{
            remoteView.setImageViewBitmap(R.id.img, null);
            bigRemoteView.setImageViewBitmap(R.id.img, null);
        }


        //setting background Color

        if(bgColor!=null){
            remoteView.setInt(R.id.layout, "setBackgroundColor",
                    Color.parseColor(bgColor));
            bigRemoteView.setInt(R.id.Biglayout, "setBackgroundColor",
                    Color.parseColor(bgColor));
        }
        if(titleColor!=null){
            remoteView.setTextColor(R.id.title, Color.parseColor(titleColor));
            bigRemoteView.setTextColor(R.id.title, Color.parseColor(titleColor));
        }
        if(subtitleColor!=null){
            remoteView.setTextColor(R.id.author, Color.parseColor(subtitleColor));
            bigRemoteView.setTextColor(R.id.author, Color.parseColor(subtitleColor));
        }
        if(iconColor!=null){
            Bitmap PrevBmp = BitmapFactory.decodeResource(parent.getResources(), R.drawable.baseline_skip_previous_black_36);
            remoteView.setImageViewBitmap(R.id.prev, changeBitmapColor(PrevBmp,Color.parseColor(iconColor)));
            bigRemoteView.setImageViewBitmap(R.id.prev, changeBitmapColor(PrevBmp,Color.parseColor(iconColor)));
            Bitmap NextBmp = BitmapFactory.decodeResource(parent.getResources(), R.drawable.baseline_skip_next_black_36);
            remoteView.setImageViewBitmap(R.id.next, changeBitmapColor(NextBmp,Color.parseColor(iconColor)));
            bigRemoteView.setImageViewBitmap(R.id.next, changeBitmapColor(NextBmp,Color.parseColor(iconColor)));
        }


        if (this.play) {
            Bitmap toggleBmp = BitmapFactory.decodeResource(parent.getResources(), R.drawable.baseline_pause_black_48);
            remoteView.setImageViewBitmap(R.id.toggle, changeBitmapColor(toggleBmp,Color.parseColor(iconColor)));
            bigRemoteView.setImageViewBitmap(R.id.toggle, changeBitmapColor(toggleBmp,Color.parseColor(iconColor)));
        } else {
            Bitmap toggleBmp = BitmapFactory.decodeResource(parent.getResources(), R.drawable.baseline_play_arrow_black_48);
            remoteView.setImageViewBitmap(R.id.toggle, changeBitmapColor(toggleBmp,Color.parseColor(iconColor)));
            bigRemoteView.setImageViewBitmap(R.id.toggle, changeBitmapColor(toggleBmp,Color.parseColor(iconColor)));
        }

        setListeners(remoteView);
        nBuilder.setCustomContentView(remoteView);
        nBuilder.setCustomBigContentView(bigRemoteView);

        Notification notification = nBuilder.build();

        nManager = (NotificationManager) parent.getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(1, notification);
    }

    public void setListeners(RemoteViews view) {
        // Пауза/Воспроизведение
        Intent intent = new Intent(parent, NotificationReturnSlot.class)
                .setAction("toggle")
                .putExtra("title", this.title)
                .putExtra("author", this.author)
                .putExtra("bgColor", this.bgColor)
                .putExtra("titleColor", this.titleColor)
                .putExtra("subtitleColor", this.subtitleColor)
                .putExtra("iconColor", this.iconColor)
                .putExtra("action", !this.play ? "play" : "pause");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(parent, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.toggle, pendingIntent);

        // Вперед
        Intent nextIntent = new Intent(parent, NotificationReturnSlot.class)
                .setAction("next");
        PendingIntent pendingNextIntent = PendingIntent.getBroadcast(parent, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.next, pendingNextIntent);

        // Назад
        Intent prevIntent = new Intent(parent, NotificationReturnSlot.class)
                .setAction("prev");
        PendingIntent pendingPrevIntent = PendingIntent.getBroadcast(parent, 0, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.prev, pendingPrevIntent);

        // Нажатие на уведомление
        Intent selectIntent = new Intent(parent, NotificationReturnSlot.class)
                .setAction("select");
        PendingIntent selectPendingIntent = PendingIntent.getBroadcast(parent, 0, selectIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        view.setOnClickPendingIntent(R.id.layout, selectPendingIntent);
    }


    public void notificationCancel() {
        nManager.cancel(1);
    }

    public static Bitmap changeBitmapColor(Bitmap srcBmp, int dstColor)
    {

        int width = srcBmp.getWidth();
        int height = srcBmp.getHeight();

        float srcHSV[] = new float[3];
        float dstHSV[] = new float[3];

        Bitmap dstBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int pixel = srcBmp.getPixel(col, row);
                int alpha = Color.alpha(pixel);
                Color.colorToHSV(pixel, srcHSV);
                Color.colorToHSV(dstColor, dstHSV);

                // If it area to be painted set only value of original image

                //dstHSV[2] = srcHSV[2];  // value
                dstBitmap.setPixel(col, row, Color.HSVToColor(alpha, dstHSV));
            }
        }

        return dstBitmap;
    }
}

