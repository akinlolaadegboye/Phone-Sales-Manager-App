package com.salesrepmanager.Activities;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.salesrepmanager.R;
import com.salesrepmanager.helperClass.SQLiteHandler;
import com.salesrepmanager.helperClass.SessionManager;
public class Splash extends Activity {

    private static int SPLASH_TIME_OUT = 2500;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());

    new Handler().postDelayed(new Runnable() {

            /*
             * Showing Splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                if (session.isLoggedIn()) {
                    Intent i = new Intent(Splash.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
                else if(!session.isLoggedIn()){
                    Intent i = new Intent(Splash.this, RepRegistration.class);
                    startActivity(i);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }
}


