package de.matthias_ramsauer.fh.n_backmemorytraining.util;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DatabaseExecutor {
    private static final DatabaseExecutor ourInstance = new DatabaseExecutor();

    public static DatabaseExecutor getInstance() {
        return ourInstance;
    }

    private final Executor executor;

    private DatabaseExecutor() {
        executor = Executors.newSingleThreadExecutor();
    }

    public void execute(Runnable r) {
        executor.execute(r);
    }
}
