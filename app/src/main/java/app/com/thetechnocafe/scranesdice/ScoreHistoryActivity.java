package app.com.thetechnocafe.scranesdice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
        private TextView mComputerScoreText;

        public ViewHolder(View view) {
            super(view);
            mDateText = (TextView) view.findViewById(R.id.date_text);
            mUserScoreText = (TextView) view.findViewById(R.id.user_score_history);
            mComputerScoreText = (TextView) view.findViewById(R.id.computer_score_history);
        }

        public void bindData() {

        }
    }

    class Adapter extends RecyclerView.Adapter<ViewHolder> {
        @Override
        public int getItemCount() {
            return 0;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_score_history, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

        }
    }
}
