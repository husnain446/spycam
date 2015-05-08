package com.byteshaft.wrecspycam;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Helpers {

    void writeDataToDrive(byte[] data) {
        File absoluteFileLocation = getAbsoluteFilePath();
        try {
            FileOutputStream out = new FileOutputStream(absoluteFileLocation);
            out.write(data);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getDefaultPicturesDirectory() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    }

    private String getTimeStamp() {
        return new SimpleDateFormat("yyyyMMddhhmm'.jpg'", Locale.US).format(new Date());
    }

    private File getAbsoluteFilePath() {
        File picturesDirectory = getDefaultPicturesDirectory();
        return new File(picturesDirectory + "/" + getTimeStamp());
    }
}
