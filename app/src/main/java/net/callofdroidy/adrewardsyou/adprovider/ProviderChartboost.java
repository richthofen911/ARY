package net.callofdroidy.adrewardsyou.adprovider;

import android.content.Context;
import android.widget.Toast;

import com.chartboost.sdk.CBLocation;
import com.chartboost.sdk.Chartboost;

import net.callofdroidy.adrewardsyou.R;

public class ProviderChartboost implements BaseAdProvider {
    private boolean activated;

    public ProviderChartboost() {
        activated = false;
    }

    @Override
    public void play(Context context) {
        if (activated) {
            Chartboost.showRewardedVideo(CBLocation.LOCATION_GAMEOVER);
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
        return "Chartboost";
    }

    @Override
    public void reset() {
        activated = false;
    }

    public void activate() {
        activated = true;
    }
}
