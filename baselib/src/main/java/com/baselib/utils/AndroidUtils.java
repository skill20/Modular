package com.baselib.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * @author devilxie
 * @version 1.0
 */
public class AndroidUtils {
    public static String getTopActivity(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

        try {
            if (runningTaskInfos != null)
                return (runningTaskInfos.get(0).topActivity).toString();
            else
                return "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getCurrentProcessName(Context context) {
        String currentProcessName = "";
        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == pid) {
                currentProcessName = processInfo.processName;
                break;
            }
        }

        return currentProcessName;
    }
}
