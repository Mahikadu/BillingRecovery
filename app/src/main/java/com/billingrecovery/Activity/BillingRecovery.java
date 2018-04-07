package com.billingrecovery.Activity;

import android.app.Application;

import com.billingrecovery.Dbconfig.DataBaseCon;

/**
 * Created by Admin on 29-11-2017.
 */

public class BillingRecovery extends Application {

    public static DataBaseCon dbCon = null;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        try {
            if (dbCon != null) {
                dbCon.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
