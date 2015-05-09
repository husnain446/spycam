package com.byteshaft.wrecspycam;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.SurfaceHolder;

import com.byteshaft.ezflashlight.CameraStateChangeListener;
import com.byteshaft.ezflashlight.Flashlight;

import java.util.List;


@SuppressWarnings("deprecation")
public class SpyPictureService extends Service implements CameraStateChangeListener,
        Camera.PictureCallback, Camera.AutoFocusCallback, Camera.ShutterCallback {

    private Flashlight mFlashlight;
    private Helpers mHelpers;
    private int  mBurstValue;
    private int mPictureCapture = 1;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mBurstValue = intent.getIntExtra("burstValue" , 1);
        System.out.println("burstValue :" + mBurstValue);
        mHelpers = new Helpers();
        mFlashlight = new Flashlight(getApplicationContext());
        mFlashlight.setCameraStateChangedListener(this);
        mFlashlight.setupCameraPreview();
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCameraInitialized(Camera camera) {
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onCameraViewSetup(Camera camera, SurfaceHolder holder) {
        Camera.Parameters params = camera.getParameters();
        List<Camera.Size> sizes = params.getSupportedPreviewSizes();
        Camera.Size selected = sizes.get(0);
        params.setPreviewSize(selected.width, selected.height);
        camera.setDisplayOrientation(90);
        params.setRotation(90);
        camera.setParameters(params);
        camera.enableShutterSound(false);
        camera.startPreview();
        camera.autoFocus(this);
    }

    @Override
    public void onCameraBusy() {
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        System.out.println("picTakenCalled");
        mHelpers.writeDataToFile(data);
        System.out.println(mPictureCapture < mBurstValue);
        if (mPictureCapture < mBurstValue) {
            camera.autoFocus(this);
            mPictureCapture++;
            System.out.println(mPictureCapture);
        } else {
            mFlashlight.releaseAllResources();
            stopSelf();
        }
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        System.out.println("autoFocus");
        if (success) {
            camera.takePicture(this, null, null, this);
        }
    }

    @Override
    public void onShutter() {
    }
}
