package net.callofdroidy.adrewardsyou.adprovider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.vungle.warren.AdConfig;
import com.vungle.warren.PlayAdCallback;
import com.vungle.warren.Vungle;

import net.callofdroidy.adrewardsyou.R;

public class ProviderVungle extends BaseAdProvider {
    private static final String TAG = "ProviderVungle";

    private PlayAdCallback callback;
    private AdConfig adConfig;

    public ProviderVungle() {
        super();
        adConfig = new AdConfig();
        adConfig.setAutoRotate(false);
        activated = false;
    }

    public void setPlayCallback(@NonNull PlayAdCallback callback) {
        this.callback = callback;
    }

    @Override
    public void play(final Context context) {
        if (callback != null) {
            if (activated) {
                Vungle.playAd(context.getString(R.string.vungle_placement_id), adConfig, callback);
            } else {
                Toast.makeText(
                        context,
                        context.getString(
                                R.string.provider_not_avaialable_template, name()), Toast.LENGTH_SHORT)
                        .show();
            }
        } else {
            Log.e(TAG, "PlayAdCallback cannot be null");
        }
    }

    @Override
    public String name() {
        return "Vungle";
    }
}
