package com.android.example.syncadapterdemo.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by bodhidipta on 01/06/17.
 */

public class SyncAdapterClassDemo extends AbstractThreadedSyncAdapter {
    private Context mContext = null;
    private ContentResolver mContentResolver = null;
    private final String TAG = "ON_SYNC_DEMO";
    private final String syncUrl = "http://core.uaveditor.com/api/weather/cities";



    public SyncAdapterClassDemo(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
        mContentResolver = mContext.getContentResolver();
    }

    @Override
    public void onSyncCanceled(Thread thread) {
        super.onSyncCanceled(thread);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        Log.i(TAG, "On syncAdapter class -> onPerformSync :: ");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    OkHttpClient client = new OkHttpClient.Builder().connectTimeout(2000, TimeUnit.MILLISECONDS).build();
                    Request request = new Request.Builder()
                            .get()
                            .url(syncUrl)
                            .build();
                    Response response = client.newCall(request).execute();
                    String response_string = response.body().string();
                    response.close();
                    Log.i(TAG, "On syncAdapter -> Fetched :: ******** " + response_string);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }
}
