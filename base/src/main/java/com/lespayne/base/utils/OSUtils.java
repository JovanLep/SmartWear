package com.lespayne.base.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;

/**
 * 手机系统判断
 */
public class OSUtils {
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_EMUI_VERSION_NAME = "ro.build.version.emui";
    private static final String KEY_DISPLAY = "ro.build.display.id";
    private static final String KEY_HARMONY_OS = "harmony";

    public static boolean isIgnoringBatteryOptimizations(Context context) {
        boolean isIgnoring = false;
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            isIgnoring = powerManager.isIgnoringBatteryOptimizations(getPackageName());
        }
        return isIgnoring;
    }

    public  static void requestIgnoreBatteryOptimizations(Context context) {
        try {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getPackageName() {
        return "com.lespayne.smartwear";
    }

    /**
     * 跳转到指定应用的首页
     */
    private static void showActivity(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        context.startActivity(intent);
    }

    /**
     * 跳转到指定应用的指定页面
     */
    private static void showActivity(Context context, String packageName, String activityDir) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(packageName, activityDir));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void startSystem(Context context){
        if (isXiaomi()){
            goXiaomiSetting(context);
        }else if (isHuawei()){
            goHuaweiSetting(context);
        }else if (isOPPO()){
            goOPPOSetting(context);
        }else if (isVIVO()){
            goVIVOSetting(context);
        }else if (isMeizu()){
            goMeizuSetting(context);
        }else if (isSamsung()){
            goSamsungSetting(context);
        }
    }

    public static boolean isXiaomi() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("xiaomi");
    }

    public static boolean isHuawei() {
        if (Build.BRAND == null) {
            return false;
        } else {
            return Build.BRAND.toLowerCase().equals("huawei") || Build.BRAND.toLowerCase().equals("honor");
        }
    }

    public static boolean isOPPO() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("oppo");
    }

    public static boolean isVIVO() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("vivo");
    }

    public static boolean isMeizu() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("meizu");
    }

    public static boolean isSamsung() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("samsung");
    }

    private static void goHuaweiSetting(Context context) {
        try {
            showActivity(context, "com.huawei.systemmanager", "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity");
        } catch (Exception e) {
            showActivity(context, "com.huawei.systemmanager",
                    "com.huawei.systemmanager.optimize.bootstart.BootStartActivity");
        }
    }

    private static void goXiaomiSetting(Context context) {
        showActivity(context, "com.miui.securitycenter",
                "com.miui.permcenter.autostart.AutoStartManagementActivity");
    }

    private static void goOPPOSetting(Context context) {
        try {
            showActivity(context, "com.coloros.phonemanager");
        } catch (Exception e1) {
            try {
                showActivity(context, "com.oppo.safe");
            } catch (Exception e2) {
                try {
                    showActivity(context, "com.coloros.oppoguardelf");
                } catch (Exception e3) {
                    showActivity(context, "com.coloros.safecenter");
                }
            }
        }
    }

    private static void goVIVOSetting(Context context) {
        showActivity(context, "com.iqoo.secure");
    }

    private static void goMeizuSetting(Context context) {
        showActivity(context, "com.meizu.safe");
    }

    private static void goSamsungSetting(Context context) {
        try {
            showActivity(context, "com.samsung.android.sm_cn");
        } catch (Exception e) {
            showActivity(context, "com.samsung.android.sm");
        }
    }
}
