package com.taccotap.taigidict;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.eggheadgames.realmassethelper.IRealmAssetHelperStorageListener;
import com.eggheadgames.realmassethelper.RealmAssetHelper;
import com.eggheadgames.realmassethelper.RealmAssetHelperStatus;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class TaigiDictApp extends Application {

    public static final String DATABASE_ASSETS_PATH = "preload_realm_db";
    public static final String DATABASE_BASE_NAME = "taigidict";

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        initRealm();
        initCalligraphy();
    }

    private void initCalligraphy() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-RobotoRegular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }

    private void initRealm() {
        Realm.init(this);
        RealmAssetHelper.getInstance(this).loadDatabaseToStorage(DATABASE_ASSETS_PATH, DATABASE_BASE_NAME, new IRealmAssetHelperStorageListener() {
            @Override
            public void onLoadedToStorage(String realmDbName, RealmAssetHelperStatus realmAssetHelperStatus) {
                RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                        .name(realmDbName)
                        .build();
                Realm.setDefaultConfiguration(realmConfig);
            }
        });
    }
}
