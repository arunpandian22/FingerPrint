package me.arun.securitytestapp;

import android.app.Application;

import me.arun.securitytestapp.Util.LocalData;

public class SecurityApp extends Application {

    LocalData localData;
    public void onCreate() {
        super.onCreate();
        localData = new LocalData(getApplicationContext());
        localData.setBooleanPreferenceValue("isFingerPrintNeed", true);
    }

    /**
     * A method to get the decide to the fingerprint has to used or not
     * @return it return the true if the fingerprint sensor need to authenticate
     */
    public boolean getIsFingerPrintNeed()
    {
       return localData.getBooleanPreferenceValue("isFingerPrintNeed");
    }

}
