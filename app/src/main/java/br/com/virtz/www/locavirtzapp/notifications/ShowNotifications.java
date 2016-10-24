package br.com.virtz.www.locavirtzapp.notifications;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import br.com.virtz.www.locavirtzapp.MonitoramentoBeaconActivity;
import br.com.virtz.www.locavirtzapp.R;


/**
 * Classe responsável por exibir notificações.
 */
public class ShowNotifications {

    public void sendNotification(Notification notification) {
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText(notification.getText());

        Drawable drawable = ContextCompat.getDrawable(notification.getContext(), R.drawable.wifi_big);
        Bitmap bitmapIcon = ((BitmapDrawable)drawable).getBitmap();

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(notification.getContext())
                        .setContentTitle(notification.getTitle())
                        .setContentText(notification.getText())
                        .setSmallIcon(R.drawable.wifi)
                        .setLargeIcon(bitmapIcon)
                        .setAutoCancel(true)
                        .setStyle(bigTextStyle);
                        //.setSubText("Sub texto pode ser muito legal. Sub texto pode ser muito legal. Sub texto pode ser muito legal. ");

        //builder.contentView.setImageViewResource(android.R.id.icon, R.drawable.antenna_big);

        builder.setVibrate(new long[] { 0, 500, 500 });
        builder.setLights(Color.BLUE, 2000, 2000);


        //Intent monitoramentIntent = new Intent(notification.getContext(), notification.getActivity().getClass());
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(notification.getContext());
        stackBuilder.addParentStack(MonitoramentoBeaconActivity.class);
        stackBuilder.addNextIntent(notification.getIntent());

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) notification.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

}
