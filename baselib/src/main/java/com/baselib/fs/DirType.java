package com.worldunion.library.fs;

/**
 * & author pc-qing
 * & date 2016/7/11 15:39
 * & since 6.0.0
 * & copyright TCL-MIG
 */
public enum DirType {
    log,
    image,
    cache,
    crash;

    public int value() {
        return ordinal() + 1;
    }
    }
