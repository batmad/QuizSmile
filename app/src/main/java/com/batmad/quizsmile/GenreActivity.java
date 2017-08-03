package com.batmad.quizsmile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class GenreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre);
    }

    public void PlayCommon(View view){
        BtnAnimation((Button)view);
        StartGame(R.array.emojis, "common");
    }

    public void PlayMovies(View view){
        BtnAnimation((Button)view);
        StartGame(R.array.emoji_movies, "movies");
    }

    public void PlayMelody(View view){
        BtnAnimation((Button)view);
        StartGame(R.array.emoji_melody, "melody");
    }

    public void PlayCountry(View view){
        BtnAnimation((Button)view);
        StartGame(R.array.emoji_country, "country");
    }

    private void StartGame(int Rid, String theme){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("emoji_pack", Rid);
        intent.putExtra("theme_name", theme);
        startActivity(intent);
    }

    private void BtnAnimation(Button bt){
        final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.scale);
        bt.startAnimation(animScale);
    }
}
