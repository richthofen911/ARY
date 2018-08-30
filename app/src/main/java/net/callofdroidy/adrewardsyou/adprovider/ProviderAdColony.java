package net.callofdroidy.adrewardsyou.adprovider;

import android.content.Context;
import android.widget.Toast;

import com.adcolony.sdk.AdColonyInterstitial;

import net.callofdroidy.adrewardsyou.R;

public class ProviderAdColony extends BaseAdProvider<AdColonyInterstitial> {
    //private AdColonyInterstitial interstitial;

    public ProviderAdColony() {
        super();
    }

    @Override
    public void play(Context context) {
        if (activated) {
            super.t.show();
        } else {
            Toast.makeText(
                    context,
                    context.getString(
                            R.string.provider_not_avaialable_template, name()), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public String name() {
        return "AdColony";
    }

    /*

    @Override
    public void activate(AdColonyInterstitial adColonyInterstitial) {
        this.interstitial = adColonyInterstitial;
        activated = true;
    }
    */
}
