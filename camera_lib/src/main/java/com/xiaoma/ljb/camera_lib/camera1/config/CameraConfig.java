package com.xiaoma.ljb.camera_lib.camera1.config;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Author: LiangJingBo.
 * @Date: 2019/05/23
 * @Describe:
 */

public class CameraConfig implements Parcelable {

    private String picPath;//相片路径
    private boolean overStart;//拍完照是否可以继续拍照

    public String getPicPath() {
        return picPath;
    }

    public boolean isOverStart() {
        return overStart;
    }

    private CameraConfig(Build build) {
        this.picPath = build.picPath;
        this.overStart = build.overStart;
    }


    public static class Build implements Parcelable {
        private String picPath;//相片路径
        private boolean overStart = true;//拍完照是否可以继续拍照

        public Build path(String path) {
            this.picPath = path;
            return this;
        }

        public Build overStart(boolean start) {
            this.overStart = start;
            return this;
        }

        public CameraConfig build() {
            return new CameraConfig(this);
        }


        @Override
        public int describeContents() { return 0; }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.picPath);
            dest.writeByte(this.overStart ? (byte) 1 : (byte) 0);
        }

        public Build() {}

        protected Build(Parcel in) {
            this.picPath = in.readString();
            this.overStart = in.readByte() != 0;
        }

        public static final Creator<Build> CREATOR = new Creator<Build>() {
            @Override
            public Build createFromParcel(Parcel source) {return new Build(source);}

            @Override
            public Build[] newArray(int size) {return new Build[size];}
        };
    }


    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.picPath);
        dest.writeByte(this.overStart ? (byte) 1 : (byte) 0);
    }

    protected CameraConfig(Parcel in) {
        this.picPath = in.readString();
        this.overStart = in.readByte() != 0;
    }

    public static final Creator<CameraConfig> CREATOR = new Creator<CameraConfig>() {
        @Override
        public CameraConfig createFromParcel(Parcel source) {return new CameraConfig(source);}

        @Override
        public CameraConfig[] newArray(int size) {return new CameraConfig[size];}
    };
}
