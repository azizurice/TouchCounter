package com.ps.touchcounter;

import android.app.Application;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Azizur on 05/02/2017.
 */

public class TCApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initializeTwitterConfig();
    }

    private void initializeTwitterConfig() {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(
                getString(R.string.twitter_consumer_key),
                getString(R.string.twitter_consumer_secret));
        Fabric.with(this, new Twitter(authConfig));
    }

}
