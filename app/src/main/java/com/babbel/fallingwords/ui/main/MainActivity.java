package com.babbel.fallingwords.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.babbel.fallingwords.R;
import com.babbel.fallingwords.domain.interactors.WordInteractorImpl;
import com.babbel.fallingwords.ui.compoundviews.ScoreCustomView;

/**
 * Created by felipe on 8/10/16.
 */
public class MainActivity extends AppCompatActivity implements MainView, View.OnClickListener,Animation.AnimationListener {


    private TextView fallingWordTxtView;
    private TextView currentWord;
    private Button wordOkBtn;
    private Button workNotOkBtn;
    private Button startGameBtn;
    private RelativeLayout gameLayout;
    private Animation animationFalling;
    private ScoreCustomView score;
    private ProgressBar progressBar;

    private MainPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fallingWordTxtView = (TextView) findViewById(R.id.main_falling_word);
        currentWord = (TextView) findViewById(R.id.main_current_word);
        workNotOkBtn = (Button) findViewById(R.id.main_word_nok);
        wordOkBtn = (Button) findViewById(R.id.main_word_ok);
        startGameBtn = (Button) findViewById(R.id.main_start_game);
        gameLayout = (RelativeLayout) findViewById(R.id.main_game_layout);
        score = (ScoreCustomView) findViewById(R.id.main_score);
        progressBar = (ProgressBar) findViewById(R.id.main_progress);

        wordOkBtn.setOnClickListener(this);
        workNotOkBtn.setOnClickListener(this);
        startGameBtn.setOnClickListener(this);
        presenter = new MainPresenterImpl(this, new WordInteractorImpl());
        presenter.populateData();
    }


    @Override
    public void setWordToTranslate(String currentWord) {
        this.currentWord.setText(currentWord);
    }

    @Override
    public void startFallingAnimation() {

        animationFalling = AnimationUtils.loadAnimation(this, R.anim.falling);
        animationFalling.setAnimationListener(this);
        fallingWordTxtView.startAnimation(animationFalling);
    }

    @Override
    public void hideStartBtnAndShowGame() {
        startGameBtn.setVisibility(View.GONE);
        gameLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void setFallingWord(String fallingWord) {
        this.fallingWordTxtView.setText(fallingWord);
    }

    @Override
    public void setRightAnswer(int score) {
        this.score.setRightAnswer("✓ " + score);
    }

    @Override
    public void setWrongAnswer(int score) {
        this.score.setWrongAnswer("X " + score);
    }

    @Override
    public void setNotAnswered(int score) {
        this.score.setNotAnswered("O "+ score);
    }


    @Override
    public void setGameEndedState(){
        animationFalling.setAnimationListener(null);
        fallingWordTxtView.setText("");
        currentWord.setText("");
        startGameBtn.setVisibility(View.VISIBLE);
        enableWrongOkButtons(false);
    }



    @Override
    public void enableWrongOkButtons(boolean enable) {
        wordOkBtn.setEnabled(enable);
        workNotOkBtn.setEnabled(enable);

        if(enable){
            wordOkBtn.setAlpha(1f);
            workNotOkBtn.setAlpha(1f);
        }
        else{
            wordOkBtn.setAlpha(0.5f);
            workNotOkBtn.setAlpha(0.5f);
        }
    }

    @Override
    public void startLoadingData() {
        progressBar.setVisibility(View.VISIBLE);
        startGameBtn.setAlpha(0.5f);
        startGameBtn.setEnabled(false);
    }

    @Override
    public void endLoadingData() {
        progressBar.setVisibility(View.GONE);
        startGameBtn.setAlpha(1f);
        startGameBtn.setEnabled(true);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.main_word_ok:
                presenter.onWordOkBtnClicked(fallingWordTxtView.getText().toString());
                break;
            case R.id.main_word_nok:
                presenter.onWordNokBtnClicked(fallingWordTxtView.getText().toString());
                presenter.getNextWordOption();
                break;

            case R.id.main_start_game:
                presenter.startGame();
                presenter.getNextWordToTranslate();
                break;
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        presenter.setWordNotAnswered();
        presenter.getNextWordOption();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }
}
