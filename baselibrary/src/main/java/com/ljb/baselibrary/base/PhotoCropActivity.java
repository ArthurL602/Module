package com.ljb.baselibrary.base;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ljb.baselibrary.utils.PhotoUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yalantis.ucrop.UCrop;
import com.zhihu.matisse.Matisse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Author      :meloon
 * Date        :2018/11/26
 * Description : 拍照、剪辑、相册一体的activity
 */
public abstract class PhotoCropActivity extends AppCompatActivity {
    public static final int REQUEST_TAKE_PHOTO = 1; // 拍照
    public static final int REQUEST_TAKE_CROP = 2; // 剪辑
    public static final int REQUEST_OPEN_PIC = 3; // 系统相册
    public static final int REQUEST_OPEN_PIC2 = 4; // 第三方相册
    private RxPermissions mRxPermissions;
    private OnTakePhotoListener mTakePhotoListener;
    private OnTakeCropListener mOnTakeCropListener;
    private OnOpenPicListener mOnOpenPicListener;


    /**
     * 调用系统拍照
     *
     * @param filePath 保存文件路径
     * @param listener 回调接口
     */
    public void takePhoto(final String filePath, final OnTakePhotoListener listener) {
        mTakePhotoListener = listener;
        if (mRxPermissions == null) {
            mRxPermissions = new RxPermissions(this);
        }
        mRxPermissions.request(Manifest.permission.CAMERA)//
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            File file = new File(filePath);
                            PhotoUtils.takePhoto(PhotoCropActivity.this, file, REQUEST_TAKE_PHOTO);
                        } else {
                            if (listener != null) {
                                listener.noPermission();
                            }
                        }
                    }
                });

    }

    /**
     * 调用系统剪辑
     *
     * @param srcPath  源文件路径
     * @param width    剪辑宽
     * @param height   剪辑高
     * @param listener 回调接口
     */
    public void takeCrop(final String srcPath, int width, int height, final OnTakeCropListener listener) {
        mOnTakeCropListener = listener;
        File file = new File(srcPath);
        Uri uri = PhotoUtils.getUri(this, file);
        PhotoUtils.takeCrop(this, uri, file, width, height, REQUEST_TAKE_CROP);
    }

    /**
     * 调用第三方剪辑 -- ucrop
     *
     * @param srcPath
     * @param dst
     * @param width
     * @param height
     * @param listener
     */
    public void cropRawPhoto(final String srcPath, String dst, int width, int height, final OnTakeCropListener
            listener) {
        mOnTakeCropListener = listener;
        PhotoUtils.cropRawPhoto(srcPath, dst, this, width, height);
    }

    /**
     *  调用第三方 相册 -- 知乎
     * @param listener
     */
    public void openRawPic(OnOpenPicListener listener) {
        mOnOpenPicListener = listener;

        if (mRxPermissions == null) {
            mRxPermissions = new RxPermissions(this);
        }
        mRxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE)//
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            PhotoUtils.openRawPic(PhotoCropActivity.this, REQUEST_OPEN_PIC2);
                        }
                    }
                });
    }

    /**
     * 打开系统相册
     */
    public void openPic(OnOpenPicListener listener) {
        mOnOpenPicListener = listener;
        PhotoUtils.openPic(this, REQUEST_OPEN_PIC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_TAKE_PHOTO) { // 拍照成功
            if (mTakePhotoListener != null) {
                mTakePhotoListener.takePhotoSuccess();
            }
        }
        if (resultCode == RESULT_OK && requestCode == REQUEST_TAKE_CROP) { // 剪辑成功
            if (mOnTakeCropListener != null) {
                mOnTakeCropListener.takeCropSuccess();
            }
        }
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) { // 第三方剪辑成功
            if (mOnTakeCropListener != null) {
                mOnTakeCropListener.takeCropSuccess();
            }
        }
        if (resultCode == UCrop.RESULT_ERROR) { // 第三方剪辑失败
            if (mOnTakeCropListener != null) {
                mOnTakeCropListener.takeCropError();
            }
        }
        if (resultCode == RESULT_OK && requestCode == REQUEST_OPEN_PIC) { // 打开系统图册
            if (mOnOpenPicListener != null) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    mOnOpenPicListener.openPic(inputStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }

        if (requestCode == REQUEST_OPEN_PIC2 && resultCode == RESULT_OK) { // 打开第三方图册
            List<Uri> selected = Matisse.obtainResult(data);
            if (mOnOpenPicListener != null) {
                mOnOpenPicListener.openPic(selected);
            }
        }

    }

    public interface OnTakePhotoListener {
        void takePhotoSuccess();

        void noPermission();
    }

    public interface OnTakeCropListener {
        void takeCropSuccess();

        void takeCropError();
    }

    public interface OnOpenPicListener {
        void openPic(InputStream inputStream);

        void openPic(List<Uri> uris);
    }
}
