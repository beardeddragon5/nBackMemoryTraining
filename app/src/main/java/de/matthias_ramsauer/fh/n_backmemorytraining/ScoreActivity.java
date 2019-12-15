package de.matthias_ramsauer.fh.n_backmemorytraining;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;

import de.matthias_ramsauer.fh.n_backmemorytraining.db.StatsDatabase;
import de.matthias_ramsauer.fh.n_backmemorytraining.util.DatabaseExecutor;
import de.matthias_ramsauer.fh.n_backmemorytraining.util.NBackPreferences;

public class ScoreActivity extends AppCompatActivity {

    public static final String INTENT_EXTRA_CORRECT = "correct";
    public static final String INTENT_EXTRA_EXPRESSIONS_COUNT = "expressions";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        final Locale locale = getResources().getConfiguration().locale;
        final int n = NBackPreferences.getN(this);
        final int correct = getIntent().getIntExtra(INTENT_EXTRA_CORRECT, -1);
        final int expressionCount = getIntent().getIntExtra(INTENT_EXTRA_EXPRESSIONS_COUNT, -1);
        final String correctText = String.format(locale, "%d / %d", correct, expressionCount);
        final String percent = expressionCount != 0 ?
                String.format(locale, "%d%%", (correct * 100) / expressionCount) :
                "n.a.";

        final int score = (int) Math.ceil(((double) correct / expressionCount) * expressionCount * Math.pow(10, n));
        final TextView bestScoreToday = findViewById(R.id.score_best_today);
        final TextView bestScore = findViewById(R.id.score_best);

        final Calendar cal = Calendar.getInstance(locale);
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        bestScoreToday.setText("...");
        bestScore.setText("...");

        DatabaseExecutor.getInstance().execute(() -> {
            final StatsDatabase db = StatsDatabase.getInstance(this);

            db.statsDao().addStats(n, expressionCount, score, new Date());

            final int bestToday = db.statsDao().getBestScoreSince(cal.getTime());
            final int best = db.statsDao().getBestScore();

            bestScoreToday.setText(String.valueOf(bestToday));
            bestScore.setText(String.valueOf(best));
        });

        ((TextView) findViewById(R.id.score_n)).setText(String.valueOf(n));
        ((TextView) findViewById(R.id.score_correct)).setText(correctText);
        ((TextView) findViewById(R.id.score_percent)).setText(percent);
        ((TextView) findViewById(R.id.score_score)).setText(String.valueOf(score));

    }

    public void onClickReplay(@SuppressWarnings("unused") View view) {
        final Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
    }

    public void onClickMenu(@SuppressWarnings("unused") View view) {
        super.onBackPressed();
    }
}
