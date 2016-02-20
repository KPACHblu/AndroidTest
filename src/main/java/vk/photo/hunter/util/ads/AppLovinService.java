package vk.photo.hunter.util.ads;

import android.app.Activity;
import android.util.Log;

import com.applovin.adview.AppLovinInterstitialAd;

import java.lang.ref.WeakReference;

public class AppLovinService {
    public static final String METHOD_CALLS_COUNTER_TAG = "vk.photo.hunter.util.ads.AppLovinService.methodCallsCounter";
    public static final String ADS_DISPLAYED_COUNTER_TAG = "vk.photo.hunter.util.ads.AppLovinService.adsDisplayedCounter";
    private static final String TAG = "AppLovinService";
    private static final byte MAX_ADS_DISPLAYED_TIMES = 3;

    private int methodCallsCounter;
    private int adsDisplayedCounter;
    private WeakReference<Activity> activityRef;

    public AppLovinService(Activity activity) {
        this(activity, 0, 0);
    }

    public AppLovinService(Activity activity, int methodCallsCounter, int adsDisplayedCounter) {
        activityRef = new WeakReference<>(activity);
        this.methodCallsCounter = methodCallsCounter;
        this.adsDisplayedCounter = adsDisplayedCounter;

        Log.d(TAG, "Created instance with methodCallsCounter:" + methodCallsCounter + "; adsDisplayedCounter:" + adsDisplayedCounter);
    }

    public void tryToShowAds() {
        if (adsDisplayedCounter < MAX_ADS_DISPLAYED_TIMES) {
            methodCallsCounter++;
            Log.d(TAG, "tryToShowAds, methodCallsCounter:" + methodCallsCounter + "; adsDisplayedCounter:" + adsDisplayedCounter);
            if (methodCallsCounter == 5 || methodCallsCounter == 15 || methodCallsCounter == 25) {
                showAd();
            } else if (methodCallsCounter > 35) {
                showAd();
                //There is no matter if ads has shown in this case, just increase the counter
                adsDisplayedCounter++;
            }
        }
    }

    private void showAd() {
        if (AppLovinInterstitialAd.isAdReadyToDisplay(activityRef.get())) {
            adsDisplayedCounter++;
            AppLovinInterstitialAd.show(activityRef.get());
        }
    }

    public int getAdsDisplayedCounter() {
        return adsDisplayedCounter;
    }

    public int getMethodCallsCounter() {
        return methodCallsCounter;
    }
}
