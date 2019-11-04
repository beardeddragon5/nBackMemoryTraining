package de.matthias_ramsauer.fh.n_backmemorytraining;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import de.matthias_ramsauer.fh.n_backmemorytraining.util.NBackPreferences;

public class ScoreActivity extends AppCompatActivity {

    public static final String INTENT_EXTRA_CORRECT = "correct";
    public static final String INTENT_EXTRA_EXPRESSIONS_COUNT = "expressions";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        final int n = NBackPreferences.getN(this);
        final int correct = getIntent().getIntExtra(INTENT_EXTRA_CORRECT, -1);
        final int expressionCount = getIntent().getIntExtra(INTENT_EXTRA_EXPRESSIONS_COUNT, -1);
        final String correctText = String.format("%d / %d", correct, expressionCount);
        final String percent = String.format("%d%%", (correct * 100) / expressionCount);
        final int score = (correct - (int) Math.ceil(0.5 * (expressionCount - correct))) * (int) Math.pow(10, n);
        final int bestToday = -1;
        final int best = -1;

        ((TextView) findViewById(R.id.score_n)).setText(String.valueOf(n));
        ((TextView) findViewById(R.id.score_correct)).setText(correctText);
        ((TextView) findViewById(R.id.score_percent)).setText(percent);
        ((TextView) findViewById(R.id.score_score)).setText(String.valueOf(score));
        ((TextView) findViewById(R.id.score_best_today)).setText(String.valueOf(bestToday));
        ((TextView) findViewById(R.id.score_best)).setText(String.valueOf(best));
    }

    public void onClickReplay(View view) {
        final Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
    }

    public void onClickMenu(View view) {
        super.onBackPressed();
    }
}
