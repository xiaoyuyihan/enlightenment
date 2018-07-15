package com.utils;

import android.support.v7.app.AppCompatActivity;

import permissions.dispatcher.PermissionRequest;

/**
 * Created by admin on 2018/6/22.
 */

public abstract class AppActivity extends AppCompatActivity {

    public abstract void initAudio();

    public abstract void initCamera();

    public abstract void showRecordDenied();

    public abstract void onRecordNeverAskAgain();

    public abstract void showRationaleForRecord(PermissionRequest initAudioPermissionRequest);
}
