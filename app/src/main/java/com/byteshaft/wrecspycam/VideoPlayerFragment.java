package com.byteshaft.wrecspycam;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.util.Calendar;

public class VideoPlayerFragment extends Fragment implements Button.OnClickListener , OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    View mView;
    Button mButton;
    Button dateButton;
    Button timeButton;
    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.video_recorder, container, false);
        this.mView = view;
        initialize(savedInstanceState);
        return view;
    }

    void initialize(Bundle savedInstanceState) {
        mButton = (Button)mView.findViewById(R.id.videoRecorderButton);
        if (!SpyVideoService.videoServiceRunning) {
            mButton.setBackgroundResource(R.drawable.start_video_recorder);
        } else {
            mButton.setBackgroundResource(R.drawable.stop_video_recorder);
        }
        mButton.setOnClickListener(this);
        dateButton = (Button) mView.findViewById(R.id.dateButton);
        timeButton = (Button) mView.findViewById(R.id.timeButton);
        dateButton.setOnClickListener(this);
        timeButton.setOnClickListener(this);
        final Calendar calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY) ,calendar.get(Calendar.MINUTE), false, false);

        if (savedInstanceState != null) {
            DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag(DATEPICKER_TAG);
            if (dpd != null) {
                dpd.setOnDateSetListener(this);
            }

            TimePickerDialog tpd = (TimePickerDialog) getFragmentManager().findFragmentByTag(TIMEPICKER_TAG);
            if (tpd != null) {
                tpd.setOnTimeSetListener(this);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.videoRecorderButton:
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
                break;
            case R.id.dateButton:
                datePickerDialog.setYearRange(2015, 2037);
                datePickerDialog.show(getFragmentManager(), DATEPICKER_TAG);
                break;
            case R.id.timeButton:
                timePickerDialog.show(getFragmentManager(), TIMEPICKER_TAG);
                break;
        }
    }

    private void startVideoRecordingService() {
        Intent videoIntent = new Intent(getActivity() , SpyVideoService.class);
        videoIntent.putExtra("video_delay", 60);
        getActivity().startService(videoIntent);
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        Toast.makeText(getActivity(), "new date:" + year + "-" + month + "-" + day, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hourOfDay, int minute) {
        Toast.makeText(getActivity(), "new time:" + hourOfDay + "-" + minute, Toast.LENGTH_SHORT).show();
    }
}
