package app.com.thetechnocafe.scranesdice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class ScoreHistoryActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<String> mDateList;
    private ArrayList<String> mComputerScoreList;
    private ArrayList<String> mUserScoreList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_history);

        mRecyclerView = (RecyclerView) findViewById(R.id.history_score_recycler_view);
        Adapter adapter = new Adapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        setUpData();
        mRecyclerView.setAdapter(adapter);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mDateText;
        private TextView mUserScoreText;
        private TextView mComputerScoreText;

        public ViewHolder(View view) {
            super(view);
            mDateText = (TextView) view.findViewById(R.id.date_text);
            mUserScoreText = (TextView) view.findViewById(R.id.user_score_history);
            mComputerScoreText = (TextView) view.findViewById(R.id.computer_score_history);
        }

        public void bindData(int position) {
            mDateText.setText(mDateList.get(position));
            mComputerScoreText.setText(getString(R.string.computer_score) + " : " + mUserScoreList.get(position));
            mUserScoreText.setText(getString(R.string.your_score) + " : " + mComputerScoreList.get(position));
        }
    }

    class Adapter extends RecyclerView.Adapter<ViewHolder> {
        @Override
        public int getItemCount() {
            return mDateList.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_score_history, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bindData(position);
        }
    }

    private void setUpData() {
        mDateList = new ArrayList<>();
        mComputerScoreList = new ArrayList<>();
        mUserScoreList = new ArrayList<>();

        try {
            InputStream inputStream = openFileInput(MainActivity.SCORE_FILE_NAME);
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String newLine = null;
                while ((newLine = bufferedReader.readLine()) != null) {
                    String[] score = newLine.split("-");
                    mDateList.add(score[0]);
                    mComputerScoreList.add(score[1]);
                    mUserScoreList.add(score[2]);
                }
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.reverse(mDateList);
        Collections.reverse(mUserScoreList);
        Collections.reverse(mComputerScoreList);
    }
}
