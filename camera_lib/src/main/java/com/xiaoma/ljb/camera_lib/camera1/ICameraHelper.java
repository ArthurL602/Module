package com.xiaoma.ljb.camera_lib.camera1;

import android.hardware.Camera;

/**
 * CameraHelper的统一接口
 *
 * @author jerry
 * @date 2015-09-01
 */
public interface ICameraHelper {
    Camera openCameraFacing(int facing) throws Exception;

    boolean hasCamera(int facing);

    void getCameraInfo(int cameraId, Camera.CameraInfo cameraInfo);
}
