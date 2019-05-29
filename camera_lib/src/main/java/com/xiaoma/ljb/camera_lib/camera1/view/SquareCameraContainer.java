package com.xiaoma.ljb.camera_lib.camera1.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.Toast;


import com.xiaoma.ljb.camera_lib.R;
import com.xiaoma.ljb.camera_lib.camera1.CameraConstants;
import com.xiaoma.ljb.camera_lib.camera1.CameraManager;
import com.xiaoma.ljb.camera_lib.camera1.IActivityLifiCycle;
import com.xiaoma.ljb.camera_lib.camera1.ICameraOperation;
import com.xiaoma.ljb.camera_lib.camera1.SensorControler;
import com.xiaoma.ljb.camera_lib.camera1.config.CameraConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 正方形的CamerContainer
 *
 * @author jerry
 * @date 2015-09-16
 */
public class SquareCameraContainer extends FrameLayout implements ICameraOperation, IActivityLifiCycle {

    private Context mContext;

    /**
     * 相机绑定的SurfaceView
     */
    private CameraView mCameraView;

    /**
     * 触摸屏幕时显示的聚焦图案
     */
    private FocusImageView mFocusImageView;
    /**
     * 缩放控件
     */
    private SeekBar mZoomSeekBar;

    private Activity mActivity;

    private SoundPool mSoundPool;

    private boolean mFocusSoundPrepared;

    private int mFocusSoundId;

    private String mImagePath;

    private SensorControler mSensorControler;

    public static final int RESETMASK_DELY = 1000; //一段时间后遮罩层一定要隐藏
    private TakePhotoCallback mTakePhotoCallback;

    private CameraConfig mConfig;

    public void setConfig(CameraConfig config) {
        mConfig = config;
    }

    public void setTakePhotoCallback(TakePhotoCallback takePhotoCallback) {
        mTakePhotoCallback = takePhotoCallback;
    }

    public SquareCameraContainer(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public SquareCameraContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    void init() {
        mConfig = new CameraConfig.Build()//
                .path(Environment.getExternalStorageDirectory().getAbsolutePath() + "/mark/")//
                .build();
        inflate(mContext, R.layout.custom_camera_container, this);

        mCameraView = findViewById(R.id.cameraView);
        mFocusImageView = findViewById(R.id.focusImageView);
        mZoomSeekBar = findViewById(R.id.zoomSeekBar);

        mSensorControler = SensorControler.getInstance(mContext);

        mSensorControler.setCameraFocusListener(new SensorControler.CameraFocusListener() {
            @Override
            public void onFocus() {
                int screenWidth = CameraConstants.CAMERA_WIDTH;
                int screenHeight = CameraConstants.CAMERA_HEIGHT;
                Point point = new Point(screenWidth / 2, screenHeight / 2);

                onCameraFocus(point);
            }
        });
        mCameraView.setOnCameraPrepareListener(new CameraView.OnCameraPrepareListener() {
            @Override
            public void onPrepare(CameraManager.CameraDirection cameraDirection) {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, RESETMASK_DELY);
                //在这里相机已经准备好 可以获取maxZoom
                mZoomSeekBar.setMax(mCameraView.getMaxZoom());

                if (cameraDirection == CameraManager.CameraDirection.CAMERA_BACK) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            int screenWidth = CameraConstants.CAMERA_WIDTH;
                            int screenHeight = CameraConstants.CAMERA_HEIGHT;
                            Point point = new Point(screenWidth / 2, screenHeight / 2);
                            onCameraFocus(point);
                        }
                    }, 800);
                }
            }
        });
        mCameraView.setSwitchCameraCallBack(new CameraView.SwitchCameraCallBack() {
            @Override
            public void switchCamera(boolean isSwitchFromFront) {
                if (isSwitchFromFront) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            int screenWidth = CameraConstants.CAMERA_WIDTH;
                            int screenHeight = CameraConstants.CAMERA_HEIGHT;
                            Point point = new Point(screenWidth / 2, screenHeight / 2);
                            onCameraFocus(point);
                        }
                    }, 300);
                }
            }
        });
        mCameraView.setPictureCallback(pictureCallback);
        mZoomSeekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);

    }

    private SoundPool getSoundPool() {
        if (mSoundPool == null) {
            mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
            mFocusSoundId = mSoundPool.load(mContext, R.raw.camera_focus, 1);
            mFocusSoundPrepared = false;
            mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    mFocusSoundPrepared = true;
                }
            });
        }
        return mSoundPool;
    }

    public void bindActivity(Activity activity) {
        this.mActivity = activity;
        if (mCameraView != null) {
            mCameraView.bindActivity(activity);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int len = 0;
        if (CameraConstants.CAMERA_WIDTH != 0) {
            len = CameraConstants.CAMERA_WIDTH;
        } else {
            len = getMeasuredWidth();
        }
        //保证View是正方形
        setMeasuredDimension(len, len);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (CameraConstants.CAMERA_WIDTH == 0) {
            CameraConstants.CAMERA_WIDTH = w;
        }
        if (CameraConstants.CAMERA_HEIGHT == 0) {
            CameraConstants.CAMERA_HEIGHT = h;

        }
    }

    /**
     * 记录是拖拉照片模式还是放大缩小照片模式
     */

    private static final int MODE_INIT = 0;
    /**
     * 放大缩小照片模式
     */
    private static final int MODE_ZOOM = 1;
    private int mode = MODE_INIT;// 初始状态

    private float startDis;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /** 通过与运算保留最后八位 MotionEvent.ACTION_MASK = 255 */
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            // 手指压下屏幕
            case MotionEvent.ACTION_DOWN:
                mode = MODE_INIT;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                //如果mZoomSeekBar为null 表示该设备不支持缩放 直接跳过设置mode Move指令也无法执行
                if (mZoomSeekBar == null) return true;
                //移除token对象为mZoomSeekBar的延时任务
                mHandler.removeCallbacksAndMessages(mZoomSeekBar);
                //                mZoomSeekBar.setVisibility(View.VISIBLE);
                mZoomSeekBar.setVisibility(View.GONE);

                mode = MODE_ZOOM;
                /** 计算两个手指间的距离 */
                startDis = spacing(event);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == MODE_ZOOM) {
                    //只有同时触屏两个点的时候才执行
                    if (event.getPointerCount() < 2) return true;
                    float endDis = spacing(event);// 结束距离
                    //每变化10f zoom变1
                    int scale = (int) ((endDis - startDis) / 10f);
                    if (scale >= 1 || scale <= -1) {
                        int zoom = mCameraView.getZoom() + scale;
                        //zoom不能超出范围
                        if (zoom > mCameraView.getMaxZoom()) zoom = mCameraView.getMaxZoom();
                        if (zoom < 0) zoom = 0;
                        mCameraView.setZoom(zoom);
                        mZoomSeekBar.setProgress(zoom);
                        //将最后一次的距离设为当前距离
                        startDis = endDis;
                    }
                }
                break;
            // 手指离开屏幕
            case MotionEvent.ACTION_UP:
                if (mode != MODE_ZOOM) {
                    Log.e("TAG", "onTouchEvent(SquareCameraContainer.java:260): mode != MODE_ZOOM  ");
                    //设置聚焦
                    Point point = new Point((int) event.getX(), (int) event.getY());
                    onCameraFocus(point);
                } else {
                    Log.e("TAG", "onTouchEvent(SquareCameraContainer.java:260): mode == MODE_ZOOM  ");
                    //ZOOM模式下 在结束两秒后隐藏seekbar 设置token为mZoomSeekBar用以在连续点击时移除前一个定时任务
                    mHandler.postAtTime(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            mZoomSeekBar.setVisibility(View.GONE);
                        }
                    }, mZoomSeekBar, SystemClock.uptimeMillis() + 2000);
                }
                break;
        }
        return true;
    }

    /**
     * 两点的距离
     */
    private float spacing(MotionEvent event) {
        if (event == null) {
            return 0;
        }
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 相机对焦  默认不需要延时
     *
     * @param point
     */
    private void onCameraFocus(final Point point) {
        onCameraFocus(point, false);
    }

    /**
     * 相机对焦
     *
     * @param point
     * @param needDelay 是否需要延时
     */
    public void onCameraFocus(final Point point, boolean needDelay) {
        long delayDuration = needDelay ? 300 : 0;

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!mSensorControler.isFocusLocked()) {
                    if (mCameraView.onFocus(point, autoFocusCallback)) {
                        mSensorControler.lockFocus();
                        mFocusImageView.startFocus(point);

                        //播放对焦音效
                        if (mFocusSoundPrepared) {
                            mSoundPool.play(mFocusSoundId, 1.0f, 0.5f, 1, 0, 1.0f);
                        }
                    }
                }
            }
        }, delayDuration);
    }

    @Override
    public boolean takePicture() {
        setMaskOn();
        boolean flag = mCameraView.takePicture();
        if (!flag) {
            mSensorControler.unlockFocus();
        }
        setMaskOff();
        return flag;
    }

    @Override
    public int getMaxZoom() {
        return mCameraView.getMaxZoom();
    }

    @Override
    public void setZoom(int zoom) {
        mCameraView.setZoom(zoom);
    }

    @Override
    public int getZoom() {
        return mCameraView.getZoom();
    }

    @Override
    public void releaseCamera() {
        if (mCameraView != null) {
            mCameraView.releaseCamera();
        }
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };

    private final Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {

        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            Log.e("TAG", "onAutoFocus(SquareCameraContainer.java:372): 111111111111111111111");
            //聚焦之后根据结果修改图片
            if (success) {
                mFocusImageView.onFocusSuccess();
            } else {
                //聚焦失败显示的图片，由于未找到合适的资源，这里仍显示同一张图片
                mFocusImageView.onFocusFailed();
            }
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //一秒之后才能再次对焦
                    mSensorControler.unlockFocus();
                }
            }, 1000);
        }
    };

    private final Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(final byte[] data, Camera camera) {
            //            mActivity.rest();
            if (mConfig.isOverStart()) {
                camera.startPreview(); // 拍完照后，重新开始预览
            }
            new SavePicTask(data, mCameraView.isBackCamera()).start();
        }
    };

    private final SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            // TODO Auto-generated method stub
            mCameraView.setZoom(progress);
            mHandler.removeCallbacksAndMessages(mZoomSeekBar);
            //ZOOM模式下 在结束两秒后隐藏seekbar 设置token为mZoomSeekBar用以在连续点击时移除前一个定时任务
            mHandler.postAtTime(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    mZoomSeekBar.setVisibility(View.GONE);
                }
            }, mZoomSeekBar, SystemClock.uptimeMillis() + 2000);
        }


        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub

        }


        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub

        }
    };

    @Override
    public void onStart() {
        mSensorControler.onStart();

        if (mCameraView != null) {
            mCameraView.onStart();
        }

        mSoundPool = getSoundPool();
    }

    @Override
    public void onStop() {
        mSensorControler.onStop();

        if (mCameraView != null) {
            mCameraView.onStop();
        }

        mSoundPool.release();
        mSoundPool = null;
    }

    public void setMaskOn() {

    }

    public void setMaskOff() {

    }


    private class SavePicTask extends Thread {
        private byte[] data;
        private boolean isBackCamera;

        SavePicTask(byte[] data, boolean isBackCamera) {

            this.data = data;
            this.isBackCamera = isBackCamera;
        }

        @Override
        public void run() {
            super.run();

            Message msg = handler.obtainMessage();
            msg.obj = saveToSDCard(data);

            handler.sendMessage(msg);

        }

        /**
         * 将拍下来的照片存放在SD卡中
         *
         * @param data
         * @return imagePath 图片路径
         */
        Bundle bundle = null; // 声明一个Bundle对象，用来存储数据

        public boolean saveToSDCard(byte[] data) {
            bundle = new Bundle();
            bundle.putByteArray("bytes", data);    //将图片字节数据保存在bundle当中，实现数据交换

            //ADD 生成保存图片的路径
            mImagePath = absolutePath(data);

            //保存到SD卡
            if (TextUtils.isEmpty(mImagePath)) {
                return false;
            }
            try {
                FileOutputStream outputStream = null; // 文件输出流
                outputStream = new FileOutputStream(mImagePath);
                outputStream.write(data); // 写入sd卡中
                outputStream.close(); // 关闭输出流
            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        }

        public String absolutePath(byte[] data) {
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); // 格式化时间
            String filename = format.format(date) + ".jpg";
            File fileFolder = new File(mConfig.getPicPath());
            if (!fileFolder.exists()) { // 如果目录不存在，则创建一个名为"finger"的目录
                fileFolder.mkdir();
            }
            File jpgFile = new File(fileFolder, filename);
            return jpgFile.getAbsolutePath();
        }


        private Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                boolean result = (Boolean) msg.obj;

                if (result) {
                    // releaseCamera();    //不要在这个地方释放相机资源   这里是浪费时间的最大元凶  约1500ms左右
                    if (mTakePhotoCallback != null) {
                        mTakePhotoCallback.onTakePhoto(mImagePath);
                    }
                } else {
                    Toast.makeText(mContext, R.string.topic_camera_takephoto_failure, Toast.LENGTH_SHORT).show();
                    //                    mActivity.rest();
                    mCameraView.startPreview();
                }
            }
        };

    }

    public interface TakePhotoCallback {
        void onTakePhoto(String imagePath);
    }
}