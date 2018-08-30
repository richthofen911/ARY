package net.callofdroidy.adrewardsyou;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.adcolony.sdk.AdColony;
import com.chartboost.sdk.CBLocation;
import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.Libraries.CBLogging;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.vungle.warren.Vungle;

import net.callofdroidy.adrewardsyou.adlistenerImpl.AdColonyListenerImpl;
import net.callofdroidy.adrewardsyou.adlistenerImpl.AdMobListenerImpl;
import net.callofdroidy.adrewardsyou.adlistenerImpl.ChartboostListenerImpl;
import net.callofdroidy.adrewardsyou.adlistenerImpl.VungleListenerImpl;
import net.callofdroidy.adrewardsyou.adprovider.BaseAdProvider;
import net.callofdroidy.adrewardsyou.adprovider.ProviderAdColony;
import net.callofdroidy.adrewardsyou.adprovider.ProviderAdMob;
import net.callofdroidy.adrewardsyou.adprovider.ProviderChartboost;
import net.callofdroidy.adrewardsyou.adprovider.ProviderVungle;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
        AdapterView.OnItemSelectedListener{
    private static final String TAG = "MainActivity";

    private RadioGroup rgAdProviders;
    private TextView tvAdProvider;

    private ImageView ivStatusAdcolony;

    private RewardedVideoAd admobClient;

    private BaseAdProvider selectedAdProvider = null;
    private ProviderAdColony providerAdColony;
    private ProviderAdMob providerAdMob;
    private ProviderVungle providerVungle;
    private ProviderChartboost providerChartboost;

    private VungleListenerImpl vungleListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatSpinner spDefaultProviderList = findViewById(R.id.spinner_default_providers);
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.ad_providers));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDefaultProviderList.setAdapter(arrayAdapter);
        spDefaultProviderList.setOnItemSelectedListener(this);
        spDefaultProviderList.setSelection(
                getSharedPreferences("app_config", MODE_PRIVATE)
                        .getInt("default_provider", 0));

        ivStatusAdcolony = findViewById(R.id.iv_adcolony_status);
        ImageView ivStatusAdMob = findViewById(R.id.iv_admob_status);
        ImageView ivStatusVungle = findViewById(R.id.iv_vungle_status);
        ImageView ivStatusChartboost = findViewById(R.id.iv_chartboost_status);

        rgAdProviders = findViewById(R.id.rg_ad_providers);
        rgAdProviders.setOnCheckedChangeListener(this);
        tvAdProvider = findViewById(R.id.tv_ad_provider);

        admobClient = MobileAds.getRewardedVideoAdInstance(MainActivity.this);
        providerAdMob = new ProviderAdMob(admobClient);
        admobClient.setRewardedVideoAdListener(
                new AdMobListenerImpl(this, providerAdMob, ivStatusAdMob, admobClient));

        providerAdColony = new ProviderAdColony();
        providerVungle = new ProviderVungle();
        providerChartboost = new ProviderChartboost();

        vungleListener = new VungleListenerImpl(this, providerVungle, ivStatusVungle);
        providerVungle.setPlayCallback(vungleListener);

        Chartboost.startWithAppId(this, getString(R.string.chartboost_app_id),
                getString(R.string.chartboost_app_signature));
        Chartboost.setActivityCallbacks(false);
        Chartboost.setDelegate(new ChartboostListenerImpl(
                this, ivStatusChartboost, providerChartboost));
        Chartboost.setAutoCacheAds(false);
        Chartboost.setLoggingLevel(CBLogging.Level.ALL);
        Chartboost.onCreate(this);

        requestPermissions();

        loadAds();
    }

    @Override
    public void onStart() {
        super.onStart();
        Chartboost.onStart(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Chartboost.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Chartboost.onPause(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        Chartboost.onStop(this);
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
        // AdColony
        AdColony.requestInterstitial(getString(R.string.adcolony_zone_id),
                new AdColonyListenerImpl(this, ivStatusAdcolony, providerAdColony));
        // AdMob
        admobClient.loadAd(getString(R.string.admob_ad_unit_id), new AdRequest.Builder().build());
        // Vungle
        if (Vungle.isInitialized()) {
            Vungle.loadAd(getString(R.string.vungle_placement_id), vungleListener);
        }
        // Chartboost
        Chartboost.cacheRewardedVideo(CBLocation.LOCATION_DEFAULT);
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

        Chartboost.onDestroy(this);
        // TODO: 27/08/18 destroy ad clients
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_adcolony:
                tvAdProvider.setText(getString(R.string.providing_by_template, "AdColony"));
                selectedAdProvider = providerAdColony;
                break;
            case R.id.rb_admob:
                tvAdProvider.setText(getString(R.string.providing_by_template, "AdMob"));
                selectedAdProvider = providerAdMob;
                break;
            case R.id.rb_vungle:
                tvAdProvider.setText(getString(R.string.providing_by_template, "Vungle"));
                selectedAdProvider = providerVungle;
                break;
            case R.id.rb_chartboost:
                tvAdProvider.setText(getString(R.string.providing_by_template, "Chartboost"));
                selectedAdProvider = providerChartboost;
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
            case 3:
                rgAdProviders.check(R.id.rb_chartboost);
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
