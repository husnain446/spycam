package com.byteshaft.wrecspycam;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Helpers {

    final private String ALARM_STATE = "Alarm_State";

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

    void saveAlarmState(boolean state , Context context) {
        SharedPreferences preferences = getPreferenceManager(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(ALARM_STATE, state);
        editor.apply();
    }
    private SharedPreferences getPreferenceManager(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    boolean getAlarmState(Context context) {
        SharedPreferences preferences = getPreferenceManager(context);
        return preferences.getBoolean(ALARM_STATE, false);
    }

}
