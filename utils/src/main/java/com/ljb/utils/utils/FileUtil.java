package com.ljb.utils.utils;

import java.io.File;

/**
 * 文件工具类
 */

public class FileUtil {

    /**
     * 根据路径获取文件
     *
     * @param filePath
     * @return
     */
    public static File getFileByPath(String filePath) {
        return filePath != null ? new File(filePath) : null;
    }

    /**
     * 根据文件路径判断文件是否存在
     *
     * @return
     */
    public static boolean isFileExists(String filePath) {

        return isFileExists(getFileByPath(filePath));
    }

    /**
     * 判断文件是否存在
     *
     * @param file
     * @return
     */
    public static boolean isFileExists(File file) {
        return file != null && file.exists();
    }

    /**
     * 判断是否为目录
     *
     * @param dir
     * @return
     */
    public static boolean isDir(File dir) {
        return dir != null && dir.exists() && dir.isDirectory();
    }

    /**
     * 判断文件是否为目录
     *
     * @param dirPath
     * @return
     */
    public static boolean isDir(String dirPath) {
        return isDir(getFileByPath(dirPath));
    }

    /**
     * 判断是否为文件
     *
     * @param file
     * @return
     */
    public static boolean isFile(File file) {
        return file != null && file.exists() && file.isFile();
    }

    /**
     * 判断是否为文件
     *
     * @param filePath
     * @return
     */
    public static boolean isFile(String filePath) {
        return isFile(getFileByPath(filePath));
    }

    /**
     * 删除目录（包括目录和目录里面的所有文件）
     *
     * @param file
     */
    public static void deleteFiles(File file) {
        if (file == null) {
            return;
        }
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null || files.length == 0) {
                file.delete();
                return;
            }
            for (int i = 0; i < files.length; i++) {
                deleteFiles(files[i]);
            }
            file.delete();
        }
    }

    /**
     * 删除文件
     *
     * @param filePath
     */
    public static void deleteFile(String filePath) {
        deleteFiles(getFileByPath(filePath));
    }

}
