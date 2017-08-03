package com.batmad.quizsmile;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Tm on 01.05.2017.
 */

public class WarningAdDialog {
    private Dialog dialog;
    private RewardAdManager adManager;
    private Activity mActivity;

    WarningAdDialog(Activity activity, RewardAdManager rewardAdManager){
        this.mActivity = activity;
        this.adManager = rewardAdManager;
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_warning_ad);
        dialog.setOwnerActivity(activity);

        Button exitBtn = (Button) dialog.findViewById(R.id.warning_no_btn);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Button adBtn = (Button) dialog.findViewById(R.id.warning_ad_btn);
        adBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adManager.ShowAd();
                dialog.dismiss();
            }
        });

        Button coinBtn = (Button) dialog.findViewById(R.id.warning_coin_btn);
        coinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adManager.GetAdType() == RewardAdManager.AdType.OPEN_WORD)
                    ((Quizable)mActivity).OpenWord(Quizable.COST.COIN);
                else
                    ((Quizable)mActivity).ShowTip(Quizable.COST.COIN);
                dialog.dismiss();
            }
        });

        int cost = (adManager.GetAdType() == RewardAdManager.AdType.OPEN_WORD) ? Quizable.OPEN_WORD_COST : Quizable.GET_TIP_COST;
        String str = activity.getResources().getString(R.string.warning_ad, cost);
        TextView text = (TextView)dialog.findViewById(R.id.warning_ad_text);
        text.setText(str);
    }

    public void show(){
        dialog.show();
    }
}
