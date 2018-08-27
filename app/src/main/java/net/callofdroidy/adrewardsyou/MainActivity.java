package net.callofdroidy.adrewardsyou;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hyprmx.mediate.HyprMediate;
import com.hyprmx.mediate.HyprMediateError;
import com.hyprmx.mediate.HyprMediateListener;
import com.hyprmx.mediate.HyprMediateReward;

import org.json.JSONException;

import java.util.UUID;

public class MainActivity extends AppCompatActivity implements HyprMediateListener, IAdNetworkRequest{
    private static final String TAG = "MainActivity";

    private Handler handler;

    private boolean isHyprMediateAdAvailable = false;

    //private int checkHyprMediateCount = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissions();

        handler = new Handler();
        initAdNetwork();

        Log.e(TAG, "onCreate: " + getString(R.string.hypr_api_token));
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

    private void initAdNetwork() {
        //try {

            String adId = UUID.randomUUID().toString();

            // init HyprMediate
            HyprMediate.getInstance().
                    initialize(this, getString(R.string.hypr_api_token),
                            adId, this);

            /*
            AMSDK.setGdprConsent(MainActivity.this, isGDPRConsented);
            //init
            AMSDK.init(MainActivity.this, ai.metaData.getString("appmediation_APP_KEY"));

            // init AdMob
            mAdMob_APP_ID = ai.metaData.getString("AdMob_APP_ID");
            MobileAds.initialize(MainActivity.this, mAdMob_APP_ID);

            // init StartApp
            StartAppSDK.init(this, String.valueOf(ai.metaData.getInt("StartApp_APP_ID")), false);
            StartAppAd.disableSplash();
            StartAppSDK.setUserConsent(MainActivity.this, "pas", System.currentTimeMillis(), isGDPRConsented);


            // init InMobi
            JSONObject consentObject = new JSONObject();
            consentObject.put(InMobiSdk.IM_GDPR_CONSENT_AVAILABLE, true);
            if (isGDPRConsented) {
                consentObject.put("gdpr", "1");
            } else {
                consentObject.put("gdpr", "0");
            }
            InMobiSdk.setLogLevel(InMobiSdk.LogLevel.DEBUG);
            InMobiSdk.init(MainActivity.this, ai.metaData.getString("InMobi_ACCOUNT_ID"), consentObject);
            // The PlacementID is saved in manifest.xml's metadata as well, but when trying to call getLong()
            // it throws error saying the value is Float. Can be fixed in the future.
            mInMobiClient = new InMobiInterstitial(MainActivity.this, 1531052671874L, MainActivity.this);

            //Kiip is init in Qriket.java(Application) as said in their docs

            // init Chartboost
            Chartboost.startWithAppId(MainActivity.this, ai.metaData.getString("Chartboost_App_Id"),
                    ai.metaData.getString("Chartboost_App_Signature"));
            Chartboost.restrictDataCollection(MainActivity.this, !isGDPRConsented);
            Chartboost.setActivityCallbacks(false);
            Chartboost.setDelegate(chartboostDelegate);
            Chartboost.setLoggingLevel(CBLogging.Level.ALL);
            Chartboost.setAutoCacheAds(false);
            Chartboost.onCreate(MainActivity.this);

            // init Facebook Audience Network
            mFacebookAdClient = new InterstitialAd(MainActivity.this, ai.metaData.getString("FBAudience_Placement_ID"));
            */

            // load ads
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    HyprMediate.getInstance().checkInventory();

                    /*
                    startAppAdClient = new StartAppAd(MainActivity.this);
                    startAppAdClient.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, MainActivity.this);
                    startAppAdClient.setVideoListener(MainActivity.this);

                    mAdMobClient = MobileAds.getRewardedVideoAdInstance(MainActivity.this);
                    mAdMobClient.setRewardedVideoAdListener(MainActivity.this);
                    mAdMobRequest = new AdRequest.Builder().build();
                    mAdMobClient.loadAd(mAdMob_APP_ID, mAdMobRequest);

                    mFacebookAdClient.setAdListener(MainActivity.this);
                    AdSettings.addTestDevice("27a764d8-6dd2-474d-b41e-5e610ec66cd6");
                    mFacebookAdClient.loadAd();

                    mInMobiClient.load();

                    Chartboost.cacheRewardedVideo(CBLocation.LOCATION_GAMEOVER);

                    if (AMSDK.isInitSuccess()) {
                        Log.d("<AM>", "Successfully init");
                        AMInterstitial.setListener(MainActivity.this);
                        AMInterstitial.load(MainActivity.this);
                    }

                    mSurvataClient.create(MainActivity.this, MainActivity.this);
                    */
                }
            }, 1000);
        //} catch (JSONException e) {
//            Log.e(TAG, "Init AdNetwork, " + e.toString());
  //      }
    }

    @Override
    public void hyprMediateCanShowAd(boolean b) {
        Log.w(TAG, "hyprMediateCanShowAd: " + b);
        isHyprMediateAdAvailable = b;

        /*
        if (!b) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (checkHyprMediateCount > 0) {
                        checkHyprMediateCount--;
                        HyprMediate.getInstance().checkInventory();
                    }
                }
            }, 1000);
        }
        */
    }

    @Override
    public void hyprMediateRewardDelivered(HyprMediateReward hyprMediateReward) {
        Log.w(TAG, "hyprMediateRewardDelivered: " + hyprMediateReward.virtualCurrencyName());
    }

    @Override
    public void hyprMediateErrorOccurred(HyprMediateError hyprMediateError) {
        Log.w(TAG, "hyprMediateErrorOccurred: " + hyprMediateError.errorDescription());
    }

    @Override
    public void hyprMediateStartedDisplaying() {

    }

    @Override
    public void hyprMediateFinishedDisplaying() {
        // TODO: 27/08/18 reward here
    }

    @Override
    public boolean isHyprMediateAvailable() {
        return false;
    }

    @Override
    public void playAd(int adProvider) {
        switch (adProvider) {
            case AdProvider.HyperMediate:
                HyprMediate.getInstance().showAd();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 201: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initAdNetwork();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    requestPermissions();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    public void onWatchAdClick(View view) {
        if (isHyprMediateAdAvailable) {
            playAd(AdProvider.HyperMediate);
        } else {
            Toast.makeText(
                    MainActivity.this, "HyprMediate not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // TODO: 27/08/18 destroy ad clients
    }

    public void onCheckAdClick(View view) {
        HyprMediate.getInstance().checkInventory();
    }
}
