package com.batmad.quizsmile;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.BannerCallbacks;
import com.appodeal.ads.InterstitialCallbacks;
import com.appodeal.ads.RewardedVideoCallbacks;

/**
 * Created by Tm on 30.04.2017.
 */

public class RewardAdManager implements RewardedVideoCallbacks, InterstitialCallbacks, BannerCallbacks {
    enum AdType{
        TIPS,
        OPEN_WORD,
        GET_REWARD,
        DOUBLE_REWARD
    }

    private Context context;
    private Toast mToast;
    private AdType mType;
    private boolean adFree;

    RewardAdManager(Context context, boolean adFree){
        this.context = context;
        this.adFree = adFree;
        Appodeal.initialize((Activity)context, context.getResources().getString(R.string.appodeal_key), Appodeal.INTERSTITIAL | Appodeal.REWARDED_VIDEO | Appodeal.BANNER);
        Appodeal.setRewardedVideoCallbacks(this);
        Appodeal.setInterstitialCallbacks(this);
        Appodeal.setBannerCallbacks(this);
        Appodeal.setTesting(true);
    }

    public void SetAdType(AdType type){
        mType = type;
    }

    public void ShowAd(){
        if (Appodeal.isLoaded(Appodeal.REWARDED_VIDEO) && false) //TODO remove false on build
            Appodeal.show((Activity)context, Appodeal.REWARDED_VIDEO);
        else if(Appodeal.isLoaded(Appodeal.INTERSTITIAL))
            Appodeal.show((Activity)context, Appodeal.INTERSTITIAL);
        else
            Fail();
    }

    public void Resume(){
        if(adFree)
            return;
        Appodeal.onResume((Activity)context, Appodeal.BANNER);
    }

    public void ShowBanner(){
        if(adFree)
            return;
        if (Appodeal.isLoaded(Appodeal.BANNER))
            Appodeal.show((Activity)context, Appodeal.BANNER_BOTTOM);
    }

    public void ShowWarning(){
        WarningAdDialog warningDialog = new WarningAdDialog((Activity)context, this);
        warningDialog.show();
    }

    public AdType GetAdType(){ return mType; }

    @Override
    public void onRewardedVideoLoaded() {
        showToast("onRewardedVideoLoaded");
    }
    @Override
    public void onRewardedVideoFailedToLoad() {
        showToast("onRewardedVideoFailedToLoad");
    }
    @Override
    public void onRewardedVideoShown() {
        showToast("onRewardedVideoShown");
    }
    @Override
    public void onRewardedVideoFinished(int amount, String name) {showToast(String.format("onRewardedVideoFinished. Reward: %d %s", amount, name));}
    @Override
    public void onRewardedVideoClosed(boolean finished) {
        showToast(String.format("onRewardedVideoClosed,  finished: %s", finished));
        if (finished)
            GetReward();
    }

    @Override
    public void onInterstitialLoaded(boolean b) {
        showToast("onInterstitialLoaded");
    }
    @Override
    public void onInterstitialFailedToLoad() {
        showToast("onInterstitialFailedToLoad");
    }
    @Override
    public void onInterstitialShown() {
        showToast("onInterstitialShown");
    }
    @Override
    public void onInterstitialClicked() {
        showToast("onInterstitialClicked");
    }
    @Override
    public void onInterstitialClosed() {
        showToast("onInterstitialClosed");
        GetReward();
    }

    @Override
    public void onBannerLoaded(int i, boolean b) {
        ShowBanner();
    }
    @Override
    public void onBannerFailedToLoad() {}
    @Override
    public void onBannerShown() { }
    @Override
    public void onBannerClicked() {}

    private void showToast(final String text) {
        if (mToast == null) {
            mToast = Toast.makeText((Activity)context, text, Toast.LENGTH_SHORT);
        }
        mToast.setText(text);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    private void GetReward(){
        switch(mType) {
            case OPEN_WORD:
                ((Quizable) context).OpenWord(Quizable.COST.AD);
                break;
            case TIPS:
                ((Quizable) context).ShowTip(Quizable.COST.AD);
                break;
            case GET_REWARD:
                ((Rewardable) context).GetMoneyAdView();
                break;
            case DOUBLE_REWARD:
                ((Rewardable) context).DoubleWinReward();
        }
    }

    private void Fail(){
        String str = context.getResources().getString(R.string.try_later);
        InfoDialog infoDialog = new InfoDialog((Activity) context, str);
        infoDialog.show();
    }
}
