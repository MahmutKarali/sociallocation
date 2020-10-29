package com.application.atplexam.Utility;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.application.atplexam.Controller.Login.SplashController;
import com.application.atplexam.R;

import java.util.List;
import java.util.Map;

import static android.media.RingtoneManager.getDefaultUri;

public class FirebaseMessagingService {
}
/*public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        Map<String, String> message = remoteMessage.getData();
        RemoteMessage.Notification notification = remoteMessage.getNotification();

        try {
            int requestId = (int) System.currentTimeMillis();
            long[] pattern = {500, 500, 500, 500};//Titreşim ayarı
            String messageBody = notification.getBody();
            String messageTitle = notification.getTitle();

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.app_icon)
                    .setContentTitle(messageTitle)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                    .setNumber(6)
                    .setColor(Color.RED)
                    .setChannelId("")
                    .setSound(getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setVibrate(pattern);

            if (ApplicationStateChecker.getAppIsBackground()) {
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                Intent resultIntent;
                resultIntent = new Intent(this, SplashController.class);
                resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                stackBuilder.addNextIntent(resultIntent);

                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(resultPendingIntent);
            } else {
                Intent readReceive = new Intent();
                readReceive.setAction("");
                readReceive.putExtra("count", 1);
                readReceive.putExtra("body", messageBody);
                readReceive.putExtra("title", messageTitle);
                //sendBroadcast(readReceive);

                PendingIntent pendingIntentRead = PendingIntent.getBroadcast(this, 12345, readReceive, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(pendingIntentRead);
            }

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel("", "Uskudar", importance);
                mChannel.setShowBadge(true);
                mNotificationManager.createNotificationChannel(mChannel);
            }

            mNotificationManager.notify(requestId, mBuilder.build());
        } catch (Exception ex) {
        }
    }

    public static void setBadge(Context context, int count) {
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", launcherClassName);
        context.sendBroadcast(intent);
    }

    public static String getLauncherClassName(Context context) {

        PackageManager pm = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (pkgName.equalsIgnoreCase(context.getPackageName())) {
                String className = resolveInfo.activityInfo.name;
                return className;
            }
        }
        return null;
    }
    @Override
    public void onNewToken(String token) {
        //Log.d("AXA Acentem", "Refreshed token: " + token);
    }
    // [END on_new_token]

    private void handleNow() {
        //Log.d("AXA Acentem", "Short lived task is done.");
    }

    class ForegroundCheckTask extends AsyncTask<Context, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Context... params) {
            final Context context = params[0].getApplicationContext();
            return isAppOnForeground(context);
        }

        private boolean isAppOnForeground(Context context) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
            if (appProcesses == null) {
                return false;
            }
            final String packageName = context.getPackageName();
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if ((appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) && appProcess.processName.equals(packageName)) {
                    return true;
                }
            }
            return false;
        }
    }
} */