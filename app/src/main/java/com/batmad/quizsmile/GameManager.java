package com.batmad.quizsmile;

import android.app.Activity;
import android.content.Context;

import com.plattysoft.leonids.ParticleSystem;

import org.json.JSONObject;

/**
 * Created by Tm on 09.04.2017.
 */

public class GameManager {
    Context context;
    AnswerButtons answerButtons;
    GuessButtons guessButtons;
    EmojiManager emojiManager;
    String[] data;
    String emoji;
    String title;
    String answer;

    GameManager(Context context, int emoji_pack_id){
        this.context = context;
        emojiManager = new EmojiManager(context);
        answerButtons = new AnswerButtons(context);
        guessButtons = new GuessButtons(context);
        data = context.getResources().getStringArray(emoji_pack_id);
    }

    public void Init(int level){
        LoadJson(level);
        emojiManager.Init(emoji, title);
        answerButtons.Init(answer, guessButtons);
        guessButtons.Init(answer, answerButtons);
    }

    public void ButtonPressed(int id){
        if(id < 100)
            answerButtons.ButtonPressed(id);
        else
            guessButtons.ButtonPressed(id);
    }

    public void OpenWord(){
        answerButtons.OpenWord();
        guessButtons.OpenWord();
    }

    public void NextStep(int level){
        LoadJson(level);
        emojiManager.NextStep(emoji, title);
        answerButtons.NextStep(answer);
        guessButtons.NextStep(answer);
    }

    public String GetAnswer(){
        return answerButtons.GetAnswer();
    }

    public int GetOpenedWordCount(){
        return answerButtons.GetOpenedWordCount();
    }

    public String GetLastOpenedWord(){
        return answerButtons.GetLastButtonWithWord().getText().toString();
    }

    public void StartParticle(){
        new ParticleSystem((Activity) context, 20, R.drawable.confeti1, 3000)
                .setSpeedRange(0.1f, 0.25f)
                .setRotationSpeedRange(90, 180)
                .setInitialRotationRange(0, 360)
                .oneShot(((Activity)context).findViewById(R.id.layout_answer_buttons), 100);
        new ParticleSystem((Activity) context, 20, R.drawable.confeti2, 3000)
                .setSpeedRange(0.1f, 0.25f)
                .setRotationSpeedRange(90, 180)
                .setInitialRotationRange(0, 360)
                .oneShot(((Activity)context).findViewById(R.id.layout_answer_buttons), 100);
        new ParticleSystem((Activity) context, 20, R.drawable.confeti3, 3000)
                .setSpeedRange(0.1f, 0.25f)
                .setRotationSpeedRange(90, 180)
                .setInitialRotationRange(0, 360)
                .oneShot(((Activity)context).findViewById(R.id.layout_answer_buttons), 100);
        new ParticleSystem((Activity) context, 20, R.drawable.confeti4, 3000)
                .setSpeedRange(0.1f, 0.25f)
                .setRotationSpeedRange(90, 180)
                .setInitialRotationRange(0, 360)
                .oneShot(((Activity)context).findViewById(R.id.layout_answer_buttons), 100);
        new ParticleSystem((Activity) context, 20, R.drawable.confeti5, 3000)
                .setSpeedRange(0.1f, 0.25f)
                .setRotationSpeedRange(90, 180)
                .setInitialRotationRange(0, 360)
                .oneShot(((Activity)context).findViewById(R.id.layout_answer_buttons), 100);
    }

    private void LoadJson(int level){
        try {
            JSONObject jsonValue = new JSONObject(data[level]);
            title = jsonValue.getString("title");
            emoji = jsonValue.getString("emoji");
            answer = jsonValue.getString("answer");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
