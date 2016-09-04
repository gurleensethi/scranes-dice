package app.com.thetechnocafe.scranesdice;

import android.animation.Animator;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView mUserScoreText;
    private TextView mComputerScoreText;
    private TextView mTurnScore;
    private TextView mTurnText;
    private TextView mGameOverText;
    private ImageView mDiceImage;
    private Button mRollButton;
    private Button mHoldButton;
    private Button mResetButton;
    private ProgressBar mUserProgress;
    private ProgressBar mComputerProgress;
    private int USER_SCORE = 0;
    private int COMPUTER_SCORE = 0;
    private int USER_SCORE_TURN = 0;
    private int COMPUTER_SCORE_TURN = 0;
    private int MAX_SCORE = 100;
    private Random mRandom;
    private ComputerPlayThread mComputerPlayThread;
    public final static String SCORE_FILE_NAME = "previous_score.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRandom = new Random();

        mUserScoreText = (TextView) findViewById(R.id.your_score_text);
        mComputerScoreText = (TextView) findViewById(R.id.computer_score_text);
        mDiceImage = (ImageView) findViewById(R.id.dice_image);
        mRollButton = (Button) findViewById(R.id.roll_button);
        mHoldButton = (Button) findViewById(R.id.hold_button);
        mResetButton = (Button) findViewById(R.id.reset_button);
        mTurnScore = (TextView) findViewById(R.id.turn_score_text);
        mTurnText = (TextView) findViewById(R.id.turn_name_text);
        mUserProgress = (ProgressBar) findViewById(R.id.user_progress);
        mComputerProgress = (ProgressBar) findViewById(R.id.computer_progress);
        mGameOverText = (TextView) findViewById(R.id.game_over_text);

        mRollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int userTurnScore = rollDiceUser();
                if (userTurnScore == 1) {
                    USER_SCORE_TURN = 0;
                    mTurnScore.setText(String.valueOf(USER_SCORE_TURN));
                    computerTurn();
                } else {
                    USER_SCORE_TURN += userTurnScore;
                    mTurnScore.setText(String.valueOf(USER_SCORE_TURN));
                    checkGameStatus();
                }
            }
        });

        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                USER_SCORE = COMPUTER_SCORE = USER_SCORE_TURN = COMPUTER_SCORE_TURN = 0;
                Toast.makeText(getApplicationContext(), getString(R.string.game_reset), Toast.LENGTH_SHORT).show();
                mComputerScoreText.setText(String.valueOf(0));
                mUserScoreText.setText(String.valueOf(0));
                mTurnScore.setText(String.valueOf(0));
                mUserProgress.setProgress(0);
                mComputerProgress.setProgress(0);
                if (mComputerPlayThread.getStatus() == AsyncTask.Status.RUNNING || mComputerPlayThread.getStatus() == AsyncTask.Status.PENDING) {
                    mComputerPlayThread.cancel(true);
                }
                //Make hold and reset clickable
                mHoldButton.setEnabled(true);
                mRollButton.setEnabled(true);
                mGameOverText.setVisibility(View.GONE);
                mTurnText.setText(getString(R.string.user_turn));
            }
        });

        mHoldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                USER_SCORE += USER_SCORE_TURN;
                mUserScoreText.setText(String.valueOf(USER_SCORE));
                USER_SCORE_TURN = 0;
                mTurnScore.setText(String.valueOf(USER_SCORE_TURN));
                mUserProgress.setProgress(USER_SCORE);
                if (!checkGameStatus()) {
                    computerTurn();
                }
            }
        });

        mUserProgress.setMax(MAX_SCORE);
        mComputerProgress.setMax(MAX_SCORE);

        //Set up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private int rollDiceUser() {
        int randomNumber = mRandom.nextInt(6) + 1;
        final String imageFileName = "dice" + randomNumber;
        Animator animator = null;
        if (Build.VERSION.SDK_INT >= 21) {
            animator = ViewAnimationUtils.createCircularReveal(
                    mDiceImage,
                    mDiceImage.getWidth() / 2,
                    mDiceImage.getHeight() / 2,
                    0,
                    (int) Math.hypot(mDiceImage.getWidth() / 2, mDiceImage.getHeight() / 2)
            );
            animator.start();
            mDiceImage.setImageResource(getResources().getIdentifier(imageFileName, "drawable", getPackageName()));
        } else {
            mDiceImage.setImageResource(getResources().getIdentifier(imageFileName, "drawable", getPackageName()));
        }
        return randomNumber;
    }

    private int rollDiceComputer() {
        int randomNumber = mRandom.nextInt(6) + 1;
        return randomNumber;
    }

    private void computerTurn() {
        mComputerPlayThread = new ComputerPlayThread();
        mComputerPlayThread.execute();
    }

    class ComputerPlayThread extends AsyncTask<Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            while (COMPUTER_SCORE_TURN < 20) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int computerTurnScore = rollDiceComputer();
                publishProgress(-1, computerTurnScore);
                if (computerTurnScore == 1) {
                    COMPUTER_SCORE_TURN = 0;
                    publishProgress(COMPUTER_SCORE_TURN);
                    break;
                } else {
                    COMPUTER_SCORE_TURN += computerTurnScore;
                    publishProgress(COMPUTER_SCORE_TURN);
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mTurnText.setText(getString(R.string.computer_turn));
            //Make hold and reset unclickable
            mHoldButton.setEnabled(false);
            mRollButton.setEnabled(false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (COMPUTER_SCORE_TURN != 0) {
                COMPUTER_SCORE += COMPUTER_SCORE_TURN;
                COMPUTER_SCORE_TURN = 0;
                mComputerScoreText.setText(String.valueOf(COMPUTER_SCORE));
                mTurnScore.setText(String.valueOf(COMPUTER_SCORE_TURN));
                mComputerProgress.setProgress(COMPUTER_SCORE);
            }
            mTurnText.setText(getString(R.string.user_turn));
            //Make hold and reset clickable
            mHoldButton.setEnabled(true);
            mRollButton.setEnabled(true);
            checkGameStatus();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (values[0] == -1) {
                final String imageFileName = "dice" + values[1];
                Animator animator = null;
                if (Build.VERSION.SDK_INT >= 21) {
                    animator = ViewAnimationUtils.createCircularReveal(
                            mDiceImage,
                            mDiceImage.getWidth() / 2,
                            mDiceImage.getHeight() / 2,
                            0,
                            (int) Math.hypot(mDiceImage.getWidth() / 2, mDiceImage.getHeight() / 2)
                    );
                    animator.start();
                    mDiceImage.setImageResource(getResources().getIdentifier(imageFileName, "drawable", getPackageName()));
                } else {
                    mDiceImage.setImageResource(getResources().getIdentifier(imageFileName, "drawable", getPackageName()));
                }
            } else {
                mTurnScore.setText(String.valueOf(values[0]));
            }
        }
    }

    private boolean checkGameStatus() {
        boolean gameOver = false;
        if (USER_SCORE >= 100) {
            mTurnText.setText(getString(R.string.you_win));
            gameOver = true;
        } else if (COMPUTER_SCORE >= 100) {
            mTurnText.setText(R.string.you_loose);
            gameOver = true;
        }

        if (gameOver) {
            mHoldButton.setEnabled(false);
            mRollButton.setEnabled(false);
            mGameOverText.setVisibility(View.VISIBLE);

            try {
                FileOutputStream fileOutputStream = openFileOutput(SCORE_FILE_NAME, MODE_APPEND);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("DD MMM YYY");
                String score = simpleDateFormat.format(new Date()) + "-" + String.valueOf(COMPUTER_SCORE) + "-" + String.valueOf(USER_SCORE) + "\n";
                fileOutputStream.write(score.getBytes());
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return gameOver;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(getApplication());
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.previous_scores : {
                Intent intent = new Intent(getApplicationContext(), ScoreHistoryActivity.class);
                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
