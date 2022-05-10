package com.lespayne.smartwear.noti;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;

import com.lespayne.smartwear.App;
import com.lespayne.smartwear.R;

public class NotificationHelper {
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private NotificationManager nm;

    public static final String CHANNEL_ID = "ai_voice_service";
    public static final String CHANNEL_NAME = "语音服务";

    private NotificationHelper() {
    }

    private static class SingleTon {
        @SuppressLint("StaticFieldLeak")
        private static NotificationHelper INSTANCE = new NotificationHelper();
    }

    public static NotificationHelper getInstance() {
        return NotificationHelper.SingleTon.INSTANCE;
    }

    public void initHelper(Context context) {
        this.context = context;
        nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        setBindVoiceChannel();
    }

    private void setBindVoiceChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            //呼吸灯关闭
            notificationChannel.enableLights(true);
            //震动关闭
            notificationChannel.enableVibration(true);
            //角标关闭
            notificationChannel.setShowBadge(true);
            nm.createNotificationChannel(notificationChannel);
        }
    }

    public void bindVoiceService(Service service) {
        @SuppressLint("RemoteViewLayout")
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setContentText("正在运行中...")
                .setSmallIcon(R.drawable.icon_app)
                .build();
        service.startForeground(1, notification);
    }
}
