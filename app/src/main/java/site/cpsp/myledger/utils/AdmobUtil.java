package site.cpsp.myledger.utils;

import android.content.Context;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class AdmobUtil {
    public interface Progress{
        void end();
    }

    private static AdmobUtil inst;
    private Context context;
    private Progress progress;
    private AdmobUtil(Context context){
        this.context= context;
        MobileAds.initialize(context, "ca-app-pub-5333091392909120~5255732621");
    }
    public static AdmobUtil getInst(Context context, Progress progress){
        if(inst== null) inst= new AdmobUtil(context);
        inst.progress= progress;
        return inst;
    }


    public void loadBannerAd(AdView adView){
        AdRequest adRequest= new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }
    public void loadFullSizeAd(){
        InterstitialAd ad= new InterstitialAd(context);
        ad.setAdUnitId("ca-app-pub-5333091392909120/8428690874");
        ad.loadAd(new AdRequest.Builder().build());
        ad.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                ad.show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                if(progress!= null) progress.end();
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                if(progress!= null) progress.end();
            }
        });
    }
}
