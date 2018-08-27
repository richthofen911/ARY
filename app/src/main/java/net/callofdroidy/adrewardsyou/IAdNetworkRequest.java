package net.callofdroidy.adrewardsyou;

public interface IAdNetworkRequest {
    boolean isHyprMediateAvailable();

    /*
    boolean isStartAppAvailable();
    boolean isAdMobAvailable();
    boolean isInMobiAvailable();
    boolean isChartboostAvailable();
    void isKiipAvailable(@NonNull Kiip.Callback callback);
    void isKiipVideoAvailable(@NonNull Kiip.Callback callback);
    boolean isFBAudienceAvailable();
    boolean isAppmediationAvailable();
    boolean isSurvataAvailable();

    void loadPoptart(@Nullable Poptart poptart);
    */

    void playAd(int adProvider);
}
