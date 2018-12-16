package com.shu.bakingtime.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

public class BakingTimeSyncService extends IntentService {

    private static final String TAG = BakingTimeSyncService.class.getSimpleName();

    public BakingTimeSyncService() {
        super("BakingTimeSyncService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        BakingTimeSyncTask.syncRecipes(this);
    }
}
