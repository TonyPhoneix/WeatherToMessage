package com.tony.weathertomessage.base;

import android.app.Application;
import android.content.Context;

/**
 * Created by Tony on 2017/10/16.
 */

public class BaseApplication extends Application {

    private static Context mAppContext = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();
    }

    public static Context getmAppContext() {
        return mAppContext;
    }

}
