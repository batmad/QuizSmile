package com.batmad.quizsmile;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class StartActivity extends Activity {
    private String theme;
    private int theme_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        theme = sharedPref.getString("theme", "common");
        theme_id = sharedPref.getInt("theme_id", R.array.emojis);

        boolean firstLaunch = sharedPref.getBoolean("first_launch", true);
        if(!firstLaunch){
            Button bt = (Button)findViewById(R.id.play_btn);
            bt.setText(R.string.continue_btn);
        }
    }

    public void PlayGame(View view){
        BtnAnimation((Button)view);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("emoji_pack", theme_id);
        intent.putExtra("theme_name", theme);
        startActivity(intent);
    }

    public void GoToBank(View view){
        BtnAnimation((Button)view);
        Intent intent = new Intent(this, BankActivity.class);
        startActivity(intent);
    }

    public void ChooseGenre(View view){
        BtnAnimation((Button)view);
        Intent intent = new Intent(this, GenreActivity.class);
        startActivity(intent);
    }


    private void BtnAnimation(Button bt){
        final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.scale);
        bt.startAnimation(animScale);
    }
}
