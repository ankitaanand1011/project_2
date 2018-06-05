package com.sketch.developer.gowireless.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.sketch.developer.gowireless.R;
import com.rampo.updatechecker.UpdateChecker;

/**
 * Created by Developer on 2/14/18.
 */

public class SplashScreen extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        UpdateChecker checker = new UpdateChecker(this);
        // If you are in a Activity or a FragmentActivity

        checker.start();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms

                Intent intent = new Intent(SplashScreen.this, LoginScreen.class);
                startActivity(intent);
                finish();
            }
        }, 3000);

    }


}
