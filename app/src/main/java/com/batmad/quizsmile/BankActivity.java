package com.batmad.quizsmile;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.batmad.quizsmile.util.IabHelper;
import com.batmad.quizsmile.util.IabResult;
import com.batmad.quizsmile.util.Inventory;
import com.batmad.quizsmile.util.Purchase;

import java.util.HashMap;
import java.util.Map;

public class BankActivity extends AppCompatActivity implements Rewardable {
    private RewardAdManager AdManager;
    private MoneyManager moneyManager;
    private IabHelper iabHelper;
    private static final String TAG = "IAB";
    public String SKU_REMOVE_ADS = "ads_free";
    public String SKU_BUY_COIN_1 = "coin1";
    public String SKU_BUY_COIN_2 = "coin2";
    public String SKU_BUY_COIN_3 = "coin3";
    public String SKU_BUY_COIN_4 = "coin4";
    public String SKU_BUY_COIN_5 = "coin5";
    static final int RC_REQUEST = 10001;
    private Map<String, Integer> mCoins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);

        mCoins = new HashMap<>();
        mCoins.put("coin1",200);
        mCoins.put("coin2",420);
        mCoins.put("coin3",1100);
        mCoins.put("coin4",2250);
        mCoins.put("coin5",4600);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean adFree = sharedPref.getBoolean("ad_free", false);
        AdManager = new RewardAdManager(this, adFree);
        moneyManager = new MoneyManager(this, R.id.bank_money_btn, false);

        String base64EncodedPublicKey = getString(R.string.base64_google_play_key);
        iabHelper = new IabHelper(this, base64EncodedPublicKey);

        iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
//					processPurchases();
                }
//				processPurchases();
            }
        });
    }

    public void BuyCoin1(View view){
        iabHelper.launchPurchaseFlow(this, SKU_BUY_COIN_1, RC_REQUEST,	mPurchaseFinishedListener, "HANDLE_PAYLOADS");
    }

    public void BuyCoin2(View view){
        iabHelper.launchPurchaseFlow(this, SKU_BUY_COIN_2, RC_REQUEST,	mPurchaseFinishedListener, "HANDLE_PAYLOADS");
    }

    public void BuyCoin3(View view){
        iabHelper.launchPurchaseFlow(this, SKU_BUY_COIN_3, RC_REQUEST,	mPurchaseFinishedListener, "HANDLE_PAYLOADS");
    }

    public void BuyCoin4(View view){
        iabHelper.launchPurchaseFlow(this, SKU_BUY_COIN_4, RC_REQUEST,	mPurchaseFinishedListener, "HANDLE_PAYLOADS");
    }

    public void BuyCoin5(View view){
        iabHelper.launchPurchaseFlow(this, SKU_BUY_COIN_5, RC_REQUEST,	mPurchaseFinishedListener, "HANDLE_PAYLOADS");
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

    public void processPurchases() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                iabHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });
    }

    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {

            if (iabHelper == null) {
                Log.d("CON", "Error in mGotInventoryListener1");
                return;
            }

            if (result.isFailure()) {
                Log.d("CON", "Error in mGotInventoryListener2");
                return;
            }
            else {
                if(inventory.hasPurchase(SKU_BUY_COIN_1))
                    iabHelper.consumeAsync(inventory.getPurchase(SKU_BUY_COIN_1), mConsumeFinishedListener);
                else if(inventory.hasPurchase(SKU_BUY_COIN_2))
                    iabHelper.consumeAsync(inventory.getPurchase(SKU_BUY_COIN_2), mConsumeFinishedListener);
                else  if(inventory.hasPurchase(SKU_BUY_COIN_3))
                    iabHelper.consumeAsync(inventory.getPurchase(SKU_BUY_COIN_3), mConsumeFinishedListener);
                else  if(inventory.hasPurchase(SKU_BUY_COIN_4))
                    iabHelper.consumeAsync(inventory.getPurchase(SKU_BUY_COIN_4), mConsumeFinishedListener);
                else if(inventory.hasPurchase(SKU_BUY_COIN_5))
                    iabHelper.consumeAsync(inventory.getPurchase(SKU_BUY_COIN_5), mConsumeFinishedListener);
                Log.d("CON", "consumeAsync started");
            }

        }
    };

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            if (purchase == null) return;
            if (iabHelper == null) return;
            if (result.isFailure()) return;

            Log.d(TAG, "Purchase successful.");
            iabHelper.queryInventoryAsync(mGotInventoryListener);
        }
    };

    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase, IabResult result) {
                    if (result.isSuccess()) {
                        // provision the in-app purchase to the user
                        // (for example, credit 50 gold coins to player's character)
                        Log.d("CONSUME","SUCCESSFULLY CONSUMED");
                        if(purchase.getSku().equals(SKU_BUY_COIN_1)){
                            moneyManager.Add(mCoins.get(SKU_BUY_COIN_1));
                        }
                        else if(purchase.getSku().equals(SKU_BUY_COIN_2)){
                            moneyManager.Add(mCoins.get(SKU_BUY_COIN_2));
                        }
                        else if(purchase.getSku().equals(SKU_BUY_COIN_3)){
                            moneyManager.Add(mCoins.get(SKU_BUY_COIN_3));
                        }
                        else if(purchase.getSku().equals(SKU_BUY_COIN_4)){
                            moneyManager.Add(mCoins.get(SKU_BUY_COIN_4));
                        }
                        else if(purchase.getSku().equals(SKU_BUY_COIN_5)){
                            moneyManager.Add(mCoins.get(SKU_BUY_COIN_5));
                        }
                    }
                    else {
                        // handle error
                        Log.d("CONSUME","ERROR IN CONSUMING");
                    }
                }
            };

}

