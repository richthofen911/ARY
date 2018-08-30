package net.callofdroidy.adrewardsyou.adprovider;

import android.content.Context;
import android.widget.Toast;

import com.chartboost.sdk.CBLocation;
import com.chartboost.sdk.Chartboost;

import net.callofdroidy.adrewardsyou.R;

public class ProviderChartboost extends BaseAdProvider {

    public ProviderChartboost() {
        super();
    }

    @Override
    public void play(Context context) {
        if (activated) {
            Chartboost.showRewardedVideo(CBLocation.LOCATION_DEFAULT);
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
        return "Chartboost";
    }
}
