package com.byteshaft.wrecspycam;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


public class SpyVideoService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
