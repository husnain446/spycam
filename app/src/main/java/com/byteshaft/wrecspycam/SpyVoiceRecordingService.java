package com.byteshaft.wrecspycam;


import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

public class SpyVoiceRecordingService extends Service {
    MediaRecorder mRecorder;
    Helpers mHelpers;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mHelpers = new Helpers();

        mRecorder = new MediaRecorder();
        mRecorder.reset();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        String filePath = mHelpers.getAbsoluteFilePath(".mp3");
        mRecorder.setOutputFile(filePath);
        mRecorder.setMaxDuration(1000*60);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
        }

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
        Log.i("SPY_CAM" , "recordingStop");
    }

}
