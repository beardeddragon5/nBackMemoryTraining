package de.matthias_ramsauer.fh.n_backmemorytraining.tasks;

import android.content.Context;
import android.os.AsyncTask;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.matthias_ramsauer.fh.n_backmemorytraining.db.StatsDatabase;

public class InsertScoreTask extends AsyncTask<Context, Void, int[]> {

    public interface InsertScoreTaskCallback {

        void done(int bestToday, int best);
    }

    private final int n;
    private final int expressionCount;
    private final int score;
    private final InsertScoreTaskCallback done;

    public InsertScoreTask(int n, int expressionCount, int score, InsertScoreTaskCallback done) {
        this.n = n;
        this.expressionCount = expressionCount;
        this.score = score;
        this.done = done;
    }

    @Override
    protected int[] doInBackground(Context... contexts) {
        final StatsDatabase db = StatsDatabase.getInstance(contexts[0]);
        final Locale locale = contexts[0].getResources().getConfiguration().locale;
        final Calendar cal = Calendar.getInstance(locale);

        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        db.statsDao().addStats(n, expressionCount, score, new Date());

        final int bestToday = db.statsDao().getBestScoreSince(cal.getTime());
        final int best = db.statsDao().getBestScore();

        cal.add(Calendar.DAY_OF_YEAR, -cal.getActualMaximum(Calendar.DAY_OF_YEAR));
        db.statsDao().deleteStatsUntil(cal.getTime(), best);

        return new int[]{bestToday, best};
    }

    @Override
    protected void onPostExecute(int[] ints) {
        done.done(ints[0], ints[1]);
    }
}
