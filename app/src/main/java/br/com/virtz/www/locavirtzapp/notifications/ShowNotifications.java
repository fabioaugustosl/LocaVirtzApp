package br.com.virtz.www.locavirtzapp.notifications;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

import br.com.virtz.www.locavirtzapp.MonitoramentoBeaconActivity;
import br.com.virtz.www.locavirtzapp.R;

/**
 * Classe responsável por exibir notificações.
 */
public class ShowNotifications {

    public void sendNotification(Notification notification) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(notification.getContext())
                        .setContentTitle(notification.getTitle())
                        .setContentText(notification.getText())
                        .setSmallIcon(R.drawable.antenna_small)
                        .setAutoCancel(true);

        //builder.contentView.setImageViewResource(android.R.id.icon, R.drawable.antenna_big);

        builder.setVibrate(new long[] { 0, 500, 500 });
        builder.setLights(Color.BLUE, 2000, 2000);


        Intent monitoramentIntent = new Intent(notification.getContext(), notification.getActivity().getClass());
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(notification.getContext());
        stackBuilder.addParentStack(notification.getActivity().getClass());
        stackBuilder.addNextIntent(monitoramentIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) notification.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

}
