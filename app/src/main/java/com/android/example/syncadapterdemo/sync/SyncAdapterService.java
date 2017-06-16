package com.android.example.syncadapterdemo.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by bodhidipta on 01/06/17.
 */

public class SyncAdapterService extends Service {
    SyncAdapterClassDemo syncAdapterClassDemo = null;
    private static final Object sSyncAdapterLock = new Object();

    @Override
    public void onCreate() {
        super.onCreate();
        synchronized (sSyncAdapterLock) {
            if (syncAdapterClassDemo == null)
                syncAdapterClassDemo = new SyncAdapterClassDemo(SyncAdapterService.this, true);

        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return syncAdapterClassDemo.getSyncAdapterBinder();
    }
}
