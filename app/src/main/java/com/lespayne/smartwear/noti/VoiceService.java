package com.lespayne.smartwear.noti;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;


public class VoiceService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bindNotification();
        return super.onStartCommand(intent, flags, startId);
    }

    private void bindNotification() {
        NotificationHelper.getInstance().bindVoiceService(this);
    }
}
