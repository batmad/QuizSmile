package com.batmad.quizsmile;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.TaskStackBuilder;

/**
 * Created by Tm on 08.05.2017.
 */

public class NotifManager {
    private Context context;
    private GameManager gameManager;
    private String theme;
    private int savedTips;

    NotifManager(Context context, GameManager gm, String theme){
        this.context = context;
        this.gameManager = gm;
        this.theme = theme;
        String savedWords = "words" + theme;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        savedTips = sharedPref.getInt(savedWords, 0);
    }

    private void scheduleNotification(Notification notification, long delay, int words, boolean cancel) {
        Intent notificationIntent = new Intent(context, NotificationSender.class);
        notificationIntent.putExtra(NotificationSender.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationSender.NOTIFICATION, notification);
        notificationIntent.putExtra("words", words);
        notificationIntent.putExtra("theme", theme);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int)delay, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
        if (cancel)
            alarmManager.cancel(pendingIntent);
    }

    private Notification getNotification(String content) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("Scheduled Notification");
        builder.setContentText(content);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.FLAG_SHOW_LIGHTS);

        Intent resultIntent = new Intent(context, StartActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(StartActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);

        return builder.build();
    }

    public void SetNotifications(boolean cancel){
        String answer = gameManager.GetAnswer().replaceAll("\\s+","").toUpperCase();
        int size = (int)Math.ceil(answer.length() / 2);
        int timeCounter = 1;
        for (int i = 0; i < size; ++i){
            if (i < savedTips)
                continue;
            String count = context.getResources().getQuantityString(R.plurals.words, i + 1);
            String word = String.valueOf(answer.charAt(i));
            String notificationStr = context.getResources().getString(R.string.notification, count, word);
            scheduleNotification(getNotification(notificationStr), timeCounter * 24 * 60 * 60 * 1000, i + 1, cancel);
            ++timeCounter;
        }
    }
}
