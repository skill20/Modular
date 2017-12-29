package com.baselib.app;

import android.content.Context;
import android.support.v4.content.ContextCompat;


import com.baselib.fs.DirType;
import com.baselib.fs.DirectoryManager;

import java.io.File;
import java.util.HashMap;

/**
 * & author pc-qing
 * & date 2016/7/11 15:24
 * & since 6.0.0
 * & copyright TCL-MIG
 */
public abstract class ServiceContext {

    private final Context context;
    protected static ServiceContext _instance;

    private HashMap<String, Object> mServiceMap = new HashMap<>();

    public ServiceContext(Context context) {
        this.context = context.getApplicationContext();
    }

    public static ServiceContext get() {
        return _instance;
    }

    public Context getApplicationContext() {
        return context;
    }

    public Object registerSystemObject(String name, Object obj) {
        if (obj == null) {
            return mServiceMap.remove(name);
        } else {
            return mServiceMap.put(name, obj);
        }
    }

    public Object getSystemObject(String name) {
        return mServiceMap.get(name);
    }

    public static File getDirectory(DirType type) {
        DirectoryManager manager = get().getDirectoryManager();

        File file = null;
        if (manager != null) {
            file = manager.getDir(type.value());
        }

        if (file == null || !file.exists()) {
            //never come here
            Context context = get().getApplicationContext();
            File[] dirs = ContextCompat.getExternalFilesDirs(context, type.name());
            file = dirs[0];

            if (!file.exists()) {
                file.mkdirs();
            }
        }

        return file;
    }

    public static String getDirectoryPath(DirType type) {
        File file = getDirectory(type);
        return file.getAbsolutePath();
    }

    public abstract DirectoryManager getDirectoryManager();
}
