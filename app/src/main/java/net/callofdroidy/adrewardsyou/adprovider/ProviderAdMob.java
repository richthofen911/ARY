package net.callofdroidy.adrewardsyou.adprovider;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.ads.InterstitialAd;

public class ProviderAdMob implements BaseAdProvider {
    private InterstitialAd admobClient;

    public ProviderAdMob() {
    }

    public void setClient(InterstitialAd client) {
        this.admobClient = client;
    }

    @Override
    public void play(Context context) {
        if (admobClient != null) {
            admobClient.show();
        } else {
            Toast.makeText(
                    context, name() + " is currently not available",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public String name() {
        return "AdMob";
    }

    @Override
    public void reset() {
        admobClient = null;
    }
}
