package kr.co.igo.pleasebuy.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;

import kr.co.igo.pleasebuy.R;
import kr.co.igo.pleasebuy.ui.MainActivity;
import kr.co.igo.pleasebuy.ui.NotificationActivity;
import kr.co.igo.pleasebuy.util.AppController;
import kr.co.igo.pleasebuy.util.Preference;


public class MessageListenerService extends GcmListenerService {
    public final int EVENT = 100;               // 팝업
    public final int NOTICE = 101;              // 공지사항
    public final int USER_PUSH = 102;           // AJPass 입차

    public Preference preference = new Preference();

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {

//        sendNotification(data);

        Bundle extras = data;
        int notificationId = -1;

        if (!extras.isEmpty()) {
//            Log.wtf("extras", extras.toString());
            if (extras.containsKey("type")) {
                String type = extras.getString("type");

                if (type.equals("EVENT")) {
                    notificationId = EVENT;
                } else if (type.equals("USER_NOTICE")) {
                    notificationId = NOTICE;
                } else if (type.equals("USER_PUSH")) {
                    notificationId = USER_PUSH;
                }

                getIntentByNotificationId(notificationId, extras);

                if (!AppController.isVisibleRunning()) {
                    updateBadgeCount(this);
                }
            }
        }
    }

    /**
     * @param data GCM message received.
     */
    private void sendNotification(Bundle data) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(data.getString("title"))
                .setContentText(data.getString("msg"))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    /**
     * notification에 따라 Intent를 얻음
     */
    private void getIntentByNotificationId(int notificationId, Bundle extras) {

        String title = extras.getString("title");      // 푸쉬 내용
        String msg = extras.getString("msg");      // 푸쉬 내용
        String id = extras.containsKey("id") ? extras.getString("id") : "0";         // id;

        Intent intent = new Intent(this, NotificationActivity.class);
        intent.putExtra("notificationId", notificationId);
        intent.putExtra("id", id);

        intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColor(getResources().getColor(R.color.c_f33b2d))
                .setLargeIcon(convertBitmap())
                .setContentTitle(title)
                .setContentText(msg)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify((int) System.currentTimeMillis(), notificationBuilder.build());
    }

    private Bitmap convertBitmap() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        return bitmap;
    }

    /**
     * 뱃지 설정
     */
    private void updateBadgeCount(Context context) {
//        int badgeCount = preference.getBadgeCountPreference();
//        ShortcutBadger.applyCount(context, badgeCount);
    }
}
