package site.cpsp.myledger.utils;

import android.content.Context;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
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
}
