package com.byteshaft.wrecspycam;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Helpers extends ContextWrapper {

    private AlarmManager mAlarmManager;
    final private String ALARM_STATE = "Alarm_State";
    private PendingIntent mPIntent;

    public Helpers(Context base) {
        super(base);
    }

    void writeDataToFile(byte[] data) {
        String absoluteFileLocation = getAbsoluteFilePath(".jpg");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(absoluteFileLocation);
            fileOutputStream.write(data);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getSpyCamDirectory() {
        return Environment.getExternalStorageDirectory() + "/SpyCam";
    }

    private String getTimeStamp() {
        return new SimpleDateFormat("yyyyMMddhhmmss", Locale.US).format(new Date());
    }

    String getAbsoluteFilePath(String fileType) {
        String picturesDirectory = getSpyCamDirectory();
        File file = new File(picturesDirectory);
        if (!file.exists()) {
            file.mkdir();
        }
        return file + "/" + getTimeStamp() + fileType;
    }

    void saveAlarmState(boolean state) {
        SharedPreferences preferences = getPreferenceManager();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(ALARM_STATE, state);
        editor.apply();
    }

    private SharedPreferences getPreferenceManager() {
        return PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    boolean getAlarmState() {
        SharedPreferences preferences = getPreferenceManager();
        return preferences.getBoolean(ALARM_STATE, false);
    }

    void setAlarmForVideoRecording(int date, int month,
                                           int year, int hour, int minutes) {
        mAlarmManager = getAlarmManager();
        Intent intent = new Intent("com.byteShaft.videoRecordingAlarm");
        mPIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE,date);  //1-31
        calendar.set(Calendar.MONTH, month);  //first month is 0!!! January is zero!!!
        calendar.set(Calendar.YEAR,year);//year...

        calendar.set(Calendar.HOUR_OF_DAY, hour);  //HOUR
        calendar.set(Calendar.MINUTE, minutes);       //MIN
        mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, mPIntent);
        Log.i("Video_Alarm,", "setting alarm of :" + calendar.getTime());
    }

    private AlarmManager getAlarmManager() {
        return (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    }

    void removeVideoRecordingAlarams() {
        Log.i("SPY_CAM", "removing scheduled Alarm");
        mAlarmManager.cancel(mPIntent);
    }

}
