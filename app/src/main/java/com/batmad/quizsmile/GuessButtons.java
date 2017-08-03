package com.batmad.quizsmile;

import android.app.Activity;
import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Tm on 09.04.2017.
 */

public class GuessButtons {
    private Context context;
    private AnswerButtons answerButtons;
    private LinearLayout layout_upper;
    private LinearLayout layout_lower;
    private Random random;
    private String[] alphabet;
    private String current_answer;
    private char[] randomizedStr;
    private int max_buttons = 16;
    private int max_buttons_in_row = 8;
    private int width = 115;
    private int height = 115;
    private int UPPER_ID = 100;
    private int LOWER_ID = 200;

    GuessButtons(Context context){
        this.context = context;
        layout_upper = (LinearLayout)((Activity)context).findViewById(R.id.layout_guess_buttons_upper);
        layout_lower = (LinearLayout)((Activity)context).findViewById(R.id.layout_guess_buttons_lower);
        random = new Random();
        randomizedStr = new char[max_buttons];
    }

    public void Init(String answer, AnswerButtons answerBtns){
        answerButtons = answerBtns;
        alphabet = context.getResources().getStringArray(R.array.alphabet);
        current_answer = answer;

        CreateButtons(CreateRandomStr());
    }

    public void ButtonPressed(int id){
        ((Quizable)context).PlaySound(Quizable.SOUND.GUESS);
        Button bt = (Button)GetLayout(id).getChildAt(id - GetIdOffset(id));
        Button answerBt = answerButtons.GetFreeButton();
        if (answerBt != null){
            answerBt.setText(bt.getText());
            answerBt.setTag(bt.getId());
            bt.setVisibility(View.INVISIBLE);
        }
        if(CheckAnswer())
            ((Quizable)context).Win();
        else if(isAnswerFilled()) {
            ((Quizable) context).PlaySound(Quizable.SOUND.WRONG);
            answerButtons.WrongAnser();
        }
        StartAnimation(bt);
    }

    public void RestoreButton(int id, String str){
        Button bt = (Button)GetLayout(id).getChildAt(id - GetIdOffset(id));
        bt.setText(str);
        bt.setVisibility(View.VISIBLE);
        StartAnimation(bt);
    }

    public void NextStep(String answer){
        current_answer = answer;
        CleanUp();
        CreateButtons(CreateRandomStr());
    }

    public void OpenWord(){
        Button bt_answer = answerButtons.GetLastButtonWithWord();


        boolean finded = false;
        for(int i = 0; i < layout_lower.getChildCount(); ++i){
            Button bt = (Button)layout_lower.getChildAt(i);
            if(bt.getText().equals(bt_answer.getText()) && bt.getVisibility() == View.VISIBLE) {
                bt.setVisibility(View.INVISIBLE);
                finded = true;
                break;
            }
        }

        if(!finded) {
            for (int i = 0; i < layout_upper.getChildCount(); ++i) {
                Button bt = (Button) layout_upper.getChildAt(i);
                if (bt.getText().equals(bt_answer.getText()) && bt.getVisibility() == View.VISIBLE) {
                    bt.setVisibility(View.INVISIBLE);
                    break;
                }
            }
        }

        if(CheckAnswer())
            ((Quizable)context).Win();
    }

    private boolean CheckAnswer(){
        String answer = "";
        for(int i = 0; i < answerButtons.GetLayout().getChildCount(); ++i){
            Button bt = (Button)answerButtons.GetLayout().getChildAt(i);
            if(bt.getText().equals(""))
                return false;
            answer += bt.getText().toString();
        }
        return current_answer.equals(answer);
    }

    private boolean isAnswerFilled(){
        for(int i = 0; i < answerButtons.GetLayout().getChildCount(); ++i) {
            Button bt = (Button) answerButtons.GetLayout().getChildAt(i);
            if (bt.getText().equals(""))
                return false;
        }
        return true;
    }

    private void CreateButtons(char[] randomizedStr){
        for(int i = 0; i < max_buttons_in_row; ++i){
            Button bt = new Button(new ContextThemeWrapper(context, R.style.mButton), null, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
            params.setMargins(8, 0, 8, 0);
            bt.setId(UPPER_ID + i);
            bt.setLayoutParams(params);
            bt.setOnClickListener((View.OnClickListener)context);
            bt.setText(String.valueOf(randomizedStr[i]));
            layout_upper.addView(bt);
        }

        for(int i = 0; i < max_buttons_in_row; ++i){
            Button bt = new Button(new ContextThemeWrapper(context, R.style.mButton), null, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
            params.setMargins(8, 0, 8, 0);
            bt.setId(LOWER_ID + i);
            bt.setLayoutParams(params);
            bt.setOnClickListener((View.OnClickListener)context);
            bt.setText(String.valueOf(randomizedStr[i + max_buttons_in_row]));
            layout_lower.addView(bt);
        }
    }

    private char[] CreateRandomStr(){
        char[] randomizedStr = new char[max_buttons];
        ArrayList<Integer> answer_randomized = CreateAnswerRandomPosition();
        int j = 0;
        for(int i : answer_randomized){
            if (current_answer.charAt(j) == ' ') {
                j++;
                continue;
            }
            randomizedStr[i] = current_answer.charAt(j);
            j++;
        }
        for (int i = 0; i < max_buttons; ++i ){
            if(randomizedStr[i] == '\u0000'){
                int rand;
                boolean find;
                do {
                    find = false;
                    rand = random.nextInt(alphabet.length);
                    for(int num : randomizedStr){
                        if(num == alphabet[rand].charAt(0)) {
                            find = true;
                            break;
                        }
                    }
                } while (find);
                randomizedStr[i] = alphabet[rand].charAt(0);
            }
        }
        return randomizedStr;
    }

    private ArrayList<Integer> CreateAnswerRandomPosition(){
        ArrayList<Integer> rands = new ArrayList<>();
        for(int i = 0; i < current_answer.length(); ++i){
            int rand;
            boolean find;
            do {
                find = false;
                rand = random.nextInt(max_buttons);
                for(int num : rands){
                    if(num == rand) {
                        find = true;
                        break;
                    }
                }
            } while(find);
            rands.add(rand);
        }
        return rands;
    }

    private void CleanUp(){
        layout_upper.removeAllViews();
        layout_lower.removeAllViews();
    }

    private LinearLayout GetLayout(int id){
        return id >= LOWER_ID ? layout_lower : layout_upper;
    }

    private int GetIdOffset(int id){
        return id >= LOWER_ID ? LOWER_ID : UPPER_ID;
    }

    private void StartAnimation(View bt){
        final Animation animScale = AnimationUtils.loadAnimation(context, R.anim.scale);
        bt.startAnimation(animScale);
    }
}
