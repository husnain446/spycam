package com.byteshaft.wrecspycam;

import android.app.Service;
import android.content.Intent;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.view.SurfaceHolder;

import com.byteshaft.ezflashlight.CameraStateChangeListener;
import com.byteshaft.ezflashlight.Flashlight;
import java.io.IOException;

public class SpyVideoService extends Service implements CameraStateChangeListener {

    MediaRecorder mMediaRecorder;
    Flashlight mFlashlight;
    Helpers mHelpers;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mMediaRecorder = new MediaRecorder();
        mHelpers = new Helpers();
        mFlashlight = new Flashlight(getApplicationContext());
        mFlashlight.setCameraStateChangedListener(this);
        mFlashlight.setupCameraPreview();
        return START_NOT_STICKY;
    }

    @Override
    public void onCameraInitialized(Camera camera) {   }

    @Override
    public void onCameraViewSetup(Camera camera, SurfaceHolder holder) {
        camera.unlock();
        mMediaRecorder.setCamera(camera);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        CamcorderProfile camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_720P);
        mMediaRecorder.setProfile(camcorderProfile);
        mMediaRecorder.setOrientationHint(90);
        String filePath = mHelpers.getAbsoluteFilePath(".MP4");
        mMediaRecorder.setOutputFile(filePath);
        try {
            mMediaRecorder.setPreviewDisplay(holder.getSurface());
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mMediaRecorder.start();
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mMediaRecorder.stop();
                mFlashlight.releaseAllResources();
            }
        }, 5000);
    }

    @Override
    public void onCameraBusy() {   }
}
