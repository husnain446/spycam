package com.byteshaft.wrecspycam;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by bilal on 5/8/15.
 */
public class SpyVideoService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
