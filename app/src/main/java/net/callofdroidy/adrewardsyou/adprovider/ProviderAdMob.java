package net.callofdroidy.adrewardsyou.adprovider;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.ads.InterstitialAd;

import net.callofdroidy.adrewardsyou.R;

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
                    context,
                    context.getString(
                            R.string.provider_not_avaialable_template, name()), Toast.LENGTH_SHORT)
                    .show();
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
