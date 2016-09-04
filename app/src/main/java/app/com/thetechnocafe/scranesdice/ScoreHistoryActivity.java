package app.com.thetechnocafe.scranesdice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ScoreHistoryActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_history);

        mRecyclerView = (RecyclerView) findViewById(R.id.history_score_recycler_view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mDateText;
        private TextView mUserScoreText;
        private TextView mScomputerScoreText;

        public ViewHolder(View view) {
            super(view);
            mDateText = (TextView) findViewById(R.id.date_text);

        }
    }

    class Adapter extends RecyclerView.Adapter<ViewHolder> {
        @Override
        public int getItemCount() {
            return 0;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

        }
    }
}
