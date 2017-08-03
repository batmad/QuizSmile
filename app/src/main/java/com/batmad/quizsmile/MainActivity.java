package com.batmad.quizsmile;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.amazon.device.ads.Ad;


public class MainActivity extends Activity implements View.OnClickListener, Quizable, Rewardable {
    private static final boolean debug = false;
    private GameManager gameManager;
    private NotifManager notifManager;
    private RewardAdManager AdManager;
    private MoneyManager moneyManager;
    private MediaPlayer mediaPlayer;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    private WinDialog winDialog;
    private int current_level;
    private int savedTips;
    private int LEVEL_MONEY_WIN;
    private String theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPref.edit();

        theme = getIntent().getStringExtra("theme_name");
        String level_name = "level" + theme;
        current_level = sharedPref.getInt(level_name, 0);

        if (debug)
            current_level = 0;

        int emoji_pack = getIntent().getIntExtra("emoji_pack", 0);
        editor.putString("theme", theme);
        editor.putInt("theme_id", emoji_pack);
        editor.apply();

        gameManager = new GameManager(this, emoji_pack);
        gameManager.Init(current_level);

        boolean adFree = sharedPref.getBoolean("ad_free", false);
        AdManager = new RewardAdManager(this, adFree);
        AdManager.ShowBanner();

        notifManager = new NotifManager(this, gameManager, theme);
        winDialog = new WinDialog(this, AdManager);

        moneyManager = new MoneyManager(this, R.id.money_btn, true);
        LEVEL_MONEY_WIN = MONEY_WIN;
    }

    @Override
    protected void onResume() {
        super.onResume();
        notifManager.SetNotifications(true);
        CheckOpenedWords();
        AdManager.Resume();
        moneyManager.UpdateText();
    }

    @Override
    protected void onStop() {
        super.onStop();
        notifManager.SetNotifications(false);
        editor.putString("theme", theme);
        editor.putBoolean("first_launch", false);
        editor.commit();
    }

    @Override
    public void onClick(View view) {
        gameManager.ButtonPressed(view.getId());
    }

    @Override
    public void PlaySound(SOUND sound) {
       if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer= null;
        }
        switch (sound){
            case ANSWER:
                mediaPlayer = MediaPlayer.create(this, R.raw.answer_click);
                break;
            case GUESS:
                mediaPlayer = MediaPlayer.create(this, R.raw.guess_click);
                break;
            case WIN:
                mediaPlayer = MediaPlayer.create(this, R.raw.win);
                break;
            case WRONG:
                mediaPlayer = MediaPlayer.create(this, R.raw.wrong);
                break;
        }
        mediaPlayer.start();
    }

    @Override
    public void Win() {
        PlaySound(SOUND.WIN);
        gameManager.StartParticle();
        winDialog.show(current_level);
    }

    @Override
    public void NextStep() {
        String curLevel = "level" + theme;
        editor.putInt(curLevel, ++current_level);
        String savedWords = "words" + theme;
        editor.putInt(savedWords, 0);
        editor.commit();

        moneyManager.Add(LEVEL_MONEY_WIN);
        gameManager.NextStep(current_level);

        LEVEL_MONEY_WIN = MONEY_WIN;
    }

    @Override
    public void Restart() {
        current_level = -1;
        NextStep();
    }

    public void ShowAdForWord(View view){
        BtnAnimation((Button)view);
        AdManager.SetAdType(RewardAdManager.AdType.OPEN_WORD);
        AdManager.ShowWarning();
    }

    public void ShowAdForTip(View view){
        BtnAnimation((Button)view);
        AdManager.SetAdType(RewardAdManager.AdType.TIPS);
        AdManager.ShowWarning();
    }

    @Override
    public void OpenWord(COST cost){
        if(cost == COST.COIN && !SpendMoney())
            return;
        PlaySound(SOUND.GUESS);
        gameManager.OpenWord();
    }

    @Override
    public void ShowTip(COST cost) {
        if(cost == COST.COIN && !SpendMoney())
            return;
        //TODO show tips
    }

    @Override
    public void GetMoneyAdView(){
        moneyManager.Add(MONEY_AD_WATCH);
    }

    @Override
    public void DoubleWinReward(){
        winDialog.UpdateRewardText();
        LEVEL_MONEY_WIN = MONEY_WIN * 2;
    }

    public void CheckOpenedWords(){
        String savedWords = "words" + theme;
        savedTips = sharedPref.getInt(savedWords, 0);
        for(int i = gameManager.GetOpenedWordCount(); i < savedTips; ++i){
            gameManager.OpenWord();
        }
    }

    private void BtnAnimation(Button bt){
        final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.scale);
        bt.startAnimation(animScale);
    }

    private boolean SpendMoney(){
        if(moneyManager.CanAfford(OPEN_WORD_COST)) {
            moneyManager.Remove(OPEN_WORD_COST);
            return true;
        }
        else {
            String str = getResources().getString(R.string.no_gold);
            InfoDialog info = new InfoDialog(this, str);
            info.CreateShopBtn();
            info.show();
            return false;
        }
    }

}
