package net.callofdroidy.adrewardsyou;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;

public class MainActivity extends AppCompatActivity implements IAdNetworkRequest{
    private static final String TAG = "MainActivity";

    private AdColonyInterstitial adColonyClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        AdColony.requestInterstitial(getString(R.string.adcolony_zone_id),
                new AdColonyInterstitialListener() {
            @Override
            public void onRequestFilled(AdColonyInterstitial adColonyInterstitial) {
                adColonyInterstitial.setListener(new AdColonyInterstitialListener() {
                    @Override
                    public void onRequestFilled(AdColonyInterstitial adColonyInterstitial) {
                        Log.e(TAG, "onRequestFilled: ");
                    }

                    @Override
                    public void onOpened(AdColonyInterstitial ad) {
                        Log.e(TAG, "onOpened: ");
                    }

                    @Override
                    public void onClosed(AdColonyInterstitial ad) {
                        Log.e(TAG, "onClosed: ");
                    }

                    @Override
                    public void onClicked(AdColonyInterstitial ad) {
                        Log.e(TAG, "onClicked: ");
                    }
                });
                adColonyClient = adColonyInterstitial;
            }
        });
    }


    @Override
    public boolean isAdColonyAvailable() {
        return adColonyClient != null;
    }

    @Override
    public void playAd(int adProvider) {
        switch (adProvider) {
            case AdProvider.AdColony:
                adColonyClient.show();
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
                    loadAds();
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
        if (adColonyClient != null) {
            playAd(AdProvider.AdColony);
        } else {
            Toast.makeText(
                    MainActivity.this, "AdColony not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // TODO: 27/08/18 destroy ad clients
    }
}
