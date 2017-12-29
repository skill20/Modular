package com.baselib.fs;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.content.ContextCompat;


import com.baselib.log.NLog;
import com.baselib.utils.AndroidNewApi;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * & author pc-qing
 * & date 2016/7/11 13:09
 * & since 6.0.0
 * & copyright TCL-MIG
 */
public abstract class DirectoryContext {

    private final static String BASE_DATA_DIRECTORY = "app_dir_root";
    private static final int APP_ROOT_DIR = 0;

    private IDirectoryCreator creator;
    private Context context;
    private String rootPath;

    protected DirectoryContext(Context context, String rootPath) {
        this.context = context.getApplicationContext();
        this.rootPath = rootPath;
    }

    void setCreator(IDirectoryCreator creator) {
        this.creator = creator;
    }

    protected abstract Collection<Directory> initDirectories();

    boolean buildAndClean() {
        IDirectoryCreator directoryCreator = creator;
        if (directoryCreator == null) {
            throw new IllegalStateException("can't create dir creator,please create dir creator first");
        }

        Collection<Directory> children = initDirectories();
        boolean ret = false;
        Directory directory;
        do {

            if (!isExternalSDMounted() || !hasEnoughSpace()) {
                break;
            }

            String path;
            int permission = ContextCompat.checkSelfPermission(context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permission == PackageManager.PERMISSION_GRANTED &&
                    Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                        File.separator + rootPath;
            } else {
                File[] files = ContextCompat.getExternalFilesDirs(context, rootPath);
                if (files == null || files.length <= 0 || files[0] == null) {
                    break;
                }
                path = files[0].getAbsolutePath();
            }

            directory = new Directory(path, null);
            directory.setType(APP_ROOT_DIR);

            if (children != null && !children.isEmpty()) {
                directory.addChildren(children);
            }

            try {
                ret = creator.createDirectory(directory, true);
                return ret;
            } catch (IOException e) {
                NLog.printStackTrace(e);
                directory.removeAll();
            }

        } while (false);

        File dir = context.getDir(BASE_DATA_DIRECTORY, Context.MODE_PRIVATE);
        directory = new Directory(dir.getAbsolutePath(), null);
        directory.setType(APP_ROOT_DIR);

        if (children != null && !children.isEmpty()) {
            directory.addChildren(children);
        }

        try {
            ret = creator.createDirectory(directory, true);
        } catch (IOException e) {
            NLog.printStackTrace(e);
            directory.removeAll();
        }

        return ret;
    }

    private boolean isExternalSDMounted() {
        return Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED);
    }


    private long getExternalSDFreeSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = AndroidNewApi.getBlockSizeLong(stat);
        long availableBlocks = AndroidNewApi.getAvailableBlocks(stat);
        return availableBlocks * blockSize;
    }

    private boolean hasEnoughSpace() {
        long size = getExternalSDFreeSize();
        if (size <= 0)
            return false;

        size >>= 20;
        return size >= 5;
    }
}
