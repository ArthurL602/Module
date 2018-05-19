package com.ljb.baselibrary.fixbug;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.BaseDexClassLoader;

/**
 * Author      :ljb
 * Date        :2018/5/19
 * Description :
 */
public class MyFixDexManager {
    private Context mContext;
    private File mDexDir;

    public MyFixDexManager(Context context) {
        mContext = context;
        // 获取应用可以访问的dex目录
        mDexDir = context.getDir("odex", Context.MODE_PRIVATE);
    }

    /**
     * @param fileDexPath
     * @throws Exception
     */
    public void fixDex(String fileDexPath) throws Exception {
        // 1. 获取已经运行的的 DexElement
        ClassLoader applicationClassLoader = mContext.getClassLoader();
        Object applicationDexElements = getDexElementsByClassLoader(applicationClassLoader);

        // 2. 获取下载好的补丁的 DexElement
        // 2.1 移动到系统能够访问的 dex目录下  -- > ClassLoader
        File srcFile = new File(fileDexPath);
        if (!srcFile.exists()) {
            new FileNotFoundException("srcFile不存在");
        }
        File targetFile = new File(mDexDir, srcFile.getName());
        if (targetFile.exists()) { // 名字基本不一致
            return;
        }
        copyFile(srcFile, targetFile);
        // 2.2 ClassLoader读取fixDex的路径
        List<File> fixDexFiles = new ArrayList<>();
        fixDexFiles.add(targetFile);
        // 解压路径
        File optimizeDirection = new File(mDexDir, "odex");
        if (!optimizeDirection.exists()) {
            optimizeDirection.mkdirs();
        }
        // 修复
        for (File fixDexFile : fixDexFiles) {
            // dexPath  dex路径
            // optimizedDirectory  解压路径
            // libraryPath .so文件位置
            // parent 父ClassLoader
            ClassLoader fixDexClassLoader = new BaseDexClassLoader(//
                    fixDexFile.getAbsolutePath(),// 必须要在应用目录下的odex文件中 “”
                    optimizeDirection,//
                    null,//
                    applicationClassLoader//
            );

            Object fixDexElements = getDexElementsByClassLoader(fixDexClassLoader);
            // 3. 把补丁的DexElement插到已经运行的 DexElement的最前面 合并
            // 3.1 applicationClassLoader 数组 合并 fixDexElement 数组
            applicationDexElements= combineArray(fixDexElements,applicationDexElements);
        }
        // 3.2 把合并的数组注入原来的applicationClassLoader中
        injectDexElement(applicationClassLoader,applicationDexElements);
    }

    /**
     * 把dexElements注入到classLoader中
     * @param classLoader
     * @param dexElements
     */
    private void injectDexElement(ClassLoader classLoader, Object dexElements) throws Exception {
        // 1. 先获取获取pathList
        Field pathListField = BaseDexClassLoader.class.getField("pathList");
        // 设置私有可用
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(classLoader);
        // 2. 获取pathList里面的DexElement;
        Field dexElementsField = pathList.getClass().getField("dexElements");
        // 设置私有可用
        dexElementsField.setAccessible(true);

        dexElementsField.set(pathList,dexElements);
    }

    /**
     * 加载全部的修复包
     */
    public void loadFixDex(){
        File[] dexFiles = mDexDir.listFiles();
        List<File> fixDexFiles = new ArrayList<>();
        for (File dexFile : fixDexFiles) {
            if (dexFile.getName().endsWith(".dex")) {
                fixDexFiles.add(dexFile);
            }
        }
        fixDexFiles(fixDexFiles);
    }

    /**
     * 修复dex
     * @param fixDexFiles
     */
    private void fixDexFiles(List<File> fixDexFiles) {
    }

    /**
     * 复制文件
     *
     * @param src
     * @param dest
     * @throws IOException
     */
    public static void copyFile(File src, File dest) throws IOException {
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            if (!dest.exists()) {
                dest.createNewFile();
            }
            inChannel = new FileInputStream(src).getChannel();
            outChannel = new FileOutputStream(dest).getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }

    /**
     * 从ClassLoader中获取DexElement
     *
     * @param classLoader
     * @return
     */
    private Object getDexElementsByClassLoader(ClassLoader classLoader) throws Exception {
        // 1. 先获取获取pathList
        Field pathListField = BaseDexClassLoader.class.getField("pathList");
        // 设置私有可用
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(classLoader);
        // 2. 获取pathList里面的DexElement;
        Field dexElementsField = pathList.getClass().getField("dexElements");
        // 设置私有可用
        dexElementsField.setAccessible(true);
        return dexElementsField.get(pathList);
    }

    /**
     * 合并两个数组
     *
     * @param arrayLhs
     * @param arrayRhs
     * @return
     */
    private static Object combineArray(Object arrayLhs, Object arrayRhs) {
        Class<?> localClass = arrayLhs.getClass().getComponentType();
        int i = Array.getLength(arrayLhs);
        int j = i + Array.getLength(arrayRhs);
        Object result = Array.newInstance(localClass, j);
        for (int k = 0; k < j; ++k) {
            if (k < i) {
                Array.set(result, k, Array.get(arrayLhs, k));
            } else {
                Array.set(result, k, Array.get(arrayRhs, k - i));
            }
        }
        return result;
    }
}
