package net.callofdroidy.adrewardsyou;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyZone;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.vungle.warren.LoadAdCallback;
import com.vungle.warren.PlayAdCallback;
import com.vungle.warren.Vungle;

import net.callofdroidy.adrewardsyou.adprovider.BaseAdProvider;
import net.callofdroidy.adrewardsyou.adprovider.ProviderAdColony;
import net.callofdroidy.adrewardsyou.adprovider.ProviderAdMob;
import net.callofdroidy.adrewardsyou.adprovider.ProviderVungle;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
        AdapterView.OnItemSelectedListener, LoadAdCallback, PlayAdCallback{
    private static final String TAG = "MainActivity";

    private RadioGroup rgAdProviders;
    private TextView tvAdProvider;

    private ImageView ivStatusAdcolony;
    private ImageView ivStatusAdMob;
    private ImageView ivStatusVungle;

    private InterstitialAd admobClient;

    private BaseAdProvider selectedAdProvider = null;
    private ProviderAdColony adColony;
    private ProviderAdMob adMob;
    private ProviderVungle vungle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatSpinner spDefaultProviderList = findViewById(R.id.spinner_default_providers);
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.ad_providers));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDefaultProviderList.setAdapter(arrayAdapter);
        spDefaultProviderList.setOnItemSelectedListener(this);
        spDefaultProviderList.setSelection(
                getSharedPreferences("app_config", MODE_PRIVATE)
                        .getInt("default_provider", 0));

        ivStatusAdcolony = findViewById(R.id.iv_adcolony_status);
        ivStatusAdMob = findViewById(R.id.iv_admob_status);
        ivStatusVungle = findViewById(R.id.iv_vungle_status);

        rgAdProviders = findViewById(R.id.rg_ad_providers);
        rgAdProviders.setOnCheckedChangeListener(this);
        tvAdProvider = findViewById(R.id.tv_ad_provider);

        adColony = new ProviderAdColony();
        adMob = new ProviderAdMob();
        vungle = new ProviderVungle(this);

        requestPermissions();

        loadAds();
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    201);
        }
    }

    private void loadAds() {
        // AdMob
        admobClient = new InterstitialAd(MainActivity.this);
        admobClient.setAdUnitId(getString(R.string.admob_ad_unit_id));
        admobClient.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.e(TAG, "AdMob onAdLoaded");
                adMob.setClient(admobClient);
                ivStatusAdMob.setImageDrawable(getDrawable(R.drawable.ic_check_green_24dp));
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.e(TAG, "AdMob onAdFailedToLoad: " + errorCode);
                ivStatusAdMob.setImageDrawable(getDrawable(R.drawable.ic_cross_red_24dp));
            }

            @Override
            public void onAdOpened() {
            }

            @Override
            public void onAdClosed() {
                adMob.reset();
                ivStatusAdMob.setImageDrawable(getDrawable(R.drawable.ic_cross_red_24dp));
                admobClient.loadAd(new AdRequest.Builder().build());
            }
        });
        admobClient.loadAd(new AdRequest.Builder().build());

        // AdColony
        AdColonyInterstitialListener adColonyListener = new AdColonyInterstitialListener() {
            @Override
            public void onRequestFilled(final AdColonyInterstitial adColonyInterstitial) {
                Log.e(TAG, "AdColony onRequestFilled");
                adColony.setClient(adColonyInterstitial);
                ivStatusAdcolony.setImageDrawable(getDrawable(R.drawable.ic_check_green_24dp));
            }

            @Override
            public void onRequestNotFilled(AdColonyZone zone) {
                Log.e(TAG, "AdColony onRequestNotFilled: ");
                ivStatusAdcolony.setImageDrawable(getDrawable(R.drawable.ic_cross_red_24dp));
            }

            @Override
            public void onOpened(AdColonyInterstitial ad) {
                Log.e(TAG, "AdColony onOpened: ");

            }

            @Override
            public void onClosed(AdColonyInterstitial ad) {
                Log.e(TAG, "AdColony onClosed: ");
                adColony.reset();
                ivStatusAdcolony.setImageDrawable(getDrawable(R.drawable.ic_cross_red_24dp));
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
            case R.id.rb_vungle:
                tvAdProvider.setText("Providing by Vungle");
                selectedAdProvider = vungle;
            default:
                break;
        }
    }

    // Vungle's callback
    @Override
    public void onAdLoad(String s) {
        vungle.activate();
        ivStatusVungle.setImageDrawable(getDrawable(R.drawable.ic_check_green_24dp));
    }

    // Vungle's callback
    @Override
    public void onAdStart(String s) {

    }

    // Vungle's callback
    @Override
    public void onAdEnd(String s, boolean b, boolean b1) {
        vungle.reset();
        ivStatusVungle.setImageDrawable(getDrawable(R.drawable.ic_cross_red_24dp));
        Vungle.loadAd(getString(R.string.vungle_placement_id), this);
    }

    // Vungle's callback
    @Override
    public void onError(String s, Throwable throwable) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.e(TAG, "onItemSelected: position: " + position);
        switch (position) {
            case 0:
                rgAdProviders.check(R.id.rb_adcolony);
                break;
            case 1:
                rgAdProviders.check(R.id.rb_admob);
                break;
            case 2:
                rgAdProviders.check(R.id.rb_vungle);
                break;
            default:
                break;
        }
        getSharedPreferences("app_config", MODE_PRIVATE).edit()
                .putInt("default_provider", position)
                .apply();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        rgAdProviders.check(R.id.rb_adcolony);
        getSharedPreferences("app_config", MODE_PRIVATE).edit()
                .putInt("default_provider", 0)
                .apply();
    }
}
