package net.callofdroidy.adrewardsyou.adprovider;

import android.content.Context;
import android.widget.Toast;

import com.adcolony.sdk.AdColonyInterstitial;

import net.callofdroidy.adrewardsyou.R;

public class ProviderAdColony implements BaseAdProvider {
    private AdColonyInterstitial interstitial;

    public ProviderAdColony() {
    }

    public void setClient(AdColonyInterstitial interstitial) {
        this.interstitial = interstitial;
    }

    @Override
    public void play(Context context) {
        if (interstitial != null) {
            interstitial.show();
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
        return "AdColony";
    }

    @Override
    public void reset() {
        interstitial = null;
    }
}
