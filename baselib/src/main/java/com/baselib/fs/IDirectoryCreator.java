package com.worldunion.library.fs;

import java.io.IOException;

/**
 * & author pc-qing
 * & date 2016/7/11 13:16
 * & since 6.0.0
 * & copyright TCL-MIG
 */
interface IDirectoryCreator {
    /**
     * 创建文件目录，并根据条件清除过期缓存
     * @param directory 目录实体
     * @param cleanable 是否清除过期缓存
     * @return 创建成功与否
     */
    boolean createDirectory(Directory directory, boolean cleanable) throws IOException;
}
