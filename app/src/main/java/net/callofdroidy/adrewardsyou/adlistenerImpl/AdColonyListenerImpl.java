package net.callofdroidy.adrewardsyou.adlistenerImpl;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyZone;

import net.callofdroidy.adrewardsyou.R;
import net.callofdroidy.adrewardsyou.adprovider.ProviderAdColony;

public class AdColonyListenerImpl extends AdColonyInterstitialListener {

    private Context context;
    private ImageView imageView;
    private ProviderAdColony adColony;

    public AdColonyListenerImpl(@NonNull Context context, ImageView imageView,
                                ProviderAdColony adColony) {
        this.context = context;
        this.imageView = imageView;
        this.adColony = adColony;
    }

    @Override
    public void onRequestFilled(final AdColonyInterstitial adColonyInterstitial) {
        imageView.setImageDrawable(context.getDrawable(R.drawable.ic_check_green_24dp));
        adColony.activate(adColonyInterstitial);
    }

    @Override
    public void onRequestNotFilled(AdColonyZone zone) {
    }

    @Override
    public void onOpened(AdColonyInterstitial ad) {
    }

    @Override
    public void onClosed(AdColonyInterstitial ad) {
        adColony.reset();
        imageView.setImageDrawable(context.getDrawable(R.drawable.ic_cross_red_24dp));
        AdColony.requestInterstitial(context.getString(R.string.adcolony_zone_id), this);
    }

    @Override
    public void onClicked(AdColonyInterstitial ad) {
    }
}
