package com.synerit.freshboxclient.app;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import java.io.File;

import okhttp3.Cache;

/**
 * Created by jinne on 07.06.2017.
 */

public class FreshBoxClientApplication extends Application {

    private static long mDiskCacheSize = 100 * 1024 * 1024; //Disk Cache
    private static int mMemoryCacheSize = 80 * 1024 * 1024; //Memory Cache
    private static Cache diskCache;
    private static LruCache lruCache;

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        File _cache = new File(getApplicationContext().getCacheDir(), "fresh_box_cache");
        if (!_cache.exists())
            _cache.mkdirs();
        diskCache = new Cache(_cache, mDiskCacheSize);
        lruCache = new LruCache(mMemoryCacheSize);

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE));
        builder.memoryCache(lruCache);
        Picasso built = builder.build();
        built.setIndicatorsEnabled(false);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);

    }

}
