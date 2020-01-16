package de.matthias_ramsauer.fh.n_backmemorytraining.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

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
                    /*
                    final Locale locale = context.getResources().getConfiguration().locale;
                    final Calendar cal = Calendar.getInstance(locale);
                    final Random rand = new Random();

                    for (int year = 2019; year <= 2020; year++) {
                        cal.set(Calendar.YEAR, year);
                        for (int month = Calendar.JANUARY; month <= Calendar.DECEMBER; month++) {
                            cal.set(Calendar.MONTH, month);
                            for (int day = 1; day <= cal.getActualMaximum(Calendar.DAY_OF_MONTH); day++) {
                                cal.set(Calendar.DAY_OF_MONTH, day);

                                Date date = cal.getTime();
                                if (date.getTime() > new Date().getTime()) {
                                    return dbInstance;
                                }

                                int n = rand.nextInt(6) + 1;
                                int expressionCount = rand.nextInt(20) + 5;
                                double percent = rand.nextDouble();
                                int score = (int) Math.ceil(percent * expressionCount * Math.pow(10, n));

                                dbInstance.statsDao().addStats(n, expressionCount, score, cal.getTime());
                            }
                        }
                    }
                    */
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
