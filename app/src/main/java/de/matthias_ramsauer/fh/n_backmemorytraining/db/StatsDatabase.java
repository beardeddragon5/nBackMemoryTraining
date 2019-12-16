package de.matthias_ramsauer.fh.n_backmemorytraining.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import de.matthias_ramsauer.fh.n_backmemorytraining.db.converter.DateConverter;
import de.matthias_ramsauer.fh.n_backmemorytraining.model.Stats;

@Database(entities = { Stats.class }, version = 1)
@TypeConverters({DateConverter.class})
public abstract class StatsDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "stats-db";
    private static StatsDatabase dbInstance;

    public abstract StatsDao statsDao();


    public static StatsDatabase getInstance(final Context context) {
        if (dbInstance == null) {
            synchronized (StatsDatabase.class) {
                if (dbInstance == null) {
                    dbInstance = buildDatabase(context.getApplicationContext());
                }
            }
        }
        return dbInstance;
    }

    private static StatsDatabase buildDatabase(final Context appContext) {
        return Room.databaseBuilder(appContext, StatsDatabase.class, DATABASE_NAME)
                .build();
    }
}
