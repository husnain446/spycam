package com.byteshaft.wrecspycam;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.SurfaceHolder;

import com.byteshaft.ezflashlight.CameraStateChangeListener;
import com.byteshaft.ezflashlight.Flashlight;

import java.util.List;


@SuppressWarnings("deprecation")
public class SpyPictureService extends Service implements CameraStateChangeListener,
        Camera.PictureCallback, Camera.AutoFocusCallback, Camera.ShutterCallback {

    private Flashlight mFlashlight;
    private Helpers mHelpers;
    private int mBurstValue;
    private int mPictureCount = 1;
    private String LOG_TAG = "SPY_CAM";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mBurstValue = intent.getIntExtra("burstValue" , 1);
        Log.i(LOG_TAG , String.format("Brust value %d" , mBurstValue));
        mHelpers = new Helpers(getApplicationContext());
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
        mHelpers.writeDataToFile(data);
        if (mPictureCount < mBurstValue) {
            camera.autoFocus(this);
            mPictureCount++;
        } else {
            mFlashlight.releaseAllResources();
            stopSelf();
        }
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        if (success) {
            camera.takePicture(this, null, null, this);
        }
    }

    @Override
    public void onShutter() {
    }
}
