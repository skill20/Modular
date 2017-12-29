package com.baselib.fs;

import java.util.ArrayList;
import java.util.Collection;

/**
 * & author pc-qing
 * & date 2016/7/11 13:11
 * & since 6.0.0
 * & copyright TCL-MIG
 */
public final class Directory {

    private String path;
    private Directory parent;
    private Collection<Directory> children;
    private boolean cache = false;
    private int type = -1;
    private long expiredTime = -1L;

    public Directory(String path, Directory parent) {
        this.path = path;
        this.parent = parent;
    }

    public void addChild(Directory directory) {
        if (children == null) {
            children = new ArrayList<>();
        }

        directory.parent = this;
        children.add(directory);
    }

    void addChildren(Collection<Directory> dir) {
        if (dir == null || dir.size() == 0) {
            return;
        }

        for (Directory directory : dir) {
            addChild(directory);
        }
    }

    void removeAll() {
        if (children == null || children.size() == 0) {
            return;
        }

        for (Directory d : children) {
            d.parent = null;
        }
        children.clear();
    }

    public String getPath() {
        return path;
    }

    public Directory getParent() {
        return parent;
    }

    public boolean isCache() {
        return cache;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }

    public long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(long expiredTime) {
        this.expiredTime = expiredTime;
    }

    public int getType() {
        return type;
    }

    public Collection<Directory> getChildren() {
        return children;
    }

    public void setType(int type) {
        this.type = type;
    }
}
