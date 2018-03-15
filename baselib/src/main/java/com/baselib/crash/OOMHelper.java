package com.baselib.crash;

import android.content.Context;
import android.os.Debug;
import android.view.InflateException;


import com.baselib.log.NLog;
import com.baselib.utils.FileUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author devilxie
 * @version 1.0
 */
public class OOMHelper {

    private final static String TAG = "OOMHelper";

    @SuppressWarnings("unused")
    private final static String OOM_DIR = "oom";
    private final static String OOM_SUFFIX = ".hprof";

    private OOMHelper() {
        // static usage.
    }

    public static boolean dumpHprofIfNeeded(Context context, Throwable e, String dir,boolean enableOOM) {
        if (!enableOOM || !isOOM(e)) {
            // dump only in debug mode.
            return false;
        }

        try {
            String name = getDate() + "#" + e.getClass().getSimpleName() + OOM_SUFFIX;
            String path = dir != null ? dir + File.separator + name : null;
            File file = path != null ? new File(path) : null;
            // delete others if needed.
//            if (file != null && !Constant.DEBUG) {
//                // keep only one dump file in non-package-debuggable mode.
//                FileUtils.deleteFile(file.getParentFile().getAbsolutePath());
//            }
            // perform dump.
            if (file != null && ensureDir(file.getParentFile())) {
                Debug.dumpHprofData(path);
            }
        } catch (Throwable t) {
            NLog.d(TAG, "fail to dump hprof %s", t.getMessage());
        }
        return true;
    }

    public static boolean isOOM(Throwable e) {
        int loopCount = 0;
        while (e != null && loopCount++ < 5) {
            if (isOOMInner(e)) {
                return true;
            }
            e = e.getCause();
        }
        return false;
    }

    private static boolean isOOMInner(Throwable e) {
        if (e == null) {
            return false;
        }
        return (e instanceof OutOfMemoryError) || (e instanceof InflateException);
    }

    private static String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.CHINA);
        return sdf.format(new Date(System.currentTimeMillis()));
    }

    private static boolean isDirValid(File dir) {
        return dir != null && dir.isDirectory() && dir.exists();
    }

    private static boolean ensureDir(File dir) {
        if (dir == null) {
            return false;
        }
        if (!isDirValid(dir)) {
            FileUtils.deleteDir(dir);
            return dir.mkdirs();
        }
        return true;
    }
}
