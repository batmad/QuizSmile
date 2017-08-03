package com.batmad.quizsmile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

/**
 * Created by Tm on 08.05.2017.
 */

public class MoneyManager implements View.OnClickListener{
    private Context context;
    SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private Button mButton;
    private int mMoney;

    MoneyManager(Context context, int btnId, boolean hasListener){
        this.context = context;
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        mMoney = sharedPref.getInt("money", 0);
        editor = sharedPref.edit();

        mButton = (Button) ((Activity)context).findViewById(btnId);
        if(hasListener)
            mButton.setOnClickListener(this);
        UpdateText();
    }

    public int GetMoney(){
        return mMoney;
    }

    public boolean HasMoney(){
        return mMoney > 0;
    }

    public boolean CanAfford(int offer){
        return mMoney >= offer;
    }

    public void Add(int money){
        mMoney += money;
        editor.putInt("money", mMoney);
        editor.apply();
        UpdateText();
    }

    public void Remove(int money){
        mMoney -= money;
        editor.putInt("money", mMoney);
        editor.apply();
        UpdateText();
    }

    public void UpdateText(){
        mMoney = sharedPref.getInt("money", 0);
        String text = String.valueOf(mMoney) + context.getResources().getString(R.string.money_bag);
        mButton.setText(text);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(context, BankActivity.class);
        context.startActivity(intent);
    }
}
