package de.matthias_ramsauer.fh.n_backmemorytraining.db;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Query;

import java.util.Date;
import java.util.List;


@Dao
public interface StatsDao {

    @Query("SELECT * FROM stats ORDER BY date DESC")
    DataSource.Factory<Integer, Stats> getStatsSortedByDate();

    @Query("SELECT * FROM stats ORDER BY score DESC")
    DataSource.Factory<Integer, Stats> getStatsSortedByScore();

    @Query("SELECT * FROM stats WHERE " +
            "stats.date >= strftime('%s', date('now', 'weekday 0', '-7 day'))")
    List<Stats> getWeeksState();

    @Query("SELECT * FROM stats WHERE " +
            "stats.date >= strftime('%s', date('now', 'start of month'))")
    List<Stats>  getMonthsState();

    @Query("SELECT * FROM stats WHERE " +
            "stats.date >= strftime('%s', date('now', 'start of year'))")
    List<Stats> getYearsState();

    @Query("INSERT INTO stats (n, expression_count, score, date) VALUES(:n, :expressionCount, :score, :date)")
    void addStats(int n, int expressionCount, int score, Date date);

    @Query("SELECT MAX(score) FROM stats WHERE stats.date > strftime('%s', date('now', 'start of day'))")
    int getTodaysBestScore();

    @Query("SELECT MAX(score) FROM stats")
    int getBestScore();

    @Delete
    void delete(Stats stats);
}
