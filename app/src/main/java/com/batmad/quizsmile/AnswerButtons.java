package com.batmad.quizsmile;

import android.app.Activity;
import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by Tm on 09.04.2017.
 */

public class AnswerButtons {
    private Context context;
    private GuessButtons guessButtons;
    private LinearLayout layout;
    private int[] width;
    private String current_answer;

    AnswerButtons(Context context){
        this.context = context;
        layout = (LinearLayout)((Activity)context).findViewById(R.id.layout_answer_buttons);
        width = new int[]{100,100,100,100,100,100,100,100,100,100,90,80,70,60,50,50,50};
    }

    public void Init(String answer, GuessButtons guess){
        guessButtons = guess;
        CreateButtons(answer);
    }

    public void ButtonPressed(int id){
        Button bt = (Button)layout.getChildAt(id);
        if(!bt.getText().equals("")){
            ((Quizable)context).PlaySound(Quizable.SOUND.ANSWER);
            guessButtons.RestoreButton((int)bt.getTag(), bt.getText().toString());
            bt.setText("");
        }
        final Animation animScale = AnimationUtils.loadAnimation(context, R.anim.scale);
        bt.startAnimation(animScale);
    }

    public Button GetFreeButton(){
        for(int i = 0; i < layout.getChildCount(); ++i){
            Button bt = (Button)layout.getChildAt(i);
            if(bt.getText().equals(""))
                return bt;
        }
        return null;
    }

    public Button GetLastButtonWithWord(){
        int i = 0;
        for(; i < layout.getChildCount(); ++i){
            Button bt = (Button)layout.getChildAt(i);
            if(bt.getText().equals(""))
                break;
        }
        --i;
        Button bt = (Button)layout.getChildAt(i);
        if(bt.getText().equals(" "))
            --i;
        return (Button)layout.getChildAt(i);
    }

    public int GetOpenedWordCount(){
        int i = 0;
        for(; i < layout.getChildCount(); ++i){
            Button bt = (Button)layout.getChildAt(i);
            if(bt.getText().equals(""))
                break;
        }
        --i;
        if(i < 0)
            return 0;
        Button bt = (Button)layout.getChildAt(i);
        if(bt.getText().equals(" "))
            --i;
        return i + 1;
    }

    public void NextStep(String answer){
        CleanUp();
        CreateButtons(answer);
    }

    public LinearLayout GetLayout(){
        return layout;
    }

    public void WrongAnser(){
        final Animation animScale = AnimationUtils.loadAnimation(context, R.anim.scale);
        layout.startAnimation(animScale);
    }

    public void OpenWord(){
        Button bt = GetFreeButton();
        if(bt != null) {
            bt.setText(String.valueOf(current_answer.charAt(bt.getId())));
            bt.setOnClickListener(null);
        }
    }

    public String GetAnswer(){
        return current_answer;
    }

    private void CreateButtons(String answer){
        current_answer = answer;
        for(int i = 0; i < current_answer.length(); ++i){
            layout.addView(isSpace(current_answer.charAt(i)) ? CreateSpace() : CreateEmptyButton(i));
        }
    }

    private Button CreateEmptyButton(int i){
        Button bt = new Button(new ContextThemeWrapper(context, R.style.mAnswer), null, 0);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width[current_answer.length()], 100);
        params.setMargins(10, 0, 0, 0);
        bt.setId(i);
        bt.setOnClickListener((View.OnClickListener)context);
        bt.setLayoutParams(params);
        return bt;
    }

    private Button CreateSpace(){
        Button bt = new Button(context);
        bt.setBackgroundColor(0);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(25, 100);
        bt.setText(" ");
        bt.setLayoutParams(params);
        return bt;
    }

    private boolean isSpace(char c){
        return c == ' ';
    }

    private void CleanUp(){
        layout.removeAllViews();
    }

}
