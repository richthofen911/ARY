package net.callofdroidy.adrewardsyou.adprovider;

import android.content.Context;

public abstract class BaseAdProvider<T> {
    boolean activated;
    T t;

    BaseAdProvider() {
        activated = false;
    }

    public abstract void play(Context context);
    public abstract String name();

    public void reset() {
        activated = false;
    }

    public void activate(T t) {
        this.t = t;
        activated = true;
    }
}
