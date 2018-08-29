package net.callofdroidy.adrewardsyou.adprovider;

import android.content.Context;
import android.widget.Toast;

import com.vungle.warren.AdConfig;
import com.vungle.warren.PlayAdCallback;
import com.vungle.warren.Vungle;

import net.callofdroidy.adrewardsyou.R;

public class ProviderVungle implements BaseAdProvider {

    private PlayAdCallback callback;
    private AdConfig adConfig;
    private boolean activated;

    public ProviderVungle(PlayAdCallback callback) {
        this.callback = callback;
        adConfig = new AdConfig();
        adConfig.setAutoRotate(false);
        activated = false;
    }

    @Override
    public void play(Context context) {
        if (activated) {
            Vungle.playAd(context.getString(R.string.vungle_placement_id), adConfig, callback);
        } else {
            Toast.makeText(context, "Vungle is currently unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public String name() {
        return "Vungle";
    }

    @Override
    public void reset() {
        activated = false;
    }

    public void activate() {
        activated = true;
    }
}
