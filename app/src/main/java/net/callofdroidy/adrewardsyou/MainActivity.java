package net.callofdroidy.adrewardsyou;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAppOptions;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyZone;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.vungle.warren.LoadAdCallback;
import com.vungle.warren.Vungle;

import net.callofdroidy.adrewardsyou.adprovider.BaseAdProvider;
import net.callofdroidy.adrewardsyou.adprovider.ProviderAdColony;
import net.callofdroidy.adrewardsyou.adprovider.ProviderAdMob;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, LoadAdCallback{
    private static final String TAG = "MainActivity";

    private RadioGroup rgAdProviders;
    private RadioButton rbAdColony;
    private RadioButton rbAdMob;
    private TextView tvAdProvider;

    private InterstitialAd admobClient;

    private BaseAdProvider selectedAdProvider = null;
    private ProviderAdColony adColony;
    private ProviderAdMob adMob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rgAdProviders = findViewById(R.id.rg_ad_providers);
        rgAdProviders.setOnCheckedChangeListener(this);
        rbAdColony = findViewById(R.id.rb_adcolony);
        rbAdMob = findViewById(R.id.rb_admob);
        tvAdProvider = findViewById(R.id.tv_ad_provider);

        adColony = new ProviderAdColony();
        adMob = new ProviderAdMob();

        rgAdProviders.check(R.id.rb_adcolony);

        requestPermissions();

        loadAds();
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    201);
        }
    }

    private void loadAds() {
        admobClient = new InterstitialAd(MainActivity.this);
        admobClient.setAdUnitId(getString(R.string.admob_ad_unit_id));
        admobClient.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                adMob.setClient(admobClient);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.e(TAG, "onAdFailedToLoad: " + errorCode);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.

            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
                adMob.reset();
            }
        });
        admobClient.loadAd(new AdRequest.Builder().build());

        AdColonyInterstitialListener adColonyListener = new AdColonyInterstitialListener() {
            @Override
            public void onRequestFilled(final AdColonyInterstitial adColonyInterstitial) {
                Log.e(TAG, "onRequestFilled: ");
                adColony.setClient(adColonyInterstitial);
            }

            @Override
            public void onRequestNotFilled(AdColonyZone zone) {
                Log.e(TAG, "onRequestNotFilled: ");
            }

            @Override
            public void onOpened(AdColonyInterstitial ad) {
                Log.e(TAG, "onOpened: ");

            }

            @Override
            public void onClosed(AdColonyInterstitial ad) {
                Log.e(TAG, "onClosed: ");
                adColony.reset();
                AdColony.requestInterstitial(getString(R.string.adcolony_zone_id), this);
            }

            @Override
            public void onClicked(AdColonyInterstitial ad) {
                Log.e(TAG, "onClicked: ");
            }
        };
        AdColony.requestInterstitial(getString(R.string.adcolony_zone_id), adColonyListener);

        if (Vungle.isInitialized()) {
            Vungle.loadAd(getString(R.string.vungle_placement_id), this);
        }

    }


    public void playAd(BaseAdProvider adProvider) {
        adProvider.play(MainActivity.this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 201: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadAds();
                } else {
                    requestPermissions();
                }
            }
        }
    }

    public void onWatchAdClick(View view) {
        if (selectedAdProvider == null) {
            Toast.makeText(this, "Please select an ad provider", Toast.LENGTH_SHORT).show();
        } else {
            playAd(selectedAdProvider);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // TODO: 27/08/18 destroy ad clients
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_adcolony:
                tvAdProvider.setText("Providing by AdColony");
                selectedAdProvider = adColony;
                break;
            case R.id.rb_admob:
                tvAdProvider.setText("Providing by AdMob");
                selectedAdProvider = adMob;
                break;
            default:
                break;
        }
    }

    @Override
    public void onAdLoad(String s) {

    }

    @Override
    public void onError(String s, Throwable throwable) {

    }
}
