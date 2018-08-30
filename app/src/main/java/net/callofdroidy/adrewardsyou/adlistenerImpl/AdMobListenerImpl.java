package net.callofdroidy.adrewardsyou.adlistenerImpl;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import net.callofdroidy.adrewardsyou.R;
import net.callofdroidy.adrewardsyou.adprovider.ProviderAdMob;

public class AdMobListenerImpl extends BaseAdListenerImpl implements RewardedVideoAdListener{
    private RewardedVideoAd adMobClient;

    public AdMobListenerImpl(
            @NonNull Context context,
            @NonNull ProviderAdMob adProvider,
            @Nullable ImageView imageView,
            RewardedVideoAd adMobClient) {
        super(context, adProvider, imageView);

        this.adMobClient = adMobClient;
        this.adMobClient.setRewardedVideoAdListener(this);
    }

    // admob listener
    @Override
    public void onRewardedVideoAdLoaded() {
        adProvider.activate(null);
        imageView.setImageDrawable(context.getDrawable(R.drawable.ic_check_green_24dp));
    }

    // admob listener
    @Override
    public void onRewardedVideoAdOpened() {

    }

    // admob listener
    @Override
    public void onRewardedVideoStarted() {

    }

    // admob listener
    @Override
    public void onRewardedVideoAdClosed() {
        adProvider.reset();
        imageView.setImageDrawable(context.getDrawable(R.drawable.ic_cross_red_24dp));
        adMobClient.loadAd(context.getString(R.string.admob_ad_unit_id), new AdRequest.Builder().build());
    }

    // admob listener
    @Override
    public void onRewarded(RewardItem rewardItem) {

    }

    // admob listener
    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    // admob listener
    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    // admob listener
    @Override
    public void onRewardedVideoCompleted() {

    }
}
