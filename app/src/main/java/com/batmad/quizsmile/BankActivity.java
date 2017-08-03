package com.batmad.quizsmile;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BankActivity extends AppCompatActivity implements Rewardable {
    private RewardAdManager AdManager;
    private MoneyManager moneyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean adFree = sharedPref.getBoolean("ad_free", false);
        AdManager = new RewardAdManager(this, adFree);
        moneyManager = new MoneyManager(this, R.id.bank_money_btn, false);
    }

    public void BuyCoin1(View view){

    }

    public void BuyCoin2(View view){

    }

    public void BuyCoin3(View view){

    }

    public void BuyCoin4(View view){

    }

    public void BuyCoin5(View view){

    }

    public void BuyCoinFree(View view){
        AdManager.SetAdType(RewardAdManager.AdType.GET_REWARD);
        AdManager.ShowAd();
    }

    @Override
    public void GetMoneyAdView() {
        moneyManager.Add(MONEY_AD_WATCH);
    }

    @Override
    public void DoubleWinReward() {

    }
}
