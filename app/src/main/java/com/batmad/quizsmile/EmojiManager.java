package com.batmad.quizsmile;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Tm on 09.04.2017.
 */

public class EmojiManager {
    private Context context;
    String[] emojis;
    String[] titles;
    TextView mEmojiView;
    TextView mEmojiTitleView;

    EmojiManager(Context context){
        this.context = context;
        AssetManager assetManager = context.getAssets();
        Typeface custom_font = Typeface.createFromAsset(assetManager,  "fonts/NotoColorEmoji.ttf");

        mEmojiView = (TextView)((Activity)context).findViewById(R.id.emoji);
        mEmojiView.setTypeface(custom_font);
        mEmojiTitleView = (TextView)((Activity)context).findViewById(R.id.emoji_title);
    }

    public void Init(String emoji, String title){
        NextStep(emoji, title);
    }

    public void NextStep(String emoji, String title){
        mEmojiView.setText(emoji);
        mEmojiTitleView.setText(title);
    }


}
