package com.byteshaft.wrecspycam;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

public class VideoPlayerFragment extends Fragment implements Button.OnClickListener {
    View mView;
    Button mButton;


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.video_recorder, container, false);
        this.mView = view;
        setTextForDisplay();
        return view;
    }

    void setTextForDisplay() {
        mButton = (Button)mView.findViewById(R.id.videoRecorderButton);
        if (!SpyVideoService.videoServiceRunning) {
            mButton.setBackgroundResource(R.drawable.start_video_recorder);
        } else {
            mButton.setBackgroundResource(R.drawable.stop_video_recorder);
        }
        mButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (!SpyVideoService.videoServiceRunning) {
            startVideoRecordingService();
            mButton.setBackgroundResource(R.drawable.stop_video_recorder);
            Log.i("SPY_CAM", "Start_Video");
        } else if (SpyVideoService.videoServiceRunning) {
            Intent stopVideoRecording = new Intent(getActivity() , SpyVideoService.class);
            getActivity().stopService(stopVideoRecording);
            mButton.setBackgroundResource(R.drawable.start_video_recorder);
            Log.i("SPY_CAM", "STOP_VIDEO");
        }
    }

    private void startVideoRecordingService() {
        Intent videoIntent = new Intent(getActivity() , SpyVideoService.class);
        videoIntent.putExtra("video_delay", 60);
        getActivity().startService(videoIntent);
    }
}
