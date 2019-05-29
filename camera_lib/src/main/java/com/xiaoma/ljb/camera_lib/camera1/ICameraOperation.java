package com.xiaoma.ljb.camera_lib.camera1;


/**
 * 相机操作的接口
 *
 * @author jerry
 * @date 2015-09-24
 */
public interface ICameraOperation {
    /**
     * 拍照
     */
    boolean takePicture();

    /**
     * 相机最大缩放级别
     *
     * @return
     */
    int getMaxZoom();

    /**
     * 设置当前缩放级别
     *
     * @param zoom
     */
    void setZoom(int zoom);

    /**
     * 获取当前缩放级别
     *
     * @return
     */
    int getZoom();

    /**
     * 释放相机
     */
    void releaseCamera();
}
