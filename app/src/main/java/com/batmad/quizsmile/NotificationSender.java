package com.batmad.quizsmile;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Tm on 17.04.2017.
 */

public class NotificationSender extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "com.batmad.quizsmile.id";
    public static String NOTIFICATION = "notification";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        notificationManager.notify(id, notification);
        CheckTips(context, intent);
    }

    public void CheckTips(Context context, Intent intent){
        int intentTips = intent.getIntExtra("words", 0);
        String theme = intent.getStringExtra("theme");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        String savedWords = "words" + theme;
        int savedTips = prefs.getInt(savedWords, 0);
        if(intentTips > savedTips) {
            savedTips = intentTips;
            editor.putInt(savedWords, savedTips);
            editor.commit();
        }
    }
}
