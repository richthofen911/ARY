package net.callofdroidy.adrewardsyou.adprovider;

import android.content.Context;

public interface BaseAdProvider {
    void play(Context context);
    boolean isAvailable();
    String name();
    void reset();
}
