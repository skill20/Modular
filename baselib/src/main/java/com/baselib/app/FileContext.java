package com.baselib.app;

import android.content.Context;


import com.baselib.fs.DirType;
import com.baselib.fs.Directory;
import com.baselib.fs.DirectoryContext;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author devilxie
 * @version 1.0
 */
public class FileContext extends DirectoryContext {
    private static final long ONE_DAY_MS = 1000 * 60 * 60 * 24L;

    FileContext(Context context, String rootPath) {
        super(context, rootPath);
    }

    @Override
    protected Collection<Directory> initDirectories() {
        ArrayList<Directory> children = new ArrayList<>();
        children.add(newDirectory(DirType.log));
        children.add(newDirectory(DirType.crash));
        children.add(newDirectory(DirType.image));
        children.add(newDirectory(DirType.cache));
        return children;
    }

    private Directory newDirectory(DirType type) {
        Directory child = new Directory(type.name(), null);
        child.setType(type.value());
        if (type.equals(DirType.cache)) {
            child.setCache(true);
            child.setExpiredTime(ONE_DAY_MS);
        }

        return child;
    }
}
