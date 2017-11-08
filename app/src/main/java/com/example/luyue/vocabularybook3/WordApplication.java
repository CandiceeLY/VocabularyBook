package com.example.luyue.vocabularybook3;

import android.app.Application;
import android.content.Context;

/**
 * Created by luyue on 2017/10/24.
 */

public class WordApplication extends Application{
    private static Context context;

    public static Context getContext() {
        return WordApplication.context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        WordApplication.context = context.getApplicationContext();
    }
}
