package net.callofdroidy.adrewardsyou.adlistenerImpl;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import net.callofdroidy.adrewardsyou.adprovider.BaseAdProvider;

public class BaseAdListenerImpl {
    protected Context context;
    protected BaseAdProvider adProvider;
    protected ImageView imageView;

    public BaseAdListenerImpl(
            @NonNull Context context, @NonNull BaseAdProvider adProvider,
            @Nullable ImageView imageView) {
        this.context = context;
        this.adProvider = adProvider;
        this.imageView = imageView;
    }
}
