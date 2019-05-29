package com.xiaoma.ljb.camera_lib;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.xiaoma.ljb.camera_lib.camera1.CameraManager;
import com.xiaoma.ljb.camera_lib.camera1.config.CameraConfig;
import com.xiaoma.ljb.camera_lib.camera1.view.SquareCameraContainer;

import java.io.File;

public class CameraActivity extends AppCompatActivity {

    public static final String SAVE_PATH = "save_path";
    public static final String CONFIG = "config";

    private SquareCameraContainer mContainer;
    private CameraManager mCameraManager;

    private String picPath;
    private CameraConfig mConfig;
    private TextView mTvTakePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
//        CameraConstants.CAMERA_WIDTH = this.getResources().getDisplayMetrics().widthPixels;
//        CameraConstants.CAMERA_HEIGHT =this.getResources().getDisplayMetrics().heightPixels;
        init();
        initConfig();
        initEvent();
    }

    private void initEvent() {
        mTvTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContainer.takePicture();
            }
        });
        mContainer.setTakePhotoCallback(new SquareCameraContainer.TakePhotoCallback() {
            @Override
            public void onTakePhoto(String imagePath) {
                Toast.makeText(CameraActivity.this, "拍照成功", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void init() {
        mContainer = findViewById(R.id.square_camera);
        mCameraManager = CameraManager.getInstance(this);
        mContainer.bindActivity(this);

        mTvTakePhoto = findViewById(R.id.take_photo);
    }

    private void initConfig() {
        if (getIntent().getExtras() != null) {
            picPath = getIntent().getExtras().getString(SAVE_PATH, "");
            mConfig = getIntent().getExtras().getParcelable(CONFIG);
        }
        if (TextUtils.isEmpty(picPath)) {
            picPath = getExternalFilesDir("Pictures").getAbsolutePath() + File.separator + "camera/";
        }
        if (mConfig == null) {
            mConfig = new CameraConfig.Build()
                    .path(picPath)
                    .build();
        }

        mContainer.setConfig(mConfig);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mContainer != null) {
            mContainer.onStart();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mContainer != null) {
            mContainer.onStop();
            mCameraManager.releaseActivityCamera();
        }
    }
}
