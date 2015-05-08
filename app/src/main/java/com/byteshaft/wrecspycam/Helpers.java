package com.byteshaft.wrecspycam;

import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Helpers {

    void writeDataToFOlder(byte[] data) {
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

    private String getSpycamDirectory() {
        return Environment.getExternalStorageDirectory() + "/SpyCam";
    }

    private String getTimeStamp() {
        return new SimpleDateFormat("yyyyMMddhhmm", Locale.US).format(new Date());
    }

    String getAbsoluteFilePath(String fileType) {
        String picturesDirectory = getSpycamDirectory();
        File file = new File(picturesDirectory);
        if (!file.exists()) {
            file.mkdir();
        }
        return file + "/" + getTimeStamp() + fileType;
    }
}
