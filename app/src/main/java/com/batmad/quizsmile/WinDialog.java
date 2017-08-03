package com.batmad.quizsmile;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.plattysoft.leonids.ParticleSystem;

/**
 * Created by Tm on 15.04.2017.
 */

public class WinDialog {
    private Dialog dialog;
    private Activity activity;
    private RewardAdManager AdManger;
    private Button moreRewardBtn;

    WinDialog(Activity activity, RewardAdManager adManager){
        this.activity = activity;
        this.AdManger = adManager;
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_win);
        dialog.setOwnerActivity(activity);

        moreRewardBtn = (Button) dialog.findViewById(R.id.win_more_reward_btn);
        moreRewardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Button)view).setVisibility(View.INVISIBLE);
                DoubleReward();
            }
        });

        Button okBtn = (Button) dialog.findViewById(R.id.win_ok_btn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Refresh();
                NextStep();
            }
        });

        Button restartBtn = (Button) dialog.findViewById(R.id.win_restart_btn);
        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Restart();
            }
        });

        UpdateText(Quizable.MONEY_WIN);
    }

    public void show(int current_level){
        String str = activity.getResources().getString(R.string.win, current_level + 1);
        TextView textView = (TextView) dialog.findViewById(R.id.winText);
        textView.setText(str);

        dialog.show();
    }

    public void NextStep(){
        ((Quizable)activity).NextStep();
    }

    public Dialog GetDialog(){
        return dialog;
    }

    public void Restart(){
        ((Quizable)activity).Restart();
    }

    public void Refresh(){
        moreRewardBtn.setVisibility(View.VISIBLE);
        UpdateText(Quizable.MONEY_WIN);
    }

    public void DoubleReward(){
        AdManger.SetAdType(RewardAdManager.AdType.DOUBLE_REWARD);
        AdManger.ShowAd();
    }

    public void UpdateRewardText(){
        UpdateText(Quizable.MONEY_WIN * 2);
    }

    private void UpdateText(int sum){
        String str = "+" + String.valueOf(sum) + activity.getResources().getString(R.string.money_bag);
        TextView textView = (TextView) dialog.findViewById(R.id.win_reward_money);
        textView.setText(str);
    }

}
