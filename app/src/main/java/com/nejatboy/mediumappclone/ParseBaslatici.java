package com.nejatboy.mediumappclone;

import android.app.Application;

import com.parse.Parse;

public class ParseBaslatici extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("p68OFJodmqbqSwomuhzFGgsDWBHzkinBHE1pcLAd")
                .clientKey("50GGFMomFir6IxRT7H3JdP0F36RCJXhV4jtaiTvD")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
