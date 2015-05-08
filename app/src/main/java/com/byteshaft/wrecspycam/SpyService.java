package com.byteshaft.wrecspycam;

import android.app.Service;
import android.content.Intent;
import android.hardware.Camera;
import android.os.IBinder;

import com.byteshaft.ezflashlight.CameraStateChangeListener;
import com.byteshaft.ezflashlight.Flashlight;

import java.util.List;

@SuppressWarnings("deprecation")
public class SpyService extends Service implements CameraStateChangeListener,
        Camera.PictureCallback, Camera.AutoFocusCallback, Camera.ShutterCallback {

    private Flashlight mFlashlight;
    private Helpers mHelpers;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mHelpers = new Helpers();
        mFlashlight = new Flashlight(getApplicationContext());
        mFlashlight.setCameraStateChangedListener(this);
        mFlashlight.setupCameraPreview();
        return START_NOT_STICKY;
    }

    @Override public IBinder onBind(Intent intent) { return null; }

    @Override public void onCameraInitialized() {  }

    @Override
    public void onCameraViewSetup(Camera camera) {
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

    @Override public void onCameraBusy() {   }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        mHelpers.writeDataToDrive(data);
        mFlashlight.releaseAllResources();
        stopSelf();
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        if (success) {
            camera.takePicture(this, null, null, this);
        }
    }

    @Override public void onShutter() {  }
}
