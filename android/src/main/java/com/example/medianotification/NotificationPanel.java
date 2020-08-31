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
    private String bgImageColor;
    private String titleColor;
    private String subtitleColor;
    private String bigLayoutIconColor;
    private String iconColor;
    private String timeStamp;
    private int iconId;
    public NotificationPanel(Context parent, String title, String author, boolean play, byte[] image, int length, int offset, int iconId, String bgColor, String titleColor, String subtitleColor, String iconColor, String bigLayoutIconColor,byte[] bgImg, int bgLength, int bgOffset, String bgImageBackgroundColor, String timeStamp ) {
        this.parent = parent;
        this.title = title;
        this.author = author;
        this.play = play;
        this.bgColor = bgColor;
        this.bgImageColor = bgImageBackgroundColor;
        this.titleColor=titleColor;
        this.subtitleColor=subtitleColor;
        this.bigLayoutIconColor=bigLayoutIconColor;
        this.iconColor=iconColor;
        this.iconId = iconId;
        this.timeStamp=timeStamp;
        nBuilder = new NotificationCompat.Builder(parent, "com.moda.twenty/media_notification_back")
                .setContentTitle("Player")

                .setPriority(Notification.PRIORITY_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOngoing(this.play)
                .setOnlyAlertOnce(true)
                .setVibrate(new long[]{0L})
                .setSound(null);

        if(this.iconId!=0){
            nBuilder.setSmallIcon(this.iconId);
        }else{
            nBuilder.setSmallIcon(R.drawable.ic_stat_music_note);
        }
        remoteView = new RemoteViews(parent.getPackageName(), R.layout.notificationlayout);
        bigRemoteView = new RemoteViews(parent.getPackageName(), R.layout.bignotificationlayout);

        remoteView.setTextViewText(R.id.title, title);
        bigRemoteView.setTextViewText(R.id.title, title);
        remoteView.setTextViewText(R.id.author, author);
        bigRemoteView.setTextViewText(R.id.author, author);

        if(timeStamp!=null){
            remoteView.setTextViewText(R.id.timeStamp, timeStamp);
            bigRemoteView.setTextViewText(R.id.timeStamp, timeStamp);
        }else{
            remoteView.setTextViewText(R.id.timeStamp, "--/--");
            bigRemoteView.setTextViewText(R.id.timeStamp, "--/--");
        }

        if (image != null) {

            Bitmap BmpImage = BitmapFactory.decodeByteArray(image,offset,length);

            remoteView.setImageViewBitmap(R.id.img, BmpImage);
            bigRemoteView.setImageViewBitmap(R.id.img, BmpImage);

        }else{
            remoteView.setImageViewBitmap(R.id.img, null);
            bigRemoteView.setImageViewBitmap(R.id.img, null);
            bigRemoteView.setImageViewBitmap(R.id.bgimg, null);
        }


        if(bgImg!=null){
            Bitmap BmpBgImage = BitmapFactory.decodeByteArray(bgImg,bgOffset,bgLength);
            bigRemoteView.setImageViewBitmap(R.id.bgimg, BmpBgImage);
        }else{
            bigRemoteView.setImageViewBitmap(R.id.bgimg, null);
        }


        //setting background Color

        if(bgColor!=null){
            remoteView.setInt(R.id.layout, "setBackgroundColor",
                    Color.parseColor(bgColor));
            bigRemoteView.setInt(R.id.layout, "setBackgroundColor",
                    Color.parseColor(bgColor));
        }

        if(bgImageBackgroundColor!=null){
            bigRemoteView.setInt(R.id.LowerLayout, "setBackgroundColor",
                    Color.parseColor(bgImageBackgroundColor));
        }
        if(titleColor!=null){
            remoteView.setTextColor(R.id.title, Color.parseColor(titleColor));
            bigRemoteView.setTextColor(R.id.title, Color.parseColor(titleColor));
            //The timestamp color has the same color as the Title in small layout
            remoteView.setTextColor(R.id.timeStamp, Color.parseColor(titleColor));
        }
        if(subtitleColor!=null){
            remoteView.setTextColor(R.id.author, Color.parseColor(subtitleColor));
            bigRemoteView.setTextColor(R.id.author, Color.parseColor(subtitleColor));
        }
        Bitmap PrevBmp = BitmapFactory.decodeResource(parent.getResources(), R.drawable.baseline_skip_previous_black_36);
        Bitmap NextBmp = BitmapFactory.decodeResource(parent.getResources(), R.drawable.baseline_skip_next_black_36);
        if(iconColor!=null){
            remoteView.setImageViewBitmap(R.id.prev, changeBitmapColor(PrevBmp,Color.parseColor(iconColor)));
            remoteView.setImageViewBitmap(R.id.next, changeBitmapColor(NextBmp,Color.parseColor(iconColor)));
        }

        if(bigLayoutIconColor!=null){
            bigRemoteView.setImageViewBitmap(R.id.prev, changeBitmapColor(PrevBmp,Color.parseColor(bigLayoutIconColor)));
            bigRemoteView.setImageViewBitmap(R.id.next, changeBitmapColor(NextBmp,Color.parseColor(bigLayoutIconColor)));
            //The timestamp color has the same color as the icons in big layout
            bigRemoteView.setTextColor(R.id.timeStamp, Color.parseColor(bigLayoutIconColor));
        }


        if (this.play) {
            Bitmap toggleBmp = BitmapFactory.decodeResource(parent.getResources(), R.drawable.baseline_pause_black_48);
            remoteView.setImageViewBitmap(R.id.toggle, changeBitmapColor(toggleBmp,Color.parseColor(iconColor)));
            bigRemoteView.setImageViewBitmap(R.id.toggle, changeBitmapColor(toggleBmp,Color.parseColor(bigLayoutIconColor)));
        } else {
            Bitmap toggleBmp = BitmapFactory.decodeResource(parent.getResources(), R.drawable.baseline_play_arrow_black_48);
            remoteView.setImageViewBitmap(R.id.toggle, changeBitmapColor(toggleBmp,Color.parseColor(iconColor)));
            bigRemoteView.setImageViewBitmap(R.id.toggle, changeBitmapColor(toggleBmp,Color.parseColor(bigLayoutIconColor)));
        }


        setBigLayoutListeners(bigRemoteView);
        setListeners(remoteView);
        nBuilder.setCustomContentView(remoteView);
        nBuilder.setCustomBigContentView(bigRemoteView);
        Notification notification = nBuilder.build();

        nManager = (NotificationManager) parent.getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(1, notification);
    }

    public boolean isPlay() {
        return play;
    }

    public void setTitle(String title){
        if(title!=null){
            remoteView.setTextViewText(R.id.title, title);
            bigRemoteView.setTextViewText(R.id.title, title);
            nBuilder.setCustomContentView(remoteView);
            nBuilder.setCustomBigContentView(bigRemoteView);
            nManager.notify(1,nBuilder.build());
        }
    }

    public void setSubtitle(String subtitle){
        if(subtitle!=null){
            remoteView.setTextViewText(R.id.author, subtitle);
            bigRemoteView.setTextViewText(R.id.author, subtitle);
            nBuilder.setCustomContentView(remoteView);
            nBuilder.setCustomBigContentView(bigRemoteView);
            nManager.notify(1,nBuilder.build());
        }
    }

    public void setTimeStamp(String timeStamp){
        String newTimestamp="";
        if(timeStamp==null) newTimestamp="--/--"; else newTimestamp=timeStamp;
        remoteView.setTextViewText(R.id.timeStamp, newTimestamp);
        bigRemoteView.setTextViewText(R.id.timeStamp, newTimestamp);
        nBuilder.setCustomContentView(remoteView);
        nBuilder.setCustomBigContentView(bigRemoteView);
        nManager.notify(1,nBuilder.build());
    }

    public void togglePlayPause(){
        this.play=!this.play;
        if (this.play) {
            Bitmap toggleBmp = BitmapFactory.decodeResource(parent.getResources(), R.drawable.baseline_pause_black_48);
            remoteView.setImageViewBitmap(R.id.toggle, changeBitmapColor(toggleBmp,Color.parseColor(iconColor)));
            bigRemoteView.setImageViewBitmap(R.id.toggle, changeBitmapColor(toggleBmp,Color.parseColor(iconColor)));
        } else {
            Bitmap toggleBmp = BitmapFactory.decodeResource(parent.getResources(), R.drawable.baseline_play_arrow_black_48);
            remoteView.setImageViewBitmap(R.id.toggle, changeBitmapColor(toggleBmp,Color.parseColor(iconColor)));
            bigRemoteView.setImageViewBitmap(R.id.toggle, changeBitmapColor(toggleBmp,Color.parseColor(iconColor)));
        }
        nBuilder.setCustomContentView(remoteView);
        nBuilder.setCustomBigContentView(bigRemoteView);
        nBuilder.setOngoing(this.play);
        nManager.notify(1,nBuilder.build());
    }

    public void setTo(boolean play){
        this.play=play;
        if (this.play) {
            Bitmap toggleBmp = BitmapFactory.decodeResource(parent.getResources(), R.drawable.baseline_pause_black_48);
            remoteView.setImageViewBitmap(R.id.toggle, changeBitmapColor(toggleBmp,Color.parseColor(iconColor)));
            bigRemoteView.setImageViewBitmap(R.id.toggle, changeBitmapColor(toggleBmp,Color.parseColor(iconColor)));
        } else {
            Bitmap toggleBmp = BitmapFactory.decodeResource(parent.getResources(), R.drawable.baseline_play_arrow_black_48);
            remoteView.setImageViewBitmap(R.id.toggle, changeBitmapColor(toggleBmp,Color.parseColor(iconColor)));
            bigRemoteView.setImageViewBitmap(R.id.toggle, changeBitmapColor(toggleBmp,Color.parseColor(iconColor)));
        }
        nBuilder.setCustomContentView(remoteView);
        nBuilder.setCustomBigContentView(bigRemoteView);
        nBuilder.setOngoing(this.play);
        nManager.notify(1,nBuilder.build());
    }


    public void setIcon(int iconId){
        nBuilder.setSmallIcon(iconId);
        nManager.notify(1,nBuilder.build());
    }


    public void setListeners(RemoteViews view) {
        // Пауза/Воспроизведение
        Intent intent = new Intent(parent, NotificationReturnSlot.class)
                .setAction("toggle")
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

        //Deprecated intent usage. the intent will be triggered for both base and big view. this is due to an unknown problem with the views
        //where it does only register the intent to a sing view, namely the one that is registered for last. This might be problematic if we want to register
        //different callbacks for the views but for now, that is not a feature so te intent will be added as a global notification intent.

        //view.setOnClickPendingIntent(R.id.layout, selectPendingIntent);

        nBuilder.setContentIntent(selectPendingIntent);
    }

    public void setBigLayoutListeners(RemoteViews view) {
        // Пауза/Воспроизведение
        Intent intent = new Intent(parent, NotificationReturnSlot.class)
                .setAction("toggle")
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

