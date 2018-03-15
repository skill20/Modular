package com.baselib.fs;

/**
 * @author devilxie
 * @version 1.0
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
