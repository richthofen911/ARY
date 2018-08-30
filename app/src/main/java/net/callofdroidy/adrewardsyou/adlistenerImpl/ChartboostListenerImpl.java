package net.callofdroidy.adrewardsyou.adlistenerImpl;

import android.content.Context;
import android.widget.ImageView;

import com.chartboost.sdk.CBLocation;
import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.ChartboostDelegate;
import com.chartboost.sdk.Model.CBError;

import net.callofdroidy.adrewardsyou.R;
import net.callofdroidy.adrewardsyou.adprovider.ProviderChartboost;

public class ChartboostListenerImpl extends ChartboostDelegate {
    private Context context;
    private ImageView imageView;
    private ProviderChartboost adProvider;

    public ChartboostListenerImpl(Context context, ImageView imageView, ProviderChartboost adProvider) {
        this.context = context;
        this.imageView = imageView;
        this.adProvider = adProvider;
    }

    @Override
    public void didCacheRewardedVideo(String location) {
        super.didCacheRewardedVideo(location);
        imageView.setImageDrawable(context.getDrawable(R.drawable.ic_check_green_24dp));
        adProvider.activate(null);
    }

    @Override
    public void didFailToLoadRewardedVideo(String location, CBError.CBImpressionError error) {
        super.didFailToLoadRewardedVideo(location, error);
    }

    @Override
    public void didDisplayRewardedVideo(java.lang.String location) {
        super.didDisplayRewardedVideo(location);
        imageView.setImageDrawable(context.getDrawable(R.drawable.ic_cross_red_24dp));
        adProvider.reset();
    }

    @Override
    public void didCloseRewardedVideo(String location) {
        super.didCloseRewardedVideo(location);
        Chartboost.cacheRewardedVideo(CBLocation.LOCATION_DEFAULT);
    }
}
