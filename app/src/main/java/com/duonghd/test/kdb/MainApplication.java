package com.duonghd.test.kdb;

import android.util.Log;

import androidx.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.duonghd.test.kdb.utils.AppUtils;

import io.fabric.sdk.android.Fabric;

/**
 * Create on 2019-06-03
 *
 * @author duonghd
 */

public class MainApplication extends MultiDexApplication {
    private final String TAG = MainApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            Fabric fabric = new Fabric.Builder(this).debuggable(false).kits(new Crashlytics(), new CrashlyticsNdk()).build();
            Fabric.with(fabric);
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
        }

        AppUtils.init(this);
    }
}
