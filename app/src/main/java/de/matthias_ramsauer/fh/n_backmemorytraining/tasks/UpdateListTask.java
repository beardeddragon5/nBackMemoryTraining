package de.matthias_ramsauer.fh.n_backmemorytraining.tasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import de.matthias_ramsauer.fh.n_backmemorytraining.db.StatsDatabase;
import de.matthias_ramsauer.fh.n_backmemorytraining.model.Stats;
import de.matthias_ramsauer.fh.n_backmemorytraining.model.StatsSort;

public class UpdateListTask extends AsyncTask<Context, Void, LiveData<PagedList<Stats>>> {

    public interface UpdateListTaskCallback {
        void done(LiveData<PagedList<Stats>> stats);
    }

    private final StatsSort sort;
    private final UpdateListTaskCallback cb;

    public UpdateListTask(StatsSort sort, UpdateListTaskCallback cb) {
        this.sort = sort;
        this.cb = cb;
    }

    @Override
    protected LiveData<PagedList<Stats>> doInBackground(Context... contexts) {
        final StatsDatabase db = StatsDatabase.getInstance(contexts[0]);
        final DataSource.Factory<Integer, Stats> factory;

        switch (sort) {
            case Date:
                factory = db.statsDao().getStatsSortedByDate();
                break;
            case Score:
                factory = db.statsDao().getStatsSortedByScore();
                break;
            default:
                throw new IllegalStateException("sort type not implemented");
        }
        return new LivePagedListBuilder<>(factory, 20).build();
    }

    @Override
    protected void onPostExecute(LiveData<PagedList<Stats>> stats) {
        cb.done(stats);
    }
}
