package com.procoin.util;

import android.Manifest;


public class PermissionUtils {

    public static final String[] CAMERA=new String[]{Manifest.permission.CAMERA};

    public static final String[] EXTERNAL_STORAGE=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};

    public static final String[] CAMERA_EXTERNAL_STORAGE=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};

    public static final String[] CAMERA_RECORDAUDIO_EXTERNALSTORAGE=new String[]{Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};

    public static final String[] RECORDAUDIO_EXTERNALSTORAGE=new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};

    public static final String[] READ_PHONE_STATE_EXTERNAL_STORAGE=new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};

    public static final String[] INSTALL_PACKAGES=new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES};

}
