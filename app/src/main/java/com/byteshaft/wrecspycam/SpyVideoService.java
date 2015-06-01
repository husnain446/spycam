package com.byteshaft.wrecspycam;

import android.app.Service;
import android.content.Intent;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.util.Log;
import android.view.SurfaceHolder;

import com.byteshaft.ezflashlight.CameraStateChangeListener;
import com.byteshaft.ezflashlight.Flashlight;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class SpyVideoService extends Service implements CameraStateChangeListener {

    private MediaRecorder mMediaRecorder;
    private Flashlight mFlashlight;
    private Helpers mHelpers;
    int mVideoDelay;
    private long delayInMilliSeconds;
    private String LOG_TAG = "SPY_CAM";
    static boolean videoServiceRunning = false;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mVideoDelay = intent.getIntExtra("video_delay", 1);
        delayInMilliSeconds = TimeUnit.MINUTES.toMillis(mVideoDelay);
        mMediaRecorder = new MediaRecorder();
        mHelpers = new Helpers();
        mFlashlight = new Flashlight(getApplicationContext());
        mFlashlight.setCameraStateChangedListener(this);
        mFlashlight.setupCameraPreview();
        videoServiceRunning = true;
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
        CamcorderProfile camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_LOW);
        mMediaRecorder.setProfile(camcorderProfile);
        mMediaRecorder.setOrientationHint(90);
        mMediaRecorder.setMaxDuration((int) delayInMilliSeconds);
        mMediaRecorder.setVideoFrameRate(24);
        String filePath = mHelpers.getAbsoluteFilePath(".mp4");
        mMediaRecorder.setOutputFile(filePath);
        try {
            mMediaRecorder.setPreviewDisplay(holder.getSurface());
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
        }

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stopVideoRecording();
            }
        }, delayInMilliSeconds);
    }

    void stopVideoRecording() {
        try {
            mMediaRecorder.stop();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        mFlashlight.releaseAllResources();
        videoServiceRunning = false;
        Log.i(LOG_TAG, "finish");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopVideoRecording();
    }

    @Override
    public void onCameraBusy() {   }
}
