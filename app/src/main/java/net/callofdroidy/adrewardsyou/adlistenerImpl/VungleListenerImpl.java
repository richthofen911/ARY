package net.callofdroidy.adrewardsyou.adlistenerImpl;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.vungle.warren.LoadAdCallback;
import com.vungle.warren.PlayAdCallback;
import com.vungle.warren.Vungle;

import net.callofdroidy.adrewardsyou.R;
import net.callofdroidy.adrewardsyou.adprovider.ProviderVungle;

public class VungleListenerImpl extends BaseAdListenerImpl implements LoadAdCallback, PlayAdCallback{
    private static final String TAG = "VungleListener";

    public VungleListenerImpl(@NonNull Context context,
                              @NonNull ProviderVungle adProvider,
                              @Nullable ImageView imageView) {
        super(context, adProvider, imageView);
    }


    @Override
    public void onAdLoad(String s) {
        adProvider.activate(null);
        imageView.setImageDrawable(context.getDrawable(R.drawable.ic_check_green_24dp));
    }

    @Override
    public void onAdStart(String s) {

    }

    @Override
    public void onAdEnd(String s, boolean b, boolean b1) {
        adProvider.reset();
        imageView.setImageDrawable(context.getDrawable(R.drawable.ic_cross_red_24dp));
        Vungle.loadAd(context.getString(R.string.vungle_placement_id), this);
    }

    @Override
    public void onError(String s, Throwable throwable) {
        Log.e(TAG, "onError: " + s);
    }
}
