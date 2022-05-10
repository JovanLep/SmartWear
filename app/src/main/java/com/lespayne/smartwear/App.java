package com.lespayne.smartwear;

import android.app.Application;
import com.lespayne.smartwear.noti.NotificationHelper;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mmkv.MMKV;

public class App extends Application {
    public boolean isConnect = false;
    public boolean mDeviceConnected = false;
    public int beat = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        MMKV.initialize(this);
        NotificationHelper.getInstance().initHelper(this);
        CrashReport.initCrashReport(getApplicationContext(), "c00cc51050", false);
    }

    private static App myApplication = null;

    public static App getApp() {
        return myApplication;
    }
}
