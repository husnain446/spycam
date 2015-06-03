package com.byteshaft.wrecspycam;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

public class SpyVoiceRecordingService extends Service {

    private MediaRecorder mRecorder;
    private Helpers mHelpers;
    private final int ONE_HOUR = 1000*60*60;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mHelpers = new Helpers(this);
        mRecorder = new MediaRecorder();
        mRecorder.reset();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        String filePath = mHelpers.getAbsoluteFilePath(".mp3");
        mRecorder.setOutputFile(filePath);
        mRecorder.setMaxDuration(ONE_HOUR);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
        }
        //Stop recording after one minute
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stopRecorder();
            }
        }, 1000*60);

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void stopRecorder() {
        mRecorder.stop();
        mRecorder.release();
        stopSelf();
        Log.i("SPY_CAM","Stop Recording");
    }
}
