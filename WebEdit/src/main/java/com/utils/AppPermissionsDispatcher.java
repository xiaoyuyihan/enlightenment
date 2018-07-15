package com.utils;

import android.support.v4.app.ActivityCompat;

import java.lang.ref.WeakReference;

import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.PermissionUtils;

/**
 * Created by admin on 2018/6/22.
 */

public class AppPermissionsDispatcher {
    private static final int REQUEST_INITAUDIO = 0;

    private static final String[] PERMISSION_INITAUDIO = new String[]{
            "android.permission.RECORD_AUDIO",
            "android.permission.CAMERA",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE"};

    private AppPermissionsDispatcher() {
    }

    public static void initAudioWithCheck(AppActivity target) {
        if (PermissionUtils.hasSelfPermissions(target, PERMISSION_INITAUDIO)) {
            target.initAudio();
        } else {
            if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_INITAUDIO)) {
                target.showRationaleForRecord(new InitAudioPermissionRequest(target));
            } else {
                ActivityCompat.requestPermissions(target, PERMISSION_INITAUDIO, REQUEST_INITAUDIO);
            }
        }
    }

    static void initCameraWithCheck(AppActivity target){
        if (PermissionUtils.hasSelfPermissions(target, PERMISSION_INITAUDIO)) {
            target.initCamera();
        } else {
            if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_INITAUDIO)) {
                target.showRationaleForRecord(new InitAudioPermissionRequest(target));
            } else {
                ActivityCompat.requestPermissions(target, PERMISSION_INITAUDIO, REQUEST_INITAUDIO);
            }
        }
    }

    static void onRequestPermissionsResult(AppActivity target, int requestCode, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_INITAUDIO:
                if (PermissionUtils.getTargetSdkVersion(target) < 23 && !PermissionUtils.hasSelfPermissions(target, PERMISSION_INITAUDIO)) {
                    target.showRecordDenied();
                    return;
                }
                if (PermissionUtils.verifyPermissions(grantResults)) {
                    target.initAudio();
                } else {
                    if (!PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_INITAUDIO)) {
                        target.onRecordNeverAskAgain();
                    } else {
                        target.showRecordDenied();
                    }
                }
                break;
            default:
                break;
        }
    }

    private static final class InitAudioPermissionRequest implements PermissionRequest {
        private final WeakReference<AppActivity> weakTarget;

        private InitAudioPermissionRequest(AppActivity target) {
            this.weakTarget = new WeakReference<>(target);
        }

        @Override
        public void proceed() {
            AppActivity target = weakTarget.get();
            if (target == null) return;
            ActivityCompat.requestPermissions(target, PERMISSION_INITAUDIO, REQUEST_INITAUDIO);
        }

        @Override
        public void cancel() {
            AppActivity target = weakTarget.get();
            if (target == null) return;
            target.showRecordDenied();
        }
    }
}
