package com.byteshaft.wrecspycam;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScheduleAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent videoIntent = new Intent(context, SpyVideoService.class);
        context.startService(videoIntent);

    }
}
