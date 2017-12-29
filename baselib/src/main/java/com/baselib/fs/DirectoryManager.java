package com.worldunion.library.fs;

import android.util.SparseArray;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collection;

/**
 * & author pc-qing
 * & date 2016/7/11 13:07
 * & since 6.0.0
 * & copyright TCL-MIG
 */
public final class DirectoryManager implements IDirectoryCreator {

    private DirectoryContext directoryContext;
    private SparseArray<File> sparseArray;

    public DirectoryManager(DirectoryContext context) {
        this.directoryContext = context;
        this.directoryContext.setCreator(this);
        sparseArray = new SparseArray<>(10);
    }

    public boolean buildAndClean() {
        return directoryContext.buildAndClean();
    }

    @Override
    public boolean createDirectory(Directory directory, boolean cleanable) throws IOException {

        String path;
        Directory parent = directory.getParent();
        if (parent == null) {
            path = directory.getPath();
        } else {
            path = parent.getPath() + File.separator + directory.getPath();
        }

        File file = new File(path);
        boolean ret = true;
        if (!file.exists()) {
            ret = file.mkdirs();
        } else if (cleanable && directory.isCache()) {
            cleanCache(file, directory.getExpiredTime());
        }

        if (!ret) {
            return false;
        }

        sparseArray.put(directory.getType(), file);

        Collection<Directory> children = directory.getChildren();
        if (children != null && !children.isEmpty()) {
            for (Directory dir : children) {
                if (!createDirectory(dir, true)) {
                    return false;
                }
            }
        }

        return ret;
    }

    private void cleanCache(File dir, final long expiredTime) {
        File[] files = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isFile() &&
                        CacheChecker.expired(pathname, expiredTime);
            }
        });

        if (files == null || files.length <= 0) {
            return;
        }

        for (File file : files) {
            file.delete();
        }
    }

    public File getDir(int type) {
        if (type < 0 || sparseArray.size() == 0)
            return null;
        return sparseArray.get(type);
    }
}
