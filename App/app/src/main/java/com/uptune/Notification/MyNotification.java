package com.uptune.Notification;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.uptune.Account.MyFiles;
import com.uptune.Navigation.SpaceTab;
import com.uptune.R;

public class MyNotification {

    Context context;
    String name, type;

    public MyNotification(Context context, String name, String type) {
        this.context = context;
        this.name = name;
        this.type = type;

    }

    public void send() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Notification");
        PendingIntent contentIntent;
        if (type == "Pay") {
            contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, MyFiles.class), PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText("Your order: " + name + " has been purchased!\nCheck your file and history and then,\nmake a review about it!"));
            builder.setContentTitle("Payment made!");
        } else {
            contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, SpaceTab.class), PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText("Your album: " + name + " has been added to our store!\nCheck the used store!"));
            builder.setContentTitle("Album sold!");
        }
        builder.setCategory(NotificationCompat.CATEGORY_MESSAGE);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setSmallIcon(R.drawable.logo2);
        builder.setColor(Color.rgb(255, 122, 0));
        builder.setAutoCancel(true);
        builder.setContentIntent(contentIntent);


        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(1, builder.build());
    }
}
