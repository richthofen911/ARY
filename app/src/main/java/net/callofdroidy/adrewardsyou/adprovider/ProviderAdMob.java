package net.callofdroidy.adrewardsyou.adprovider;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.ads.reward.RewardedVideoAd;

import net.callofdroidy.adrewardsyou.R;

public class ProviderAdMob extends BaseAdProvider {
    private RewardedVideoAd adMobClient;

    public ProviderAdMob(RewardedVideoAd adMobClient) {
        super();
        this.adMobClient = adMobClient;
    }

    @Override
    public void play(Context context) {
        if (activated) {
            adMobClient.show();
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
        return "AdMob";
    }
}
