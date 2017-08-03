package com.batmad.quizsmile;

/**
 * Created by Tm on 10.04.2017.
 */

public interface Quizable {
    static final int MONEY_WIN = 10;
    static final int OPEN_WORD_COST = 20;
    static final int GET_TIP_COST = 40;
    enum SOUND {
        GUESS,
        ANSWER,
        WIN,
        WRONG
    }
    enum COST{
        COIN,
        AD
    }
    public void Win();
    public void Restart();
    public void NextStep();
    public void PlaySound(SOUND sound);
    public void OpenWord(COST cost);
    public void ShowTip(COST cost);
}
