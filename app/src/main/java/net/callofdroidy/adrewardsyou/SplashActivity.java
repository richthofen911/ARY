package net.callofdroidy.adrewardsyou;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.adcolony.sdk.AdColony;
import com.google.android.gms.ads.MobileAds;
import com.vungle.warren.InitCallback;
import com.vungle.warren.Vungle;

public class SplashActivity extends AppCompatActivity {
    private TextView mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);

        mContentView = findViewById(R.id.fullscreen_content);

        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        initAdNetworks();

    }

    private void initAdNetworks() {
        AdColony.configure(
                SplashActivity.this,
                getString(R.string.adcolony_app_id),
                getString(R.string.adcolony_zone_id));
        Vungle.init(getString(R.string.vungle_app_id), this.getApplicationContext(), new InitCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onAutoCacheAdAvailable(String s) {

            }
        });
        MobileAds.initialize(SplashActivity.this, getString(R.string.admob_app_id));
    }
}
