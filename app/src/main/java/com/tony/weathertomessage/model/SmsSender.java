package com.tony.weathertomessage.model;

import android.telephony.SmsManager;

import java.util.ArrayList;

/**
 * Created by Tony on 2017/10/16.
 */

public class SmsSender {

    private static SmsSender smsSender;

    private SmsSender() {
    }

    public static SmsSender getInstance() {
        if (smsSender == null) {
            synchronized (SmsSender.class) {
                if (smsSender == null) {
                    return new SmsSender();
                }
            }
        }
        return smsSender;
    }

    /**
     * 直接调用短信接口发短信
     *
     * @param phoneNumber
     * @param message
     */
    public void sendSMS(String phoneNumber, String message) {
        //获取短信管理器
        SmsManager smsManager = SmsManager.getDefault();
            ArrayList<String> msgs = smsManager.divideMessage(message);
//            smsManager.sendMultipartTextMessage(phoneNumber, null, msgs, null, null);
        for (String sms :
                msgs) {
            smsManager.sendTextMessage(phoneNumber, null, sms, null, null);
        }
    }
}
