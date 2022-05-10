package com.lespayne.config;

import java.text.SimpleDateFormat;

public class ProjectConfig {

    public static boolean isModuleDebug = false;

    public static final int compileSdkVersion = 31;

    public static final String applicationId = "com.lespayne.smartwear";
    public static final int minSdkVersion = 23;
    public static final int targetSdkVersion = 31;
    public static final int versionCode = 1;
    public static final String versionName = "1.0";


    public static boolean isApp(String projectName){
        return projectName.equals("app");
    }

    public static String getDate(){
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMddHHmm");
        return  dateFormat.format(System.currentTimeMillis());
    }

}
