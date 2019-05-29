package com.xiaoma.ljb.camera_lib.camera1;

import android.content.Context;
import android.hardware.Camera;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * 相机管理类 （singleTon）
 *
 * @author jerry
 * @date 2015-09-01
 */
public class CameraManager implements ICameraHelper {

    private static CameraManager mInstance;
    private final ICameraHelper mCameraHelper;
    private Camera mActivityCamera;

    private CameraDirection mFlashDirection;

    private Context mContext;

    public static final String SP_CAMERA_DIRECTION = "SP_CAMERA_DIRECTION";

    public static final int LEN_PIC = 64;   //图片的边长   px

    public static final int TYPE_PREVIEW = 0;
    public static final int TYPE_PICTURE = 1;

    public static final int ALLOW_PIC_LEN = 2000;       //最大允许的照片尺寸的长度   宽或者高

    //屏蔽默认构造方法
    private CameraManager(Context context) {

        mContext = context;
        mCameraHelper = new CameraHelperGBImpl();
        mFlashDirection = CameraDirection.valueOf( CameraDirection.CAMERA_BACK.ordinal()); //默认后置摄像头
    }

    public static CameraManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (CameraManager.class) {
                if (mInstance == null) {
                    mInstance = new CameraManager(context);
                }
            }
        }
        return mInstance;
    }

    public void setActivityCamera(Camera mActivityCamera) {
        this.mActivityCamera = mActivityCamera;
    }


    public CameraDirection getCameraDirection() {
        return mFlashDirection;
    }


    @Override
    public Camera openCameraFacing(int facing) throws Exception {
        Camera camera = mCameraHelper.openCameraFacing(facing);
        return camera;
    }

    @Override
    public boolean hasCamera(int facing) {
        return mCameraHelper.hasCamera(facing);
    }

    @Override
    public void getCameraInfo(int cameraId, Camera.CameraInfo cameraInfo) {
        mCameraHelper.getCameraInfo(cameraId, cameraInfo);
    }

    /**
     * 设置相机拍照的尺寸
     *
     * @param camera
     */
    public void setUpPicSize(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();

        try {
            Camera.Size adapterSize = findBestResolution(camera, 1.0d, TYPE_PICTURE);
            parameters.setPictureSize(adapterSize.width, adapterSize.height);
            camera.setParameters(parameters);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param camera
     * @param bl
     */
    public void setFitPicSize(Camera camera, float bl) {
        Camera.Parameters parameters = camera.getParameters();

        try {
            Camera.Size adapterSize = findFitPicResolution(camera, bl);
            parameters.setPictureSize(adapterSize.width, adapterSize.height);
            camera.setParameters(parameters);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置合适的预览尺寸
     *
     * @param camera
     */
    public void setFitPreSize(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();

        try {
            Camera.Size adapterSize = findFitPreResolution(camera);
            parameters.setPictureSize(adapterSize.width, adapterSize.height);
            camera.setParameters(parameters);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回合适的照片尺寸参数
     *
     * @param camera
     * @param bl
     * @return
     */
    private Camera.Size findFitPicResolution(Camera camera, float bl) throws Exception {
        Camera.Parameters cameraParameters = camera.getParameters();
        List<Camera.Size> supportedPicResolutions = cameraParameters.getSupportedPictureSizes();

        Camera.Size resultSize = null;
        for (Camera.Size size : supportedPicResolutions) {
            if ((float) size.width / size.height == bl && size.width <= ALLOW_PIC_LEN && size.height <= ALLOW_PIC_LEN) {
                if (resultSize == null) {
                    resultSize = size;
                } else if (size.width > resultSize.width) {
                    resultSize = size;
                }
            }
        }
        if (resultSize == null) {
            return supportedPicResolutions.get(0);
        }
        return resultSize;
    }

    /**
     * 返回合适的预览尺寸参数
     *
     * @param camera
     * @return
     */
    private Camera.Size findFitPreResolution(Camera camera) throws Exception {
        Camera.Parameters cameraParameters = camera.getParameters();
        List<Camera.Size> supportedPicResolutions = cameraParameters.getSupportedPreviewSizes();

        Camera.Size resultSize = null;
        for (Camera.Size size : supportedPicResolutions) {
            if (size.width <= ALLOW_PIC_LEN) {
                if (resultSize == null) {
                    resultSize = size;
                } else if (size.width > resultSize.width) {
                    resultSize = size;
                }
            }
        }
        if (resultSize == null) {
            return supportedPicResolutions.get(0);
        }
        return resultSize;
    }

    /**
     * 找到合适的尺寸
     *
     * @param cameraInst
     * @param maxDistortion 最大允许的宽高比
     * @return
     * @type 尺寸类型 0：preview  1：picture
     */
    public Camera.Size findBestResolution(Camera cameraInst, double maxDistortion, int type) throws Exception {
        Camera.Parameters cameraParameters = cameraInst.getParameters();
        List<Camera.Size> supportedPicResolutions = type == TYPE_PREVIEW ? cameraParameters.getSupportedPreviewSizes() : cameraParameters.getSupportedPictureSizes(); // 至少会返回一个值

        StringBuilder picResolutionSb = new StringBuilder();
        for (Camera.Size supportedPicResolution : supportedPicResolutions) {
            picResolutionSb.append(supportedPicResolution.width).append('x')
                    .append(supportedPicResolution.height).append(" ");
        }

        Camera.Size defaultPictureResolution = cameraParameters.getPictureSize();

        // 排序
        List<Camera.Size> sortedSupportedPicResolutions = new ArrayList<Camera.Size>(
                supportedPicResolutions);
        Collections.sort(sortedSupportedPicResolutions, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size a, Camera.Size b) {
                int aRatio = a.width / a.height;
                int bRatio = b.width / a.height;

                if (Math.abs(aRatio - 1) <= Math.abs(bRatio - 1)) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });

        //返回最合适的
        return sortedSupportedPicResolutions.get(0);
    }


    public void releaseActivityCamera() {
        if (mActivityCamera != null) {
            try {
                mActivityCamera.stopPreview();
                mActivityCamera.setPreviewCallback(null);
                mActivityCamera.setPreviewCallbackWithBuffer(null);
                mActivityCamera.release();
                mActivityCamera = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void releaseCamera(Camera camera) {
        if (camera != null) {
            try {
                camera.release();
                camera.stopPreview();
                camera.setPreviewCallback(null);
                camera.setPreviewCallbackWithBuffer(null);
                camera = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 前置还是后置摄像头
     */
    public enum CameraDirection {
        CAMERA_BACK, CAMERA_FRONT;

        //不断循环的枚举
        public CameraDirection next() {
            int index = ordinal();
            int len = CameraDirection.values().length;
            return CameraDirection.values()[(index + 1) % len];
        }

        public static CameraDirection valueOf(int index) {
            return CameraDirection.values()[index];
        }
    }
}
